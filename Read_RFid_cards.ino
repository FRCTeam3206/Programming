// Final copy to be of code for Robotics sign in/out moduale
#define TIME_HEADER "T"
#define TIME_REQUEST 7

#include <Wire.h>
#include <SPI.h>
#include <Adafruit_PN532.h>
#include <SoftwareSerial.h>
#include <SparkFunESP8266WiFi.h>

#define PN532_IRQ (2)
#define PN532_RESET (3)

Adafruit_PN532 nfc(PN532_IRQ, PN532_RESET);
#if defined(ARDUINO_ARCH_SAND)
  #define Serial SerialUSB
#endif

ESP8266Server server = ESP8266Server(80);
String htmlBody = "HelloWorld";
const String MASTER = "0000";
const String htmlHeader = "HTTP/1.1 200 OK\r\n"
                          "Content-Type: text/html\r\n"
                          "Connection: close\r\n\r\n"
                          "<!DOCTYPE HTML>\r\n"
                          "<html>\r\n";

void setup() {
  // put your setup code here, to run once:
  #ifndef ESP8266
    while (!Serial); // for Leonardo/Micro/Zero
  #endif
  Serial.begin(115200);
//  Serial.println("Hello!");
int test = esp8266.begin();
  while (test != true) {
    Serial.println("SparkFun ESP826 Shield not here");
    test = esp8266.begin();
  }
  Serial.println("SparkFun ESP8266 Shield is here");
  esp8266.setMode(ESP8266_MODE_AP);
  server.begin();
  IPAddress myIP = esp8266.localIP();
  Serial.print("Your IP is: ");
  Serial.println(myIP);
  
  pinMode(13, OUTPUT);
  nfc.begin();

  uint32_t versiondata = nfc.getFirmwareVersion();
  if (! versiondata) {
    Serial.print("Didn't find PN53x board");
    while (1); // halt
  }
  // Got ok data, print it out!
  Serial.print("Found chip PN5"); Serial.println((versiondata>>24) & 0xFF, HEX); 
  Serial.print("Firmware ver. "); Serial.print((versiondata>>16) & 0xFF, DEC); 
  Serial.print('.'); Serial.println((versiondata>>8) & 0xFF, DEC);
  
  // configure board to read RFID tags
  nfc.SAMConfig();
  
  Serial.println("Waiting for an ISO14443A Card ...");
}

uint8_t data[16];


void loop() {
  // put your main code here, to run repeatedly:
  
  delay(1000);
  uint8_t success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };
  uint8_t uidLength;
  boolean currentLineIsBlank = true;

  ESP8266Client client = server.available(500);
  
  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);

  if (success) {
    // Display some basic information about the card
    Serial.println("Found an ISO14443A card");
    Serial.print("  UID Length: ");Serial.print(uidLength, DEC);Serial.println(" bytes");
    Serial.print("  UID Value: ");
    nfc.PrintHex(uid, uidLength);

    if (uidLength >= 4) {
      uint8_t keya[6] = { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
      success = nfc.mifareclassic_AuthenticateBlock(uid, uidLength, 4, 0, keya);
      if (success) {
        Serial.println("Authenticated");
        uint8_t data[16];
//        memcpy(data, (const uint8_t[]){ "Hello World" }, sizeof data);
//        nfc.mifareclassic_WriteDataBlock (4,data);
        success = nfc.mifareclassic_ReadDataBlock (4, data);
        if (success) {
          nfc.PrintHexChar(data, 16);
          Serial.println(nfc.mifareclassic_ReadDataBlock (4, data));
          digitalWrite(13,HIGH);
          delay(2000);
          digitalWrite(13,LOW);
//          if (nfc.mifareclassic_ReadDataBlock (4, data) == MASTER) {
//            clientHTTP();
//          }
        }
      }
      else {
        Serial.println("Authentication Failed");
      }
    }
  }
}

void clientHTTP() {
  ESP8266Client client = server.available(500);

  IPAddress myIP = esp8266.localIP();
  Serial.print("Your IP is: ");
  Serial.println(myIP);

  if (client) 
  {
    Serial.println(F("Client Connected!"));
    // an http request ends with a blank line
    boolean currentLineIsBlank = true;
    while (client.connected()) 
    {
      if (client.available()) 
      {
        char c = client.read();
        // if you've gotten to the end of the line (received a newline
        // character) and the line is blank, the http request has ended,
        // so you can send a reply
        if (c == '\n' && currentLineIsBlank) 
        {
          Serial.println(F("Sending info"));
          // send a standard http response header:
          client.print(htmlHeader);
          
          // output the value of each analog input pin
          htmlBody += "\n";
          htmlBody += "</html>\n";
          client.print(htmlBody);
          
          break;
        }
        if (c == '\n') 
        {
          // you're starting a new line
          currentLineIsBlank = true;
        }
        else if (c != '\r') 
        {
          // you've gotten a character on the current line
          currentLineIsBlank = false;
        }
      }
    }
    // give the web browser time to receive the data
    delay(1);
   
    // close the connection:
    client.stop();
    Serial.println(F("Client disconnected"));
  }
}


