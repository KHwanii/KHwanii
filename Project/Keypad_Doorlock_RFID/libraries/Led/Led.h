#pragma once

#include <Arduino.h>

class Led {
protected:
  int pin;  // 연결할 핀 번호

public:
  Led(int pin);     // 원형

  void on();
  void off();
  void setValue(int value);
  int toggle();

};