import java.util.*;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String toString() {
        return name + ":" + age;
    }
}

class Motorcycle {
    private Person person; // agregacao
    private int power;
    private int time;

    // Inicia o atributo power, time com zero e person com null
    public Motorcycle(int power) {
        this.power = power;
        this.time = 0;
        this.person = null;
    }

    public int getPower() {
        return power;
    }

    public int getTime() {
        return time;
    }

    public Person getPerson() {
        return person;
    }

    // Se estiver vazio, coloca a pessoa na moto e retorna true
    public boolean enter(Person person) {
        if (this.person == null) {
            this.person = person;
            return true;
        }
        System.out.println("fail: busy motorcycle");
        return false;
    }

    public Person leave() {
        if (this.person != null) {
            Person removed = this.person;
            this.person = null;
            return removed;
        }
        System.out.println("fail: empty motorcycle");
        return null;
    }

    public void buyTime(int time) {
        this.time += time;
    }

    public void drive(int time) {
        if (this.time == 0) {
            System.out.println("fail: buy time first");
            return;
        }
        if (this.person == null) {
            System.out.println("fail: empty motorcycle");
            return;
        }
        if (this.person.getAge() > 10) {
            System.out.println("fail: too old to drive");
            return;
        }
        if (time > this.time) {
            System.out.printf("fail: time finished after %d minutes\n", this.time);
            this.time = 0;
        } else {
            this.time -= time;
        }
    }

    public void honk(int power) {
        System.out.print("Pe");
        for (int i = 0; i < power - 1; i++) {
            System.out.print("e");
        }
        System.out.println("m");

    }

    public String toString() {
        return String.format("power:%d, time:%d, person:(%s)", power, time, (person == null) ? "empty" : person);
    }
}

class Solver {
    static Motorcycle motoca = new Motorcycle(1);

    public static void main(String[] args) {
        while (true) {
            String line = input();
            args = line.split(" ");
            write('$' + line);

            if (args[0].equals("show")) {
                System.out.println(motoca);
            } else if (args[0].equals("init")) {
                motoca = new Motorcycle(number(args[1]));
            } else if (args[0].equals("enter")) {
                motoca.enter(new Person(args[1], number(args[2])));
            } else if (args[0].equals("end")) {
                break;
            } else if (args[0].equals("leave")) {
                Person person = motoca.leave();
                if (person != null) {
                    System.out.println(person.toString());
                }
            } else if (args[0].equals("buy")) {
                motoca.buyTime(number(args[1]));
            } else if (args[0].equals("drive")) {
                motoca.drive(number(args[1]));
            } else if (args[0].equals("honk")) {
                motoca.honk(motoca.getPower());
            } else {
                System.out.println("fail: comando invalido");
            }

        }
    }

    static Scanner scanner = new Scanner(System.in);

    public static String input() {
        return scanner.nextLine();
    }

    public static void write(String value) {
        System.out.println(value);
    }

    public static int number(String str) {
        return Integer.parseInt(str);
    }
}