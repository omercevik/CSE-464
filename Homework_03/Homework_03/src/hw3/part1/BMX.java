package hw3.part1;

public class BMX extends Order
{
    private int getBit(int n, int k)
    {
        return (n >> k) & 1;
    }


    @Override
    boolean compare(int r1, int g1, int b1, int r2, int g2, int b2)
    {
        int RGB1 = 0, RGB2 = 0;

        for (int i = 7; i > 0; --i)
        {
            RGB1 |= getBit(r1,i);
            RGB1 <<= 1;
            RGB1 |= getBit(g1,i);
            RGB1 <<= 1;
            RGB1 |= getBit(b1,i);
            RGB1 <<= 1;

            RGB2 |= getBit(r2,i);
            RGB2 <<= 1;
            RGB2 |= getBit(g2,i);
            RGB2 <<= 1;
            RGB2 |= getBit(b2,i);
            RGB2 <<= 1;
        }
        return RGB1 > RGB2;
    }
}
