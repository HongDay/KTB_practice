package week1;

public class Timer implements Runnable{

    private int time;

    public Timer(int t) {this.time = t;}

    @Override
    public void run() {
        try {
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
