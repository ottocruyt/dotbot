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
void setup()
{
  leftServo.attach(6);
  rightServo.attach(5);
  
  forwardTime(2000);
}

void loop()
{
 
}
