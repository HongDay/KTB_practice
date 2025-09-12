# Week1 과제 명 : CLI 프로그램 제작

## 과제 목차

1. 프로그램 클래스 설계도(다이어그램) 작성하기 
    - 속성, 메서드, 상속의 관계 정의 
    - 2차 상속은 최소 하나 포함
2. 간단한 스레드 구현 (예: 시간 흐름, 날씨 변화, 음악 플레이 등)
3. 스레드간 상호작용할 수 있는 기능 구현 (예 : 사람 스레드와 몹 스레드가 싸워서 사람스레드의 체력이 줄어듬)

## 과제 진행 상황
- [x]  1번 : 프로그램 클래스 다이어그램 작성
- [ ]  2번 : 간단한 스레드 구현
- [ ]  3번 : 스레드간 상호작용할 수 있는 기능 구현

---

<aside>

## 목표 CLI 프로그램 : 미니 야구 게임

사용자가 직접 타자, 수비수를 컨트롤하여 간단한 야구게임을 시뮬레이션할 수 있는 프로그램입니다.

```text
- 사용자는 공격, 수비 턴을 번갈아가며 총 9회의 야구게임에 참여할 수 있다.
- 공격턴에 사용자는 타자를 컨트롤 할 수 있다.
    - swing 할지, 넘어갈지 고른다.
        - swing 했을 시 :
          투수가 strike을 던졌다면 타율에 따라 타구여부 결정됨
          투수가 ball을 던졌다면 strike count +1
        - pass 했을 시 :
          투수가 strike을 던졌다면 strike count +1
          투수가 ball을 던졌다면 ball count +1
    - 안타라면, 무조건 타자는 출루하고 모든 베이스의 주자가 +1 씩 진루한다.
- 수비턴에 사용자는 수비수를 컨트롤 할 수 있다.
    1. 투수가 볼을 던질 지 스트라이크를 던질 지 고른다.
    2. 만약 안타일 시, 어떤 수비수를 활성화 할 지 고른다.
       (외야수 3명, 내야수 4명 중 총 3명 활성화 가능)
        - 타자가 날린 공은 7명의 포지션으로 랜덤하게 떨어진다.
        - 활성화한 수비수에게 공이 떨어질 시, 수비수의 방어율에 따라 out count +1 일지 안타일지 결정된다.
- 메인 클래스 (BallPark class)에 현재 경기상황 : 점수현황, 아웃카운트 현황, 현재 타자 순서 등등이 기록된다.
- 경기 종료 시 사용자가 낸 점수가, 상대(컴퓨터)가 낸 점수보다 높을 시 승리한다.
```

### 1. 프로그램 클래스 다이어그램 작성 (✅완료)

![Class Diagram.jpg](./ClassDiagram.jpg)

- 모든 선수 관리를 위한 등번호, 이름 필드가 필요함

  👉 **“player” 클래스 필요**
- 사용자가 컨트롤할 수 있는 선수 중에는 타자가 있고, 투수가 있음.

  👉 **“player” 클래스를 상속하는 Pitcher 클래스, Hitter 클래스 필요**
- 타자중에 일부는 수비도 해야하는 선수가 있음.

  👉 **Hitter 클래스를 상속하는 Defender 클래스 필요 (2차 상속)**
- 현재 베이스 상황 관리 / 점수 관리 / 카운트 관리 / 선수 로테이션을 관리할 “게임 관리” 클래스가 필요함.

  👉 **BallPark라는 이름의 메인 클래스에서 관리**

  👉 **아웃, 볼, 스트라이크 카운트를 한번에 관리해야 하므로 CountManager 클래스를 따로 정의**


### 2. 간단한 스레드 구현 (✅완료)
**아래 2개 스레드가 동시 실행되는 구조를 구현**

1. 투수 투구 시간 / 타자 타구 결정 시간 제한을 위한 타이머 스레드

    \: 타이머 (시간제한 초 입력 → 카운트다운 1초마다 출력 → 시간 초과 시 안내 출력)

    ```java
    public class Timer implements Runnable{
    
        private int time;
    
        public Timer(int t, CountManager counts) {
            this.time = t;
        }
    
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println("시간안에 안고를시 out count + 1 됩니다.");
                while (time > 0) {
                    System.out.println(time + "초 남았습니다 ..");
                    Thread.sleep(1000);
                    time--;
                }
                System.out.println("out count가 하나 올라갔습니다.");
    
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    ```

