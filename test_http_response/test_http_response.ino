#include <SoftwareSerial.h>
#include <SparkFunESP8266WiFi.h>

ESP8266Server server = ESP8266Server(80);

String htmlBody;
const char *ssid = "ESPap";
const char *password = "thereisnospoon";

const String htmlHeader = "HTTP/1.1 200 OK\r\n"
                          "Content-Type: text/html\r\n"
                          "Connection: close\r\n\r\n"
                          "<!DOCTYPE HTML>\r\n"
                          "<html>\r\n";

const String httpRequest = "GET / HTTP/1.1\n"
                           "Host: example.com\n"
                           "Connection: close\n\n";
void setup() {
  Serial.begin(115200);
  int test = esp8266.begin();
  while (test != true) {
    Serial.println("Sield not here");
    test = esp8266.begin();
  }
  Serial.println("Shield is here");
  esp8266.setMode(ESP8266_MODE_AP);
  server.begin();
  IPAddress myIP = esp8266.localIP();
  Serial.print("Your IP is: ");
  Serial.println(myIP);
  htmlBody += "test";
}
boolean currentLineIsBlank = true;

void loop() {
  ESP8266Client client = server.available(500);
  if (client)
    while (client.connected())
      httpResponse(client);
}
void httpResponse(ESP8266Client client) {
  
    if (client.available()) {
      char c = client.read();
      if (c == '\n' && currentLineIsBlank) {
        client.print(htmlHeader);
        htmlBody += "test";
        htmlBody += "\n";
        htmlBody += "</html>\n";
        client.print(htmlBody);
        if (c == '\n') {
          // you're starting a new line
          currentLineIsBlank = true;
        } else if (c != '\r') {
          // you've gotten a character on the current line
        currentLineIsBlank = false;
       }
     }
   }
  delay(1);
  client.stop();
}

