void setup() 
{ 
  Serial.begin(9600);
} 
 
void loop() 
{ 
  
  bool msgReceived = false;
  char msg;
  while(!msgReceived)
  {
      while(Serial.available())
      {
         //Serial.readBytes(msg,3);
         msg = Serial.read();
         Serial.print(msg);
         msgReceived = true;

      }
  }
 
}
