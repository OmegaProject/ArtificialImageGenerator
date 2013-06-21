import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister64;

import com.sun.media.jai.codec.SeekableOutputStream;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codecimpl.TIFFImageEncoder;

/**
 * 
 */

public class ArtificialImageGenerator {

	public static final int MOV_HORIZONTAL = 1;
	public static final int MOV_VERTICAL = 2;

	public static final int RGB = 0;
	public static final int R = 1;
	public static final int G = 2;
	public static final int B = 3;

	private final Map<Integer, ArrayList<Integer>> images;
	private final Map<Integer, Map<Integer, Point>> particles;

	private final String imageNamePrefix;
	private final int imageNamePostifxDigits;

	private final int numberOfImages;
	private final int color;
	private final int sizeW;
	private final int sizeH;

	private final int intensityPeakValue;
	private final int backgroundValue;

	private final int numberOfParticles;
	private final int particlesMovDirection;
	private final int particlesMovSpeed;
	private final int particlesMovOscillation;

	public ArtificialImageGenerator(final String imageNamePrefix,
			final int imageNamePostifxDigits, final int numberOfImages,
			final int sizeW, final int sizeH, final int backgroundValue,
			final int color, final int intensityPeakValue,
			final int numberOfParticles, final int particlesMovDirection,
			final int particlesMovSpeed, final int particlesMovOscillation) {
		this.imageNamePrefix = imageNamePrefix;
		this.imageNamePostifxDigits = imageNamePostifxDigits;
		this.numberOfImages = numberOfImages;
		this.sizeW = sizeW;
		this.sizeH = sizeH;
		this.backgroundValue = backgroundValue;
		this.color = color;
		this.intensityPeakValue = intensityPeakValue;
		this.numberOfParticles = numberOfParticles;
		this.particlesMovDirection = particlesMovDirection;
		this.particlesMovSpeed = particlesMovSpeed;
		this.particlesMovOscillation = particlesMovOscillation;

		images = new HashMap<Integer, ArrayList<Integer>>();
		particles = new HashMap<Integer, Map<Integer, Point>>();
	}

	public void generate() throws IOException {
		initializeImages();
		generateImagesBackgrounds();
		initializeImageParticleCenters(images.get(0));
		moveParticleCentersBetweenFrames();
		generateImagesRealParticles();
		applyImagesPoissonDistribution();
		writeImages();
	}

	public int computePoint(final int x, final int y) {
		return ((y * sizeW) + x);
	}

	private void initializeImages() {
		for (int i = 0; i < numberOfImages; i++) {
			final ArrayList<Integer> img = new ArrayList<Integer>();
			for (int x = 0; x < sizeW; x++)
				for (int y = 0; y < sizeH; y++)
					img.add(0);
			// for (int k = 0; k < sizeW * sizeH; k++)
			// img.add(0);
			images.put(i, img);
		}

	}

	private void generateImagesBackgrounds() {
		for (int i = 0; i < numberOfImages; i++)
			generateImageBackground(images.get(i));
	}

	private void generateImageBackground(final ArrayList<Integer> image) {
		int point;
		for (int x = 0; x < sizeW; x++)
			for (int y = 0; y < sizeH; y++) {
				point = computePoint(x, y);
				image.set(point, backgroundValue);
			}
		// for (int i = 0; i < sizeW * sizeH; i++)
		// image.set(i, backgroundValue);
	}

	public void initializeImageParticleCenters(final ArrayList<Integer> image) {
		int point;
		int sideSize = 0;
		int spaceBetweenParticles = 0;
		int anchorPointX = 0;
		int anchorPointY = 0;
		final int intensityValue = intensityPeakValue - backgroundValue;
		final Map<Integer, Point> imageParticles = new HashMap<Integer, Point>();
		switch (particlesMovDirection) {
		case MOV_HORIZONTAL:
			sideSize = sizeH;
			spaceBetweenParticles = sideSize / numberOfParticles;
			anchorPointY = spaceBetweenParticles / 2;
			anchorPointX = 10;
			for (int z = 0; z < numberOfParticles; z++) {
				point = computePoint(anchorPointX, anchorPointY);
				image.set(point, intensityValue);
				imageParticles.put(z, new Point(anchorPointX, anchorPointY));
				anchorPointY += spaceBetweenParticles;
			}
			break;
		case MOV_VERTICAL:
			sideSize = sizeW;
			spaceBetweenParticles = sideSize / numberOfParticles;
			anchorPointX = spaceBetweenParticles / 2;
			anchorPointY = 10;
			for (int z = 0; z < numberOfParticles; z++) {
				image.set(computePoint(anchorPointX, anchorPointY),
						intensityValue);
				imageParticles.put(z, new Point(anchorPointX, anchorPointY));
				anchorPointX += spaceBetweenParticles;
			}
			break;
		default:
			return;
		}

		particles.put(0, imageParticles);
	}

