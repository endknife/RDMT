#define pin_up_one 6
#define pin_stop_one 7
#define pin_down_one 8

#define pin_up_two 3
#define pin_stop_two 4
#define pin_down_two 5

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

// VARIABILI GLOBALI CON DATI TRASMESSI
const String idAula = "B112";
const int banchi[] = {2,3,4};
const int nrBanchi = sizeof(banchi)/sizeof(int);

String firstValue;
int numbers[16]; // Supponiamo che ci siano al massimo 15 banchi + 1 perchè deve trovare uno zero il while
char lastChar;

void setup()
{
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

        //TROVA IL NUMERO DELL'AULA TRA I CANCELLETTI
        int firstHashIndex = receivedString.indexOf('#');
        int secondHashIndex = receivedString.indexOf('#', firstHashIndex + 1);
        if (firstHashIndex != -1 && secondHashIndex != -1) {
            firstValue = receivedString.substring(firstHashIndex + 1, secondHashIndex);
            Serial.print("Primo valore tra i cancelletti: ");
            Serial.println(firstValue);
        }

        /*
         * firstValue = valore stanza
         * lastChar = ultimo carattere
         * numbers[] = tutti i numeri
         */
        
        //TROVA L'ULTIMO CARATTERE PER LA MODALITA' DI MOVIMENTO DEI BANCHI
        lastChar = receivedString.charAt(receivedString.length() - 1);
        Serial.print("Ultimo carattere: ");
        Serial.println(lastChar);

        //TROVA I NUMERI DEI BANCHI SELEZIONATI
        //estrapola i numeri
        int SecondHashIndex = receivedString.indexOf('#', receivedString.indexOf('#') + 1);
        
        // Trova la posizione del ;
        int lastSemicolonIndex = receivedString.lastIndexOf(';');

        if (SecondHashIndex != -1 && lastSemicolonIndex != -1) {
            String numbersString = receivedString.substring(SecondHashIndex + 1, lastSemicolonIndex);
            
            // Split della stringa in numeri separati da ;
            
            int index = 0;
            char* token = strtok((char*)numbersString.c_str(), ";");
            while (token != NULL && index < sizeof(numbers)) {
                numbers[index++] = atoi(token);
                token = strtok(NULL, ";");
            }

            // Stampa i numeri estratti
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
