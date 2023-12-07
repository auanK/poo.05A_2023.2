import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

class Client {
    private String id;
    private String fone;

    public Client(String id, String fone) {
        this.id = id;
        this.fone = fone;
    }

    @Override
    public String toString() {
        return id + ":" + fone;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFone() {
        return this.fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }
}

class Sala {
    private List<Client> cadeiras;

    public Sala(int capacidade) {
        cadeiras = new ArrayList<>(capacidade);
        for (int i = 0; i < capacidade; i++) {
            cadeiras.add(null);
        }
    }

    public List<Client> getCadeiras() {
        return this.cadeiras;
    }

    public boolean reservar(String id, String fone, int ind) {
        if (ind < 0 || ind >= cadeiras.size()) {
            System.out.println("fail: cadeira nao existe");
            return false;
        }

        if (cadeiras.get(ind) != null) {
            System.out.println("fail: cadeira ja esta ocupada");
            return false;
        }

        for (Client cliente : cadeiras) {
            if (cliente != null && cliente.getId().equals(id)) {
                System.out.println("fail: cliente ja esta no cinema");
                return false;
            }
        }

        cadeiras.set(ind, new Client(id, fone));
        return true;
    }

    public void cancelar(String id) {
        for (int i = 0; i < cadeiras.size(); i++) {
            if (cadeiras.get(i) != null && cadeiras.get(i).getId().equals(id)) {
                cadeiras.set(i, null);
                return;
            }
        }
        System.out.println("fail: cliente nao esta no cinema");
    }

    @Override
    public String toString() {
        String saida = "[";
        for (Client cliente : cadeiras) {
            if (cliente == null)
                saida += "- ";
            else
                saida += cliente + " ";
        }
        saida = saida.trim();
        return saida + "]";
    }
}

class Solver {
    static Shell sh = new Shell();
    static Sala sala = new Sala(0);

    public static void main(String args[]) {
        sh.chain.put("init", () -> {
            sala = new Sala(getInt(1));
        });
        sh.chain.put("show", () -> {
            System.out.println(sala);
        });
        sh.chain.put("reservar", () -> {
            sala.reservar(getStr(1), getStr(2), getInt(3));
        });
        sh.chain.put("cancelar", () -> {
            sala.cancelar(getStr(1));
        });

        sh.execute();
    }

    static int getInt(int pos) {
        return Integer.parseInt(sh.param.get(pos));
    }

    static String getStr(int pos) {
        return sh.param.get(pos);
    }
}

class Shell {
    public Scanner scanner = new Scanner(System.in);
    public HashMap<String, Runnable> chain = new HashMap<>();
    public ArrayList<String> param = new ArrayList<>();

    public Shell() {
        Locale.setDefault(new Locale("en", "US"));
    }

    public void execute() {
        while (true) {
            param.clear();
            String line = scanner.nextLine();
            Collections.addAll(param, line.split(" "));
            System.out.println("$" + line);
            if (param.get(0).equals("end")) {
                break;
            } else if (chain.containsKey(param.get(0))) {
                chain.get(param.get(0)).run();
            } else {
                System.out.println("fail: comando invalido");
            }
        }
    }
}
