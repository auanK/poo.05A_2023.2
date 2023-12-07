import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

class Pessoa {
    private String nome;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }
}

class Mercantil {
    private ArrayList<Pessoa> caixas;
    private LinkedList<Pessoa> esperando;

    public Mercantil(int qtd_caixas) {
        this.caixas = new ArrayList<>();
        for (int i = 0; i < qtd_caixas; i++) {
            caixas.add(null);
        }
        this.esperando = new LinkedList<>();
    }

    public boolean validarIndice(int indice) {
        if (indice >= this.caixas.size()) {
            return false;
        }
        return true;
    }

    public void chegar(Pessoa person) {
        this.esperando.add(person);
    }

    public void chamarNoCaixa(int indice) {
        if (!validarIndice(indice)) {
            System.out.println("fail: caixa inexistente");
            return;
        }
        if (this.caixas.get(indice) != null) {
            System.out.println("fail: caixa ocupado");
            return;
        }
        if (this.esperando.isEmpty()) {
            System.out.println("fail: sem clientes");
            return;
        }
        this.caixas.set(indice, this.esperando.remove());
    }

    public Pessoa finalizar(int indice) {
        if (!validarIndice(indice)) {
            System.out.println("fail: caixa inexistente");
            return null;
        }
        if (this.caixas.get(indice) == null) {
            System.out.println("fail: caixa vazio");
            return null;
        }

        Pessoa person = this.caixas.get(indice);
        this.caixas.set(indice, null);
        return person;
    }

    public String toString() {
        String caixas = "Caixas: [";
        for (int i = 0; i < this.caixas.size(); i++) {
            if (this.caixas.get(i) == null) {
                caixas += "-----";
            } else {
                caixas += this.caixas.get(i).getNome();
            }
            if (i < this.caixas.size() - 1) {
                caixas += ", ";
            }
        }

        caixas += "]\n";

        String espera = "Espera: [";
        for (int i = 0; i < this.esperando.size(); i++) {
            espera += this.esperando.get(i).getNome();
            if (i < this.esperando.size() - 1) {
                espera += ", ";
            }
        }
        espera += "]";

        return caixas + espera;
    }
}

class Solver {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Mercantil mercantil = new Mercantil(0);

        while (true) {
            String line = sc.nextLine();
            System.out.println("$" + line);
            String ui[] = line.split(" ");

            if (ui[0].equals("init")) {
                mercantil = new Mercantil(Integer.parseInt(ui[1]));
            } else if (ui[0].equals("call")) {
                mercantil.chamarNoCaixa(Integer.parseInt(ui[1]));
            } else if (ui[0].equals("finish")) {
                mercantil.finalizar(Integer.parseInt(ui[1]));
            } else if (ui[0].equals("arrive")) {
                mercantil.chegar(new Pessoa(ui[1]));
            } else if (ui[0].equals("show")) {
                System.out.println(mercantil);
            } else if (ui[0].equals("end")) {
                break;
            } else {
                System.out.println("fail: comando invalido");
            }

        }
        sc.close();

    }

}
