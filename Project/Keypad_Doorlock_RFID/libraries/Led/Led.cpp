#include "Led.h" // <~~.h>?�� �??��?�� ?��?��브러리에?�� 찾기 
                      // "~~.h"?�� ?��?�� ?��?��?��리에?�� 찾고, 그다?�� �??�� ?��치에?�� 찾기

Led::Led(int pin) : pin(pin) { // ?��?�� :: 멤버
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
