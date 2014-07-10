// From http://home.comcast.net/~saustin98/misc/WeatherStationADC.txt

/* Arduino sketch for Weather device from Sparkfun.
Uses only the wind direction vane and the anemometer (not the rain gauge).

Although the inclination for a weather logger is to run it for
a long time, due to the way Wiring.c implements the millis() function,
this should be restarted, oh, monthly. The millis() functions overflows
after about 49 days. We could allow for that here, and handle the
wraparound, but you've got bigger problems anyway with the delay()
function at an overflow, so it's best to "reboot".

=========================================================
ANEMOMETER
=========================================================
This is connected to Arduino ground on one side, and pin 2 (for the
attachInterrupt(0, ...) on the other.
Pin 2 is pulled up, and the reed switch on the anemometer will send
that to ground once per revolution, which will trigger the interrupt.
We count the number of revolutions in 5 seconds, and divide by 5.
One Hz (rev/sec) = 1.492 mph.

=========================================================
WIND DIRECTION VANE
=========================================================
We use a classic voltage divider to measure the resistance in
the weather vane, which varies by direction.
Using a 10K resistor, our ADC reading will be:
   1023 * (R/(10000+R))
where R is the unknown resistance from the vane. We'll scale
the 1023 down to a 255 range, to match the datasheet docs.

                  +5V
                   |
                   <
                   >     10K
                   <   Resistor
                   <
                   >
                   |
 Analog Pin 5------|
                   |
                   -----------| To weather vane
                              | (mystery resistance)
                   -----------|
                   |
                   |
                 -----
                  ---
                   -
The ADC values we get for each direction (based on a 255 max)
follow, assuming that pointing away from the assembly center
is sector zero. The sector number is just which 45-degree sector
it is, clockwise from the "away" direction. The direction
shown is assuming that "away" is West. Depending how
you orient the system, you'll have to adjust the directions.

Sector   Reading  Direction
  0        18        W
  1        33        NW
  2        57        N
  7        97        SW
  3       139        NE
  6       183        S
  5       208        SE
  4       232        E
The values in the ADC table below list the midpoints between
these, so our reading can vary a bit. We'll pick the first value
that's >= our reading.
=========================================================
RAIN GAUGE
=========================================================
Implemented here.
It is done the same way as the anemometer, and use
attachInterrupt(1, ...) on pin 3. Each interrupt represents
.011 inches of rain (0.2794 mm), according to the docs.



*********************************************************************/

#define uint  unsigned int
#define ulong unsigned long

#define PIN_ANEMOMETER  3     // Digital 2
#define PIN_RAINGAUGE  2     // Digital 3
#define PIN_VANE        0     // Analog 5


// How often we want to calculate wind speed or direction
#define MSECS_CALC_WIND_SPEED 3000  
#define MSECS_CALC_WIND_DIR   3000
#define MSECS_CALC_RAIN_FALL  3000

volatile int numRevsAnemometer = 0; // Incremented in the interrupt
volatile int numDropsRainGauge = 0; // Incremented in the interrupt
ulong nextCalcSpeed;                // When we next calc the wind speed
ulong nextCalcDir;                  // When we next calc the direction
ulong nextCalcRain;                  // When we next calc the rain drop
ulong time;                         // Millis() at each start of loop().

// ADC readings:
#define NUMDIRS 8
ulong   adc[NUMDIRS] = {26, 45, 77, 118, 161, 196, 220, 256};

// These directions match 1-for-1 with the values in adc, but
// will have to be adjusted as noted above. Modify 'dirOffset'
// to which direction is 'away' (it's West here).
char *strVals[NUMDIRS] = {"W","NW","N","SW","NE","S","SE","E"};
byte dirOffset=0;

//=======================================================
// Initialize
//=======================================================
void setup() {
   Serial.begin(9600);
   pinMode(PIN_ANEMOMETER, INPUT);
   digitalWrite(PIN_ANEMOMETER, HIGH);
   digitalWrite(PIN_RAINGAUGE, HIGH);
      attachInterrupt(0, countRainGauge, FALLING);
   attachInterrupt(1, countAnemometer, FALLING);

   nextCalcRain = millis() + MSECS_CALC_RAIN_FALL;
   nextCalcSpeed = millis() + MSECS_CALC_WIND_SPEED;
   nextCalcDir   = millis() + MSECS_CALC_WIND_DIR;
}

//=======================================================
// Main loop.
//=======================================================
void loop() {
   time = millis();

   if (time >= nextCalcSpeed) {
      calcWindSpeed();
      nextCalcSpeed = time + MSECS_CALC_WIND_SPEED;
   }
   if (time >= nextCalcDir) {
      calcWindDir();
      nextCalcDir = time + MSECS_CALC_WIND_DIR;
   }
   if (time >= nextCalcRain) {
      calcRainFall();
      nextCalcRain = time + MSECS_CALC_RAIN_FALL;
   }

}

//=======================================================
// Interrupt handler for anemometer. Called each time the reed
// switch triggers (one revolution).
//=======================================================
void countAnemometer() {
   numRevsAnemometer++;
}

//=======================================================
// Interrupt handler for rain gauge. Called each time the reed
// switch triggers (one drop).
//=======================================================
void countRainGauge() {
   numDropsRainGauge++;
}


//=======================================================
// Find vane direction.
//=======================================================
void calcWindDir() {
   int val;
   byte x, reading;

   val = analogRead(PIN_VANE);
   Serial.println();
  // Serial.println("Value: ");
   Serial.println(val);
   val >>=2;                        // Shift to 255 range
   Serial.println(val);
   reading = val;
  // Serial.println(reading);

   // Look the reading up in directions table. Find the first value
   // that's >= to what we got.
   for (x=0; x<NUMDIRS; x++) {
      if (adc[x] >= reading)
         break;
   }
   //Serial.println(reading, DEC);
   x = (x + dirOffset) % 8;   // Adjust for orientation
   Serial.print("  Dir: ");
   Serial.println(strVals[x]);
}


//=======================================================
// Calculate the wind speed, and display it (or log it, whatever).
// 1 rev/sec = 1.492 mph = 2.40114125 kph
//=======================================================
void calcWindSpeed() {
   int x, iSpeed;
   // This will produce kph * 10
   // (didn't calc right when done as one statement)
   long speed = 24011;
   speed *= numRevsAnemometer;
   speed /= MSECS_CALC_WIND_SPEED;
   iSpeed = speed;         // Need this for formatting below

   Serial.print("Wind speed: ");
   x = iSpeed / 10;
   Serial.print(x);
   Serial.print('.');
   x = iSpeed % 10;
   Serial.print(x);
   Serial.println("");

   numRevsAnemometer = 0;        // Reset counter
}

//=======================================================
// Calculate the rain , and display it (or log it, whatever).
// 1 bucket = 0.2794 mm
//=======================================================
void calcRainFall() {
   int x, iVol;
   // This will produce mm * 10000
   // (didn't calc right when done as one statement)
   long vol = 2794; // 0.2794 mm
   vol *= numDropsRainGauge;
   vol /= MSECS_CALC_RAIN_FALL;
   iVol = vol;         // Need this for formatting below

   Serial.print("Rain fall: ");
   x = iVol / 10000;
   Serial.print(x);
   Serial.print('.');
   x = iVol % 10000;
   Serial.print(x);
   Serial.println();
   
   numDropsRainGauge = 0;        // Reset counter
}
