#include <SparkFunESP8266WiFi.h>
#include <SoftwareSerial.h>


ESP8266Server server = ESP8266Server(80);

const char *ssid = "ESPap";
const char *password = "thereisnospoon";

const char destServer[] = "";
const String htmlHeader = "HTTP/1.1 200 OK\r\n"
                          "Content-Type: text/html\r\n"
                          "Connection: close\r\n\r\n"
                          "<!DOCTYPE HTML>\r\n"
                          "<html>\r\n";

const String httpRequest = "GET / HTTP/1.1\n"
                           "Host: example.com\n"
                           "Connection: close\n\n";

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  Serial.println();
  Serial.println("press any key to begin");
  Serial.println();
//  while (!Serial.available())
//    ;
//  while (Serial.available())
//    Serial.read();
    
  int test = esp8266.begin();
  if (test != true)
  {
    Serial.println(F("Error talking to ESP8266."));
  }
  Serial.println(F("ESP8266 Shield Present"));

  esp8266.setMode(ESP8266_MODE_AP);

  
  server.begin();

  char connectedSSID[24];
  memset(connectedSSID, 0, 24);
  Serial.println(esp8266.getAP(connectedSSID));

  IPAddress myIP = esp8266.localIP();
  Serial.print(F("My IP: ")); Serial.println(myIP);

  
}
void handleRoot() {
  server.send(200, "text/html", "<h1>You are connected</h1>");
}

void loop() {
  // put your main code here, to run repeatedly:
}