2. 점수 managing 스레드

   \: 메인 스레드. 점수 관리 / 사용자 input 받기 / 선수 동작 제어 등등이 일어남

### 3. 스레드간 상호작용 기능 구현 (✅완료)

투수 투구 시간 제한 초과 시, 패널티로 사용자 스레드의 메인 점수 및 카운트 관리 변수가 업데이트 되어야 함.

```java
// 추가된 코드부분 주석으로 표시해뒀습니다.
public class Timer implements Runnable{

    private int time;
    private final CountManager countManager; // 👈 추가

    public Timer(int t, CountManager counts) {
        this.time = t;
        this.countManager = counts; // 👈 추가
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println("시간안에 안고를시 out count + 1 됩니다.");
            while (time > 0) {
                System.out.println(time + "초 남았습니다 ..");
                Thread.sleep(1000);
                time--;
            }

            countManager.addOut(); // 👈 추가
            System.out.println("out count가 하나 올라갔습니다.");
            countManager.printCount(); // 👈 추가

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

```java
// 이 클래스는 변경된 부분만 옮겨왔습니다. (가독성 이슈)
public class CountManager {
    private int outCount;

    public synchronized void addOut(){ // 👈 synchronized로 변경
        outCount++;
    }
}
```

```java
// main함수 호출부분
timer = new Thread(new Timer(5, current_count));
timer.start();

System.out.println("Swing 하시겠습니까?ㅋ (y/n) :");
String answer = br.readLine();

boolean swing;
while(true) {
    if (answer.equals("y")){
        swing = true;
        System.out.println("\n스윙 하기로 결정했습니다!");
        break;
    } else if (answer.equals("n")){
        swing = false;
        System.out.println("\n참기로 결정했습니다!");
        break;
    } else {
        System.out.println("wrong input!");
        System.out.println("Swing 하시겠습니까?ㅋ (y/n) :");
        answer = br.readLine();
    }
}

// timer 종료 (이전에 timer 끝날 시, 타이머 스레드에서 우리 스레드를 변형하는 구조)
timer.interrupt();
timer.join();
```

## 회고

- thread, runnable에 대한 개념, 멀티스레딩과 동시성에 대한 개념을 완전히 복습하지 않은 상태로 해서, 기획했던 것과 다르게 멀티스레딩 부분 코드도 많이 단순해졌고, 스레드간 상호작용도 너무 단순해졌다.
  ⇒ 개념에 대해 복습한 후에, 현재 기획에서 멀티스레딩 부분을 조금 더 고도화 시켜서 적용할 수 있는 아이디어를 구상해봐야겠다.
- thread, runnable, 동시성과 관련된 코드를 짜는 것을 복습을 안하고 부랴부랴 급하게 과제를 했다. 클래스, 상속 등을 구현할 때는 직접 코딩했지만, runnable 구현 방법은 GPT를 많이 참고했다는 점이 아쉬운 것 같다.
  ⇒ 이 역시 개념에 대해 복습한 후에, 고도화 하는 코드를 짜면서는 직접 해봐야겠다.
- 코드를 짜면서 모든 코드 한줄한줄 확신이 없었다. 내가 코딩하고 있는게 이런 방식, 이런 의도로 코딩하라고 만든 기능이 맞는지, 2중 상속은 이런 목적으로 사용하라고 만들어진게 맞는건지, 멀티스레딩의 정석이 맞는건지, 내 프로젝트의 경우에 공유 변수 접근에 대한 제한을 synchronized 로만 해도 괜찮은건지 등등..
- **개인적으로 내가 기획하고 구현한 것에 대한 아쉬움이 많은 과제였다.**
    1. 보편적인 2차상속의 존재 이유와 조금 멀어지게 기획했다는 점
    2. 멀티스레딩을 복습하지 않고 과제를 시작해서, “필요한 곳에 적절하게 이를 적용하기 위해” 사용했다기 보다는 “멀티스레딩을 구현해보기 위해” 억지로 끼워맞춘듯한 느낌이 있었다.
    3. 충분한 복습이 없어서 멀티스레딩 부분은 GPT를 참고했다는 점이 아쉬웠고,
    4. 멀티스레딩도 우선 너무 단순하게 구현하기도 했고, 개발자들의 일반적인 사용방식(목적) 과 조금 동떨어지지 않았나하는 아쉬움이 있다.