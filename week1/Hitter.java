package week1;

import java.util.concurrent.ThreadLocalRandom;

public class Hitter extends Player{
    private final float hittingRate;

    public Hitter (int backNumber, String name, float rate){
        super(backNumber, name);
        this.hittingRate = rate;
    }

    // computer ver action -> return random (swing or not)
    public boolean swingRandom() {
        return (ThreadLocalRandom.current().nextFloat() < 0.5);
    }

    // user ver action -> return hit success or not
    public boolean swing() {
        return (ThreadLocalRandom.current().nextFloat() < hittingRate);
    }

}
