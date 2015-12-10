#include <Servo.h>

#define RIGHT 1
#define LEFT -1

Servo leftServo;
Servo rightServo;

const byte power = 100;

void forward()
{
  leftServo.writeMicroseconds(1500+power);
  rightServo.writeMicroseconds(1500-power);
}

void stop(int time = 200)
{
  leftServo.writeMicroseconds(1500);
  rightServo.writeMicroseconds(1500);
  delay(time);
}

void forwardTime(unsigned int time)
{
  forward();
  delay(time);
  stop();
}



void turn(int direction, int time)
{
  leftServo.writeMicroseconds(1500+power*direction);
  rightServo.writeMicroseconds(1500+power*direction); 
  delay(time);
  stop();
}

void setup()
{
  leftServo.attach(6);
  rightServo.attach(5);

  turn(LEFT,500);
  turn(RIGHT,500);
}

void loop()
{

}