	public void moveParticleCentersBetweenFrames() {
		int point;
		int sideSize = 0;
		int spaceBetweenParticles = 0;
		int anchorPointX = 0;
		int anchorPointY = 0;
		final int intensityValue = intensityPeakValue - backgroundValue;
		switch (particlesMovDirection) {
		case MOV_HORIZONTAL:
			sideSize = sizeH;
			spaceBetweenParticles = sideSize / numberOfParticles;
			anchorPointX = 10;
			for (int i = 1; i < numberOfImages; i++) {
				anchorPointX += particlesMovSpeed;
				anchorPointY = spaceBetweenParticles / 2;
				if (i % 2 == 0)
					anchorPointY += particlesMovOscillation;
				else
					anchorPointY -= particlesMovOscillation;
				final ArrayList<Integer> image = images.get(i);
				final Map<Integer, Point> imageParticles = new HashMap<Integer, Point>();
				for (int z = 0; z < numberOfParticles; z++) {
					point = computePoint(anchorPointX, anchorPointY);
					image.set(point, intensityValue);
					imageParticles
							.put(z, new Point(anchorPointX, anchorPointY));
					anchorPointY += spaceBetweenParticles;
				}
				particles.put(i, imageParticles);
			}
			break;
		case MOV_VERTICAL:
			sideSize = sizeW;
			spaceBetweenParticles = sideSize / numberOfParticles;
			anchorPointY = 10;
			for (int i = 1; i < numberOfImages; i++) {
				anchorPointY += particlesMovSpeed;
				anchorPointX = spaceBetweenParticles / 2;
				if (i % 2 == 0)
					anchorPointX += particlesMovOscillation;
				else
					anchorPointX -= particlesMovOscillation;
				final ArrayList<Integer> image = images.get(i);
				final Map<Integer, Point> imageParticles = new HashMap<Integer, Point>();
				for (int z = 0; z < numberOfParticles; z++) {
					point = computePoint(anchorPointX, anchorPointY);
					image.set(point, intensityValue);
					imageParticles
							.put(z, new Point(anchorPointX, anchorPointY));
					anchorPointX += spaceBetweenParticles;
				}
				particles.put(i, imageParticles);
			}
			break;
		default:
			return;
		}
	}

	private void generateImagesRealParticles() {
		for (int i = 0; i < numberOfImages; i++) {
			final ArrayList<Integer> image = images.get(i);
			final Map<Integer, Point> imageParticles = particles.get(i);
			generateImageRealParticles(image, imageParticles);
		}
	}

	private void generateImageRealParticles(final ArrayList<Integer> image,
			final Map<Integer, Point> imageParticles) {
		final int intensityValue = intensityPeakValue - backgroundValue;
		int x, y;
		int radius = 0;
		Double newValue;
		boolean stop;
		for (final Point p : imageParticles.values()) {
			stop = false;
			while (!stop) {
				radius++;
				for (int xMov = -radius; xMov <= radius; xMov++) {
					x = p.x + xMov;
					for (int yMov = -radius; yMov <= radius; yMov++) {
						y = p.y + yMov;
						newValue = findRealValue(intensityValue, p.x, p.y, x, y);
						if (newValue < backgroundValue) {
							stop = true;
							continue;
						}
						image.set(computePoint(x, y), newValue.intValue());
					}
				}
			}
		}
	}

	private double findRealValue(final double realValue, final int xP,
			final int yP, final int x, final int y) {
		return realValue
				* Math.exp(-(Math.pow((x - xP), 2) + Math.pow((y - yP), 2)) / 4);
	}

	private void applyImagesPoissonDistribution() {
		final MersenneTwister64 rand = new MersenneTwister64(Calendar
				.getInstance().getTime());
		final Poisson poisson = new Poisson(0, rand);
		int replaceValue;
		for (int i = 0; i < numberOfImages; i++) {
			final ArrayList<Integer> image = images.get(i);
			for (int n = 0; n < image.size(); n++) {
				replaceValue = poisson.nextInt(image.get(n));
				image.set(n, replaceValue);
			}
		}
	}

	private void writeImages() throws IOException {
		int point, pointVal;
		RandomAccessFile f;
		OutputStream os;
		final TIFFEncodeParam encodeParam = new TIFFEncodeParam();
		encodeParam.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);
		TIFFImageEncoder encoder;
		// encodeParam.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);
		final BufferedImage buffImg = new BufferedImage(sizeW, sizeH,
				BufferedImage.TYPE_INT_RGB);
		final WritableRaster raster = buffImg.getRaster();
		final int anInt[] = new int[3];
		for (int i = 0; i < numberOfImages; i++) {
			final ArrayList<Integer> image = images.get(i);
			f = new RandomAccessFile("imgs\\" + imageNamePrefix + i + ".tif",
					"rw");
			os = new SeekableOutputStream(f);
			encoder = new TIFFImageEncoder(os, encodeParam);
			for (int x = 0; x < sizeW; x++)
				for (int y = 0; y < sizeH; y++) {
					point = computePoint(x, y);
					pointVal = image.get(point);
					anInt[0] = 0;
					anInt[1] = 0;
					anInt[2] = 0;
					switch (color) {
					case RGB:
						anInt[0] = pointVal;
						anInt[1] = pointVal;
						anInt[2] = pointVal;
						break;
					case R:
						anInt[0] = pointVal;
						break;
					case G:
						anInt[1] = pointVal;
						break;
					case B:
						anInt[2] = pointVal;
					default:
						break;
					}
					raster.setPixel(x, y, anInt);
				}
			encoder.encode(buffImg);
			// JAI.create("encode", buffImg, fos, "TIFF", encodeParam);
			// encoder = new TIFFImageEncoder(fos, encodeParam);
			// encoder.encode(buffImg);
			os.close();
		}
	}
}
