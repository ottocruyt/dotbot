#include 'geluid.h'

int sensorPin=6;
int speakerPin=8;
void GameOfThrones();
void setup()
  {
    pinMode(speakerPin,OUTPUT);
    pinMode(sensorPin,INPUT);
  }
void loop()
  {  //play when entering or leaving you thrones, chair etc.
      //im using negative logic infrared sensor(if positive logic, use HIGH insted of LOW)
    GameOfThrones();
  }

  
void GameOfThrones()
  {
    for(int i=0; i<4; i++)
    {
    tone(speakerPin, NOTE_G4);
    delay(500);
    noTone(speakerPin);
    tone(speakerPin, NOTE_C4);
    delay(500);
    noTone(speakerPin);
    tone(speakerPin, NOTE_DS4);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_F4);
    delay(250);
    noTone(speakerPin);
    }
    for(int i=0; i<4; i++)
    {
    tone(speakerPin, NOTE_G4);
    delay(500);
    noTone(speakerPin);
    tone(speakerPin, NOTE_C4);
    delay(500);
    noTone(speakerPin);
    tone(speakerPin, NOTE_E4);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_F4);
    delay(250);
    noTone(speakerPin);
    }
        tone(speakerPin, NOTE_G4);
        delay(500);
        noTone(speakerPin);
        tone(speakerPin, NOTE_C4);
        delay(500);
        tone(speakerPin, NOTE_DS4);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_F4);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_D4);
        delay(500);
        noTone(speakerPin);
    for(int i=0; i<3; i++)
    {
    tone(speakerPin, NOTE_G3);
    delay(500);
    noTone(speakerPin);
    tone(speakerPin, NOTE_AS3);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_C4);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_D4);
    delay(500);
    noTone(speakerPin);
    }//
        tone(speakerPin, NOTE_G3);
        delay(500);
        noTone(speakerPin);
        tone(speakerPin, NOTE_AS3);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_C4);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_D4);
        delay(1000);
        noTone(speakerPin);
        
        tone(speakerPin, NOTE_F4);
        delay(1000);
        noTone(speakerPin);
        tone(speakerPin, NOTE_AS3);
        delay(1000);
        noTone(speakerPin);
        tone(speakerPin, NOTE_DS4);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_D4);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_F4);
        delay(1000);
        noTone(speakerPin);
        tone(speakerPin, NOTE_AS3);
        delay(1000);
        noTone(speakerPin);
        tone(speakerPin, NOTE_DS4);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_D4);
        delay(250);
        noTone(speakerPin);
        tone(speakerPin, NOTE_C4);
        delay(500);
        noTone(speakerPin);
    for(int i=0; i<3; i++)
    {
    tone(speakerPin, NOTE_GS3);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_AS3);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_C4);
    delay(500);
    noTone(speakerPin);
    tone(speakerPin, NOTE_F3);
    delay(500);
    noTone(speakerPin);
    }
          tone(speakerPin, NOTE_G4);
          delay(1000);
          noTone(speakerPin);
          tone(speakerPin, NOTE_C4);
          delay(1000);
          noTone(speakerPin);
          tone(speakerPin, NOTE_DS4);
          delay(250);
          noTone(speakerPin);
          tone(speakerPin, NOTE_F4);
          delay(250);
          noTone(speakerPin);
          tone(speakerPin, NOTE_G4);
          delay(1000);
          noTone(speakerPin);
          tone(speakerPin, NOTE_C4);
          delay(1000);
          noTone(speakerPin);
          tone(speakerPin, NOTE_DS4);
          delay(250);
          noTone(speakerPin);
          tone(speakerPin, NOTE_F4);
          delay(250);
          noTone(speakerPin);
          tone(speakerPin, NOTE_D4);
          delay(500);
          noTone(speakerPin);
    for(int i=0; i<4; i++)
    {
    tone(speakerPin, NOTE_G3);
    delay(500);
    noTone(speakerPin);
    tone(speakerPin, NOTE_AS3);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_C4);
    delay(250);
    noTone(speakerPin);
    tone(speakerPin, NOTE_D4);
    delay(500);
    noTone(speakerPin);
    }
}
