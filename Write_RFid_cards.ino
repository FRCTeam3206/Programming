#include <Wire.h>
#include <SPI.h>
#include <Adafruit_PN532.h>

#define PN532_IRQ (2)
#define PN532_RESET (3)

Adafruit_PN532 nfc(PN532_IRQ, PN532_RESET);
#if defined(ARDUINO_ARCH_SAND)
  #define Serial SerialUSB
#endif

const int block = 32;

void setup() {
  // put your setup code here, to run once:
  #ifndef ESP8266
    while (!Serial); // for Leonardo/Micro/Zero
  #endif
  Serial.begin(115200);
  Serial.println("Hello!");

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
int i = 0000;
void loop() {
  // put your main code here, to run repeatedly:
  uint8_t success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };
  uint8_t uidLength;
  

  
          
  

  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);
  if (success) {
    // Display some basic information about the card
    Serial.println("Found an ISO14443A card");
    Serial.print("  UID Length: ");Serial.print(uidLength, DEC);Serial.println(" bytes");
    Serial.print("  UID Value: ");
    nfc.PrintHex(uid, uidLength);

    if (uidLength >= 4) {
      uint8_t keya[6] = { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
      uint8_t data[16] = { 0x0E };
//      memcpy(data, (uint8_t[]){ i }, sizeof data);
//      i = i + 1;
      success = nfc.mifareclassic_AuthenticateBlock(uid, uidLength, block, 0, keya);
      if (success) {
         nfc.mifareclassic_WriteDataBlock (block, data);
          success = nfc.mifareclassic_ReadDataBlock (block, data);
            if (success) {
              nfc.PrintHexChar(data, 16);
//          delay(2000);
            
        }
      }
    }
  delay(3000);
  }
}

