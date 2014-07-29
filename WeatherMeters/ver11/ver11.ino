#include <DHT.h>
#include "DHT.h"
#include <SPI.h>
#include <Ethernet.h>


#include <Wire.h>
#include <ArduinoNunchuk.h>
#include <stdlib.h>
#include <Servo.h>



#define BMP085_ADDRESS 0x77  // I2C address of BMP085
#define lm35_pin 13
#define dht11_pin 9
#define DHTPIN 9
#define DHTTYPE DHT22   // DHT 22  (AM2302)
#define ANEMOMETER_PIN 3
#define VANE_PIN 15
#define RAIN_GAUGE_PIN 2
#define ANEMOMETER_INT 1
#define VANE_PWR 4
#define RAIN_GAUGE_INT 0
#define uint  unsigned int
#define ulong unsigned long
#define SERVO_1_INT 2
#define SERVO_2_INT 3
#define SERVO_1_PIN 40
#define SERVO_2_PIN 41

DHT dht(DHTPIN, DHTTYPE);
int base_servo_position = 0;    // variable to store the servo position
int inner_servo_position = 80;

Servo myservo;  // create servo object to control a servo 
Servo myservo2;


// this must be unique
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};

// change to your network settings
IPAddress ip(192, 168, 1, 11);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

// change to your server
IPAddress server(192, 168, 1, 5); //Ip of the Computer running apache
EthernetServer srv(8888);                             //server port     
String readString;

//Change to your domain name for virtual servers
//char serverName[] = "www.google.com";
// If no domain name, use the ip address above
// char serverName[] = "74.125.227.16";

// change to your server's port
int serverPort = 80;

EthernetClient client;
int totalCount = 0;
int gustCount = 0;
char pageAdd[256];
char outBuf[2048];
char tBuf[16];


// set this to the number of milliseconds delay
// this is 30 seconds
#define delayMillis 160000UL
#define gustDelayMillis 30000L

unsigned long thisMillis = 0;
unsigned long lastMillis = 0;
unsigned long thisGustMillis = 0;
unsigned long lastGustMillis = 0;


int value0 = 0;
float dht_temp = 0;
float dht_humidity = 0;
float dht_temp_Fh = 0;

//BMP035 Global Variables
float temperature;
long pressure;
float altitude;

double windSpeed;
double gust;
double dir;
double knots;
double rain;



ulong nextCalcGust;
ulong nextCalcSpeed;                // When we next calc the wind speed
ulong nextCalcDir;                  // When we next calc the direction
ulong nextCalcRain;                  // When we next calc the rain drop
ulong time;                         // Millis() at each start of loop().

#define MSECS_CALC_WIND_SPEED 60000
#define MSECS_CALC_GUST 20000
#define MSECS_CALC_WIND_DIR   10000
#define MSECS_CALC_RAIN_FALL  90000

int gitEeeex;


