package inno.edu.utils;

import inno.edu.annotations.Cache;
import inno.edu.annotations.Mutator;

import java.util.Objects;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }
    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(3000)
    public double doubleValue() {
        //Эмуляция долгой работы метода.
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("Invoke Double Value!");
        return (double) num/denum;
    }

    @Cache(3000)
    @Override
    public double doubleValue(int newNum) {
        return (double) newNum/denum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return num == fraction.num && denum == fraction.denum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, denum);
    }
}
