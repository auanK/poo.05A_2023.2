import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum Coin {
    C10(0.1, 1, "C10"),
    C25(0.25, 2, "C25"),
    C50(0.5, 3, "C25"),
    C100(1, 4, "C100");

    private double value;
    private int volume;
    private String label;

    Coin(double value, int volume, String label) {
        this.value = value;
        this.volume = volume;
        this.label = label;
    }

    double getValue() {
        return this.value;
    }

    int getVolume() {
        return this.volume;
    }

    String getLabel() {
        return this.label;
    }

    public String toString() {
        return String.format("%.2f:%d", getValue(), getVolume());
    }
}

class Item {
    private String label;
    private int volume;

    Item(String label, int volume) {
        this.label = label;
        this.volume = volume;
    }

    String getLabel() {
        return this.label;
    }

    int getVolume() {
        return this.volume;
    }

    void setLabel(String label) {
        this.label = label;
    }

    void setVolume(int volume) {
        this.volume = volume;
    }

    public String toString() {
        return String.format("%s:%d", getLabel(), getVolume());
    }
}

class Pig {
    private boolean broken;
    private ArrayList<Coin> coins;
    private ArrayList<Item> items;
    private int volumeMax;

    Pig(int volumeMax) {
        this.volumeMax = volumeMax;
        coins = new ArrayList<>();
        items = new ArrayList<>();
    }

    boolean isBroken() {
        return this.broken;
    }

    int getVolumeMax() {
        return this.volumeMax;
    }

    int getVolume() {
        if (isBroken()) {
            return 0;
        }
        int v = 0;
        for (Coin coin : coins) {
            v += coin.getVolume();
        }
        for (Item item : items) {
            v += item.getVolume();
        }
        return v;
    }

    double getValue() {
        double v = 0;
        for (Coin coin : coins) {
            v += coin.getValue();
        }
        return v;
    }

    void addCoin(Coin coin) {
        if (isBroken()) {
            System.out.println("fail: the pig is broken");
            return;
        }
        if (getVolume() + coin.getVolume() > volumeMax) {
            System.out.println("fail: the pig is full");
            return;
        }

        this.coins.add(coin);

    }

    void addItem(Item item) {
        if (isBroken()) {
            System.out.println("fail: the pig is broken");
            return;
        }
        if (getVolume() + item.getVolume() > volumeMax) {
            System.out.println("fail: the pig is full");
            return;
        }
        this.items.add(item);
    }

    void breakPig() {
        if (!isBroken()) {
            this.broken = true;
            return;
        }
    }

    ArrayList<Coin> extractCoins() {
        if (!isBroken()) {
            System.out.println("fail: you must break the pig first");
            return new ArrayList<Coin>();
        }
        ArrayList<Coin> removed = new ArrayList<>();
        removed.addAll(coins);
        coins.removeAll(coins);
        return removed;
    }

    List<Item> extractItems() {
        if (!isBroken()) {
            System.out.println("fail: you must break the pig first");
            return new ArrayList<Item>();
        }

        ArrayList<Item> removed = new ArrayList<>();
        removed.addAll(items);
        items.removeAll(items);
        return removed;
    }

    public String toString() {
        return String.format("state=%s : coins=%s : items=%s : value=%.2f : volume=%d/%d",
                isBroken() ? "broken" : "intact", coins, items, getValue(),
                getVolume(), getVolumeMax());
    }

}

public class Solver {
    public static void main(String[] Args) {
        Scanner scanner = new Scanner(System.in);
        Pig pig = new Pig(0);

        while (true) {
            String line = scanner.nextLine();
            System.out.println("$" + line);
            String[] args = line.split(" ");

            if (args[0].equals("end")) {
                break;
            }

            else if (args[0].equals("addCoin")) {
                if (args[1].equals("10")) {
                    pig.addCoin(Coin.C10);
                } else if (args[1].equals("25")) {
                    pig.addCoin(Coin.C25);
                } else if (args[1].equals("50")) {
                    pig.addCoin(Coin.C50);
                } else if (args[1].equals("100")) {
                    pig.addCoin(Coin.C100);
                }
            } else if (args[0].equals("init")) {
                pig = new Pig(number(args[1]));
            } else if (args[0].equals("addItem")) {
                pig.addItem(new Item(args[1], number(args[2])));
            } else if (args[0].equals("break")) {
                pig.breakPig();
            }

            else if (args[0].equals("extractCoins")) {
                write("[" + pig.extractCoins().stream().map(coin -> "" + coin.toString())
                        .collect(Collectors.joining(", ")) + "]");
            }

            else if (args[0].equals("extractItems")) {
                write("[" + pig.extractItems().stream().map(item -> "" + item.toString())
                        .collect(Collectors.joining(", ")) + "]");
            }

            else if (args[0].equals("show")) {
                write(pig.toString());
            } else {
                write("fail: invalid command");
            }
        }
        scanner.close();
    }

    public static int number(String number) {
        return Integer.parseInt(number);
    }

    public static void write(String str) {
        System.out.println(str);
    }
}
