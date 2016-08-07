(defproject ubik/stepper "0.0.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.93"]
                 [org.clojure/core.async "0.2.385"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [com.taoensso/sente "1.10.0"]
                 [com.taoensso/timbre "4.6.0"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [org.clojure/core.match "0.3.0-alpha4"]]

  :npm {:dependencies [[source-map-support "*"]
                       [serialport "4.0.1"]
                       [websocket "1.0.23"]]}

  :plugins [[lein-pprint "1.1.2"]
            [lein-cljsbuild "1.1.3"]
            [lein-shell "0.5.0"]
            [lein-npm "0.6.2"]]

  :cljsbuild
  {:builds
   [{:id :node-client
    :source-paths ["src"]
    :compiler {:main ubik.stepper
               :output-to "target/stepper.js"
               :output-dir "target/stepper-out"
               :target :nodejs
               :optimizations :none
               :source-map true
               :pretty-print true}}]}

  :aliases {"start" ["do" "clean," "npm" "install," "cljsbuild" "once," "shell" "node" "target/stepper.js"]}
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"})
