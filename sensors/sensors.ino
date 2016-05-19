#include <Servo.h>
#include "dotbot_udemy_motors.h"
#include "DotbotSensors.h"

const byte ledPin = 13;
const byte buttonPin = 9;

DotBotMotors motors;
DotbotSensors<4, A5, 3, A6, 7, A4> sensors;

void setup()
{

  motors.attach(5,6);
  pinMode(ledPin, OUTPUT);
  pinMode(buttonPin, INPUT_PULLUP);

  sensors.configure();

  Serial.begin(9600);

  while (digitalRead(buttonPin))
  {
    delay(10);
  }
}

void loop()
{
  avoid(state());
  // sensors.sense();
  // sensors.view();
}

byte state()
{
  int treshold = 80;
  byte event = 0;

  sensors.sense();

  if (sensors.front > treshold)
  {
    event += 1;
  }
  if (sensors.left > treshold)
  {
    event += 2;
  }
  if (sensors.right > treshold)
  {
    event += 4;
  }

  return event;
}

void avoid(byte event)
{
  switch (event)
  {
    case 1: //front sensor triggered
      motors.turn(LEFT, 90);
      sensors.initialize();
      break;
    case 2: //left sensor triggered
      motors.turn(RIGHT, 45);
      sensors.initialize();
      break;
    case 4: //right sensor triggered
      motors.turn(LEFT, 45);
      sensors.initialize();
      break;
    default:
      motors.forward();
      break;
  }
}

