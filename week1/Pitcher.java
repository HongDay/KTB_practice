package week1;

import java.util.concurrent.ThreadLocalRandom;

public class Pitcher extends Player {
    private float ballSpeed;

    public Pitcher(int backNumber, String name, float speed){
        super(backNumber, name);
        this.ballSpeed = speed;
    }

    // computer ver -> return ball or strike random
    public boolean throwRandom() {
        return (ThreadLocalRandom.current().nextFloat() < 0.5);
    }

}
