#include <Servo.h>  
Servo myservo;  // create servo object to control a servo 
int val;    // variable to read the value from the analog pin 
 
void setup() 
{ 
  myservo.attach(9);  // attaches the servo on pin 9 to the servo object 
  Serial.begin(9600);
} 
 
void loop() 
{ 
  
  bool msgReceived = false;
  char msg[1];
  while(!msgReceived)
  {
      while(Serial.available())
      {
         Serial.readBytes(msg,1);
         msgReceived = true;
      }
  }
  
  val = msg[0];     // scale it to use it with the servo (value between 0 and 180) 
  myservo.write(val);                  // sets the servo position according to the scaled value 
 
 } 
