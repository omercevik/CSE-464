package hw3.part2;

import java.io.*;
import java.util.Arrays;
import java.util.stream.IntStream;

class NN
{
    private Double[][] train = new Double[480][497];
    private Double[][] test = new Double[4320][497];
    private Double[] classes = new Double[24];
    private Integer[] trainClass = new Integer[480];
    private Integer[] testClass = new Integer[4320];
    private Integer[] results = new Integer[4320];

    NN(final String imagesPath, final int testType)
    {
        final String trainFile = "histogramTrain.txt";
        final String testFile1 = "histogramTest1.txt";
        final String testFile2 = "histogramTest2.txt";

        Arrays.setAll(train, i -> new Double[497]);
        Arrays.setAll(test, i -> new Double[497]);
        Arrays.fill(classes, 0.0);
        Arrays.fill(results,0);
        Arrays.fill(trainClass,0);

        try {
            LBP lbp = new LBP();

            System.out.println("\nReading training files to evaluate histogram...");
            lbp.createLBPHistograms(imagesPath,0,480, trainFile);
            readTrainFile(trainFile, train);

            if (testType == 0)
            {
                System.out.println("Using first test files to evaluate histogram and accuracy results :");
                lbp.createLBPHistograms(imagesPath,480,4800, testFile1);
                readTrainFile(testFile1, test);
            }
            else if (testType == 1)
            {
                System.out.println("Using second test files to evaluate histogram and accuracy results :");
                lbp.createLBPHistograms(imagesPath,4800,9120,testFile2);
                readTrainFile(testFile2, test);
            }
            else
                throw new IOException("Test Type must be for 000 or 001!\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Double L_2Distance(final Double[] testRow, final Double[] trainRow)
    {
        return IntStream.range(0, testRow.length).mapToDouble(i -> Math.abs(testRow[i] - trainRow[i])).sum();
    }

    void evaluateNN()
    {
        Double[] min = new Double[497];
        Double[] max = new Double[497];

        Arrays.fill(min, Double.MAX_VALUE);
        Arrays.fill(max, 0.0);

        setMinAndMaxArrays(min, max);

        setTrainAndTestUsingMinAndMax(min, max);

        evaluateL2Distance();

        evaluateAccuracy();
    }

    private void readTrainFile(String path, Double[][] hists) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/hw3/part2/Output/" + path)));

        for (int i = 0; i < hists.length; i++)
        {
            Double[] hist = hists[i];
            String row = bufferedReader.readLine();
            String[] rowElements = row.split(" ");

            if (hists.length == 480)
                trainClass[i] = Integer.parseInt(rowElements[rowElements.length - 1]);
            else if (hists.length == 4320)
            {
                testClass[i] = Integer.parseInt(rowElements[rowElements.length - 1]);
                ++classes[testClass[i]];
            }

            Arrays.setAll(hist, j -> Double.parseDouble(rowElements[j]));
        }
    }

    private void setMinAndMaxArrays(Double[] min, Double[] max)
    {
        for(int i = 0; i < 480; ++i)
            for(int j = 0;  j < 497; ++j)
            {
                if (train[i][j] > max[j])
                    max[j] = train[i][j];

                if (train[i][j] < min[j])
                    min[j] = train[i][j];
            }
    }

    private void setTrainAndTestUsingMinAndMax(Double[] min, Double[] max)
    {
        for(int i = 0; i < 497; ++i)
        {
            for(int j = 0; j < 480; ++j)
                if (max[i] - min[i] != 0)
                    train[j][i] = (train[j][i] - min[i]) / (max[i] - min[i]);

            for(int j = 0; j < 4320; ++j)
                if (max[i] - min[i] != 0)
                    test[j][i] = (test[j][i] - min[i]) / (max[i] - min[i]);
        }
    }

    private void evaluateL2Distance()
    {
        for(int i = 0; i < 4320; ++i)
        {
            double distanceOfMinimum = Double.MAX_VALUE;
            for(int j = 0; j < 480; ++j)
            {
                Double resultDistance = L_2Distance(test[i], train[j]);
                if (resultDistance < distanceOfMinimum)
                {
                    distanceOfMinimum = resultDistance;
                    results[i] = trainClass[j];
                }
            }
        }
    }

    private void evaluateAccuracy()
    {
        Double[] accuracies = new Double[24];
        Arrays.fill(accuracies,0.0);

        for(int i = 0; i < testClass.length; ++i)
            if (testClass[i].equals(results[i]))
                ++accuracies[testClass[i]];

        for(int i = 0; i < 24; ++i)
        {
            accuracies[i] /= classes[i];
            System.out.println(i + ". Class evaluates " + String.format("%.2f", accuracies[i]*100) + " accuracy!");
        }
    }
}
