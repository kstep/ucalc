package me.kstep.ucalc.numbers;

class Test {
    public static void println(Object line) {
        System.out.println(line.toString());
    }

    public static void main(String argv[]) {
        UNumber a = new UInteger(-1);
        println(a.root());

        UNumber b = new UComplex(-1, 0);
        println(b.root());
    }
}

