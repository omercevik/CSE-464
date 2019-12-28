import vpt.ByteImage;
import vpt.Image;
import vpt.algorithms.display.Display2D;
import vpt.algorithms.io.Load;
import vpt.algorithms.mm.binary.BClosing;
import vpt.algorithms.mm.binary.path.BPathOpening;
import vpt.algorithms.mm.gray.GClosing;
import vpt.algorithms.mm.gray.path.GPathOpening;
import vpt.util.se.FlatSE;

/**
 * CSE 464 Digital Image Processing
 * Homework 02
 * 161044004
 * @author Omer CEVIK
 */
public class Main
{
	static boolean isConvert(Image image)
	{
		int darkSize = 0;
		int lightSize = 0;

		for (int i = 0; i < image.getXDim(); ++i)
			for (int j = 0; j < image.getYDim(); ++j) {
				if (image.getXYByte(i,j) > 127)
					++lightSize;
				else
					++darkSize;
			}

		return darkSize < lightSize;
	}

	static Image convertImage(Image image)
	{
		Image convertedImage = image.newInstance(false);

		for (int i = 0; i < convertedImage.getXDim(); ++i)
			for (int j = 0; j < convertedImage.getYDim(); ++j)
				convertedImage.setXYByte(i,j,255-image.getXYByte(i,j));

		return convertedImage;
	}

	static Image invokeMedian(Image source)
	{
		int[] SE = new int[9];
		Image medianImage = source.newInstance(false);
		Image operandImage = new ByteImage(medianImage.getXDim() + 2, medianImage.getYDim() + 2, medianImage.getCDim());

		operandImage.fill(true);

		for (int i = 0; i < medianImage.getXDim(); ++i)
			for (int j = 0; j < medianImage.getYDim(); ++j)
				for (int k = 0; k < medianImage.getCDim(); ++k)
					operandImage.setXYCByte(i + 1, j + 1, k, source.getXYByte(i, j));

		int dimOfX = operandImage.getXDim();
		int dimOfY = operandImage.getYDim();

		for (int x = 0; x < dimOfX - 3; ++x)
			for (int y = 0; y < dimOfY - 3; ++y)
				for (int i = 0; i < operandImage.getCDim(); ++i)
				{
					for (int xMask = 0; xMask < 3; ++xMask)
						for (int yMask = 0; yMask < 3; ++yMask)
							SE[xMask * 3 + yMask] = operandImage.getXYByte(x + xMask, y + yMask);

					java.util.Arrays.sort(SE);
					medianImage.setXYByte(x, y,SE[3]);
				}
		return medianImage;
	}

	static void createOutput(Image originalImage, String outputName, boolean isBinary)
	{
		Image convertedImage = isConvert(originalImage) ? convertImage(originalImage) : originalImage;

		Image medianImage = invokeMedian(convertedImage);

		Image pathOpeningImage = isBinary ? BPathOpening.invoke(medianImage, 80) : GPathOpening.invoke(medianImage, 100);

		Image closingImage = isBinary ? BClosing.invoke(pathOpeningImage, FlatSE.square(3)) : GClosing.invoke(pathOpeningImage, FlatSE.square(3));

		closingImage = isConvert(closingImage) ? closingImage : convertImage(closingImage);

		Display2D.invoke(closingImage,outputName);
	}

	public static void main(String[] args)
	{
		Image originalImageGrayScale = Load.invoke("grayscale-input.png");
		Display2D.invoke(originalImageGrayScale,"Original Gray Scale Image");

		createOutput(originalImageGrayScale, "Gray Scale Output", false);

		Image originalImageBinary = Load.invoke("binary-input.png");
		Display2D.invoke(originalImageBinary,"Original Binary Image");

		createOutput(originalImageBinary, "Binary Output", true);
	}
}