void setup() {
  Serial.begin(9600);
  // disable SD SPI
  pinMode(4, OUTPUT);
  digitalWrite(4, HIGH);
  // Start ethernet
  Serial.println(F("Starting ethernet..."));
  Ethernet.begin(mac, ip, gateway, gateway, subnet);

  // If using dhcp, comment out the line above
  // and uncomment the next 2 lines

  // if(!Ethernet.begin(mac)) Serial.println(F("failed"));
  // else Serial.println(F("ok"));
  digitalWrite(10, HIGH);
  Serial.println(Ethernet.localIP());

  Wire.begin();
  bmp085Calibration();
  //-------------Weather Meters setup
  pinMode(ANEMOMETER_PIN, INPUT);
  digitalWrite(ANEMOMETER_PIN, HIGH); // Turn on the internal Pull Up Resistor
  pinMode(RAIN_GAUGE_PIN, INPUT);
  digitalWrite(RAIN_GAUGE_PIN, HIGH); // Turn on the internal Pull Up Resistor
  pinMode(VANE_PWR, OUTPUT);
  digitalWrite(VANE_PWR, LOW);
  //Specifies a named Interrupt Service Routine (ISR) to call when an interrupt occurs. Replaces any previous function that was attached to the interrupt.
  //Most Arduino boards have two external interrupts: numbers 0 (on digital pin 2) and 1 (on digital pin 3).
  attachInterrupt(RAIN_GAUGE_INT, rainGageClick, FALLING);
  attachInterrupt(ANEMOMETER_INT, anemometerClick, FALLING);
  //attachInterrupt(SERVO_1_INT,analogYchange,CHANGE);
 // attachInterrupt(SERVO_2_INT,analogXchange,CHANGE);
  interrupts();
  
//  nunchuk.init();
  myservo.attach(SERVO_1_PIN);  // attaches the servo on pin 2 to the servo object 
  myservo2.attach(SERVO_2_PIN);  
  
  nextCalcRain = millis() + MSECS_CALC_RAIN_FALL;
  nextCalcSpeed = millis() + MSECS_CALC_WIND_SPEED;
  nextCalcDir   = millis() + MSECS_CALC_WIND_DIR;
  Serial.print("Free ram is: ");
  Serial.println(freeRam());
  srv.begin();
  myservo.write(inner_servo_position);
   dht.begin();
  delay(1000);

}

char params[32];
// Use these for altitude conversions
const float p0 = 101325;     // Pressure at sea level (Pa)
const float p1 = 1013.25;     // Pressure at sea level (hPa)


void loop() {
  servoControl();

  thisMillis = millis();
  thisGustMillis = millis();

  if (thisGustMillis - lastGustMillis > gustDelayMillis) {
    unsigned long factor = thisGustMillis - lastGustMillis;
    lastGustMillis = thisGustMillis;

    gust =  getGust(factor);
    gust = gust * 0.868976;
    Serial.print("Gust: ");
    Serial.print(gust, 2);
    Serial.println();
    
    strcpy(outBuf, "GET /gustWriter.php?gust=");
    dtostrf(gust, 2, 2, tBuf);
    strcat(outBuf, tBuf);
    
    if (!getPage(server, serverPort, outBuf)) Serial.print(F("Fail "));
    else Serial.print(F("Pass "));
    gustCount++;
    Serial.print("Gust Passes: ");
    Serial.println(gustCount, DEC);
  }

  if (thisMillis - lastMillis > delayMillis)
  {
    unsigned long factor = thisMillis - lastMillis;
    lastMillis = thisMillis;
    Serial.println(lastMillis);
    value0 = analogRead(lm35_pin);
    value0 = (5.0 * value0 * 100.0) / 1024.0;
    humidity();
        Serial.println("DEBUGggggg 1");
    temperature = (bmp085GetTemperature(bmp085ReadUT())) * 0.1;
            Serial.println("DEBUGggggg 2");
    pressure = (bmp085GetPressure(bmp085ReadUP())) * 0.01;
            Serial.println("DEBUGggggg 3");
    //altitude = (float)44330 * (1 - pow(((float) pressure/p0), 0.190295));
    long tempPres = bmp085GetPressure(bmp085ReadUP());
            Serial.println("DEBUGggggg 4");
    altitude = (float)44330 * (1 - pow(((float) tempPres / p0), 0.190295));
            Serial.println("DEBUGggggg 5");
    float alt = ((1 -  pow(((float) pressure / p1), 0.190284)) * 145366.45);
            Serial.println("DEBUGggggg 6");


    gust =  getGust(factor);
    gust = gust * 0.868976;
    dir = getWindVane();
    rain = getUnitRain();
    windSpeed = getUnitWind(factor);
    knots = windSpeed * 0.868976;


    Serial.println();
    Serial.print("Temperature: ");
    Serial.print(temperature, 2);
    Serial.println(" C");
    Serial.print("Pressure: ");
    Serial.print(pressure);
    Serial.println(" hPa (millibars)");
    Serial.print("Altitude: ");
    Serial.print(altitude, 2);
    Serial.println(" m");
    Serial.print("Wind Speed: ");
    Serial.print(knots, 2);
    Serial.println(" knots");
    Serial.print("Gust: ");
    Serial.print(gust, 2);
    Serial.println(" knots");
    Serial.print("Dir: ");
    Serial.print(dir, 2);
    Serial.println(" Degrees");
    Serial.print("Rain: ");
    Serial.print(rain, 2);
    Serial.println(" mm");
    Serial.println();

    Serial.print(F("Free ram is: "));
    Serial.println(freeRam());

    strcpy(outBuf, "GET /write3.php?value0=");
    itoa(value0, tBuf, 10);
    strcat(outBuf, tBuf);

    strcat(outBuf, "&value1=");
    itoa(dht_humidity, tBuf, 10);
    strcat(outBuf, tBuf);

    strcat(outBuf, "&value2=");
    dtostrf(temperature, 2, 2, tBuf);
    //  itoa(temperature,tBuf,10);
    Serial.println(tBuf);
    strcat(outBuf, tBuf);

    strcat(outBuf, "&value3=");
    itoa(pressure, tBuf, 10);
    strcat(outBuf, tBuf);


    strcat(outBuf, "&value4=");
    itoa(altitude, tBuf, 10);
    strcat(outBuf, tBuf);

    strcat(outBuf, "&value5=");
    dtostrf(gust, 2, 2, tBuf);
    strcat(outBuf, tBuf);

    strcat(outBuf, "&value6=");
    dtostrf(dir, 2, 2, tBuf);
    strcat(outBuf, tBuf);

    strcat(outBuf, "&value7=");
    dtostrf(rain, 2, 2, tBuf);
    strcat(outBuf, tBuf);

    strcat(outBuf, "&value8=");
    dtostrf(knots, 2, 2, tBuf);
    strcat(outBuf, tBuf);

    if (!getPage(server, serverPort, outBuf)) Serial.print(F("Fail "));
    else Serial.print(F("Pass "));
    totalCount++;
    Serial.println(totalCount, DEC);
  }


}
//char *dtostrff (double val, signed char width, unsigned char prec, char *sout) {
//  char fmt[20];
//  sprintf(fmt, "%%%d.%df", width, prec);
//  sprintf(sout, fmt, val);
//  return sout;
//
//}

