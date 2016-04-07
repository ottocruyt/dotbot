#include <Servo.h>  
Servo leftServo;  // create servo object to control a servo 
Servo rightServo;
 
void setup() 
{ 
  leftServo.attach(6);  // attaches the servo on pin 9 to the servo object 
  rightServo.attach(5);
  Serial.begin(9600);
} 
 
void loop() 
{ 
  
  bool msgReceived = false;
  char msg[2];
  while(!msgReceived)
  {
      while(Serial.available())
      {
         Serial.readBytes(msg,2);
         msgReceived = true;
      }
  }
  
  leftServo.writeMicroseconds(1500+msg[0]*5);     
  rightServo.writeMicroseconds(1500-msg[1]*5*0.9250);
 
 } 
