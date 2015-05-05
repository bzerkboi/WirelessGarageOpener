int garageSwitch = D7; //this connects to the onboard led and to the rela
int garageTest=D7;
int garageCloseIndicator = D1;
int DOOR_ACTIVATION_PERIOD = 600; // [ms]
int garageClose;
int calibrateTime = 10000;      // wait for the thingy to calibrate
int newGarageClose;

unsigned long lastTime = 0UL;
void setup() {

    //Register the function to be called over the internet
    Spark.function("control",controlGarageDoor);

    //Expose the door status to the api <-- Lets try to expose this variable throught a publish
    //Spark.variable("doorStatus",&garageClose, INT);

    //This pin will be used to send the signal to the relay to open/close
    //the garage door
    pinMode(garageSwitch, OUTPUT);


    //When we set up i want the reed switch to be connected indicating it is
    //HIGH and the garage is closed
    //We want to make our pin accept input from the Reed switch telling us
    //if the door is open or closed
    pinMode(garageCloseIndicator,INPUT_PULLUP);

    //For Testing
    //digitalWrite(garageCloseIndicator,LOW); //This is assuming the garage is closed

    //check to see what the garage door thinks it is at
    garageClose=digitalRead(garageCloseIndicator);
    //We want to ensure the garage is closed by default at startup
    //controlGarageDoor("close");

    //for testing set the switch to low to test the led
    //digitalWrite(garageSwitch, 0);
}

void loop()
{
  if (calibrated()) {
    readTheSensor();
    reportTheData();
  }
}

bool calibrated() {
  return millis() - calibrateTime > 0;
}

void readTheSensor()
{
    newGarageClose=digitalRead(garageCloseIndicator);
}

void reportTheData()
{
  if (newGarageClose != garageClose)
  {
    Spark.publish("GarageMoved",String(garageClose));
    garageClose=newGarageClose;
  }

}

//This method will tell the garage to move, either open or close
//It will return an int telling us if we actually moved or not
int controlGarageDoor(String command)
{
  //command.toCharArray(commandInput,64);

  int retValue; //a 1 tells us we moved a 0 tells us we didn't

  //We may just want to send a signal to the garage regardless if its open or
  //closed

  if(command == "move")
  {
    // write to the appropriate pin
    digitalWrite(garageSwitch, HIGH);
    delay(DOOR_ACTIVATION_PERIOD);
    digitalWrite(garageSwitch, LOW);
    return 1;
  }

  return 0;

  //return the state indicating if we opened or closed the garage
  return retValue;
}
