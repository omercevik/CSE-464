package hw3.part2;

/**
 * CSE 464
 * Digital Image Processing
 * Homework 03
 * Part 2
 * 161044004
 * @author Omer CEVIK
 */
public class Main
{
    public static void main(String[] args)
    {
        NN knn1 = new NN(args[0],0);
        knn1.evaluateNN();

        NN knn2 = new NN(args[0],1);
        knn2.evaluateNN();
    }
}
