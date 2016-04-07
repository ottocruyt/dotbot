#include <Servo.h>
#include "dotbot_udemy_motors.h"
DotBotMotors motors;

void setup()
{

  
}

void loop()
{

    motors.attach(6,5);
    motors.turn(RIGHT, 360);
    delay(1000);

    
    

}

