import java.util.Scanner;
import java.lang.Math;

class Ponto {
    float x;
    float y;

    public Ponto(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float calcularDistancia(Ponto outroPonto) {
        float dx = outroPonto.x - this.x;
        float dy = outroPonto.y - this.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}

class Circulo {
    Ponto centro;
    float raio;

    public Circulo(Ponto centro, float raio) {
        this.centro = centro;
        this.raio = raio;
    }
}

public class Solver {
    static int quantidade(int n, Ponto p[], Circulo c) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            float d = p[i].calcularDistancia(c.centro);
            if (d <= c.raio)
                count++;
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        Ponto[] pontos = new Ponto[n];

        for (int i = 0; i < n; i++)
            pontos[i] = new Ponto(sc.nextFloat(), sc.nextFloat());

        Circulo c = new Circulo(new Ponto(sc.nextFloat(), sc.nextFloat()), sc.nextFloat());

        sc.close();

        int numPontos = quantidade(n, pontos, c);
        System.out.println(numPontos);
    }
}