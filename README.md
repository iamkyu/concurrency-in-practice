# Java Concurrency in Practice

> 멀티 코어를 100% 활용하는 자바 병렬 프로그래밍(브라이언게츠 외 1명 저) 학습

## 1장. 개요

초창기 컴퓨터는 운영체제가 없고 처음부터 끝까지 하나의 프로그램을 실행하기만 함. 해당 프로그램은 컴퓨터 내 모든 자원을 직접 접근하고 비효율적으로 사용.
운영체제가 등장하며 *여러 프로그램을 각자의 프로세스 내에서 실행*하도록 발전.
각 프로세스마다 메모리, 파일핸들, 보안권한 등의 자원을 핸들.

### 여러 프로그램을 동시에 실행하는 운영체제를 개발하게 된 이유
1. 자원 활용: 하나의 프로그램이 기다리는 동안 다른 일을 한다.
2. 공정성: 여러 사용자와 프로그램이 컴퓨터 내 자원에 대해 동일한 권한을 가진다.
3. 편의성: 프로그램을 작은 단위로 나눠 각자 맡은 일을 수행한다.

### Lightweight Process
> Thread: 어떠한 프로그램 내에서, 특히 프로세스 내에서 실행되는 흐름의 단위 - https://ko.wikipedia.org/wiki/스레드

자원활용, 공정성, 편의성 등 프로세스 개념을 만들어내게 된 것과 같은 동기를 갖고 스레드가 고안 됨. 스레드로 인해 한 프로세스 안에 여러 개의 프로그램 제어 흐름이 공존 가능.

### 스레드의 이점
1. 멀티프로세서 활용: 활성 상태인 스레드가 여러 개인 프로그램은 여러 프로세서에서 동시 실행 가능. 제대로 설계 된 멀티스레드 프로그램은 프로세서 자원을 더 효율적으로 사용.
2. 단순한 모델링: 한 종류 일을 순차적으로 처리하는 프로그램은 작성하기 쉽고 테스트 하기 쉽다. 종류별 작업마다 또는 시뮬레이션 작업의 각 요소마다 스레드를 하나씩 할당함으로써 복잡하면서 비동기적은 작업 흐름을 각기 별도 스레드에서 수행되는 더 단순하고 동기적임 작업 흐름 몇 개로 나눌 수 있다.
3. 단순한 비동기 이벤트 처리: 각 요청을 별개 스레드에서 처리하면 대기 상태에 들어가도 다른 스레드가 요청을 처리하는 데는 별 영향을 끼치지 않는다.

### 스레드의 위험성
1. 안전성 위해 요소
2. 활동성 위험
3. 성능 위험

## 2장. 스레드 안전성

병렬 프로그램을 작성하려면 스레드와 락을 잘 사용해야 한다. 하지만 이 두가지는 목적을 위한 도구일 뿐이다. 스레드 안전한 코드를 작성하는 것은 근본적으로 상태, 특히 공유되고 변경할 수 있는 상태에 대한 접근을 관리하는 것이다.

> 여러 스레드가 클래스에 접근할 때, 실행 환경이 해당 스레드들의 실행을 어떻게 스케쥴 하든 어디에 끼워 넣든, 호출하는 쪽에서 추가적인 동기화나 다른 조율 없이 정확하게 동작하면 해당 클래스는 스레드 안전하다고 말한다. 즉, 스레드 안전성은 정확성 개념과 관계 있다.

### 여러 스레드가 변경할 수 있는 하나의 상태 변수를 적절한 동기화 없이 접근하는 프로그램을 고치는법
1) 해당 상태 변수를 스레드간에 공유하지 않거나
2) 해당 상태 변수를 변경할 수 없도록 만들거나
3) 해당 상태 변수에 접근할 땐 언제나 동기화를 사용한다.

프로그램 상태를 잘 캡슐화할수록 스레드에 안전하게 만들기 쉽다. 상태 없는 객체는 항상 스레드 안전하다.

