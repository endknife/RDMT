//Definitions of the pins used to move the benches

//First bench
#define pin_up_one 6
#define pin_stop_one 7
#define pin_down_one 8

//Second bench
#define pin_up_two 3
#define pin_stop_two 4
#define pin_down_two 5

//Third bench
#define pin_up_three A5
#define pin_stop_three A4
#define pin_down_three 2

#include <RH_ASK.h>
#ifdef RH_HAVE_HARDWARE_SPI
#include <SPI.h> // Not actually used but needed to compile
#endif

RH_ASK driver;
const int pinUp[] = {pin_up_one, pin_up_two, pin_up_three};
const int pinDown[] = {pin_down_one, pin_down_two, pin_down_three};
const int pinStop[] = {pin_stop_one, pin_stop_two, pin_stop_three};

// Global variables with transmitted values
const String idAula = "B112";
const int banchi[] = {2,3,4};
const int nrBanchi = sizeof(banchi)/sizeof(int);

String firstValue;
int numbers[16]; // We suppose that there are going to be MAX 15 benches (+1 for the while loop)
char lastChar;

void setup()
{

    //Pins iniitialization
    for(int i = 0; i<nrBanchi; i++){
      pinMode(pinUp[i], OUTPUT);
      pinMode(pinDown[i], OUTPUT);
      pinMode(pinStop[i], OUTPUT);

      digitalWrite(pinUp[i], HIGH);
      digitalWrite(pinDown[i], HIGH);
      digitalWrite(pinStop[i], LOW);
    }
    Serial.begin(9600);	  // Debugging only
    
    Serial.print("ID AULA: ");
    Serial.println(idAula);

    Serial.println("BANCHI:");

    for(int i = 0; i<nrBanchi; i++){
      Serial.println(banchi[i]);
    }

    if (!driver.init())
       Serial.println("init failed");
}

void loop()
{
    uint8_t buf[RH_ASK_MAX_MESSAGE_LEN];
    uint8_t buflen = sizeof(buf);

    if (driver.available()) 
    {
      driver.recv(buf, &buflen);

      buf[buflen] = '\0';
        String receivedString = String((char*)buf);

        //Finds the room ID between the two "#"
        int firstHashIndex = receivedString.indexOf('#');
        int secondHashIndex = receivedString.indexOf('#', firstHashIndex + 1);
        if (firstHashIndex != -1 && secondHashIndex != -1) {
            firstValue = receivedString.substring(firstHashIndex + 1, secondHashIndex);
            Serial.print("Primo valore tra i cancelletti: ");
            Serial.println(firstValue);
        }

        /*
         * firstValue = room ID
         * lastChar = Movement type
         * numbers[] = Selected benches
         */
        
        //Finds the last char for the benches moving mode
        lastChar = receivedString.charAt(receivedString.length() - 1);
        Serial.print("Ultimo carattere: ");
        Serial.println(lastChar);

      
        //Finds and extracts the selected benches numbers
        int SecondHashIndex = receivedString.indexOf('#', receivedString.indexOf('#') + 1);
        
        // Finds the position of ";"
        int lastSemicolonIndex = receivedString.lastIndexOf(';');

        if (SecondHashIndex != -1 && lastSemicolonIndex != -1) {
            String numbersString = receivedString.substring(SecondHashIndex + 1, lastSemicolonIndex);
            
      
            //Splits the string every ";"
            int index = 0;
            char* token = strtok((char*)numbersString.c_str(), ";");
            while (token != NULL && index < sizeof(numbers)) {
                numbers[index++] = atoi(token);
                token = strtok(NULL, ";");
            }

            // Print the correct numbers
            Serial.println("Numeri estratti:");
            for (int i = 0; i < index; i++) {
                Serial.println(numbers[i]);
            }
        }
        
        Serial.print("Aula giusta: ");
        if(firstValue == idAula){
          Serial.println("True");
        }
        else{
          Serial.println("False");
          goto fine;
        }

        //If the last char is "T" the selected pins are being stopped
        if(lastChar == 'T'){
          for(int i = 0; i<nrBanchi; i++){
            digitalWrite(pinStop[i], HIGH);
            delay(200);
            digitalWrite(pinStop[i], LOW);
          }
        } 

        for(int i = 0; i<sizeof(banchi)/sizeof(int); i++){
          int y = 0;
          while(numbers[y] != 0){

            //Debugging info printed on Arduino serial monitor
            Serial.print("Banchi: ");
            Serial.print(banchi[i]);
            Serial.print("  ==  Numbers: ");
            Serial.print(numbers[y]);
            Serial.print("  ");
            if(banchi[i] == numbers[y]){
              Serial.println("TRUE");
            }else{
              Serial.println("FALSE");
            }

            //Sets the selected pins in the desired mode [ S=UP, G=DOWN ]
            if(banchi[i] == numbers[y]){
              switch(lastChar){
                case 'S': 
                  digitalWrite(pinUp[i], LOW);
                  delay(200);
                  digitalWrite(pinUp[i], HIGH);
                  break;
                case 'G': 
                  digitalWrite(pinDown[i], LOW);
                  delay(200);
                  digitalWrite(pinDown[i], HIGH);
                  break;
              }
            }
            y++;
          }
        }
        fine:
        for(int i = 0; i<sizeof(numbers)/sizeof(int); i++){
          numbers[i] = 0;
        }
    } 
}
