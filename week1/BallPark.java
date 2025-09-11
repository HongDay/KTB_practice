package week1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BallPark {

    private static int my_score;
    private static int computer_score;

    private static int inning;

    private static List<Player> rotation = new ArrayList<>();
    private static int cur_player;

    private static int base;
    private static CountManager current_count = new CountManager();


    public static void main (String[] argv) throws IOException, InterruptedException {

        // 입력 시간 잴 타이머 (시간안에 투구 or 스윙 안할 시 out count + 1)
        Thread timer;

        // 선수명단 추가 (지명타자2, 수비7, 투수1)
        rotation.add(new Hitter(31, "손아섭", 0.314F)); // 지명타자
        rotation.add(new Hitter(13, "최재훈", 0.479F)); // 지명타자 (포수X)
        rotation.add(new Defender(51, "문현빈", 0.338F, 0.4F, DefendPosition.outfield)); // 타자 & 외야수
        rotation.add(new Defender(10, "이진영", 0.328F, 0.45F, DefendPosition.outfield)); // 타자 & 외야수
        rotation.add(new Defender(0, "리베라토", 0.538F, 0.5F, DefendPosition.outfield)); // 타자 & 외야수
        rotation.add(new Defender(8, "노시환", 0.302F, 0.44F, DefendPosition.infield)); // 타자 & 내야수
        rotation.add(new Defender(7, "이도윤", 0.238F, 0.38F, DefendPosition.infield)); // 타자 & 내야수
        rotation.add(new Defender(22, "채은성", 0.438F, 0.43F, DefendPosition.infield)); // 타자 & 내야수
        rotation.add(new Defender(2, "심우준", 0.278F, 0.55F, DefendPosition.infield)); // 타자 & 내야수
        rotation.add(new Pitcher(1, "문동주", 165)); // 투수

        // 컴퓨터 선수 추가 (로테이션 없이 고정상수)
        Pitcher AIPitcher = new Pitcher(99, "투수봇", 140);
        Defender AIDefender = new Defender(99, "수비봇", 0.25F, 0.35F, DefendPosition.outfield);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n---------- 미니 야구 게임 시작 -----------");
        System.out.println("@@ 한화이글스 라인업 소개\n");
        for (int i = 0; i < 10; i++){
            rotation.get(i).introduce(i+1);
        }

        while (inning < 9){

            // attack (공격턴)
            while (current_count.getOutCount() < 3){
                Hitter nowHitter = (Hitter) rotation.get(cur_player % 9);

                System.out.println("\n@@ " + (inning+1) + "회 초");
                System.out.print("당신이 공격할 차례입니다.\n이번 타자는 -- ");
                nowHitter.introduce(cur_player % 9 + 1);

                // timer 시작
                timer = new Thread(new Timer(5));
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

                // sleep 1초
                Thread.sleep(1000);

                boolean isStrike = AIPitcher.throwRandom();
                if (isStrike) {System.out.println("\n스트라이크 였습니다!");}
                else{System.out.println("\n볼 이었습니다!");}

                // sleep 1초
                Thread.sleep(1000);

                if (swing) {
                    if (nowHitter.swing()){
                        System.out.println("\n타구 성공!!");

                        // sleep 1초
                        Thread.sleep(1000);

                        if (AIDefender.catchBall()) {
                            System.out.println("\n 플라이아웃입니다 ㅠㅠ");
                            current_count.addOut();
                            current_count.resetPartial();
                            cur_player++;
                        } else {
                            System.out.println("\n 안타 !!!!!!!!!!!!!!");
                            baseProgress(true);
                            cur_player++;
                        }

                    } else {
                        System.out.println("\n헛스윙ㅠㅠ");
                        current_count.addStrike();
                    }
                } else {
                    if (isStrike) {current_count.addStrike();}
                    else {current_count.addBall();}
                }

                // 볼넷
                if (current_count.getBallCount() == 4) {
                    baseProgress(true);
                    current_count.resetPartial();
                    cur_player++;
                }
                // 삼진 아웃
                if (current_count.getStrikeCount() == 3) {
                    current_count.resetPartial();
                    current_count.addOut();
                    cur_player++;
                }

                current_count.printCount();
            }

            current_count.resetCount();
            base = 0;
            cur_player++;


            // 수비 턴
            while (current_count.getOutCount() < 3) {
                Pitcher nowPitcher = (Pitcher) rotation.get(9);

                System.out.println("\n@@ " + (inning + 1) + "회 말");
                System.out.print("당신이 수비할 차례입니다.\n우리 투수는 -- ");
                nowPitcher.introduce(10);

                // timer 시작

                System.out.print("Ball, Strike 중에 어떤것을 던지시겠습니까? (b/s) :");
                String answer = br.readLine();
                boolean isStrike;
                while(true) {
                    if (answer.equals("b")){
                        isStrike = false;
                        System.out.println("\n 한화 투수 " + nowPitcher.getName() + "!! 볼을 던졌습니다!!");
                        break;
                    } else if (answer.equals("s")){
                        isStrike = true;
                        System.out.println("\n 한화 투수 " + nowPitcher.getName() + "!! 스트라이크를 던졌습니다!!");
                        break;
                    } else {
                        System.out.println("wrong input!");
                        System.out.println("Ball, Strike 중에 어떤것을 던지시겠습니까? (b/s) :");
                        answer = br.readLine();
                    }
                }

                // timer 종료 (이전에 timer 끝날 시, 타이머 스레드에서 우리 스레드를 변형하는 구조)

                boolean swing = AIDefender.swingRandom();

                // sleep 1초
                Thread.sleep(1000);

                if (swing) {
                    System.out.println("\n상대 타자가 배트를 스윙했습니다!");
                    if (AIDefender.swing()) {

                        // sleep 1초
                        Thread.sleep(1000);

                        System.out.println("\n타구 성공ㅠㅠ 활성화 시킬 수비수 2명을 고르세요! 2명 중 한명이 잡으면 아웃시킬 수 있습니다! (3번타자 ~ 9번타자 중에 선택, 숫자 입력)");
                        boolean isCatch = false;
                        for (int i = 0; i < 2; i++) {
                            int select = Integer.parseInt(br.readLine());
                            Defender selectedDefender = (Defender) rotation.get(select - 1);
                            if (selectedDefender.catchBall()) {
                                System.out.println("\n수비수 " + selectedDefender.getName() + " 이 수비에 성공했습니다!");
                                isCatch = true;
                                break;
                            }
                        }

                        // sleep 1초
                        Thread.sleep(1000);

                        if (isCatch) {
                            current_count.addOut();
                        } else {
                            System.out.println("\n 수비에 실패했습니다.. 안타 ㅠㅠㅠㅠㅠ");
                            baseProgress(false);
                        }

                    } else {
                        // sleep 1초
                        Thread.sleep(1000);
                        System.out.println("\n헛스윙!!");
                        current_count.addStrike();
                    }
                } else {
                    System.out.println("\n상대 타자가 배트를 안휘둘렀습니다");
                    if (isStrike) {
                        current_count.addStrike();
                    } else {
                        current_count.addBall();
                    }
                }

                // 볼넷
                if (current_count.getBallCount() == 4) {
                    baseProgress(false);
                    current_count.resetPartial();
                }
                // 삼진 아웃
                if (current_count.getStrikeCount() == 3) {
                    current_count.resetPartial();
                    current_count.addOut();
                }
                current_count.printCount();
            }
            current_count.resetCount();
            base = 0;

            inning++;
        }

    }

    public static void baseProgress(boolean myturn) {
        base <<= 1;
        base |= 0b001;
        printBase();
        if ((base >> 3) == 1) {
            System.out.println("\n @@@@@@@ 득점!! 점수 + 1!! @@@@@@@@");
            if (myturn) {my_score++;}
            else {computer_score++;}
            printScore();
            base = base & 0b111;
        }
    }

    public static void printScore() {
        System.out.println("@@ 우리팀 점수 : " + my_score);
        System.out.println("@@ 상대팀 점수 : " + computer_score + "\n");
    }

    public static void printBase() {
        System.out.println("-----------베이스상황------------");
        System.out.println("| 1루 : " + (base & 1) + " | 2루 : " + ((base >> 1) & 1) + " | 3루 : " + ((base >> 2) & 1) + " |");
        System.out.println("-------------------------------");
    }

}
