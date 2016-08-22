(ns ubik.stepper
  (:require [taoensso.sente :as sente]
            [taoensso.timbre :as timbre :refer-macros [debugf]]
            [cljs.core.match :refer-macros [match]]
            [cljs.core.async :refer [<! >! chan]]
            [cljs.nodejs :as nodejs])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(def SerialPort (nodejs/require "serialport"))
(def serial-port (SerialPort. "/dev/cu.usbmodem1421" #js {:baudRate 9600}))

(def type-mapping {:top 0 :center 1 :bottom 2})
(def direction-mapping {:prev -100 :next 100})

(.on serial-port "open" (fn [] (debugf "serial port open")))

(defn write-serial [anim]
  (match anim
         {:direction direction :type type} (let [cmd (str \x (type-mapping type) (direction-mapping direction))]
                                             (debugf "cmd %s" cmd)
                                             (.write serial-port cmd))
         :else (debugf "unhandled anim %s" anim)))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client! "/chsk" {:type :auto :packer :edn :protocol :http :host "localhost:3000"})]
    (def ch-chsk ch-recv)
    (def chsk-send! send-fn))

(defn event-msg-handler [{:keys [?data event]}]
  (match ?data
         [:ubik/change-anim anim] (write-serial anim)
         :else (debugf "unhandled event %s" event)))

(def router (atom nil))
(defn stop-router! [] (when-let [stop-f @router] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router (sente/start-chsk-router! ch-chsk event-msg-handler)))

(set! *main-cli-fn* start-router!)
