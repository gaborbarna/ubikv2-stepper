#include <AccelStepper.h>

AccelStepper steppers[3] = {
  AccelStepper(4, 2, 3, 4, 5), 
  AccelStepper(4, 10, 11, 12, 13), 
  AccelStepper(4, 6, 7, 8, 9)
};

void setup() {  
  Serial.setTimeout(10);
  Serial.begin(9600);
  for (int i = 0; i < 3; i++) {
    steppers[i].setMaxSpeed(50);
    steppers[i].setSpeed(50);
    steppers[i].setAcceleration(50);
  }
}

void loop() {
  if (Serial.available() > 0) {
    String in = Serial.readString();
    Serial.println(in);
    char cmd = in[0];
    if (cmd == 'x') {
      int idx = in[1] - '0';
      int steps = in.substring(2).toInt(); 
      if (steppers[idx].distanceToGo() == 0) {
        steppers[idx].moveTo(steppers[idx].currentPosition() + steps);
      }
    }
  }
  for (int i = 0; i < 3; i++) {
    steppers[i].run();
  }
}
