
public class StringTest {
    public static void main(String[] args) {
        String a = "abc"; // 1 Object: "abc" added to pool

        String b = "abc"; // 0 Object: because it is already in the pool

        String c = new String("abc"); // 1 Object

        String d = new String("def"); // 1 Object + "def" is added to the Pool

        String e = d.intern(); // (e==d) is "false" because e refers to the String in pool

        String f = e.intern(); // (f==e) is "true"

        System.out.println("a == b :" + (a == b));
        System.out.println("c == b :" + (c == b));
        System.out.println("e == d :" + (e == d));
        System.out.println("f == e :" + (f == e));
//Total Objects: 4 ("abc", c, d, "def").
    }
}
