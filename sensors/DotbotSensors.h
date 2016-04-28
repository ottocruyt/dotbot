#include <Arduino.h>

template <byte leftEmitter, byte leftDetector, byte frontEmitter, byte frontDetector, byte rightEmitter, byte rightDetector>

class DotbotSensors
{
  private:
    int leftAmbient;
    int frontAmbient;
    int rightAmbient;

    int leftCombined;
    int frontCombined;
    int rightCombined;

    int leftReflected;
    int frontReflected;
    int rightReflected;
  
  public:
  void configure()
  {
    pinMode(leftEmitter,OUTPUT);
    pinMode(frontEmitter,OUTPUT);
    pinMode(rightEmitter,OUTPUT);
  }

  void sense()
  {
    digitalWrite(leftEmitter,HIGH);
    digitalWrite(frontEmitter,HIGH);
    digitalWrite(rightEmitter,HIGH);
    delay(1);
    leftCombined = analogRead(leftDetector);
    frontCombined = analogRead(frontDetector);
    rightCombined = analogRead(rightDetector);
    //delay(1);
    digitalWrite(leftEmitter,LOW);
    digitalWrite(frontEmitter,LOW);
    digitalWrite(rightEmitter,LOW);
    delay(1);
    leftAmbient = analogRead(leftDetector);
    frontAmbient = analogRead(frontDetector);
    rightAmbient = analogRead(rightDetector);
    
    leftReflected = leftCombined - leftAmbient;
    frontReflected = frontCombined - frontAmbient;
    rightReflected = rightCombined - rightAmbient;
  }
  
  void view()
  {
    Serial.print(leftReflected);
    Serial.print("\t");
    Serial.print(leftCombined);
    Serial.print("\t");
    Serial.print(leftAmbient);
    Serial.print("\t /");
    Serial.print(frontReflected);
    Serial.print("\t"); 
    Serial.println(rightReflected); 
  }
};
