package me.kstep.ucalc.numbers;

public class Pair<A extends UNumber, B extends UNumber> {
    final public A first;
    final public B second;

    public Pair(A a, B b) {
        first = a;
        second = b;
    }
}
