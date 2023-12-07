import java.util.*;
import java.text.DecimalFormat;

class Client {
    private String clientId;
    private ArrayList<Account> accounts;

    public Client(String clientId) {
        this.clientId = clientId;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    public String getClientId() {
        return this.clientId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.clientId).append(" [");
        for (Account acc : this.accounts) {
            sb.append(acc.getAccId()).append(", ");
        }
        if (!this.accounts.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]\n");
        return sb.toString();
    }
}

abstract class Account {
    protected double balance;
    private static int nextAccountId = 0;
    protected int accId;
    protected String clientId;
    protected String typeId;

    public Account(String clientId, String typeId) {
        this.accId = nextAccountId++;
        this.clientId = clientId;
        this.typeId = typeId;
    }

    public void deposit(double value) {
        this.balance += value;
    }

    public void withdraw(double value) throws Exception {
        if (this.balance >= value) {
            this.balance -= value;
        } else {
            throw new Exception("fail: saldo insuficiente");
        }
    }

    public void transfer(Account other, double value) throws Exception {
        this.withdraw(value);
        other.deposit(value);
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return this.accId + ":" + this.clientId + ":" + df.format(this.balance) + ":" + this.typeId + "\n";
    }

    public double getBalance() {
        return this.balance;
    }

    public int getAccId() {
        return this.accId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public abstract void updateMonthly();
}

class CheckingAccount extends Account {
    private static final double MONTHLY_FEE = 20.0;

    public CheckingAccount(String clientId) {
        super(clientId, "CC");
    }

    @Override
    public void updateMonthly() {
        this.balance -= MONTHLY_FEE;
    }
}

class SavingsAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.01;

    public SavingsAccount(String clientId) {
        super(clientId, "CP");
    }

    @Override
    public void updateMonthly() {
        this.balance += this.balance * MONTHLY_INTEREST_RATE;
    }
}

class Agency {
    private Map<Integer, Account> accounts;
    private Map<String, Client> clients;

    private Account getAccount(int accountId) throws Exception {
        Account acc = this.accounts.get(accountId);
        if (acc == null) {
            throw new Exception("fail: conta nao encontrada");
        }
        return acc;
    }

    public Agency() {
        this.accounts = new HashMap<>();
        this.clients = new LinkedHashMap<>();
    }

    public void addClient(String clientId) {
        Client client = new Client(clientId);
        Account checkingAccount = new CheckingAccount(clientId);
        Account savingsAccount = new SavingsAccount(clientId);
        client.addAccount(checkingAccount);
        client.addAccount(savingsAccount);
        this.clients.put(clientId, client);
        this.accounts.put(checkingAccount.getAccId(), checkingAccount);
        this.accounts.put(savingsAccount.getAccId(), savingsAccount);
    }

    public void deposit(int accId, double value) throws Exception {
        Account acc = this.getAccount(accId);
        acc.deposit(value);
    }

    public void withdraw(int accId, double value) throws Exception {
        Account acc = this.getAccount(accId);
        acc.withdraw(value);
    }

    public void transfer(int fromAccId, int toAccId, double value) throws Exception {
        Account fromAcc = this.getAccount(fromAccId);
        Account toAcc = this.getAccount(toAccId);
        fromAcc.transfer(toAcc, value);
    }

    public void updateMonthly() {
        for (Account acc : this.accounts.values()) {
            acc.updateMonthly();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("- Clients\n");
        for (Client client : this.clients.values()) {
            sb.append(client);
        }
        sb.append("- Accounts\n");
        for (Account acc : this.accounts.values()) {
            sb.append(acc);
        }
        return sb.toString();
    }
}

public class Solver {
    public static void main(String[] args) {
        Agency agency = new Agency();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = input(scanner);
            println("$" + line);
            String[] inputArgs = line.split(" ");

            try {
                if (inputArgs[0].equals("end")) {
                    break;
                } else if (inputArgs[0].equals("show")) {
                    print(agency);
                } else if (inputArgs[0].equals("addCli")) {
                    agency.addClient(inputArgs[1]);
                } else if (inputArgs[0].equals("deposito")) {
                    agency.deposit((int) number(inputArgs[1]), number(inputArgs[2]));
                } else if (inputArgs[0].equals("saque")) {
                    agency.withdraw((int) number(inputArgs[1]), number(inputArgs[2]));
                } else if (inputArgs[0].equals("transf")) {
                    agency.transfer((int) number(inputArgs[1]), (int) number(inputArgs[2]), number(inputArgs[3]));
                } else if (inputArgs[0].equals("update")) {
                    agency.updateMonthly();
                } else {
                    println("fail: comando invalido");
                }
            } catch (Exception e) {
                println(e.getMessage());
            }
        }

        scanner.close();
    }

    private static String input(Scanner scanner) {
        return scanner.nextLine();
    }

    private static double number(String value) {
        return Double.parseDouble(value);
    }

    private static void println(Object value) {
        System.out.println(value);
    }

    private static void print(Object value) {
        System.out.print(value);
    }
}