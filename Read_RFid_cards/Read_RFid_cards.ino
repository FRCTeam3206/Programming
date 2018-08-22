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

ESP8266Client client;
boolean done;
int cards[64];
int howManycards;
const int block = 4;
const uint8_t MASTER32[32] = { 00000000000000000000000000000000 };
const uint8_t MASTER16[16] = { 0000000000000000 };
const String htmlHeader = "HTTP/1.1 200 OK\r\n"
                          "Content-Type: text/html\r\n"
                          "Connection: close\r\n\r\n"
                          ""
                          "<html>\r\n"
                          "<body>\n"
                          "Hello World\n"
                          "</body>\n";

const char mySSID[] = "BubbleGum";
const char myPSK[] = "cleverowl99";
int out = 0;

char devid = "v4C5601DA106A350";  // THIS IS THE DEVICE ID FROM PUSHINGBOX

char server[] = "docs.google.com/forms/d/e/1FAIpQLSfq0tJQ63-IHm7X6fR5OZnQ2Z4tgColhc8dk4SlD7EbN6Oscg/viewform?usp=pp_url&entry.1307536738=";
char postmsg[100];

void setup() {
  // put your setup code here, to run once:
//  #ifndef ESP8266
//    while (!Serial); // for Leonardo/Micro/Zero
//  #endif
  Serial.begin(115200);
//  Serial.println("Hello!");
int test = esp8266.begin();
  while (test != true) {
    Serial.println("SparkFun ESP826 Shield not here");
    test = esp8266.begin();
  }
  Serial.println("SparkFun ESP8266 Shield is here");
  esp8266.setMode(ESP8266_MODE_STA);
  if (esp8266.status() <= 0)
  {
    while (esp8266.connect(mySSID, myPSK) < 0) {
      delay(1000);
    }
  }
  Serial.print("connected to ");
  Serial.println(mySSID);
//  client.begin();
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



void loop() {
  // put your main code here, to run repeatedly:
  
//  delay(1000);
//  ESP8266Client client = server.available(500);
//  RFIDRead();
//  clientHTTP();
}

void clientHTTP() {
  if(client.connect(server, 80)) {
    Serial.println("connected");
    Serial.println(client.read());
    sprintf(postmsg,"GET HTTP/1.1");  // NOTE** In this line of code you can see where the temperature value is inserted into the wed address. It follows 'status=' Change that value to whatever you want to post.
    
//    client.println(postmsg);
//    Serial.println(postmsg);
    
    client.print("Host: ");
    client.print(server);
    client.println("TeSt");
    Serial.print("Host: ");
    Serial.println(server);
    
//    client.println("Content-Type: application/x-www-form-urlencoded");
//    Serial.println("Content-Type: application/x-www-form-urlencoded");

    Serial.println(client.read());
    
    client.println("Connection: close");
    Serial.println("Connection: close");
    
    client.println();
    Serial.println();

 
     delay(1000);
     client.stop();
  }
}

void RFIDRead() {
  uint8_t success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };
  uint8_t uidLength;
  boolean currentLineIsBlank = true;
  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);

  if (success) {
    // Display some basic information about the card
    Serial.println("Found an ISO14443A card");
    Serial.print("  UID Length: ");Serial.print(uidLength, DEC);Serial.println(" bytes");
    Serial.print("  UID Value: ");
    nfc.PrintHex(uid, uidLength);
    uint8_t master[] = { 0x04, 0xFD, 0x8A, 0x7A, 0x29, 0x49, 0x80 };
    clientHTTP();
    delay(1000);
  }
}


