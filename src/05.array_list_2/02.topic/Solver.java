import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Pass {
    private String name;
    private int age;

    public Pass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public boolean isPriority() {
        return age >= 65;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name + ":" + this.age;
    }

}

class Topic {
    private List<Pass> prioritySeats;
    private List<Pass> normalSeats;

    public Topic(int capacity, int qtdPriority) {
        this.prioritySeats = new ArrayList<>(Collections.nCopies(qtdPriority, null));
        this.normalSeats = new ArrayList<>(Collections.nCopies(capacity - qtdPriority, null));
    }

    private static int findFirstFreePos(List<Pass> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private static int findByName(String name, List<Pass> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean insertOnList(Pass pass, List<Pass> list) {
        int i = findFirstFreePos(list);
        if (i == -1) {
            return false;
        }
        list.set(i, pass);
        return true;
    }

    private static boolean removeFromList(String name, List<Pass> list) {
        int i = findByName(name, list);
        if (i == -1) {
            return false;
        }

        list.remove(i);
        list.add(i, null);
        return true;
    }

    public boolean insert(Pass pass) {
        if (findByName(pass.getName(), normalSeats) != -1 || findByName(pass.getName(), prioritySeats) != -1) {
            System.out.println("fail: " + pass.getName() + " ja esta na topic");
            return false;
        }
        if (pass.isPriority()) {
            if (!insertOnList(pass, prioritySeats) && !insertOnList(pass, normalSeats)) {
                System.out.println("fail: topic lotada");
                return false;
            }
        } else {
            if (!insertOnList(pass, normalSeats) && !insertOnList(pass, prioritySeats)) {
                System.out.println("fail: topic lotada");
                return false;
            }
        }
        return true;
    }

    public boolean remove(String name) {
        if (!removeFromList(name, normalSeats) && !removeFromList(name, prioritySeats)) {
            System.out.println("fail: " + name + " nao esta na topic");
            return false;
        }
        return true;
    }

    public String toString() {
        return "[" + Stream.concat(
                this.prioritySeats.stream().map(p -> ("@" + ((p == null) ? ("") : (p.toString())))),
                this.normalSeats.stream().map(p -> ("=" + ((p == null) ? ("") : (p.toString())))))
                .collect(Collectors.joining(" ")) + "]";
    }
}

class Solver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Topic topic = new Topic(0, 0);
        while (true) {
            String line = scanner.nextLine();
            System.out.println("$" + line);
            String ui[] = line.split(" ");
            if (line.equals("end")) {
                break;
            } else if (ui[0].equals("init")) { // capacity qtdPriority
                topic = new Topic(Integer.parseInt(ui[1]), Integer.parseInt(ui[2]));
            } else if (ui[0].equals("show")) {
                System.out.println(topic);
            } else if (ui[0].equals("in")) {
                topic.insert(new Pass(ui[1], Integer.parseInt(ui[2])));
            } else if (ui[0].equals("out")) {// value value
                topic.remove(ui[1]);
            } else {
                System.out.println("fail: comando invalido");
            }
        }
        scanner.close();
    }
}
