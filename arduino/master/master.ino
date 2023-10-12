// ask_transmitter.pde
// -*- mode: C++ -*-
// Simple example of how to use RadioHead to transmit messages
// with a simple ASK transmitter in a very simple way.
// Implements a simplex (one-way) transmitter with an TX-C1 module
// Tested on Arduino Mega, Duemilanova, Uno, Due, Teensy, ESP-12

#include <RH_ASK.h>
#ifdef RH_HAVE_HARDWARE_SPI
#include <SPI.h> // Not actually used but needed to compile
#endif

RH_ASK driver;
// RH_ASK driver(2000, 4, 5, 0); // ESP8266 or ESP32: do not use pin 11 or 2
// RH_ASK driver(2000, 3, 4, 0); // ATTiny, RX on D3 (pin 2 on attiny85) TX on D4 (pin 3 on attiny85), 
// RH_ASK driver(2000, PD14, PD13, 0); STM32F4 Discovery: see tx and rx on Orange and Red LEDS

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

      //messaggio.toCharArray(msg, sizeof(messaggio));
      msg = messaggio.c_str();
     
      Serial.println("send");
      driver.send((uint8_t *)msg, strlen(msg));
      driver.waitPacketSent();
      delay(500);
    }

    
}
