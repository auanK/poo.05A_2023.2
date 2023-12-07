import java.util.Scanner;

class Car {
    int pass;
    int passMax;
    int gas;
    int gasMax;
    int km;

    public Car() {
        this.pass = 0;
        this.km = 0;
        this.passMax = 2;
        this.gas = 0;
        this.gasMax = 100;
    }

    public void enter() {
        if (this.passMax > this.pass)
            pass++;
        else
            System.out.println("fail: limite de pessoas atingido");
    }

    public void leave() {
        if (this.pass > 0)
            this.pass--;
        else
            System.out.println("fail: nao ha ninguem no carro");
    }

    public void drive(int km) {
        if (this.pass > 0) {
            if (this.gas > 0) {
                if (this.gas >= km) {
                    this.km += km;
                    this.gas -= km;
                } else {
                    this.km += this.gas;
                    System.out.println("fail: tanque vazio apos andar " + this.gas + " km");
                    this.gas = 0;
                }
            } else
                System.out.println("fail: tanque vazio");
        } else
            System.out.println("fail: nao ha ninguem no carro");
    }

    public void fuel(int gas) {
        if (this.gas + gas <= this.gasMax)
            this.gas += gas;
        else
            this.gas = this.gasMax;

    }

    public String toString() {
        return "pass: " + this.pass + ", gas: " + this.gas + ", km: " + this.km;
    }

}

public class Solver {
    public static void main(String[] a) {
        Car car = new Car();

        while (true) {
            var line = input();
            write("$" + line);
            var args = line.split(" ");

            if (args[0].equals("end")) {
                break;
            } else if (args[0].equals("show")) {
                System.out.println(car);
            } else if (args[0].equals("enter")) {
                car.enter();
            } else if (args[0].equals("leave")) {
                car.leave();
            } else if (args[0].equals("drive")) {
                car.drive((int) number(args[1]));
            } else if (args[0].equals("fuel")) {
                car.fuel((int) number(args[1]));
            } else {
                write("fail: comando invalido");
            }
        }
    }

    private static Scanner scanner = new Scanner(System.in);

    private static String input() {
        return scanner.nextLine();
    }

    private static double number(String value) {
        return Double.parseDouble(value);
    }

    private static void write(String value) {
        System.out.println(value);
    }
}
