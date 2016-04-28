#include <Servo.h>
#include "dotbot_udemy_motors.h"
#include "DotbotSensors.h"

const byte ledPin = 13;
const byte buttonPin = 9;

DotBotMotors motors;
DotbotSensors<4,A5,3,A6,7,A4> sensors;

void setup()
{

  motors.attach(6,5);
  pinMode(ledPin,OUTPUT);
  pinMode(buttonPin,INPUT_PULLUP);

  sensors.configure();
  
  Serial.begin(9600);

while(digitalRead(buttonPin))
  {
    delay(10);
  }
}

void loop()
{
  sensors.sense();
  sensors.view();
  delay(100);
}

