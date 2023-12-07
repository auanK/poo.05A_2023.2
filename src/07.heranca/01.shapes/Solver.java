import java.util.*;

class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Point2D p) {
        double cH = this.x - p.x;
        double cV = this.y - p.y;
        return Math.sqrt(cH * cH + cV * cV);
    }
}

abstract class Shape {
    private String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    public String getInfo() {
        return String.format("%s: A=%.2f P=%.2f", name, getArea(), getPerimeter());
    }
}

class Circle extends Shape {
    private Point2D center;
    private double radius;

    public Circle(Point2D center, double radius) {
        super("Circ");
        this.center = center;
        this.radius = radius;
    }

    public Point2D getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public boolean inside(Point2D p) {
        return center.distance(p) <= radius;
    }

    public String toString() {
        return String.format("Circ: C=(%.2f, %.2f), R=%.2f", center.getX(), center.getY(), radius);
    }
}

class Rectangle extends Shape {
    private Point2D P1;
    private Point2D P2;

    public Rectangle(Point2D P1, Point2D P2) {
        super("Rect");
        this.P1 = P1;
        this.P2 = P2;
    }

    public double getArea() {
        return Math.abs(P1.getX() - P2.getX()) * Math.abs(P1.getY() - P2.getY());
    }

    public double getPerimeter() {
        return 2 * (Math.abs(P1.getX() - P2.getX()) + Math.abs(P1.getY() - P2.getY()));
    }

    public boolean inside(Point2D p) {
        return P1.getX() <= p.getX() && p.getX() <= P2.getX() && P1.getY() <= p.getY() && p.getY() <= P2.getY();
    }

    public String toString() {
        return String.format("Rect: P1=(%.2f, %.2f) P2=(%.2f, %.2f)", P1.getX(), P1.getY(), P2.getX(), P2.getY());
    }
}

public class Solver {
    public static void main(String[] args) {
        ArrayList<Shape> shapes = new ArrayList<Shape>();

        while (true) {
            String line = input();
            println("$" + line);
            String[] arguments = line.split(" ");

            if (arguments[0].equals("end")) {
                break;
            } else if (arguments[0].equals("circle")) {
                shapes.add(new Circle(new Point2D(number(arguments[1]), number(arguments[2])), number(arguments[3])));
            } else if (arguments[0].equals("rect")) {
                shapes.add(new Rectangle(new Point2D(number(arguments[1]), number(arguments[2])),
                        new Point2D(number(arguments[3]), number(arguments[4]))));
            } else if (arguments[0].equals("show")) {
                for (Shape shape : shapes) {
                    println(shape);
                }
            } else if (arguments[0].equals("info")) {
                for (Shape shape : shapes) {
                    println(shape.getInfo());
                }
            } else {
                println("fail: comando invalido");
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