//void wiiNunchuk() {
//  nunchuk.update();
//  int analogX = nunchuk.analogX;
//  int analogY = nunchuk.analogY;
//  analogX = map(analogX, 0, 255, 0, 179);
//  analogY = map(analogY, 0, 255, 0, 179);
//  Serial.println(analogX);
//  myservo2.write(analogX);
//  myservo.write(analogY);
//  delay(30);
//
//}

void humidity() {
    // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  dht_humidity = dht.readHumidity();
  // Read temperature as Celsius
  dht_temp = dht.readTemperature();
  // Read temperature as Fahrenheit
  dht_temp_Fh = dht.readTemperature(true);
    if (isnan(dht_humidity) || isnan(dht_temp) || isnan(dht_temp_Fh)) {
      Serial.println("Failed to read from DHT sensor!");
      return;
    }
  Serial.print("Humidity: "); 
  Serial.print(dht_humidity);
  Serial.print(" %\t");
  Serial.print("Temperature: "); 
  Serial.print(dht_temp);
  Serial.print(" *C ");
}




int freeRam() {
  extern int __heap_start, *__brkval;
  int v;
  return (int)&v - (__brkval == 0 ? (int)&__heap_start : (int) __brkval);
}

byte getPage(IPAddress ipBuf, int thisPort, char *outBuf)
{

  int inChar;

  Serial.print(F("connecting..."));

  if (client.connect(ipBuf, thisPort))
  {
    Serial.println(F("connected"));

    Serial.print(F("outBuf length = "));
    Serial.println(strlen(tBuf), DEC);

    Serial.println("DEBUG___2");
    Serial.print(F("outBuf length = "));
    Serial.println(strlen(outBuf), DEC);
    client.write(outBuf);
    Serial.println("DEBUG___3");
    client.println(" HTTP/1.1");
    Serial.println("DEBUG___4");
    client.println("Host: 192.168.1.5");
    Serial.println("DEBUG___5");
    client.println("Connection: close");
    client.println();


    //    client.print("GET /write3.php?value0=");  //GET to php file which handle data (value0 and value1) into a database
    //    //Temperature
    //    client.print(value0);
    //    //Humidity
    //    client.print("&value1=");
    //    client.print(dht_humidity);
    //    //bmp085 values
    //    client.print("&value2=");
    //    client.print(temperature);
    //
    //    client.print("&value3=");
    //    client.print(pressure);
    //
    //    client.print("&value4=");
    //    client.print(altitude);
    //
    //    //Weather Meters (Wind speed, direction, rain)
    //    client.print("&value5=");
    //    client.print(gust); //Wind speed every 20 seconds
    //
    //    client.print("&value6=");
    //    client.print(dir);
    //
    //    client.print("&value7=");
    //    client.print(rain);
    //
    //    client.print("&value8=");
    //    client.print(knots); //Wind speed every 60 seconds
    //
    //      client.println(" HTTP/1.1");
    //    client.println("Host: 192.168.1.3");
    //    client.println("Connection: close");
    //    client.println();
  }
  else
  {
    Serial.println(F("failed"));
    return 0;
  }

  // connectLoop controls the hardware fail timeout
  int connectLoop = 0;

  while (client.connected())
  {
    while (client.available())
    {
      inChar = client.read();
      Serial.write(inChar);
      // set connectLoop to zero if a packet arrives
      connectLoop = 0;
    }

    connectLoop++;

    // if more than 10000 milliseconds since the last packet
    if (connectLoop > 10000)
    {
      // then close the connection from this end.
      Serial.println();
      Serial.println(F("Timeout"));
      client.stop();
    }
    // this is a delay for the connectLoop timing
    delay(1);
  }

  Serial.println();

  Serial.println(F("disconnecting."));
  // close client end
  client.stop();

  return 1;
}


