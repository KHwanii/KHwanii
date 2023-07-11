#include <MiniCom.h>
#include <Keypad.h>
#include <Led.h>
#include <Servo.h>
#include <SimpleTimer.h>
#include <EEPROM.h>

MiniCom com;
Led led(12);
Servo sv;
SimpleTimer timer;          

const int servo_pin = 3;

const int _OPEN = 180;
const int _CLOSE = 90;
const int _LOCKED = 0;        // 상태변수 선언

int fail_count = 0;          // 실패 횟수
int state = _CLOSE;          // 초기상태 
int backlight_state = -1;    // LCD 상태

const byte ROWS = 4; // 행(rows) 개수
const byte COLS = 4; // 열(columns) 개수

char keys[ROWS][COLS] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};

byte rowPins[ROWS] = {7, 6, 5, 4}; // R1, R2, R3, R4 단자가 연결된 핀 번호
byte colPins[COLS] = {8, 9, 10, 11}; // C1, C2, C3, C4 단자가 연결된 핀 번호

Keypad keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);
String input = "";          // 입력
String real_input = "";     // 실제 입력값

String password = "0000";   // 초기 패스워드

boolean pw_reset = false;    // 패스워드 변경모드 상태 

void beep() {
  led.on();
  delay(100);
  led.off();
}

void success() {
  for (int i=0; i<3; i++) {
    beep();
    delay(200);
  }
}

void alert() {
  for (int i=0; i<5; i++) {
    beep();
    delay(20);
  }
}

void key_reset() {
  input = "";
  real_input = "";
}

void open() {
  success();
  com.print(0, "[[Door Open!!]]");
  sv.write(_OPEN);
  key_reset();
  fail_count = 0;
  timer.setTimeout(3000, [](){ 
          close();
          com.BacklightOff();
          key_reset();
        });
  delay(3000);                       // 3초뒤 close로 바뀜
}


void close() {
  alert();
  com.print(0, "[[Door Closed!!]]");
  sv.write(_CLOSE);
  key_reset();
}

void lock() {
  alert();
  sv.write(_LOCKED);
  unsigned long startTime = millis();
  com.print(0, "[[Door Locked!!]]");
  while (millis() - startTime < 60000) {     // 1분 동안 대기

  }
  com.print(0, "[[Input Password]]");
  fail_count = 0;                            // fail_count 초기화
}


void key_input() {
  char key = keypad.getKey();

  if(key) {
    com.BacklightOn();
    beep();

    real_input += key;        // 실제 키 입력값
    input += "*";             // 키 입력시 input 은 * 이 추가됨

    timer.deleteTimer(backlight_state);    // 키 입력이 감지될때마다 기존 타이머 삭제
    
    backlight_state = timer.setTimeout(3000, [](){
          com.BacklightOff();
          key_reset();
        });                                        // 키 입력 이후 3초뒤에 LCD OFF

      if(key == '*' && !pw_reset) {                                 // pw_reset 상태가 false 이고, * 키를 눌렀을 때 비밀번호 확인
        if(real_input.substring(0, real_input.length() - 1) == password) {      // 실제 키 입력값에서 마지막 * 을 제외하고 비교
          open();
        }
        else {
          close();
          fail_count ++;               // 실패시 fail_count 1 증가
        }
      }
      else if(key == '#' && !pw_reset) {              // pw_reset 상태가 false 이고, # 키를 눌렀을 때 비밀번호 변경모드 진입
        pw_reset = true;                                
        key_reset();
        com.print(0, "[[Set New Password]]");
      }
      else if(key == '*' && pw_reset) {                 // pw_reset 상태가 True 이고, * 키를 눌렀을 때 비밀번호 변경하기
        for (int i = 0; i < password.length(); i++) {        // 비밀번호의 길이만큼 배열을 앞에서부터 하나씩 읽기
          EEPROM.write(i, password[i]);                      // EEPROM의 i 번지부터 시작해서 하나씩 쓰기
        }
        key_reset();
        pw_reset = false;
        com.print(0, "[[Password Updated]]");
        timer.setTimeout(1000, close);
        delay(2000);
      }
      else if(key == '#' && pw_reset) {              // pw_reset 상태가 True 이고, # 키를 눌렀을 때 비밀번호 입력모드로 변경
        key_reset();
        pw_reset = false;
        com.print(0, "[[Input Password]]");
      }

  }
}


void setup() {
  com.init();
  sv.attach(servo_pin);
  sv.write(_CLOSE);
  password = "";                                // 시작시 비밀번호 초기화
  for (int i = 0; i < 4; i++) {
    password += char(EEPROM.read(i));          // EEPROM의 i 번지부터 데이터를 가져와서 password에 추가하여 읽기
  }    
  com.print(0, "[[Input Password]]");
  com.BacklightOff();
}

void loop() {
  timer.run();
  key_input();
  com.print(1, input);

  if (fail_count >= 3) {
    lock();
  }
}
