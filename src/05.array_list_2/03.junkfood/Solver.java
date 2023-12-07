import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

class Slot {
    private String name;
    private float price;
    private int quantity;

    public Slot(String name, float price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        if (name == null) {
            return;
        }
        this.name = name;
    }

    public void setPrice(float price) {
        if (price < 0) {
            return;
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            return;
        }
        this.quantity = quantity;
    }

    public String toString() {
        return String.format("%8s", this.name) + " : " +
                this.quantity + " U : " +
                Solver.decForm.format(this.price) + " RS";
    }
}

class VendingMachine {
    private ArrayList<Slot> slots;
    private float profit;
    private float cash;
    private int capacity;

    public VendingMachine(int capacity) {
        this.slots = new ArrayList<Slot>();
        this.profit = 0;
        this.cash = 0;
        this.capacity = capacity;

        for (int i = 0; i < this.capacity; i++) {
            this.slots.add(new Slot("empty", 0, 0));
        }
    }

    private boolean indiceInvalido(int ind) {
        if (ind < 0 || ind >= this.slots.size()) {
            System.out.println("fail: indice nao existe");
            return true;
        }
        return false;
    }

    public Slot getSlot(int ind) {
        if (this.indiceInvalido(ind)) {
            return null;
        }

        return this.slots.get(ind);
    }

    public void setSlot(int ind, Slot slot) {
        if (this.indiceInvalido(ind)) {
            return;
        }
        this.slots.set(ind, slot);
    }

    public void clearSlot(int ind) {
        if (this.indiceInvalido(ind)) {
            return;
        }
        this.slots.set(ind, new Slot("empty", 0, 0));
    }

    public float withdrawCash() {
        Solver.println("voce recebeu " + Solver.decForm.format(this.cash) + " RS");
        float cash = this.cash;
        this.cash = 0;
        return cash;
    }

    public void insertCash(float cash) {
        if (cash < 0) {
            Solver.println("fail: valor invalido");
            return;
        }
        this.cash += cash;
    }

    public float getProfit() {
        return this.profit;
    }

    public void buyItem(int ind) {
        if (this.indiceInvalido(ind)) {
            return;
        }
        if (this.slots.get(ind).getQuantity() == 0) {
            Solver.println("fail: espiral sem produtos");
            return;
        }
        if (this.cash < this.slots.get(ind).getPrice()) {
            Solver.println("fail: saldo insuficiente");
            return;
        }
        this.cash -= this.slots.get(ind).getPrice();
        this.profit += this.slots.get(ind).getPrice();
        this.slots.get(ind).setQuantity(this.slots.get(ind).getQuantity() - 1);
        Solver.println("voce comprou um " + this.slots.get(ind).getName());
    }

    public String toString() {
        String s = "saldo: " + Solver.decForm.format(this.cash) + "\n";
        for (int i = 0; i < this.slots.size(); i++) {
            Slot slot = this.getSlot(i);
            s += i + " [" + slot + "]\n";
        }
        return s;
    }
}

class Solver {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine(0);

        while (true) {
            String linha = input();
            String[] palavras = linha.split(" ");
            println("$" + linha);

            if (palavras[0].equals("end")) {
                break;
            } else if (palavras[0].equals("init")) {
                machine = new VendingMachine(Integer.parseInt(palavras[1]));
            } else if (palavras[0].equals("show")) {
                print(machine);
            } else if (palavras[0].equals("set")) {
                machine.setSlot(Integer.parseInt(palavras[1]),
                        new Slot(palavras[2], Float.parseFloat(palavras[4]), Integer.parseInt(palavras[3])));
            } else if (palavras[0].equals("limpar")) {
                machine.clearSlot(Integer.parseInt(palavras[1]));
            } else if (palavras[0].equals("apurado")) {
                println("apurado total: " + decForm.format(machine.getProfit()));
            } else if (palavras[0].equals("dinheiro")) {
                machine.insertCash(Float.parseFloat(palavras[1]));
            } else if (palavras[0].equals("troco")) {
                machine.withdrawCash();
            } else if (palavras[0].equals("comprar")) {
                machine.buyItem(Integer.parseInt(palavras[1]));
            } else {
                println("comando inválido!");
            }
        }
    }

    public static Scanner scan = new Scanner(System.in);

    public static String input() {
        return scan.nextLine();
    }

    public static void println(Object str) {
        System.out.println(str);
    }

    public static void print(Object str) {
        System.out.print(str);
    }

    public static DecimalFormat decForm = new DecimalFormat("0.00");
}