const unsigned char OSS = 0;  // Oversampling Setting

// Calibration values
int ac1;
int ac2;
int ac3;
unsigned int ac4;
unsigned int ac5;
unsigned int ac6;
int b1;
int b2;
int mb;
int mc;
int md;

// b5 is calculated in bmp085GetTemperature(...), this variable is also used in bmp085GetPressure(...)
// so ...Temperature(...) must be called before ...Pressure(...).
long b5;






// Stores all of the bmp085's calibration values into global variables
// Calibration values are required to calculate temp and pressure
// This function should be called at the beginning of the program
void bmp085Calibration()
{
  ac1 = bmp085ReadInt(0xAA);
  ac2 = bmp085ReadInt(0xAC);
  ac3 = bmp085ReadInt(0xAE);
  ac4 = bmp085ReadInt(0xB0);
  ac5 = bmp085ReadInt(0xB2);
  ac6 = bmp085ReadInt(0xB4);
  b1 = bmp085ReadInt(0xB6);
  b2 = bmp085ReadInt(0xB8);
  mb = bmp085ReadInt(0xBA);
  mc = bmp085ReadInt(0xBC);
  md = bmp085ReadInt(0xBE);

}

// Calculate temperature given ut.
// Value returned will be in units of 0.1 deg C
short bmp085GetTemperature(unsigned int ut)
{
  long x1, x2;

  x1 = (((long)ut - (long)ac6) * (long)ac5) >> 15;
  x2 = ((long)mc << 11) / (x1 + md);
  b5 = x1 + x2;

  return ((b5 + 8) >> 4);
}

