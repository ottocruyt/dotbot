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

    int leftTotal;
    int frontTotal;
    int rightTotal;
    static const byte numReadings = 20;
    byte index;
    int leftReadings[numReadings];
    int frontReadings[numReadings];
    int rightReadings[numReadings];
    int leftSmoothed;
    int frontSmoothed;
    int rightSmoothed;

  public:

    int front;
    int left;
    int right;

    void configure()
    {
      pinMode(leftEmitter, OUTPUT);
      pinMode(frontEmitter, OUTPUT);
      pinMode(rightEmitter, OUTPUT);
    }

    void sense()
    {
      digitalWrite(leftEmitter, HIGH);
      digitalWrite(frontEmitter, HIGH);
      digitalWrite(rightEmitter, HIGH);
      delay(1);
      leftCombined = analogRead(leftDetector);
      frontCombined = analogRead(frontDetector);
      rightCombined = analogRead(rightDetector);
      //delay(1);
      digitalWrite(leftEmitter, LOW);
      digitalWrite(frontEmitter, LOW);
      digitalWrite(rightEmitter, LOW);
      delay(1);
      leftAmbient = analogRead(leftDetector);
      frontAmbient = analogRead(frontDetector);
      rightAmbient = analogRead(rightDetector);

      leftReflected = leftCombined - leftAmbient;
      frontReflected = frontCombined - frontAmbient;
      rightReflected = rightCombined - rightAmbient;

      leftTotal -= leftReadings[index];
      leftReadings[index] = leftReflected;
      leftTotal += leftReadings[index];
      frontTotal -= frontReadings[index];
      frontReadings[index] = frontReflected;
      frontTotal += frontReadings[index];
      rightTotal -= rightReadings[index];
      rightReadings[index] = rightReflected;
      rightTotal += rightReadings[index];
      index += 1;

      if (index >= numReadings)
      {
        index = 0;
      }

      leftSmoothed = leftTotal / numReadings;
      frontSmoothed = frontTotal / numReadings;
      rightSmoothed = rightTotal / numReadings;

      left = leftSmoothed;
      front = frontSmoothed;
      right = rightSmoothed;
    }

    void view()
    {
      Serial.print(left);
      Serial.print("\t");
      Serial.print(front);
      Serial.print("\t");
      Serial.println(right);
    }

    void initialize()
    {
      for (byte i = 0 ; i < numReadings ; i++)
      {
        sense();
      }
    }
};
