package week1;

import java.util.concurrent.ThreadLocalRandom;

public class Defender extends Hitter {
    private DefendPosition position;
    private final float defendRate;

    public Defender(int backNumber, String name, float rate, float rate2, DefendPosition pos){
        super(backNumber, name, rate);
        this.defendRate = rate2;
        this.position = pos;
    }

    public boolean catchBall() {
        return (ThreadLocalRandom.current().nextFloat() < defendRate);
    }
}
