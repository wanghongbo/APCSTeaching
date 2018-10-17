package package1;

public class Program {
    public static void main(String[] args) {
        //定义与赋值
        int count = 10;
        int COUNT = 12;
        int x = 1, y = 2;
        int z;
        z = 3;
        boolean checked = true;
        double totalPrice = 30.14;

        System.out.println(count);
        System.out.println(COUNT);
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
        System.out.println(checked);
        System.out.println(totalPrice);

        //四则运算
        int a = x + y + z;
        System.out.println("a: " + a);
        int b = x - y - z;
        System.out.println("b: " + b);
        int c = x * y * z;
        System.out.println("c: " + c);
        int d = (x + y) / z;
        System.out.println("d: " + d);

        //隐形转换
        double mount = count;
        System.out.println("mount: " + mount);

        //显示转换
        int i = (int)totalPrice;
        System.out.println("i: " + i);

        //包含隐式转换的计算
        double averagePrice = totalPrice / count;
        System.out.println("averagePrice: " + averagePrice);

        //练习
        Excise e = new Excise();
        e.excise();

    }
}
