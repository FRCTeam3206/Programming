#include <SoftwareSerial.h>
#include <SparkFunESP8266WiFi.h>

const char mySSID[] = "Master Sword";
const char myPSK[] = "cleverowl88";



ESP8266Server server = ESP8266Server(80);

void setup() {
  Serial.begin(115200);
  int test = esp8266.begin();
  while (test != true) {
    Serial.println("Sield not here");
    test = esp8266.begin();
  }
  Serial.println("Shield is here");
  esp8266.setMode(ESP8266_MODE_STA);
  Serial.println(esp8266.connect(mySSID, myPSK));
  if (esp8266.status() <= 0)
  {
    while (esp8266.connect(mySSID, myPSK) < 0) {
      delay(1000);
    }
  }
  Serial.print("connected to ");
  Serial.println(mySSID);
  server.begin();
  IPAddress myIP = esp8266.localIP();
  Serial.print("Your IP is: ");
  Serial.println(myIP);
  Serial.println();
}

void loop() {
  ESP8266Client client = server.available(500);
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
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println("Connection: close");
          client.println();
          client.println("<!DOCTYPE HTML>");
          client.println("<html>");
          client.println("<meta http-equiv=\"refresh\" content=\"5\">");
          client.println("Hello World");
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
