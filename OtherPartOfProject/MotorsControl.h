class MotorsControl {
  int _outputPinTwoVar;
  int _outputPinThreeVar;
  int _outputPinFourVar;
  int _outputPinFiveVar;

public:
  MotorsControl(int outputPinTwoVar, int outputPinThreeVar, int outputPinFourVar, int outputPinFiveVar) {
    _outputPinTwoVar = outputPinTwoVar;
    _outputPinThreeVar = outputPinThreeVar;
    _outputPinFourVar = outputPinFourVar;
    _outputPinFiveVar = outputPinFiveVar;
    pinMode(_outputPinTwoVar, OUTPUT);
    pinMode(_outputPinThreeVar, OUTPUT);
    pinMode(_outputPinFourVar, OUTPUT);
    pinMode(_outputPinFiveVar, OUTPUT);

  }
  void resetPins() {
    digitalWrite(_outputPinTwoVar, LOW);
    digitalWrite(_outputPinThreeVar, LOW);
    digitalWrite(_outputPinFourVar, LOW);
    digitalWrite(_outputPinFiveVar, LOW);
  }

  void moveForward(int duration) {
    digitalWrite(_outputPinTwoVar, HIGH);
    digitalWrite(_outputPinThreeVar, LOW);
    digitalWrite(_outputPinFourVar, HIGH);
    digitalWrite(_outputPinFiveVar, LOW);
    delay(duration);
  }

  void moveBackward(int duration) {
    digitalWrite(_outputPinTwoVar, LOW);
    digitalWrite(_outputPinThreeVar, HIGH);
    digitalWrite(_outputPinFourVar, LOW);
    digitalWrite(_outputPinFiveVar, HIGH);
    delay(duration);
  }

  void moveLeftForward(int duration) {
    digitalWrite(_outputPinTwoVar, HIGH);
    digitalWrite(_outputPinThreeVar, LOW);
    digitalWrite(_outputPinFourVar, LOW);
    digitalWrite(_outputPinFiveVar, LOW);
    delay(duration);
  }

  void moveRightBackward(int duration) {
    digitalWrite(_outputPinTwoVar, LOW);
    digitalWrite(_outputPinThreeVar, LOW);
    digitalWrite(_outputPinFourVar, LOW);
    digitalWrite(_outputPinFiveVar, HIGH);
    delay(duration);
  }
};