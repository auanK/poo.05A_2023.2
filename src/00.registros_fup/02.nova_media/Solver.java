import java.util.Scanner;

class Aluno {
    String nome;
    float notas[];

    void read() {
        Scanner sc = new Scanner(System.in);
        this.nome = sc.nextLine();
        this.notas = new float[3];

        for (int i = 0; i < 3; i++)
            this.notas[i] = sc.nextFloat();
        sc.close();
    }

    float media() {
        float soma = 0;
        for (int i = 0; i < 3; i++)
            soma += notas[i];
        soma /= 3;

        return soma;
    }

}

public class Solver {
    public static void main(String[] args) {
        Aluno a = new Aluno();
        a.read();
        System.out.printf("%.2f\n", a.media());
    }
}
