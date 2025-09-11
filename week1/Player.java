package week1;

public class Player {
    private int backNumber;
    private String name;

    public Player(int backNumber, String name){
        this.backNumber = backNumber;
        this.name = name;
    }

    public void introduce(int order) {
        if (order == 10) {
            System.out.println("투수 :: " + backNumber + "번 - " + name + "!!");
        } else {
            System.out.println(order + "번 타자 :: " + backNumber + "번 - " + name + "!!");
        }
    }

    public String getName() {
        return this.name;
    }

}
