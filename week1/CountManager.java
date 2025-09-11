package week1;

public class CountManager {
    private int outCount;
    private int ballCount;
    private int strikeCount;

    public int getOutCount(){
        return outCount;
    }
    public int getBallCount(){
        return ballCount;
    }
    public int getStrikeCount(){
        return strikeCount;
    }

    public void resetCount(){
        outCount = 0;
        ballCount = 0;
        strikeCount = 0;
    }
    public void resetPartial() {
        ballCount = 0;
        strikeCount = 0;
    }

    public void addOut(){
        outCount++;
    }
    public void addBall(){
        ballCount++;
    }
    public void addStrike(){
        strikeCount++;
    }

    public void printCount() {
        System.out.println("---------------------------------------------------------");
        System.out.println("| Ball : " + ballCount + " | Strike : " + strikeCount + " | Out : " + outCount + " |");
        System.out.println("---------------------------------------------------------");
    }
}
