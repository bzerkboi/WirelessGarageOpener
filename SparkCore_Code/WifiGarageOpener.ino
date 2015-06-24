int garageSwitch = D7; //this connects to the onboard led
int garageCloseIndicator = D1;
int DOOR_ACTIVATION_PERIOD = 600; // [ms]
int garageClose;

void setup() {

    //Register the function to be called over the internet
    Spark.function("control",controlGarageDoor);

    //Expose the door status to the api
    Spark.variable("doorStatus",&garageClose, INT);

    //This pin will be used to send the signal to the relay to open/close
    //the garage door
    pinMode(garageSwitch, OUTPUT);

    //When we set up i want the reed switch to be connected indicating it is
    //HIGH and the garage is closed
    //We want to make our pin accept input from the Reed switch telling us
    //if the door is open or closed
    pinMode(garageCloseIndicator,INPUT);
    //digitalWrite(garageCloseIndicator,LOW); //This is assuming the garage is closed

    //check to see what the garage door thinks it is at
    garageClose=digitalRead(garageCloseIndicator);
    //We want to ensure the garage is closed by default at startup
    controlGarageDoor("close");

    //for testing set the switch to low to test the led
    //digitalWrite(garageSwitch, 0);
}

void loop()
{
  garageClose=digitalRead(garageCloseIndicator);
  //garageClose=1;
  delay(3000);
  //garageClose=0;
  //delay(3000);
}

int controlGarageDoor(String command)
{
  //command.toCharArray(commandInput,64);

  //We want to open or close the garage.
  int state = LOW;
  int retValue;

  //We may just want to send a signal to the garage regardless if its open or
  //closed

  //LOW means we are current open
  if((command=="open") && (garageClose==1))
  {
    state=HIGH;
    retValue=10;
    //digitalWrite(garageSwitch, HIGH); //for testing
  }
  else if((command=="close") && (garageClose==0)) //hitting close may also stop the garage mid motion
  {
    state=HIGH;
    retValue=20;
    //digitalWrite(garageSwitch, LOW); //for testing
  }
  else //if(command=="stop") in order to pulse the garage to stop we need another reed switch
  {
    retValue=30;
    state=LOW;
  }

  //If we are going to open to open or close the garage we want to stop
  //sending a high signal to the garage after 600ms (pusling the switch)
  if(state==HIGH)
  {
    // write to the appropriate pin
    digitalWrite(garageSwitch, state);
    delay(DOOR_ACTIVATION_PERIOD);
    digitalWrite(garageSwitch, LOW);
  }

  //return the state indicating if we opened or closed the garage
  return retValue;
}