// Calculate pressure given up
// calibration values must be known
// b5 is also required so bmp085GetTemperature(...) must be called first.
// Value returned will be pressure in units of Pa.
long bmp085GetPressure(unsigned long up)
{
  long x1, x2, x3, b3, b6, p;
  unsigned long b4, b7;

  b6 = b5 - 4000;
  // Calculate B3
  x1 = (b2 * (b6 * b6) >> 12) >> 11;
  x2 = (ac2 * b6) >> 11;
  x3 = x1 + x2;
  b3 = (((((long)ac1) * 4 + x3) << OSS) + 2) >> 2;

  // Calculate B4
  x1 = (ac3 * b6) >> 13;
  x2 = (b1 * ((b6 * b6) >> 12)) >> 16;
  x3 = ((x1 + x2) + 2) >> 2;
  b4 = (ac4 * (unsigned long)(x3 + 32768)) >> 15;

  b7 = ((unsigned long)(up - b3) * (50000 >> OSS));
  if (b7 < 0x80000000)
    p = (b7 << 1) / b4;
  else
    p = (b7 / b4) << 1;

  x1 = (p >> 8) * (p >> 8);
  x1 = (x1 * 3038) >> 16;
  x2 = (-7357 * p) >> 16;
  p += (x1 + x2 + 3791) >> 4;

  return p;
}

// Read 1 byte from the BMP085 at 'address'
char bmp085Read(unsigned char address)
{
  unsigned char data;

  Wire.beginTransmission(BMP085_ADDRESS);
  Wire.write(address);
  Wire.endTransmission();

  Wire.requestFrom(BMP085_ADDRESS, 1);
  while (!Wire.available())
    ;

  return Wire.read();
}

// Read 2 bytes from the BMP085
// First byte will be from 'address'
// Second byte will be from 'address'+1
int bmp085ReadInt(unsigned char address)
{
  unsigned char msb, lsb;

  Wire.beginTransmission(BMP085_ADDRESS);
  Wire.write(address);
  Wire.endTransmission();

  Wire.requestFrom(BMP085_ADDRESS, 2);
  while (Wire.available() < 2)
    ;
  msb = Wire.read();
  lsb = Wire.read();
  return (int) msb << 8 | lsb;
}

// Read the uncompensated temperature value
unsigned int bmp085ReadUT()
{
  unsigned int ut;

  // Write 0x2E into Register 0xF4
  // This requests a temperature reading
  Wire.beginTransmission(BMP085_ADDRESS);
  Wire.write(0xF4);
  Wire.write(0x2E);
  Wire.endTransmission();

  // Wait at least 4.5ms
  delay(5);

  // Read two bytes from registers 0xF6 and 0xF7
  ut = bmp085ReadInt(0xF6);
  return ut;
}

// Read the uncompensated pressure value
unsigned long bmp085ReadUP()
{
  unsigned char msb, lsb, xlsb;
  unsigned long up = 0;

  // Write 0x34+(OSS<<6) into register 0xF4
  // Request a pressure reading w/ oversampling setting
  Wire.beginTransmission(BMP085_ADDRESS);
  Wire.write(0xF4);
  Wire.write(0x34 + (OSS << 6));
  Wire.endTransmission();

  // Wait for conversion, delay time dependent on OSS
  delay(2 + (3 << OSS));

  // Read register 0xF6 (MSB), 0xF7 (LSB), and 0xF8 (XLSB)
  Wire.beginTransmission(BMP085_ADDRESS);
  Wire.write(0xF6);
  Wire.endTransmission();
  Wire.requestFrom(BMP085_ADDRESS, 3);

  // Wait for data to become available
  while (Wire.available() < 3)
    ;
  msb = Wire.read();
  lsb = Wire.read();
  xlsb = Wire.read();

  up = (((unsigned long) msb << 16) | ((unsigned long) lsb << 8) | (unsigned long) xlsb) >> (8 - OSS);

  return up;
}






static int vaneValues[] PROGMEM =      {
  66, 84, 92, 127, 184, 244, 287, 406, 461, 600, 631, 702, 786, 827, 889, 946
};
static int vaneDirections[] PROGMEM = {
  1125, 675, 900, 1575, 1350, 2025, 1800, 225, 450, 2475, 2250, 3375, 0, 2925, 3150, 2700
};

