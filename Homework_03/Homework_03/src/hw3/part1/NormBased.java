package hw3.part1;

public class NormBased extends Order
{
    @Override
    boolean compare(int r1, int g1, int b1, int r2, int g2, int b2)
    {
        return Math.sqrt(r1*r1 + g1*g1 + b1*b1) > Math.sqrt(r2*r2 + g2*g2 + b2*b2);
    }
}
