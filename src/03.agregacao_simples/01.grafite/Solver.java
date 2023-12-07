import java.text.DecimalFormat;
import java.util.Scanner;

class Lead {
    private float thickness; // calibre
    private String hardness; // dureza
    private int size; // tamanho em mm

    public Lead(float thickness, String hardness, int size) {
        this.thickness = thickness;
        this.hardness = hardness;
        this.size = size;
    }

    public float getThickness() {
        return thickness;
    }

    public String getHardness() {
        return hardness;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int usagePerSheet() {
        if (hardness.equals("HB"))
            return 1;
        else if (hardness.equals("2B"))
            return 2;
        else if (hardness.equals("4B"))
            return 4;
        else
            return 6;
    }

    public String toString() {
        DecimalFormat form = new DecimalFormat("0.0");
        return form.format(thickness) + ":" + hardness + ":" + size;
    }
}

class Pencil {
    private float thickness;
    private Lead tip;

    public Pencil(float thickness) {
        this.thickness = thickness;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float value) {
        thickness = value;
    }

    public boolean hasGrafite() {
        return tip != null;
    }

    public boolean insert(Lead grafite) {
        if (grafite.getThickness() == thickness) {
            if (!hasGrafite()) {
                tip = grafite;
                return true;
            } else {
                System.out.println("fail: ja existe grafite");
            }
        } else {
            System.out.println("fail: calibre incompativel");
        }
        return false;
    }

    public Lead remove() {
        if (hasGrafite()) {
            Lead aux = tip;
            tip = null;
            return aux;
        }
        return tip;
    }

    public void writePage() {
        if (hasGrafite()) {
            if (tip.getSize() >= tip.usagePerSheet() && tip.getSize() > 10) {
                int wrt = tip.getSize() - tip.usagePerSheet();
                if (wrt < 10) {
                    System.out.println("fail: folha incompleta");
                    tip.setSize(10);
                } else {
                    tip.setSize(wrt);
                }
            } else {
                System.out.println("fail: tamanho insuficiente");
            }

        } else {
            System.out.println("fail: nao existe grafite");
        }
    }

    public String toString() {
        String saida = "calibre: " + thickness + ", grafite: ";
        if (tip != null)
            saida += "[" + tip + "]";
        else
            saida += "null";
        return saida;
    }
}

public class Solver {
    public static void main(String[] args) {
        Pencil lap = new Pencil(0.5f);

        while (true) {
            String line = input();
            String[] argsL = line.split(" ");
            write('$' + line);

            if ("end".equals(argsL[0])) {
                break;
            } else if ("init".equals(argsL[0])) {
                lap = new Pencil(number(argsL[1]));
            } else if ("insert".equals(argsL[0])) {
                lap.insert(new Lead(number(argsL[1]), argsL[2], (int) number(argsL[3])));
            } else if ("remove".equals(argsL[0])) {
                lap.remove();
            } else if ("write".equals(argsL[0])) {
                lap.writePage();
            } else if ("show".equals(argsL[0])) {
                write(lap.toString());
            }
        }
    }

    static Scanner scanner = new Scanner(System.in);

    public static String input() {
        return scanner.nextLine();
    }

    public static void write(String value) {
        System.out.println(value);
    }

    public static float number(String str) {
        return Float.parseFloat(str);
    }
}