double getWindVane()
{
  analogReference(DEFAULT);
  digitalWrite(VANE_PWR, HIGH);
  delay(100);
  for (int n = 0; n < 10; n++)
  {
    analogRead(VANE_PIN);
  }

  unsigned int reading = analogRead(VANE_PIN);
  //Serial.println("");
  //Serial.println(reading);
  digitalWrite(VANE_PWR, LOW);
  unsigned int lastDiff = 2048;

  for (int n = 0; n < 16; n++)
  {
    int diff = reading - pgm_read_word(&vaneValues[n]);
    diff = abs(diff);
    //Serial.println(diff);
    if (diff == 0)
      return pgm_read_word(&vaneDirections[n]) / 10.0;

    if (diff > lastDiff)
    {
      return pgm_read_word(&vaneDirections[n - 1]) / 10.0;
    }

    lastDiff = diff;
  }

  return pgm_read_word(&vaneDirections[15]) / 10.0;

}


#define WIND_FACTOR 2.4
#define TEST_PAUSE 60000

volatile unsigned long anem_count = 0;
volatile unsigned long anem_count_2 = 0;
volatile unsigned long anem_count_3 = 0;
volatile unsigned long anem_last = 0;
volatile unsigned long anem_min = 0xffffffff;



double getUnitWind(unsigned long factor) {
  unsigned long reading = anem_count;
  anem_count = 0;
  double result = (WIND_FACTOR * reading) / (factor / 1000);
  return result;
}


unsigned long thisMillis_2 = 0;
unsigned long lastMillis_2 = 0;

double getGust(unsigned long factor)
{
  unsigned long reading = anem_count;
  anem_count = 0;
  double result = (WIND_FACTOR * reading) / (factor / 1000);
  return result;
}
void anemometerClick() {
  long thisTime = micros() - anem_last;
  anem_last = micros();
  //Deboucning interrupts that occurs within 500μs or 0.0005s since the last interrupt
  if (thisTime > 500) {
    anem_count++;
    if (thisTime < anem_min) {
      anem_min = thisTime;
    }
  }
}


#define RAIN_FACTOR 0.2794

volatile unsigned long rain_count = 0;
volatile unsigned long rain_last = 0;

double getUnitRain()
{

  unsigned long reading = rain_count;
  rain_count = 0;
  double unit_rain = reading * RAIN_FACTOR;
  return unit_rain;
}

void rainGageClick()
{
  long thisTime = micros() - rain_last;
  rain_last = micros();
  if (thisTime > 500)
  {
    rain_count++;
  }
}


double dewPoint(double celsius, double humidity)
{
  // (1) Saturation Vapor Pressure = ESGG(T)
  double RATIO = 373.15 / (273.15 + celsius);
  double RHS = -7.90298 * (RATIO - 1);
  RHS += 5.02808 * log10(RATIO);
  RHS += -1.3816e-7 * (pow(10, (11.344 * (1 - 1 / RATIO ))) - 1) ;
  RHS += 8.1328e-3 * (pow(10, (-3.49149 * (RATIO - 1))) - 1) ;
  RHS += log10(1013.246);

  // factor -3 is to adjust units - Vapor Pressure SVP * humidity
  double VP = pow(10, RHS - 3) * humidity;

  // (2) DEWPOINT = F(Vapor Pressure)
  double T = log(VP / 0.61078); // temp var
  return (241.88 * T) / (17.558 - T);
}

// delta max = 0.6544 wrt dewPoint()
// 6.9 x faster than dewPoint()
// reference: http://en.wikipedia.org/wiki/Dew_point
double dewPointFast(double celsius, double humidity)
{
  double a = 17.271;
  double b = 237.7;
  double temp = (a * celsius) / (b + celsius) + log(humidity * 0.01);
  double Td = (b * temp) / (a - temp);
  return Td;
}

