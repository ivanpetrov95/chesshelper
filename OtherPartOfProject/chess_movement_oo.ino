#include "MotorsControl.h"
#include "FiguresMovement.h"
//pins for left motor
int pwmPinLeftMotor = 10;
int outputPinTwo = 2;    //signal pin for forward
int outputPinThree = 3;  //signal pin for backward
//pins for right motor
int pwmPinRightMotor = 6;
int outputPinFour = 4;  //signal pin for forward
int outputPinFive = 5;  //signal pin for backward
int motorSpeed = 50;
int SHORT_DURATION = 1000;
int MEDIUM_DURATION = 2000;
int LONG_DURATION = 5000;
int EXTRA_LONG_DURATION = 10000;
byte receivedData;


/////////////////////////////////////////////////////////////

void setup() {
  //setting pins to be outputing signal to the TB6612FNG
  pinMode(pwmPinLeftMotor, OUTPUT);
  pinMode(pwmPinRightMotor, OUTPUT);
  analogWrite(pwmPinLeftMotor, motorSpeed);
  analogWrite(pwmPinRightMotor, motorSpeed);
  Serial.begin(9600);
}

MotorsControl motorsControl(outputPinTwo, outputPinThree, outputPinFour, outputPinFive);
FiguresMovement figuresMovement(SHORT_DURATION, MEDIUM_DURATION, LONG_DURATION, EXTRA_LONG_DURATION);


////////////////////////////////////////////////////////

void loop() {
  if (Serial.available() > 0) {
    receivedData = Serial.read();
    switch (receivedData) {
      case 49:
        figuresMovement.pawnMovement();
        Serial.print('1');
        break;
      case 50:
        figuresMovement.bishopMovement();
        Serial.print('1');
        break;
      case 51:
        figuresMovement.kingMovement();
        Serial.print('1');
        break;
      case 52:
        figuresMovement.rookMovement();
        Serial.print('1');
        break;
      case 53:
        figuresMovement.knightMovement();
        Serial.print('1');
        break;
      case 54:
        figuresMovement.queenMovement();
        Serial.print('1');
        break;
    }
  }
}