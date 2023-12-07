import java.util.*;

enum Label {
    GIVE,
    TAKE,
    PLUS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

class Operation {
    private static int nextOpId = 0;
    private int id;
    private String name;
    private Label label;
    private int value;

    public Operation(String name, Label label, int value) {
        this.id = Operation.nextOpId++;
        this.name = name;
        this.label = label;
        this.value = value;
    }

    @Override
    public String toString() {
        return "id:" + this.id + " " + this.label + ":" + this.name + " " + this.value;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Label getLabel() {
        return this.label;
    }

    public int getValue() {
        return this.value;
    }
}

class Client {
    private String name;
    private int limite;
    ArrayList<Operation> operations;

    public Client(String name, int limite) {
        this.name = name;
        this.limite = limite;
        this.operations = new ArrayList<Operation>();
    }

    @Override
    public String toString() {
        String ss = this.name + " " + this.getBalance() + "/" + this.limite + "\n";
        for (Operation oper : this.operations) {
            ss += oper + "\n";
        }
        return ss;
    }

    public String getName() {
        return this.name;
    }

    public int getLimite() {
        return this.limite;
    }

    public ArrayList<Operation> getOperations() {
        return this.operations;
    }

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public int getBalance() {
        int balance = 0;
        for (Operation oper : this.operations) {
            if (oper.getLabel() == Label.TAKE) {
                balance -= oper.getValue();
            } else {
                balance += oper.getValue();
            }
        }
        return balance;
    }
}

class ClienteInexistenteException extends Exception {
    @Override
    public String getMessage() {
        return "fail: cliente nao existe";
    }
}

class ClienteException extends Exception {
    private boolean existe;

    public ClienteException(boolean existe) {
        this.existe = existe;
    }

    @Override
    public String getMessage() {
        if (this.existe)
            return "fail: cliente ja existe";
        else
            return "fail: cliente nao existe";
    }
}

class Agiota {
    private ArrayList<Client> aliveList;
    private ArrayList<Client> deathList;
    private ArrayList<Operation> aliveOper;
    private ArrayList<Operation> deathOper;

    private int searchClient(String name) {
        for (int i = 0; i < this.aliveList.size(); i++) {
            if (this.aliveList.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private void pushOperation(Client client, String name, Label label, int value) {
        Operation oper = new Operation(name, label, value);
        this.aliveOper.add(oper);
        client.addOperation(oper);
    }

    private void sortAliveList() {
        Collections.sort(this.aliveList, new Comparator<Client>() {
            public int compare(Client c1, Client c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
    }

    public Agiota() {
        this.aliveList = new ArrayList<Client>();
        this.deathList = new ArrayList<Client>();
        this.aliveOper = new ArrayList<Operation>();
        this.deathOper = new ArrayList<Operation>();
    }

    public Client getClient(String name) {
        int ind = this.searchClient(name);

        if (ind == -1) {
            return null;
        }

        return this.aliveList.get(ind);
    }

    public void addClient(String name, int limite) throws Exception {
        Client client = getClient(name);

        if (client != null) {
            throw new ClienteException(true);
        }

        this.aliveList.add(new Client(name, limite));

        this.sortAliveList();
    }

    public void give(String name, int value) throws Exception {
        Client client = getClient(name);

        if (client == null) {
            throw new ClienteException(false);
        }

        if (client.getBalance() + value > client.getLimite()) {
            throw new Exception("fail: limite excedido");
        }

        this.pushOperation(client, name, Label.GIVE, value);
    }

    public void take(String name, int value) throws Exception {
        Client client = getClient(name);

        if (client == null) {
            throw new ClienteException(false);
        }

        this.pushOperation(client, name, Label.TAKE, value);
    }

    public void kill(String name) {
        int ind = this.searchClient(name);

        if (ind == -1) {
            return;
        }

        this.deathList.add(this.aliveList.remove(ind));
        for (int i = 0; i < this.aliveOper.size(); i++) {
            if (this.aliveOper.get(i).getName().equals(name)) {
                this.deathOper.add(this.aliveOper.remove(i));
                i--;
            }
        }
    }

    public void plus() {
        for (Client client : this.aliveList) {
            this.pushOperation(client, client.getName(), Label.PLUS, (int) Math.ceil(0.1 * client.getBalance()));
        }

        for (int i = 0; i < this.aliveList.size(); i++) {
            Client client = this.aliveList.get(i);
            if (client.getBalance() > client.getLimite()) {
                this.kill(client.getName());
                i--;
            }
        }
    }

    @Override
    public String toString() {
        String ss = "";
        for (Client client : this.aliveList) {
            ss += ":) " + client.getName() + " " + client.getBalance() + "/" + client.getLimite() + "\n";
        }
        for (Operation oper : this.aliveOper) {
            ss += "+ " + oper + "\n";
        }
        for (Client client : this.deathList) {
            ss += ":( " + client.getName() + " " + client.getBalance() + "/" + client.getLimite() + "\n";
        }
        for (Operation oper : this.deathOper) {
            ss += "- " + oper + "\n";
        }
        return ss;
    }
}

public class Solver {
    public static void main(String[] arg) {
        System.out.println("side_by_side=080");

        Agiota agiota = new Agiota();

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");

            try {
                if (args[0].equals("end")) {
                    break;
                } else if (args[0].equals("init")) {
                    agiota = new Agiota();
                } else if (args[0].equals("show")) {
                    print(agiota);
                } else if (args[0].equals("showCli")) {
                    print(agiota.getClient(args[1]));
                } else if (args[0].equals("addCli")) {
                    agiota.addClient(args[1], (int) number(args[2]));
                } else if (args[0].equals("give")) {
                    agiota.give(args[1], (int) number(args[2]));
                } else if (args[0].equals("take")) {
                    agiota.take(args[1], (int) number(args[2]));
                } else if (args[0].equals("kill")) {
                    agiota.kill(args[1]);
                } else if (args[0].equals("plus")) {
                    agiota.plus();
                } else {
                    println("fail: comando invalido");
                }
            } catch (Exception e) {
                Solver.println(e.getMessage());
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

    public static void println(Object value) {
        System.out.println(value);
    }

    public static void print(Object value) {
        System.out.print(value);
    }
}