void servoControl() {
    // Create a client connection
  EthernetClient client = srv.available();
  if (client) {
    while (client.connected()) {   
      if (client.available()) {
        char c = client.read();
     
        //read char by char HTTP request
        if (readString.length() < 100) {
          //store characters to string
          readString += c;
          //Serial.print(c);
         }

         //if HTTP request has ended
         if (c == '\n') {          
           Serial.println(readString); //print to serial monitor for debuging
     
           client.println("HTTP/1.1 200 OK"); //send new page
           client.println("Content-Type: text/html");
           client.println();     
           client.println("<HTML>");
           client.println("<HEAD>");
           client.println("<meta name='apple-mobile-web-app-capable' content='yes' />");
           client.println("<meta name='apple-mobile-web-app-status-bar-style' content='black-translucent' />");
           client.println("<link rel='stylesheet' type='text/css' href='http://randomnerdtutorials.com/ethernetcss.css' />");
           client.println("<TITLE>Remote Camera Control</TITLE>");
           client.println("</HEAD>");
           client.println("<BODY>");
           client.println("<H1>Remote Camera Control</H1>");
           client.println("<hr />");
           client.println("<br />");  
           client.println("<H2>Arduino with Ethernet Shield</H2>");
           client.println("<br />");  
           client.println("<a href=\"/?button1on\"\">Rotate Base Servo Left</a>");
           client.println("<a href=\"/?button1off\"\">Rotate Base Servo Right</a><br />");   
           client.println("<br />");     
           client.println("<br />"); 
           client.println("<a href=\"/?button2on\"\">Rotate Inner Servo Left</a>");
           client.println("<a href=\"/?button2off\"\">Rotate Inner Servo Right</a><br />"); 
           client.println("<p>Created by Manos Kontakis.</p>");  
           client.println("<br />"); 
           client.println("</BODY>");
           client.println("</HTML>");
     
           delay(1);
           //stopping client
           client.stop();
           //controls the Arduino if you press the buttons

           //Base Servo
           if (readString.indexOf("?button1on") >0){
                for(base_servo_position = 0; base_servo_position < 160; base_servo_position += 3)  // goes from 0 degrees to 180 degrees 
                {                                  // in steps of 1 degree 
                  myservo2.write(base_servo_position);              // tell servo to go to position in variable 'pos' 
                  delay(15);                       // waits 15ms for the servo to reach the position 
                } 
           }
           if (readString.indexOf("?button1off") >0){
                for(base_servo_position = 160; base_servo_position>=1; base_servo_position-=3)     // goes from 180 degrees to 0 degrees 
                {                                
                  myservo2.write(base_servo_position);              // tell servo to go to position in variable 'pos' 
                  delay(15);                       // waits 15ms for the servo to reach the position 
                } 
           }
           //Inner Servo
           if (readString.indexOf("?button2on") >0){
               if (inner_servo_position >= 1 && inner_servo_position <= 170) {
                    if (inner_servo_position > 160) {
                      inner_servo_position -= 10;
                    }
                    inner_servo_position += 10;       
                    Serial.println(inner_servo_position);           
                    myservo.write(inner_servo_position);              // tell servo to go to position in variable 'pos' 
                    delay(15);                       // waits 15ms for the servo to reach the position 
               }
           }
           if (readString.indexOf("?button2off") >0){
                  if (inner_servo_position >= 1 && inner_servo_position <= 170) {
                    if (inner_servo_position > 10) {
                      inner_servo_position -= 10;
                    }
                    Serial.println(inner_servo_position);             
                    myservo.write(inner_servo_position);              // tell servo to go to position in variable 'pos' 
                    delay(15);                       // waits 15ms for the servo to reach the position 
                  }
           }
            //clearing string for next read
            readString="";  
           if (inner_servo_position > 170) {
             inner_servo_position = 170;
           } else if (inner_servo_position < 0) {
             inner_servo_position = 1;
           }
           
         }
       }
    }
}
}
