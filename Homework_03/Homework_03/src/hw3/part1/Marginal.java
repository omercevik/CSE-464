package hw3.part1;

public class Marginal extends Order
{
    @Override
    boolean compare(int r1, int g1, int b1, int r2, int g2, int b2) {

        if( r1 > r2 && g1 > g2 && b1 > b2)
            return true;
        else if ( r1 > r2 && g1 > g2 )
            return true;
        else if ( r1 > r2 && b1 > b2 )
            return true;
        else return g1 > g2 && b1 > b2;
    }
}
