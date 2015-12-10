#include <Servo.h>

Servo leftServo;
Servo rightServo;

const byte power = 100;

void forward()
{
  leftServo.writeMicroseconds(1500+power);
  rightServo.writeMicroseconds(1500-power);
}

void forwardTime(unsigned int time)
{
  forward();
  delay(time);
  stop();
}

void stop()
{
  leftServo.writeMicroseconds(1500);
  rightServo.writeMicroseconds(1500);
}

void turn(int time)
{
  leftServo.writeMicroseconds(1500+power);
  rightServo.writeMicroseconds(1500+power); 
  delay(time);
  stop();
}

void setup()
{
  leftServo.attach(6);
  rightServo.attach(5);

  turn(500);
}

void loop()
{

}

