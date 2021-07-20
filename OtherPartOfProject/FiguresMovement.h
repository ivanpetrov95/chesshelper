extern MotorsControl motorsControl;
class FiguresMovement {
  int _shortDuration;
  int _mediumDuration;
  int _longDuration;
  int _extraLongDuration;
public:
  FiguresMovement(int shortDuration,
                  int mediumDuration,
                  int longDuration,
                  int extraLongDuration) {
    _shortDuration = shortDuration;
    _mediumDuration = mediumDuration;
    _longDuration = longDuration;
    _extraLongDuration = extraLongDuration;
  }
  void pawnMovement() {
    motorsControl.moveForward(_shortDuration);
    motorsControl.moveBackward(_shortDuration);
    motorsControl.resetPins();
  }

  void bishopMovement() {
    motorsControl.moveLeftForward(_shortDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveBackward(_extraLongDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveRightBackward(_shortDuration);
    motorsControl.resetPins();
  }

  void kingMovement() {
    motorsControl.moveForward(_shortDuration);
    motorsControl.moveBackward(_shortDuration);
    motorsControl.moveLeftForward(_shortDuration);
    motorsControl.moveForward(_shortDuration);
    motorsControl.moveBackward(_shortDuration);
    motorsControl.moveRightBackward(_shortDuration);
    motorsControl.resetPins();
  }

  void rookMovement() {
    motorsControl.moveForward(_longDuration);
    motorsControl.moveBackward(_extraLongDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveLeftForward(_mediumDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveBackward(_extraLongDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveRightBackward(_mediumDuration);
    motorsControl.resetPins();
  }

  void knightMovement() {
    motorsControl.moveForward(_mediumDuration);
    motorsControl.moveLeftForward(_mediumDuration);
    motorsControl.moveForward(_shortDuration);
    motorsControl.moveBackward(_shortDuration);
    motorsControl.moveRightBackward(_mediumDuration);
    motorsControl.moveBackward(_mediumDuration);
    motorsControl.resetPins();
  }

  void queenMovement() {
    motorsControl.moveForward(_longDuration);
    motorsControl.moveBackward(_extraLongDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveLeftForward(_shortDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveBackward(_extraLongDuration);
    motorsControl.moveForward(_longDuration);
    motorsControl.moveRightBackward(_shortDuration);
    motorsControl.resetPins();
  }
};