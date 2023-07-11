#include "Led.h" // <~~.h>?Š” ì§?? •?•œ ?¼?´ë¸ŒëŸ¬ë¦¬ì—?„œ ì°¾ê¸° 
                      // "~~.h"?Š” ?˜„?ž¬ ?””? ‰?† ë¦¬ì—?„œ ì°¾ê³ , ê·¸ë‹¤?Œ ì§?? • ?œ„ì¹˜ì—?„œ ì°¾ê¸°

Led::Led(int pin) : pin(pin) { // ?†Œ?† :: ë©¤ë²„
  // this->pin = pin;
  pinMode(pin, OUTPUT);
}

void Led::on() {
  digitalWrite(pin, HIGH);
}

void Led::off() {
  digitalWrite(pin, LOW);
}

void Led::setValue(int value) {
  digitalWrite(pin, value);
}

int Led::toggle() {
  int v = !digitalRead(pin);
  digitalWrite(pin, v);
  return v;
}
