#include <Servo.h>
#include "dotbot_udemy_motors.h"
DotBotMotors motors;

void setup()
{

  motors.attach(6,5);
  motors.turn(LEFT,90);
}

void loop()
{

}