### 경쟁조건
타이밍이 딱 맞을 때만 정답을 얻는 경우를 경쟁조건이라 한다. 가장 일반적인 경쟁 조건 형태는 잠재적으로 유효하지 않은 값을 참조해서 다음에 뭘 할지를 결정하는 점검 후 행동 (check-then-act) 형태의 구문이다.
점검 후 행동과 읽고 수정하고 쓰기 같은 일련의 동작을 복합 동작이라고 한다. 스레드에 안전하기 위해서는 전체가 단일 연산으로 실행되는 일련의 동작이 되야 한다.

### 암묵적인 락
자바에서는 단일 연산 특성을 보장하기 위해 synchronized 라는 구문을 제공한다.
자바에서 암묵적인 락은 뮤텍스(상호배제)로 동작한다. 한 스레드가 synchronized 블록을 실행중이면 같은 락으로 보호 되는 synchronized 블록에 다른 스레드가 들어와 있을 수 없다. 반면 이 방법은 성능 문제를 일으킬 수 있다. 복잡하고 오래 걸리는 계산 작업, 네트웍 작업, 입출력 작업과 같은 부분에서는 가능한 락을 잡지 말야아 한다.

## 3장. 객체공유

### 가시성
특정 변수에 값을 지정하고 값을 읽는 행위도 여러 스레드에서는 접근하게 되면 기대 하지 않은 결과를 얻을 수도 있다. 동기화 되지 않은 상황에서 메모리상의 변수를 대상으로 작성해둔 코드가 반드시 예상 된 순서로 동작할 것이라고 단정할 수 없다. 따라서 메모리상의 공유된 변수를 여러 스레드에서 서로 사용할 수 있게 하려면 반드시 동기화 기능을 구현해야 한다.

#### 락(lock)
값을 변경할 수 있는 변수를 여러 스레드에서 동시에 사용한다면 ```synchronized``` 블록으로 막을 필요가 있다. 그렇지 않으면 스테일(stale) 상태에 빠지기 쉽다.

#### volatile 변수
```volatile``` 변수를 사용하는 것은 ```synchronized``` 키워드로 특정 코드를 묶는 게 비슷한 효과를 가져 오고, ```volatile``` 변수의 값을 읽고 나면 ```synchronized``` 블록에 진입하는 것과 비슷한 상태에 해당한다. 반면 락은 가시성과 연산의 단일성을 모두 보장받을 수 있지만 ```volatile```로 선언 된 변수는 연산의 단일성은 보장하지 못하고 가시성만 보장한다. 따라서 이 변수는 다음과 같은 상황에서만 사용하는 것이 좋다.

1. 변수에 값을 저장하는 작업이 해당 변수의 현재 값과 관련이 없거나 해당 변수의 값을 변경하는 스레드가 하나만 존재
2. 해당 변수가 객체의 불변조건을 이루는 다른 변수와 달리 불변조건에 관련되어 있지 않은 경우
3. 해당 변수를 사용하는 동안 어떤 경우라도 락을 걸어둘 필요가 없는 경우

### 공개와 유출
특정 객체를 현재 코드의 스코프 범위 밖에서 사용할수 있도록 만들면 공개되었다고 한다. 어떤 객체건 일단 공개되거나 유출되면 다른 스레드가 의돚덕이건 의도적이지 않건 간에 반드시 잘못 사용할 수 있다고 가정해야 한다.

### 스레드 한정
객체 인스턴스를 특정 스레드에 한정시켜두면 자동으로 스레드 안정성을 확보하게 된다.

### 불변성
불변 객체는 언제라도 스레드에 안전하다. 다음 조건을 만족하면 불변 객체다.
- 생성되고 난 후 객체의 상태를 변경할 수 없다.
- 내부의 모든 변수는 final로 설정돼야 한다.
- 적절한 방법으로 생성되어야 한다. (예를 들어 this 변수에 대한 참조가 외부로 유출되지 않아야 한다.)


