import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Collectors;

class Kid {
    private int age;
    private String name;

    public Kid(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public int getAge() {
        return age;
    }
    public String getName() {
        return name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return name + ":" + age;
    }
}

class Trampoline{
    private LinkedList<Kid> waiting;
    private LinkedList<Kid> playing;
    
    public Trampoline() {
        waiting = new LinkedList<>();
        playing = new LinkedList<>();
    }
    
    private Kid removeFromList(String name, LinkedList<Kid> list) {
    Kid kidToRemove = null;
    for (Kid kid : list) {
        if (kid.getName().equals(name)) {
            kidToRemove = kid;
            break;
        }
    }
    if (kidToRemove != null) {
        list.remove(kidToRemove); 
    }

    return kidToRemove; 
    }


    public void arrive(Kid kid) {
        waiting.addFirst(kid);
    }

    public void enter() {
        if(waiting == null) {
            return;
        }
        playing.addFirst(waiting.getLast());
        waiting.removeLast();
        
    }

    public void leave() {
        if(playing.isEmpty()) {
            return;
        }
        waiting.addFirst(playing.getLast());
        playing.removeLast();
    }

    public void remove(String name) {
        removeFromList(name, playing);
        removeFromList(name, waiting);

    }
    public String toString() {
        return   "[" + waiting.stream().map(Kid::toString).collect(Collectors.joining(", ")) + "]" + " => "
               + "[" + playing.stream().map(Kid::toString).collect(Collectors.joining(", ")) + "]";
    }
}


class Solver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Trampoline tramp = new Trampoline();
        while(true) {
            String line = scanner.nextLine();
            System.out.println("$"+ line);
            String[] ui = line.split(" ");
            if(ui[0].equals("end")) {
                break;
            } else if(ui[0].equals("arrive")) { // name age
                tramp.arrive(new Kid(ui[1], Integer.parseInt(ui[2]))) ;
            } else if(ui[0].equals("enter")) {
                tramp.enter();
            } else if(ui[0].equals("leave")) {
                tramp.leave();
            } else if(ui[0].equals("remove")) {//name
                tramp.remove(ui[1]);
            } else if(ui[0].equals("show")) {
                System.out.println(tramp);
            } else {
                System.out.println("fail: comando invalido");
            }
        }
        scanner.close();
    }
}
