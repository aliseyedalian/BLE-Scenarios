#include <SoftwareSerial.h>
#include <dht11.h>
#define DHT11PIN 5
dht11 DHT11;
SoftwareSerial BTSerial(2, 3); //RX,TX 
char incomingData; // To hold value of data coming through BLE
char outputingData;
char ATCOMMAND[2][15]={"AT","AT+DEFAULT"};
char strList[100][10]={"salam-","morad-","hamta-","odbar-","pedro-","efghi-","idham-","oodro-","parsi-","Teran-",
                       "99883-","54321-","95195-","19123-" ,"75532-","42311-","54362-","56654-","42314-","13451-",
                       "ebram-","quran-","Isagh-","KermR-","Risma-","oodak-","Zebra-","Havij-","DATAN-","oject-",
                       "45600-","54213-","54374-","09123-" ,"17720-","08642-","19191-","42345-","76876-","23214-",
                       "medal-","golab-","IRANI-","Seman-","Bravo-","abcde-","mahdi-","ordoo-","FARSI-","ehran-",
                       "23421-","12345-","44444-","09123-" ,"75532-","42311-","54362-","56654-","42314-","13451-",
                       "javad-","winow-","WIoij-","dAbir-","Jomid-","pirlo-","naldo-","messi-","tariz-","BYour-",
                       "42121-","98132-","42141-","23231-" ,"00000-","53499-","76544-","31224-","98763-","23123-",
                       "moham-","kerem-","tibal-","farda-","krain-","majid-","wbtel-","olaie-","ASTAN-","amyab-",
                       "53912-","21310-","54374-","46782-" ,"09390-","02323-","98765-","91452-","91352-","BYE00-"};


void setup() {
  Serial.begin(9600); //9600 
  delay(400);
  Serial.println("Enter AT commands:");
  BTSerial.begin(9600);  //9600
  //BTSerial.write("AT+DEFAULT" );
//  for(int i=0;i<2;i++){
//    BTSerial.write(ATCOMMAND[i]);
//    delay(100);
//  } 
}



void loop() {
  BTSerial.listen();
  int chk = DHT11.read(DHT11PIN);

  if(BTSerial.available()) {
    int counter = 0;
    incomingData = BTSerial.read();
    Serial.write(incomingData);
    switch (incomingData){
      case '$':
          for(int counter = 0;counter<20;counter++){
            for(int i=0;i<100;i++){
              BTSerial.write(strList[i]); 
            }    
          }
          Serial.write("*");
          BTSerial.print("****************************"); //determines the end
          break;
      case '%':
          BTSerial.println((int)DHT11.humidity);
          break;
      default:
          outputingData = incomingData;
          BTSerial.write(outputingData);
          break;
    }
  }
  if(Serial.available()) {
    BTSerial.write(Serial.read());
  }
}
