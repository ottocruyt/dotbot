#include <Servo.h>
Servo leftServo; // create servo object to control a servo
Servo rightServo;

void setup() 
{ 
  Serial.begin(9600);
  leftServo.attach(5);
  rightServo.attach(6);
} 
 
void loop() 
{ 
  
  bool msgReceived = false;
  byte msg[2];
  while(!msgReceived)
  {
      while(Serial.available())
      {
         Serial.readBytes((char *)msg,2);
         msgReceived = true;

      }
  }
  
  
  // convert unsigned byte to signed integer
  int LeftMotor = msg[0];
  int RightMotor = msg[1];
  if( LeftMotor > 127 ) {LeftMotor = LeftMotor - 256;}
  if( RightMotor > 127 ) {RightMotor = RightMotor - 256;}
  
  Serial.print(RightMotor);
  // use values to actuate the motors
  leftServo.writeMicroseconds(1500-LeftMotor*5);
  rightServo.writeMicroseconds(1500+RightMotor*5*0.9250);
 
}
