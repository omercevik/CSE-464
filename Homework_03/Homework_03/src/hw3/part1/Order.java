package hw3.part1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.awt.image.BufferedImage.*;

public abstract class Order
{
    private String filterType;

    BufferedImage filter(BufferedImage img, String filterType, int kernelSize)
    {
        BufferedImage image = new BufferedImage(img.getWidth(),img.getHeight(),TYPE_INT_RGB);
        this.filterType = filterType;

        for (int i = 0; i < image.getHeight(); ++i)
            for (int j = 0; j < image.getWidth(); ++j)
                image.setRGB(j,i,255*65536+255*256+255);

        Color[] pixels = new Color[kernelSize*kernelSize];

        for(int i = kernelSize/2; i < img.getWidth()-kernelSize/2; ++i)
            for(int j = kernelSize/2; j < img.getHeight()-kernelSize/2; ++j)
            {
                setKernel(pixels, kernelSize, img, i, j);
                sort(pixels);
                image.setRGB(i, j, pixels[pixels.length/2].getRGB());
            }
        printImage(image,filterType);

        return image;
    }

    private void sort(Color[] pixels)
    {
        if (filterType.startsWith("M"))
        {
            int[] R = new int[pixels.length];
            int[] G = new int[pixels.length];
            int[] B = new int[pixels.length];

            for (int i = 0; i < pixels.length; ++i)
            {
                R[i] = pixels[i].getRed();
                G[i] = pixels[i].getGreen();
                B[i] = pixels[i].getBlue();
            }

            Arrays.sort(R);
            Arrays.sort(G);
            Arrays.sort(B);

            pixels[pixels.length/2] = new Color(R[pixels.length/2], G[pixels.length/2], B[pixels.length/2]);
        }
        else
        {
            for (int i = 0; i < pixels.length; ++i)
                for (int j = 0; j < pixels.length; ++j)
                    if (compare(pixels[i].getRed(), pixels[i].getGreen(), pixels[i].getBlue(), pixels[j].getRed(), pixels[j].getGreen(), pixels[j].getBlue()))
                    {
                        Color temp = new Color(pixels[i].getRGB());
                        pixels[i] = new Color(pixels[j].getRGB());
                        pixels[j] = new Color(temp.getRGB());
                    }
        }
    }

    private void setKernel(Color[] pixels, int kernelSize, BufferedImage img, int i, int j)
    {
        int p = 0;
        for (int k = 0; k < kernelSize; ++k)
            for (int l = 0; l < kernelSize; ++l)
                pixels[p++] = new Color(img.getRGB(i-k+kernelSize/2,j-l+kernelSize/2));
    }

    void MSE(BufferedImage originalImage, BufferedImage filteredImage, String filterType)
    {
        BufferedImage result = new BufferedImage(originalImage.getWidth(),originalImage.getHeight(),TYPE_INT_RGB);
        double mse = 0f;
        for (int i = 0; i < originalImage.getHeight(); ++i)
            for (int j = 0; j < originalImage.getWidth(); ++j) {
                int rgb = (int)Math.pow(originalImage.getRGB(j,i) - filteredImage.getRGB(j,i),2);
                mse += Math.pow(originalImage.getRGB(j,i) - filteredImage.getRGB(j,i),2);
                result.setRGB(j,i,rgb);
            }
        System.out.println(filterType + " is : " + mse/(originalImage.getHeight()*originalImage.getWidth()));
        printImage(result,filterType);
    }

    public static void printImage(BufferedImage image, String filterType)
    {
        JFrame frame = new JFrame(filterType);
        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setSize(image.getWidth()+20, image.getHeight()+40);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    abstract boolean compare(int r1, int g1, int b1, int r2, int g2, int b2);
}
