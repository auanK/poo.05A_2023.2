import java.util.Scanner;

class Aluno {
    String nome;
    int matr;
    String disc;
    float nota;
}

public class Solver {
    public static void main(String[] args) {

        Aluno a = new Aluno();

        Scanner sc = new Scanner(System.in);
        a.nome = sc.nextLine();
        a.matr = sc.nextInt();
        sc.nextLine();
        a.disc = sc.nextLine();
        a.nota = sc.nextFloat();
        sc.close();

        System.out.println("Nome = " + a.nome);
        System.out.println("Matrícula = " + a.matr);
        System.out.println("Disciplina = " + a.disc);
        System.out.println("Nota = " + a.nota);

    }
}