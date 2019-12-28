package hw3.part2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

class LBP
{
    private BufferedImage image;
    private BufferedImage equImage;
    private Integer[] histOfLBP = new Integer[512];
    private int counter = 0;
    private FileWriter fileWriter;
    private WritableRaster raster;
    private int kernelSize = 5;

    private int g(final int x)
    {
        return x >= 0 ? 1 : 0;
    }

    private void setImage(final BufferedImage image) throws IOException
    {
        this.image = image;
        Arrays.fill(histOfLBP, 0);
        evaluateLBP();
    }

    private void evaluateLBP() throws IOException
    {
        assert image != null;
        WritableRaster wr = image.getRaster();
        Integer[][] resultsOfLBP = new Integer[this.image.getHeight()-2][this.image.getWidth()-2];

        for (int i = 0; i < resultsOfLBP.length; ++i)
            resultsOfLBP[i] = new Integer[this.image.getWidth()-2];

        for (int i = 1, m = 0; i < image.getWidth() - 1; ++i, ++m)
            for (int j = 1, n = 0; j < image.getHeight() - 1; ++j, ++n)
            {
                int sum = 0;
                for (int k = 0; k < 3; ++k)
                    for (int l = 0; l < 3; ++l)
                    {
                        int q = wr.getSample(i,j,0);
                        int qi = wr.getSample(i - k + 1,j - l + 1,0);
                        sum += g(q - qi) * Math.pow(2, k * 3 + l);
                    }
                resultsOfLBP[m][n] = sum;
            }

        for (Integer[] integers : resultsOfLBP)
            for (Integer integer : integers)
                ++histOfLBP[integer];

        for (int i = 16; i < histOfLBP.length; ++i)
            fileWriter.append(String.valueOf(histOfLBP[i])).append(" ");

        fileWriter.append(String.valueOf(counter));
        fileWriter.append('\n');
        fileWriter.flush();
    }

    void createLBPHistograms(String path, int start, int end, String outputFile) throws IOException
    {
        try {
            fileWriter = new FileWriter("src/hw3/part2/Output/" + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder zeroPadding = new StringBuilder("000000");
        counter = 0;

        for (int i = start; i < end; ++i)
        {
            if (i / 1000 != 0)
            {
                zeroPadding.deleteCharAt(2);
                zeroPadding.deleteCharAt(2);
                zeroPadding.deleteCharAt(3);
            }
            else if (i / 100 != 0)
            {
                zeroPadding.deleteCharAt(3);
                zeroPadding.deleteCharAt(4);
            }
            else if (i / 10 != 0)
                zeroPadding.deleteCharAt(4);

            zeroPadding.deleteCharAt(zeroPadding.length()-1);
            zeroPadding.append(i);

            File input_file = new File(path + zeroPadding + ".png");
            BufferedImage image = ImageIO.read(input_file);

            setEquImage(image);
            BufferedImage he = equalizeImage();

            if (i != 0 && i % 20 == 0 && end == 480)
                ++counter;
            else if (i != start && (i-start) % 180 == 0 && (end == 4800 || end == 9120))
                ++counter;

            setImage(he);
        }
    }

    private void setEquImage(BufferedImage equImage)
    {
        this.raster = equImage.getRaster();
        this.equImage = new BufferedImage(equImage.getWidth(), equImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    }

    private double P(final double n)
    {
        return (Math.pow(2, kernelSize) - 1) * n / 255;
    }

    private BufferedImage equalizeImage()
    {
        WritableRaster equRaster = equImage.getRaster();

        for (int i = kernelSize/2; i < equImage.getWidth() - kernelSize/2; ++i)
            for (int j = kernelSize/2; j < equImage.getHeight() - kernelSize/2; ++j)
            {
                double sk = 0f;
                for (int k = 0; k < kernelSize; ++k)
                    for (int l = 0; l < kernelSize; ++l)
                        sk += P(raster.getSample(i - k + kernelSize/2,j - l + kernelSize/2,0));

                equRaster.setSample(i, j,0, sk);
            }

        equImage.setData(equRaster);

        return equImage;
    }
}
