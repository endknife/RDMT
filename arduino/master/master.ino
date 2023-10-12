#include <RH_ASK.h>
#ifdef RH_HAVE_HARDWARE_SPI
#include <SPI.h> // Not actually used but needed to compile
#endif

RH_ASK driver;

String messaggio="";
char c;
String str = "";
int num = 1;

void setup(){
  pinMode(LED_BUILTIN,OUTPUT);
  Serial.begin(9600);
  
  if (!driver.init())
           Serial.println("init failed");
}

void loop(){
    const char* msg;  //il warining vuole un const ma noi non glielo diamo... 

    if(Serial.available() > 0){
      messaggio=Serial.readStringUntil('\n');

      msg = messaggio.c_str();
     
      Serial.println("send");
      driver.send((uint8_t *)msg, strlen(msg));
      driver.waitPacketSent();
      delay(500);
    }

    
}
