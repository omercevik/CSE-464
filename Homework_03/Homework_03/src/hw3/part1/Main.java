package hw3.part1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * CSE 464
 * Digital Image Processing
 * Homework 03
 * Part 1
 * 161044004
 * @author Omer CEVIK
 */
public class Main
{
    public static void main(String[] args)
    {
        BufferedImage image, originalImage;

        File input_file = new File(args[0]);

        try {
            image = ImageIO.read(input_file);
            originalImage = ImageIO.read(new File("src/hw3/part1/images/original.jpg"));

            Order bmx = new BMX();
            Order lex = new LEX();
            Order mar = new Marginal();
            Order norm = new NormBased();

            BufferedImage filteredImage;

            filteredImage = mar.filter(image,"Marginal Filter 3x3",3);
            mar.MSE(originalImage,filteredImage,"Marginal 3x3 MSE");

            filteredImage = bmx.filter(image,"Bitmix Filter 3x3",3);
            bmx.MSE(originalImage,filteredImage,"Bitmix 3x3 MSE");

            filteredImage = lex.filter(image,"Lex Filter 3x3",3);
            lex.MSE(originalImage,filteredImage,"Lex 3x3 MSE");

            filteredImage = norm.filter(image,"Norm Based Filter 3x3",3);
            norm.MSE(originalImage,filteredImage,"Norm Based 3x3 MSE");

            mar.filter(image,"Marginal Filter 5x5",5);
            bmx.filter(image,"Bitmix Filter 5x5",5);
            lex.filter(image,"Lex Filter 5x5",5);
            norm.filter(image,"Norm Based Filter 5x5",5);

            mar.filter(image,"Marginal Filter 7x7",7);
            bmx.filter(image,"Bitmix Filter 7x7",7);
            lex.filter(image,"Lex Filter 7x7",7);
            norm.filter(image,"Norm Based Filter 7x7",7);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
