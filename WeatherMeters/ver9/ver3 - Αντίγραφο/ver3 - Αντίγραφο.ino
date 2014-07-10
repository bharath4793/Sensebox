#include <SPI.h>
#include <Ethernet.h>
#include <dht11.h>
#include <Wire.h>

#define BMP085_ADDRESS 0x77  // I2C address of BMP085

#define lm35_pin 13
#define dht11_pin 9
#define ANEMOMETER_PIN 3
#define VANE_PIN A0
#define RAIN_GAUGE_PIN 2

#define ANEMOMETER_INT 1
#define VANE_PWR 4
#define RAIN_GAUGE_INT 0
#define uint  unsigned int
#define ulong unsigned long



// this must be unique
byte mac[] = {  
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

// change to your network settings
IPAddress ip(192,168,1,6);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

// change to your server
IPAddress server(192, 168, 1, 8); //Ip of the Computer running apache

//Change to your domain name for virtual servers
//char serverName[] = "www.google.com";
// If no domain name, use the ip address above
// char serverName[] = "74.125.227.16";

// change to your server's port
int serverPort = 80;

EthernetClient client;
int totalCount = 0;
char pageAdd[64];


// set this to the number of milliseconds delay
// this is 30 seconds
#define delayMillis 30000UL

unsigned long thisMillis = 0;
unsigned long lastMillis = 0;

dht11 DHT;
int value0 = 0;
int dht_temp = 0;
int dht_humidity = 0;



 
ulong nextCalcSpeed;                // When we next calc the wind speed
ulong nextCalcDir;                  // When we next calc the direction
ulong nextCalcRain;                  // When we next calc the rain drop
ulong time;                         // Millis() at each start of loop().

#define MSECS_CALC_WIND_SPEED 60000
#define MSECS_CALC_WIND_DIR   10000
#define MSECS_CALC_RAIN_FALL  90000
 

void setup() {
  Serial.begin(9600);

  // disable SD SPI
  pinMode(4,OUTPUT);
  digitalWrite(4,HIGH);

  // Start ethernet
  Serial.println(F("Starting ethernet..."));
  Ethernet.begin(mac, ip, gateway, gateway, subnet);

  // If using dhcp, comment out the line above 
  // and uncomment the next 2 lines

  // if(!Ethernet.begin(mac)) Serial.println(F("failed"));
  // else Serial.println(F("ok"));
  digitalWrite(10,HIGH);

  Serial.println(Ethernet.localIP());

  delay(2000);
  Serial.println(F("Ready"));
  
  Wire.begin();
  bmp085Calibration();
  
  //-------------Weather Meters setup
  pinMode(ANEMOMETER_PIN,INPUT);
  digitalWrite(ANEMOMETER_PIN,HIGH);  // Turn on the internal Pull Up Resistor
  pinMode(RAIN_GAUGE_PIN,INPUT);
  digitalWrite(RAIN_GAUGE_PIN,HIGH);  // Turn on the internal Pull Up Resistor
  pinMode(VANE_PWR,OUTPUT);
  digitalWrite(VANE_PWR,LOW);
  //Specifies a named Interrupt Service Routine (ISR) to call when an interrupt occurs. Replaces any previous function that was attached to the interrupt. 
  //Most Arduino boards have two external interrupts: numbers 0 (on digital pin 2) and 1 (on digital pin 3). 
  attachInterrupt(RAIN_GAUGE_INT,rainGageClick,FALLING);
  attachInterrupt(ANEMOMETER_INT,anemometerClick,FALLING);
  interrupts();
  nextCalcRain = millis() + MSECS_CALC_RAIN_FALL;
  nextCalcSpeed = millis() + MSECS_CALC_WIND_SPEED;
  nextCalcDir   = millis() + MSECS_CALC_WIND_DIR;
  

}





void loop()
{
  thisMillis = millis();

  if(thisMillis - lastMillis > delayMillis)
  {
    lastMillis = thisMillis;

    // Modify next line to load different page
    // or pass values to server
    sprintf(pageAdd,"/",totalCount);

    // sprintf(pageAdd,"/arduino.php?test=%u",totalCount);
      value0 = analogRead(lm35_pin);
      value0 = (5.0 * value0 * 100.0)/1024.0;
      humidity();
    if(!getPage(server,serverPort,pageAdd)) Serial.print(F("Fail "));
    else Serial.print(F("Pass "));
    totalCount++;
    Serial.println(totalCount,DEC);
  }
  bmp085_loop();  
  meters_loop();
}

void humidity() {
   int chk;
  Serial.print("DHT11, \t");
  chk = DHT.read(dht11_pin);    // READ DATA
  switch (chk){
    case DHTLIB_OK:  
                Serial.print("OK,\t"); 
                break;
    case DHTLIB_ERROR_CHECKSUM: 
                Serial.print("Checksum error,\t"); 
                break;
    case DHTLIB_ERROR_TIMEOUT: 
                Serial.print("Time out error,\t"); 
                break;
    default: 
                Serial.print("Unknown error,\t"); 
                break;
  }
 // DISPLAT DATA
  Serial.print(DHT.humidity,1);
  Serial.print(",\t");
  Serial.print(DHT.temperature,1);
  Serial.print(",\t");
  Serial.print(dewPoint(DHT.temperature, DHT.humidity));
  Serial.print(",\t");
  Serial.println(dewPointFast(DHT.temperature, DHT.humidity));
  dht_humidity = DHT.humidity;
  dht_temp = DHT.temperature;
  delay(1000);
}







byte getPage(IPAddress ipBuf,int thisPort, char *page)
{
  int inChar;
  char outBuf[128];

  Serial.print(F("connecting..."));

  if(client.connect(ipBuf,thisPort))
  {
    Serial.println(F("connected"));
    client.print("GET /write2.php?value0=");  //GET to php file which handle data (value0 and value1) into a database
    client.print(value0); //Temperature
    client.print("&value1=");
    client.print(dht_humidity); //Humidity
    client.println(" HTTP/1.1");
    client.println("Host: 46.198.212.78");  //webserver site with php file (www.asrdff.com/write.php)
    client.println();
  } 
  else
  {
    Serial.println(F("failed"));
    return 0;
  }

  // connectLoop controls the hardware fail timeout
  int connectLoop = 0;

  while(client.connected())
  {
    while(client.available())
    {
      inChar = client.read();
      Serial.write(inChar);
      // set connectLoop to zero if a packet arrives
      connectLoop = 0;
    }

    connectLoop++;

    // if more than 10000 milliseconds since the last packet
    if(connectLoop > 10000)
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

float temperature;
long pressure;

// Use these for altitude conversions
const float p0 = 101325;     // Pressure at sea level (Pa)
const float p1 = 1013.25;     // Pressure at sea level (hPa)
float altitude;



void bmp085_loop()
{
  temperature = (bmp085GetTemperature(bmp085ReadUT())) * 0.1;
  pressure = (bmp085GetPressure(bmp085ReadUP())) *0.01;
  //altitude = (float)44330 * (1 - pow(((float) pressure/p0), 0.190295));
  long tempPres = bmp085GetPressure(bmp085ReadUP());
  altitude = (float)44330 * (1 - pow(((float) tempPres/p0), 0.190295));
  float alt = ((1 -  pow(((float) pressure/p1), 0.190284)) * 145366.45);

  Serial.print("Temperature: ");
  Serial.print(temperature, 2);
  Serial.println(" C");
  Serial.print("Pressure: ");
  Serial.print(pressure, DEC);
  Serial.println(" hPa (millibars)");
  Serial.print("Altitude: ");
  Serial.print(altitude, 2);
  Serial.println(" m");
  Serial.println();
  
  delay(10000);
}

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
  
  x1 = (((long)ut - (long)ac6)*(long)ac5) >> 15;
  x2 = ((long)mc << 11)/(x1 + md);
  b5 = x1 + x2;

  return ((b5 + 8)>>4);  
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
  x1 = (b2 * (b6 * b6)>>12)>>11;
  x2 = (ac2 * b6)>>11;
  x3 = x1 + x2;
  b3 = (((((long)ac1)*4 + x3)<<OSS) + 2)>>2;
  
  // Calculate B4
  x1 = (ac3 * b6)>>13;
  x2 = (b1 * ((b6 * b6)>>12))>>16;
  x3 = ((x1 + x2) + 2)>>2;
  b4 = (ac4 * (unsigned long)(x3 + 32768))>>15;
  
  b7 = ((unsigned long)(up - b3) * (50000>>OSS));
  if (b7 < 0x80000000)
    p = (b7<<1)/b4;
  else
    p = (b7/b4)<<1;
    
  x1 = (p>>8) * (p>>8);
  x1 = (x1 * 3038)>>16;
  x2 = (-7357 * p)>>16;
  p += (x1 + x2 + 3791)>>4;
  
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
  while(!Wire.available())
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
  while(Wire.available()<2)
    ;
  msb = Wire.read();
  lsb = Wire.read();
  return (int) msb<<8 | lsb;
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
  Wire.write(0x34 + (OSS<<6));
  Wire.endTransmission();
  
  // Wait for conversion, delay time dependent on OSS
  delay(2 + (3<<OSS));
  
  // Read register 0xF6 (MSB), 0xF7 (LSB), and 0xF8 (XLSB)
  Wire.beginTransmission(BMP085_ADDRESS);
  Wire.write(0xF6);
  Wire.endTransmission();
  Wire.requestFrom(BMP085_ADDRESS, 3);
  
  // Wait for data to become available
  while(Wire.available() < 3)
    ;
  msb = Wire.read();
  lsb = Wire.read();
  xlsb = Wire.read();
  
  up = (((unsigned long) msb << 16) | ((unsigned long) lsb << 8) | (unsigned long) xlsb) >> (8-OSS);
  
  return up;
}




void meters_loop() {
   time = millis();

   if (time >= nextCalcSpeed) {
      double windSpeed = getUnitWind();
      Serial.print("Wind speed: ");
       Serial.print(windSpeed);
       Serial.print("  Wind speed in knots: ");
       double knots = windSpeed * 0.868976;
       Serial.println(knots);
      nextCalcSpeed = time + MSECS_CALC_WIND_SPEED;
   }
   if (time >= nextCalcDir) {
     double dir;
     dir = getWindVane();
      nextCalcDir = time + MSECS_CALC_WIND_DIR;
      Serial.print("Direction ");
      Serial.print(dir);
      Serial.print("          ");

      double gust =  getGust();
      Serial.print("  Gust is: ");
      Serial.print(gust);
      
       //calcWindSpeed();
      Serial.print("          ");
      Serial.print("ver2 function: ");
      calcWindSpeed();
      Serial.println();
   }
   if (time >= nextCalcRain) {
      double rain = getUnitRain();
      Serial.print("Rain Fall: ");
       Serial.println(rain);
      nextCalcRain = time + MSECS_CALC_RAIN_FALL;
   }

}



static int vaneValues[] PROGMEM=      {66, 84, 92, 127, 184, 244, 287,406,461,600, 631, 702,786,827, 889, 946};
static int vaneDirections[] PROGMEM={1125,675,900,1575,1350,2025,1800,225,450,2475,2250,3375,0,2925,3150,2700};
 
double getWindVane()
{
  analogReference(DEFAULT);
  digitalWrite(VANE_PWR,HIGH);
  delay(100);
  for(int n=0;n<10;n++)
  {
    analogRead(VANE_PIN);
  }
 
  unsigned int reading=analogRead(VANE_PIN);
  //Serial.println("");
  //Serial.println(reading);
  digitalWrite(VANE_PWR,LOW);
  unsigned int lastDiff=2048;
 
  for (int n=0;n<16;n++)
  {
    int diff=reading-pgm_read_word(&vaneValues[n]);
    diff=abs(diff);
    //Serial.println(diff);
    if(diff==0)
       return pgm_read_word(&vaneDirections[n])/10.0;
 
    if(diff>lastDiff)
    {
      return pgm_read_word(&vaneDirections[n-1])/10.0;
    }
 
    lastDiff=diff;
 }
 
  return pgm_read_word(&vaneDirections[15])/10.0;
 
}


#define WIND_FACTOR 2.4
#define TEST_PAUSE 60000
 
volatile unsigned long anem_count=0;
volatile unsigned long anem_count_2=0;
volatile unsigned long anem_last=0;
volatile unsigned long anem_min=0xffffffff;
 
double getUnitWind()
{
  unsigned long reading=anem_count;
  Serial.print("Anem_count: ");
  Serial.print(reading);
  Serial.println("");
  anem_count=0;
  return (WIND_FACTOR*reading)/(MSECS_CALC_WIND_SPEED/1000);
}


void calcWindSpeed() {
   int x, iSpeed;
   // This will produce kph * 10
   // (didn't calc right when done as one statement)
   long speed = 24011;
   speed *= anem_count_2;
   speed /= MSECS_CALC_WIND_DIR;
   iSpeed = speed;         // Need this for formatting below

   //Serial.print("Wind speed: ");
   x = iSpeed / 10;
   Serial.print(x);
   Serial.print('.');
   x = iSpeed % 10;
   Serial.print(x);
   Serial.println("");

   anem_count_2 = 0;        // Reset counter
}
 
double getGust()
{
 
  unsigned long reading=anem_min;
  Serial.print("Anem_min: ");
  Serial.print(reading);
  anem_min=0xffffffff;
  Serial.print("        ");
  double time=reading/1000000.0;
  
  return (1/(reading/1000000.0))*WIND_FACTOR;
}
 
void anemometerClick()
{
  long thisTime=micros()-anem_last;
  anem_last=micros();
  //Deboucning interrupts that occurs within 500μs or 0.0005s since the last interrupt
  if(thisTime>500)
  {
    anem_count++;
    anem_count_2++;
    //Serial.println("Click");
    //Serial.println("");
    if(thisTime<anem_min)
    {
      anem_min=thisTime;
    }
 
  }
}


#define RAIN_FACTOR 0.2794
 
volatile unsigned long rain_count=0;
volatile unsigned long rain_last=0;
 
double getUnitRain()
{
 
  unsigned long reading=rain_count;
  rain_count=0;
  double unit_rain=reading*RAIN_FACTOR;
 
  return unit_rain;
}
 
void rainGageClick()
{
    long thisTime=micros()-rain_last;
    rain_last=micros();
    if(thisTime>500)
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
	RHS += -1.3816e-7 * (pow(10, (11.344 * (1 - 1/RATIO ))) - 1) ;
	RHS += 8.1328e-3 * (pow(10, (-3.49149 * (RATIO - 1))) - 1) ;
	RHS += log10(1013.246);

        // factor -3 is to adjust units - Vapor Pressure SVP * humidity
	double VP = pow(10, RHS - 3) * humidity;

        // (2) DEWPOINT = F(Vapor Pressure)
	double T = log(VP/0.61078);   // temp var
	return (241.88 * T) / (17.558 - T);
}

// delta max = 0.6544 wrt dewPoint()
// 6.9 x faster than dewPoint()
// reference: http://en.wikipedia.org/wiki/Dew_point
double dewPointFast(double celsius, double humidity)
{
	double a = 17.271;
	double b = 237.7;
	double temp = (a * celsius) / (b + celsius) + log(humidity*0.01);
	double Td = (b * temp) / (a - temp);
	return Td;
}



