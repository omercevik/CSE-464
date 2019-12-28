package hw3.part1;

class LEX extends Order
{
    @Override
    boolean compare(int r1, int g1, int b1, int r2, int g2, int b2)
    {
        if( r1 > r2 )
            return true;
        else if ( r1 < r2 )
            return false;
        else if ( g1 > g2 )
            return true;
        else if ( g1 < g2 )
            return false;
        else return b1 > b2;
    }
}
