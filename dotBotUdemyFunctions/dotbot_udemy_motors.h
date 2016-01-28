#include <Arduino.h>
#include <Servo.h>

#define RIGHT 1
#define LEFT -1

class DotBotMotors{
private:
  Servo leftServo;
  Servo rightServo;

  static const byte power = 500;

public:
  void attach(byte leftMotor, byte rightMotor)
  {
    leftServo.attach(leftMotor);
    rightServo.attach(rightMotor);
  }
  void forward()
  {
    leftServo.writeMicroseconds(1500+power);
    rightServo.writeMicroseconds(1500-power*0.9250);
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

  void turn(int direction, int degrees)
  {
    leftServo.writeMicroseconds(1500+power*direction);
    rightServo.writeMicroseconds(1500+power*direction); 
    delay(degrees*5.15);
    stop();
  }
};


