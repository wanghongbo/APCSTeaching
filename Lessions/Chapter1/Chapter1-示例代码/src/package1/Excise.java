package package1;

public class Excise {
    public void excise() {
        int x = 10;
        double y = 20.5;
        int a, b;
        double c, d;
        a = (int)(y / x);
        c = y / x;
        b = (int)(x + y + a + c);
        d = x + y + a + c;
        System.out.println("a: " + a);
        System.out.println("b: " + b);
        System.out.println("c: " + c);
        System.out.println("d: " + d);
    }
}
