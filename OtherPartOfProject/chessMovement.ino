//pins for left motor
int pwmPinLeftMotor = 10;
int outputPinTwo = 2;//signal pin for forward
int outputPinThree = 3;//signal pin for backward
//pins for right motor
int pwmPinRightMotor = 6;
int outputPinFour = 4;//signal pin for forward
int outputPinFive = 5;//signal pin for backward
int motorSpeed = 50;
int SHORT_DURATION = 1000;
int MEDIUM_DURATION = 2000;
int LONG_DURATION = 5000;
int EXTRA_LONG_DURATION = 10000;
byte receivedData;

/////////////////////////////////////////////////////////////

void setup() 
{

  //setting pins to be outputing signal to the TB6612FNG
  pinMode(pwmPinLeftMotor, OUTPUT);
  pinMode(pwmPinRightMotor, OUTPUT);
  pinMode(outputPinTwo, OUTPUT);
  pinMode(outputPinThree, OUTPUT);
  pinMode(outputPinFour, OUTPUT);
  pinMode(outputPinFive, OUTPUT);
  analogWrite(pwmPinLeftMotor, motorSpeed);
  analogWrite(pwmPinRightMotor, motorSpeed);
  Serial.begin(9600);

}

////////////////////////////////////////////////////////

void resetPins()
{
  digitalWrite(outputPinTwo, LOW);
  digitalWrite(outputPinThree, LOW);
  digitalWrite(outputPinFour, LOW);
  digitalWrite(outputPinFive, LOW);
}

void moveForward(int duration)
{
  digitalWrite(outputPinTwo, HIGH);
  digitalWrite(outputPinThree, LOW);
  digitalWrite(outputPinFour, HIGH);
  digitalWrite(outputPinFive, LOW);
  delay(duration);
}

void moveBackward(int duration)
{
  digitalWrite(outputPinTwo, LOW);
  digitalWrite(outputPinThree, HIGH);
  digitalWrite(outputPinFour, LOW);
  digitalWrite(outputPinFive, HIGH);
  delay(duration);
}

void moveLeftForward(int duration)
{
  digitalWrite(outputPinTwo, HIGH);
  digitalWrite(outputPinThree, LOW);
  digitalWrite(outputPinFour, LOW);
  digitalWrite(outputPinFive, LOW);
  delay(duration);
}

void moveRightBackward(int duration)
{
  digitalWrite(outputPinTwo, LOW);
  digitalWrite(outputPinThree, LOW);
  digitalWrite(outputPinFour, LOW);
  digitalWrite(outputPinFive, HIGH);
  delay(duration);
}

////////////////////////////////////////////////////

void pawnMovement()
{
  moveForward(SHORT_DURATION);
  moveBackward(SHORT_DURATION);
  resetPins();
}

void bishopMovement()
{
  moveLeftForward(SHORT_DURATION);
  moveForward(LONG_DURATION);
  moveBackward(EXTRA_LONG_DURATION);
  moveForward(LONG_DURATION);
  moveRightBackward(SHORT_DURATION);
  resetPins();
}

void kingMovement()
{
  moveForward(SHORT_DURATION);
  moveBackward(SHORT_DURATION);
  moveLeftForward(SHORT_DURATION);
  moveForward(SHORT_DURATION);
  moveBackward(SHORT_DURATION);
  moveRightBackward(SHORT_DURATION);
  resetPins();
}

void rookMovement()
{
  moveForward(LONG_DURATION);
  moveBackward(EXTRA_LONG_DURATION);
  moveForward(LONG_DURATION);
  moveLeftForward(MEDIUM_DURATION);
  moveForward(LONG_DURATION);
  moveBackward(EXTRA_LONG_DURATION);
  moveForward(LONG_DURATION);
  moveRightBackward(MEDIUM_DURATION);
  resetPins();
}

void knightMovement()
{
  moveForward(MEDIUM_DURATION);
  moveLeftForward(MEDIUM_DURATION);
  moveForward(SHORT_DURATION);
  moveBackward(SHORT_DURATION);
  moveRightBackward(MEDIUM_DURATION);
  moveBackward(MEDIUM_DURATION);
  resetPins();
}

void queenMovement()
{
  moveForward(LONG_DURATION);
  moveBackward(EXTRA_LONG_DURATION);
  moveForward(LONG_DURATION);
  moveLeftForward(SHORT_DURATION);
  moveForward(LONG_DURATION);
  moveBackward(EXTRA_LONG_DURATION);
  moveForward(LONG_DURATION);
  moveRightBackward(SHORT_DURATION);
  resetPins();
}

//////////////////////////////////////////////////////

void loop() 
{
  if(Serial.available() > 0)
  {
    receivedData = Serial.read();
    switch(receivedData)
    {
      case 49:
        pawnMovement();
        Serial.print('1');
        break;
      case 50:
        bishopMovement();
        Serial.print('1');
        break;
      case 51:
        kingMovement();
        Serial.print('1');
        break;
      case 52:
        rookMovement();
        Serial.print('1');
        break;
      case 53:
        knightMovement();
        Serial.print('1');
        break;
      case 54:
        queenMovement();
        Serial.print('1');
        break;
    }
  }
}