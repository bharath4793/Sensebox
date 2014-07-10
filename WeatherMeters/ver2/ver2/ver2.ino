#define ANEMOMETER_PIN 3
#define ANEMOMETER_INT 1
#define VANE_PWR 4
#define VANE_PIN A15
#define RAIN_GAUGE_PIN 2
#define RAIN_GAUGE_INT 0

#define uint  unsigned int
#define ulong unsigned long
 
ulong nextCalcSpeed;                // When we next calc the wind speed
ulong nextCalcDir;                  // When we next calc the direction
ulong nextCalcRain;                  // When we next calc the rain drop
ulong time;                         // Millis() at each start of loop().

#define MSECS_CALC_WIND_SPEED 60000
#define MSECS_CALC_WIND_DIR   2000
#define MSECS_CALC_RAIN_FALL  90000
 
void setup()
{
  Serial.begin(9600);
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

void loop() {
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
