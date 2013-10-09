package core;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.SwingUtilities;

import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codecimpl.TIFFImageEncoder;

import enums.Bit;
import enums.Color;
import enums.MovementDirection;
import gui.ArtificialImageGeneratorGUI;

/**
 * Image generator engine
 * 
 * @author Alex Rigano
 * @version 0.4, Beta
 */
public class ArtificialImageGeneratorStandard implements Runnable {

	public static int FRAMES_PER_STEP = 25;

	private final File folder;

	private final Integer numOfDatasets;

	private final String imageName;
	private final Integer imagePostfixDigits;

	private final Integer numOfFrames;

	private final Integer height;
	private final Integer width;

	private final Bit bits;
	private final Color colors;

	private List<Double> signalPeakValues;
	private Double currentSignalPeakValue;
	private Double backgroundValue;

	private Integer numOfParticles;

	private Integer radius;
	private Integer sigmaValue;

	private Integer ctr;
	private Integer w;
	private Integer[][] xVal;
	private Integer[][] yVal;

	private MovementDirection movDir;
	private Double movSpeed;

	private List<File> folders;
	private List<Double[][]> frames;
	private List<List<MyPoint>> particles;

	private final Double actualMaxValue;

	private final List<MyPoint> lastAvailPoints;
	private int lastAvailIndex;

	private final boolean hasBackroundGen, hasParticleGen, hasGaussianGen,
	        hasPoissonGen, hasGenerateLog;

	private final ArtificialImageGeneratorGUI gui;

	public void setSigmaValue(final Integer sigmaValue) {
		this.sigmaValue = sigmaValue;
	}

	public void setRadius(final Integer radius) {
		this.radius = radius;
	}

	protected Integer getRadius() {
		return this.radius;
	}

	public void computeCTRAndW() {
		this.ctr = this.radius * this.sigmaValue;
		this.w = (this.ctr * 2) + 1;
	}

	public void setNumOfParticle(final int numOfParticles) {
		this.numOfParticles = numOfParticles;
	}

	protected int getNumOfParticle() {
		return this.numOfParticles;
	}

	public void setSignalPeakValue(final List<Double> signalPeakValues) {
		this.signalPeakValues = signalPeakValues;
	}

	public void setMovDir(final MovementDirection movDir) {
		this.movDir = movDir;
	}

	public void setMovSpeed(final Double movSpeed) {
		this.movSpeed = movSpeed;
	}

	protected List<List<MyPoint>> getParticles() {
		return this.particles;
	}

	protected void initParticles() {
		this.particles = new ArrayList<List<MyPoint>>();
	}

	protected void addFrameParticles(final List<MyPoint> frameParticles) {
		this.particles.add(frameParticles);
	}

	protected List<Double[][]> getFrames() {
		return this.frames;
	}

	protected Integer getCTR() {
		return this.ctr;
	}

	protected Double getSignalPeakValue() {
		return this.currentSignalPeakValue;
	}

	protected void setLastAvailPoints(final List<MyPoint> lastAvailPoints) {
		this.lastAvailPoints.clear();
		if (lastAvailPoints != null) {
			this.lastAvailPoints.addAll(lastAvailPoints);
		}
	}

	/**
	 * Create a new generatore with the given parameters
	 * 
	 * @param imageName
	 * @param imagePostfixDigits
	 * @param numOfFrames
	 * @param height
	 * @param width
	 * @param bits
	 * @param colors
	 * 
	 * @since 0.0
	 */
	public ArtificialImageGeneratorStandard(final File folder,
	        final int numOfDatasets, final String imageName,
	        final int imagePostfixDigits, final int numOfFrames,
	        final int height, final int width, final Bit bits,
	        final Color colors, final Double backgroundValue,
	        final int numOfParticles, final List<Double> signalPeakValues,
	        final MovementDirection movDirection, final Double movSpeed,
	        final int radius, final int sigmaValue,
	        final boolean hasBackgroundGen, final boolean hasParticleGen,
	        final boolean hasGaussianGen, final boolean hasPoissonGen,
	        final boolean hasGenerateLog, final ArtificialImageGeneratorGUI gui) {
		this.folder = folder;
		this.numOfDatasets = numOfDatasets;
		this.imageName = imageName;
		this.imagePostfixDigits = imagePostfixDigits;
		this.numOfFrames = numOfFrames;
		this.height = height;
		this.width = width;
		this.bits = bits;
		this.actualMaxValue = this.computeMaxValue(bits);
		this.colors = colors;
		this.backgroundValue = backgroundValue;

		this.numOfParticles = numOfParticles;
		this.signalPeakValues = signalPeakValues;
		this.movDir = movDirection;
		this.movSpeed = movSpeed;
		this.radius = radius;
		this.sigmaValue = sigmaValue;
		this.computeCTRAndW();

		this.hasBackroundGen = hasBackgroundGen;
		this.hasParticleGen = hasParticleGen;
		this.hasGaussianGen = hasGaussianGen;
		this.hasPoissonGen = hasPoissonGen;
		this.hasGenerateLog = hasGenerateLog;

		this.gui = gui;

		this.lastAvailPoints = new ArrayList<MyPoint>();
		this.lastAvailIndex = 0;
	}

	public void updateGUI(final String update) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					ArtificialImageGeneratorStandard.this.gui
					        .appendOutput(update);

				}
			});
		} catch (final InvocationTargetException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (final InterruptedException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		final int framePerStep = ArtificialImageGeneratorStandard.FRAMES_PER_STEP;
		int framePerLastStep = this.numOfFrames
		        % ArtificialImageGeneratorStandard.FRAMES_PER_STEP;
		int steps = this.numOfFrames / framePerStep;
		if (framePerLastStep > 0) {
			steps++;
		} else {
			framePerLastStep = framePerStep;
		}

		this.updateGUI("Starting generation of " + this.signalPeakValues.size()
		        + " different signals, " + this.numOfDatasets
		        + " datasets per signal, " + this.numOfFrames
		        + " frames per dataset");
		for (int k = 0; k < this.signalPeakValues.size(); k++) {
			this.lastAvailIndex = 0;
			this.lastAvailPoints.clear();
			this.currentSignalPeakValue = this.signalPeakValues.get(k);
			final int indexSignal = k + 1;
			this.updateGUI("Generating folders for signal " + " " + indexSignal
			        + "/" + this.signalPeakValues.size() + " value: "
			        + this.currentSignalPeakValue);
			this.generateFolders();
			this.updateGUI("Generating folders...completed");
			for (int i = 0; i < this.numOfDatasets; i++) {
				final int indexDataset = i + 1;
				this.updateGUI("Generating dataset " + indexDataset + "/"
				        + this.numOfDatasets);
				for (int s = 0; s < steps; s++) {
					final boolean tof = (s == (steps - 1));
					final int frameToDo = tof ? framePerLastStep : framePerStep;
					this.generateEmptyFrames(frameToDo);
					if (this.hasBackroundGen) {
						this.generateBackgroundLevel();
					}
					if (this.hasParticleGen) {
						this.generateParticles();
					}
					if (this.hasGaussianGen) {
						this.generateGaussianBlobs_V2();
					}
					if (this.hasPoissonGen) {
						this.generatePoissonDistribution();
					}
					try {
						this.generateFramesFiles(i);
						if (this.hasParticleGen && this.hasGenerateLog) {
							this.generateLogsFiles(i);
						}
						this.lastAvailIndex += this.frames.size();
					} catch (final FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				this.generateResultsFile();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.updateGUI("\nPrinting results:");
			final Double SNR_C_P = this.computeSNR_C_P();
			this.updateGUI("SNR_C_P value: " + SNR_C_P);
			final Double SNR_B_P = this.computeSNR_B_P();
			this.updateGUI("SNR_B_P value: " + SNR_B_P);
			final Double SNR_B_G = this.computeSNR_B_G();
			this.updateGUI("SNR_B_G value: " + SNR_B_G);
			this.updateGUI("************************\n");

		}
	}

	/**
	 * Compute the max value that an image with the given bit depth can have in
	 * its pixels, considering that its values are unsigned
	 * 
	 * @param bits
	 * @return
	 * 
	 * @since 0.2
	 */
	public Double computeMaxValue(final Bit bits) {
		Double val = -1.0;
		switch (bits) {
		case _4_Bits:
			val = (Math.pow(2, 4)) - 1;
			break;
		case _8_Bits:
			val = (Math.pow(2, 8)) - 1;
			break;
		case _16_Bits:
			val = (Math.pow(2, 16)) - 1;
			break;
		case _24_Bits:
			val = (Math.pow(2, 24)) - 1;
			break;
		case _32_Bits:
			val = (Math.pow(2, 32)) - 1;
			break;
		}
		return val;
	}

	/**
	 * Calculate a point as y * width + x using the width that has been set in
	 * the generator
	 * 
	 * @param x
	 * @param y
	 * @return
	 * 
	 * @since 0.0
	 */
	// public int computePoint(final int x, final int y, final int width) {
	// return (y * width) + x;
	// }

	public void generateEmptyFrames(final int framesToDo) {
		this.frames = new ArrayList<Double[][]>();
		for (int i = 0; i < framesToDo; i++) {
			final Double[][] frame = new Double[this.height][this.width];
			// final Map<Integer, Double> frame = new HashMap<Integer,
			// Double>();
			for (int y = 0; y < this.height; y++) {
				for (int x = 0; x < this.width; x++) {
					frame[y][x] = 0.0;
					// frame.put(this.computePoint(x, y, this.width), 0.0);
				}
			}
			this.frames.add(frame);
		}
	}

	// BACKGROUND LEVEL
	/**
	 * Generate the frames with the given background value
	 * 
	 * @param backgroundValue
	 * 
	 * @since 0.1
	 */
	public void setBackgroundValue(final Double backgroundValue) {
		this.backgroundValue = backgroundValue;
	}

	/**
	 * Generate the frames with the given background value
	 * 
	 * @since 0.0
	 */
	public void generateBackgroundLevel() {
		// for (Map<Integer, Double> frame : this.frames) {
		for (final Double[][] frame : this.frames) {
			for (int y = 0; y < this.height; y++) {
				for (int x = 0; x < this.width; x++) {
					// frame.put(this.computePoint(x, y, this.width),
					// this.backgroundValue);
					frame[y][x] = this.backgroundValue;
				}
			}
		}
	}

	// GENERATE PARTICLES
	/**
	 * Generate the given number of particles (centers only) in all the frames.
	 * The particles will have the given signal value and will move in the given
	 * direction at the given speed.
	 * 
	 * @param numOfParticles
	 * @param signalPeakValue
	 * @param movDir
	 * @param movSpeed
	 * 
	 * @since 0.1
	 */
	public void generateParticles() {
		if (this.lastAvailPoints.isEmpty()) {
			this.generateFirstFrameParticles();
			this.generateSubsequentialFrameParticles();
		} else {
			this.generateFirstFrameParticlesFromAvailParticles();
			this.generateSubsequentialFrameParticles();
		}
	}

	/**
	 * Generate particles centers in the first frame
	 * 
	 * @since 0.0
	 */
	private void generateFirstFrameParticles() {
		this.initParticles();
		// Map<Integer, Double> frame = this.frames.get(0);
		final Double[][] firstFrame = this.frames.get(0);
		MyPoint startingParticle;
		switch (this.movDir) {
		case Vertical:
			startingParticle = new MyPoint(10.0, 10.0);
			break;
		default:
			startingParticle = new MyPoint(10.0, 10.0);
			break;
		}

		MyPoint actualParticle = startingParticle;
		final List<MyPoint> frameParticles = new ArrayList<MyPoint>();
		for (int p = 0; p < this.numOfParticles; p++) {
			actualParticle.computeIntegerValues();

			final MyPoint particleCopy = new MyPoint();
			particleCopy.xD = actualParticle.xD.subtract(new BigDecimal(1.50))
			        .add(new BigDecimal(this.ctr));
			particleCopy.yD = actualParticle.yD.subtract(new BigDecimal(1.50))
			        .add(new BigDecimal(this.ctr));
			particleCopy.computeIntegerValues();
			// firstFrame.put(this.computePoint(actualPoint.x, actualPoint.y,
			// this.width), this.signalPeakValue);
			firstFrame[particleCopy.y][particleCopy.x] = this.currentSignalPeakValue;
			frameParticles.add(actualParticle);
			double x, y;
			int step = 10;
			switch (this.movDir) {
			case Vertical:
				step = (this.height - 20) / this.numOfParticles;
				x = actualParticle.xD.doubleValue() + step;
				y = actualParticle.yD.doubleValue();
				break;
			default:
				step = (this.width - 20) / this.numOfParticles;
				x = actualParticle.xD.doubleValue();
				y = actualParticle.yD.doubleValue() + step;
				break;
			}
			final MyPoint newPoint = new MyPoint(x, y);
			actualParticle = newPoint;
		}
		this.addFrameParticles(frameParticles);
		this.setLastAvailPoints(frameParticles);
	}

	private void generateFirstFrameParticlesFromAvailParticles() {
		this.initParticles();
		// Map<Integer, Double> frame = this.frames.get(0);
		final Double[][] firstFrame = this.frames.get(0);
		final List<MyPoint> frameParticles = new ArrayList<MyPoint>();
		for (final MyPoint particle : this.lastAvailPoints) {
			double x, y;
			switch (this.movDir) {
			case Vertical:
				x = particle.xD.doubleValue();
				y = particle.yD.doubleValue() + this.movSpeed;
				break;
			default:
				x = particle.xD.doubleValue() + this.movSpeed;
				y = particle.yD.doubleValue();
				break;
			}

			final MyPoint newParticle = new MyPoint(x, y);

			final MyPoint particleCopy = new MyPoint();
			particleCopy.xD = newParticle.xD.subtract(new BigDecimal(1.50))
			        .add(new BigDecimal(this.ctr));
			particleCopy.yD = newParticle.yD.subtract(new BigDecimal(1.50))
			        .add(new BigDecimal(this.ctr));
			particleCopy.computeIntegerValues();
			// frame.put(this.computePoint(newParticle.x, newParticle.y,
			// this.width), this.signalPeakValue);
			firstFrame[particleCopy.y][particleCopy.x] = this.currentSignalPeakValue;
			frameParticles.add(newParticle);
		}
		this.addFrameParticles(frameParticles);
		this.setLastAvailPoints(frameParticles);
	}

	/**
	 * Generate particles centers in all the frames except for the first
	 * 
	 * @since 0.0
	 */
	private void generateSubsequentialFrameParticles() {
		for (int frameIndex = 1; frameIndex < this.frames.size(); frameIndex++) {
			// Map<Integer, Double> frame = this.frames.get(frameIndex);
			final Double[][] frame = this.frames.get(frameIndex);
			final List<MyPoint> frameParticles = new ArrayList<MyPoint>();
			for (final MyPoint particle : this.particles.get(frameIndex - 1)) {
				double x, y;
				switch (this.movDir) {
				case Vertical:
					x = particle.xD.doubleValue();
					y = particle.yD.doubleValue() + this.movSpeed;
					break;
				default:
					x = particle.xD.doubleValue() + this.movSpeed;
					y = particle.yD.doubleValue();
					break;
				}
				final MyPoint newParticle = new MyPoint(x, y);

				final MyPoint particleCopy = new MyPoint();
				particleCopy.xD = newParticle.xD.subtract(new BigDecimal(1.50))
				        .add(new BigDecimal(this.ctr));
				particleCopy.yD = newParticle.yD.subtract(new BigDecimal(1.50))
				        .add(new BigDecimal(this.ctr));
				particleCopy.computeIntegerValues();
				// frame.put(this.computePoint(newParticle.x, newParticle.y,
				// this.width), this.signalPeakValue);
				frame[particleCopy.y][particleCopy.x] = this.currentSignalPeakValue;
				frameParticles.add(newParticle);
			}
			this.addFrameParticles(frameParticles);
			this.setLastAvailPoints(frameParticles);
		}
	}

	/**
	 * Generate a gaussian blob with the parameters that are in the generator
	 * 
	 * @since 0.2
	 */
	public void generateGaussianBlobs_V1() {
		for (int frameIndex = 0; frameIndex < this.frames.size(); frameIndex++) {
			// Map<Integer, Double> frame = this.frames.get(frameIndex)
			final Double[][] frame = this.frames.get(frameIndex);
			for (final MyPoint particleCenter : this.particles.get(frameIndex)) {
				// Double centerValue = frame.get(this.computePoint(
				// particleCenter.x, particleCenter.y, this.width));
				for (int x = -this.radius; x <= this.radius; x++) {
					for (int y = -this.radius; y <= this.radius; y++) {
						final Double newValue = this
						        .calculateGaussianValue(
						                (this.currentSignalPeakValue - this.backgroundValue),
						                particleCenter.x, particleCenter.y,
						                particleCenter.x + x, particleCenter.y
						                        + y, this.sigmaValue);
						// frame.put(this.computePoint(particleCenter.x + x,
						// particleCenter.y + y, this.width),
						// this.backgroundValue + newValue);
						frame[particleCenter.y + y][particleCenter.x + x] = this.backgroundValue
						        + newValue;
					}
				}
			}
		}
	}

	// GENERATE GAUSSIAN BLOBS
	/**
	 * Generate a gaussian blob of the given radius and using the given sigma
	 * value for each particle of each frame
	 * 
	 * @param radius
	 * @param sigmaValue
	 * 
	 * @since 0.1
	 */
	public void generateGaussianBlobs_V2() {
		// System.out.println("X");
		// this.xVal = new HashMap<Integer, Integer>();
		this.xVal = new Integer[this.w][this.w];
		for (int y = 0; y < this.w; y++) {
			for (int x = 0; x < this.w; x++) {
				// this.xVal.put(this.computePoint(x, y, this.w), 1 + y);
				this.xVal[y][x] = 1 + y;
				// System.out.print(1 + y + "\t");
			}
			// System.out.println();
		}
		// System.out.println("Y");
		// this.yVal = new HashMap<Integer, Integer>();
		this.yVal = new Integer[this.w][this.w];
		for (int y = 0; y < this.w; y++) {
			for (int x = 0; x < this.w; x++) {
				// this.yVal.put(this.computePoint(x, y, this.w), 1 + x);
				this.yVal[y][x] = 1 + x;
				// System.out.print(1 + x + "\t");
			}
			// System.out.println();
		}
		final Double[][] pprot = new Double[this.w][this.w];
		// final Map<Integer, Double> pprot = new HashMap<Integer, Double>();
		for (int frameIndex = 0; frameIndex < this.frames.size(); frameIndex++) {
			// final Map<Integer, Double> frame = this.frames.get(frameIndex);
			final Double[][] frame = this.frames.get(frameIndex);
			// int counter = 0;
			for (final MyPoint particle : this.particles.get(frameIndex)) {
				// if ((frameIndex == 50) && (counter == 0)) {
				// System.out.println("Frame: " + frameIndex + " PARTICLE- X:"
				// + particle.x + " Y: " + particle.y + "\tX: "
				// + particle.xD + " Y: " + particle.yD);
				// }

				// final Double centerValue = frame.get(this.computePoint(
				// particle.x, particle.y, this.width));
				Double protMax = 0.0;
				// Build pprot values modifier
				for (int y = 0; y < this.w; y++) {
					for (int x = 0; x < this.w; x++) {
						// final int point = this.computePoint(x, y, this.w);
						final Integer orgX = this.xVal[y][x];
						final Double valX = (((orgX - this.ctr) - particle.xD
						        .doubleValue()) + particle.x) - 0.5;
						final Double valX2 = Math.pow(valX, 2);
						final Integer orgY = this.yVal[y][x];
						final Double valY = (((orgY - this.ctr) - particle.yD
						        .doubleValue()) + particle.y) - 0.5;
						final Double valY2 = Math.pow(valY, 2);
						final Double val = valX2 + valY2;
						final Double expVal = val
						        / (4.0 * Math.pow(this.sigmaValue, 2));
						final Double exp = Math.exp(-expVal);
						pprot[y][x] = exp;
						// pprot.put(point, exp);
						if (exp > protMax) {
							protMax = exp;
						}
					}
				}

				// Normalize pprot values between 0 and 1
				// final DecimalFormat df = new DecimalFormat("0.0000");
				for (int y = 0; y < this.w; y++) {
					for (int x = 0; x < this.w; x++) {
						// this.computePoint(x, y, this.w);
						Double val = pprot[y][x];
						// Double val = pprot.get(point);
						val /= protMax;
						// pprot.put(point, val);
						pprot[y][x] = val;
						// if ((frameIndex == 50) && (counter == 0)) {
						// System.out.print(df.format(val) + "\t");
						// }
					}
					// if ((frameIndex == 50) && (counter == 0)) {
					// System.out.println();
					// }
				}

				// if ((frameIndex == 50) && (counter == 0)) {
				// System.out.println();
				// }

				// final DecimalFormat df = new DecimalFormat("0.0000");

				// TODO Have to understand why this -1 is needed...
				// Because this changes basically the center of particle
				final int xIndex = particle.x - 1;
				final int yIndex = particle.y - 1;
				// if (((frameIndex == 0) && (counter == 0))) {
				// System.out.println("Index x: " + xIndex + " index y: "
				// + yIndex);
				// }
				for (int y = 0; y < this.w; y++) {
					for (int x = 0; x < this.w; x++) {
						// this.computePoint(y, x, this.w);
						// this.computePoint(xIndex + x, yIndex + y,
						// this.width);
						// final Double newValue = (centerValue -
						// this.backgroundValue)
						// * pprot.get(coeffIndex);
						final Double newValue = (this.currentSignalPeakValue - this.backgroundValue)
						        * pprot[x][y];
						final double val = this.backgroundValue + newValue;

						// if (((frameIndex == 50) && (counter == 0))) {
						// System.out.print(df.format(val) + "\t");
						// }
						// frame.put(imageIndex, val);
						frame[yIndex + y][xIndex + x] = val;
					}
					// if (((frameIndex == 50) && (counter == 0))) {
					// System.out.println();
					// }
				}
				// if (((frameIndex == 50) && (counter == 0))) {
				// System.out.println();
				// }
				// counter++;
			}
		}
	}

	/**
	 * Compute the Gaussian blob new value of the point newX, newY
	 * 
	 * @param centerValue
	 * @param centerX
	 * @param centerY
	 * @param newX
	 * @param newY
	 * @param sigmaValue
	 * @return
	 * 
	 * @since 0.0
	 */
	private Double calculateGaussianValue(final Double centerValue,
	        final int centerX, final int centerY, final int newX,
	        final int newY, final double sigmaValue) {
		final Double valX = Math.pow((centerX - newX), 2);
		final Double valY = Math.pow((centerY - newY), 2);
		final Double val = valX + valY;
		final Double expVal = val / (4.0 * Math.pow(sigmaValue, 2));
		final Double exp = Math.exp(-expVal);
		final Double newValue = centerValue * exp;
		return newValue;
	}

	// GENERATE POISSON DISTRIBUTION
	/**
	 * Generate new values for the each frame using a poisson distribution. This
	 * method is using a CERN library to do the poisson.
	 * 
	 * @since 0.0
	 */
	public void generatePoissonDistribution() {
		final RandomEngine random = new MersenneTwister(Calendar.getInstance()
		        .getTime());
		for (final Double[][] frame : this.frames) {
			// for (final Map<Integer, Double> frame : this.frames) {
			for (int y = 0; y < this.height; y++) {
				for (int x = 0; x < this.width; x++) {
					// final int point = this.computePoint(x, y, this.width);
					final Double oldValue = frame[y][x];
					// final Double oldValue = frame.get(point).doubleValue();
					final Poisson distr = new Poisson(oldValue, random);
					Double newValue = distr.nextDouble();
					if (newValue < 0) {
						newValue = 0.0;
					} else if (newValue > this.actualMaxValue) {
						newValue = this.actualMaxValue;
					}
					// frame.put(point, newValue);
					frame[y][x] = newValue;
				}
			}
		}
	}

	public StringBuffer generateMainFolderName() {
		final StringBuffer folderName = new StringBuffer();
		folderName.append(this.folder);
		folderName.append(File.separatorChar);
		if (this instanceof ArtificialImageGeneratorSpecial) {
			folderName.append(GenerationType.Special.toString());
		} else {
			folderName.append(GenerationType.Standard.toString());
		}
		folderName.append("_");
		folderName.append(this.numOfFrames);
		folderName.append("_");
		folderName.append(this.currentSignalPeakValue);
		folderName.append("_");
		folderName.append(this.backgroundValue);
		folderName.append(this.bits.toString());
		folderName.append("_");
		folderName.append(this.colors.toString());
		folderName.append("_");
		folderName.append(this.numOfParticles);
		folderName.append("_");
		if (this instanceof ArtificialImageGeneratorSpecial) {

		} else {
			folderName.append(this.movDir.toString());
			folderName.append("_");
		}
		folderName.append(this.movSpeed);
		folderName.append("_");
		folderName.append(this.imageName);
		return folderName;
	}

	public void generateFolders() {
		this.folders = new ArrayList<File>();
		final StringBuffer folderName = this.generateMainFolderName();
		for (Integer i = 0; i < this.numOfDatasets; i++) {
			final StringBuffer tmp = new StringBuffer();
			tmp.append(folderName);
			tmp.append(File.separatorChar);
			final int len = this.numOfDatasets.toString().length();
			tmp.append(this.generatePostfix(len, i));
			final File f = new File(tmp.toString());
			this.folders.add(f);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
	}

	// GENERATE FILES
	/**
	 * Generate the frames files
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @since 0.0
	 */
	public void generateFramesFiles(final Integer folderIndex)
	        throws FileNotFoundException, IOException {
		for (Integer i = 0; i < this.frames.size(); i++) {
			final Double[][] frame = this.frames.get(i);
			// final Map<Integer, Double> frame = this.frames.get(i);
			final int index = this.lastAvailIndex + i + 1;
			final String postfix = this.generatePostfix(
			        this.imagePostfixDigits, index).toString();
			final String fileName = this.imageName + "_" + postfix;
			final File f = new File(this.folders.get(folderIndex).getPath()
			        + File.separatorChar + fileName + ".tif");
			final BufferedImage bi = this.generateBufferedImage();
			final OutputStream os = new FileOutputStream(f);

			final TIFFEncodeParam encodeParam = new TIFFEncodeParam();
			encodeParam.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);

			final WritableRaster raster = (WritableRaster) bi.getData();
			for (int y = 0; y < this.height; y++) {
				for (int x = 0; x < this.width; x++) {
					final double[] data = this.generatePixel(x, y, frame);
					raster.setPixel(x, y, data);
				}
			}
			bi.setData(raster);

			final TIFFImageEncoder encoder = new TIFFImageEncoder(os,
			        encodeParam);
			encoder.encode(bi);
			os.close();
		}

	}

	public void generateLogsFiles(final Integer folderIndex)
	        throws FileNotFoundException, IOException {
		final File logDir = new File(this.folders.get(folderIndex).getPath()
		        + "/logs");
		if (!logDir.exists()) {
			logDir.mkdir();
		}
		for (Integer i = 0; i < this.frames.size(); i++) {
			// final Map<Integer, Double> frame = this.frames.get(i);
			final int index = this.lastAvailIndex + i + 1;
			final String postfix = this.generatePostfix(
			        this.imagePostfixDigits, index).toString();
			final String fileName = this.imageName + "_" + postfix;
			final File f = new File(this.folders.get(folderIndex).getPath()
			        + File.separatorChar + "logs" + File.separatorChar
			        + "PT_Log_" + fileName + ".txt");
			if (!f.exists()) {
				f.createNewFile();
			}

			final FileWriter fw = new FileWriter(f);
			final BufferedWriter bw = new BufferedWriter(fw);

			final StringBuffer sb = new StringBuffer("Particles of frame: "
			        + index + "\n");
			sb.append("Format:\t\tx double\ty double\tx\ty\n");
			final List<MyPoint> frameParticles = this.particles.get(i);
			for (int p = 0; p < frameParticles.size(); p++) {
				final MyPoint particle = frameParticles.get(p);
				final MyPoint particleCopy = new MyPoint();
				particleCopy.xD = particle.xD.subtract(new BigDecimal(1.5))
				        .add(new BigDecimal(this.ctr));
				particleCopy.yD = particle.yD.subtract(new BigDecimal(1.5))
				        .add(new BigDecimal(this.ctr));
				particleCopy.computeIntegerValues();
				sb.append("Particle ");
				sb.append(p);
				sb.append("\t");
				sb.append(particleCopy.xD.toPlainString());
				sb.append("\t\t");
				sb.append(particleCopy.yD.toPlainString());
				sb.append("\t\t");
				sb.append(particleCopy.x);
				sb.append("\t");
				sb.append(particleCopy.y);
				sb.append("\n");
			}

			bw.write(sb.toString());

			bw.close();
			fw.close();
		}
	}

	public void generateResultsFile() throws IOException {
		final StringBuffer folderName = this.generateMainFolderName();
		final File f = new File(folderName.toString() + File.separatorChar
		        + "resultsFile.txt");
		if (!f.exists()) {
			f.createNewFile();
		}
		final FileWriter fw = new FileWriter(f);
		final BufferedWriter bw = new BufferedWriter(fw);
		bw.write("Gen type:\t\t\t\t");
		if (this instanceof ArtificialImageGeneratorSpecial) {
			bw.write(GenerationType.Special.toString());
		} else {
			bw.write(GenerationType.Standard.toString());
		}
		bw.write("\n");
		bw.write("Number of datasets:\t\t" + this.numOfDatasets + "\n");
		bw.write("Image names:\t\t\t" + this.imageName + "\n");
		bw.write("Number of frames:\t\t" + this.numOfFrames + "\n");
		bw.write("Height:\t\t\t\t\t" + this.height + "\n");
		bw.write("Width:\t\t\t\t\t" + this.width + "\n");
		bw.write("Bits:\t\t\t\t\t" + this.bits + "\n");
		bw.write("Color:\t\t\t\t\t" + this.colors + "\n");
		bw.write("Peak value:\t\t\t\t" + this.currentSignalPeakValue + "\n");
		bw.write("Background value:\t\t" + this.backgroundValue + "\n");
		bw.write("Number of particle:\t\t" + this.numOfParticles + "\n");
		bw.write("Radius:\t\t\t\t\t" + this.radius + "\n");
		bw.write("Sigma value:\t\t\t" + this.sigmaValue + "\n");
		bw.write("CTR:\t\t\t\t\t" + this.ctr + "\n");
		bw.write("W:\t\t\t\t\t\t" + this.w + "\n");
		bw.write("Move direction:\t\t\t" + this.movDir + "\n");
		bw.write("Move speed:\t\t\t\t" + this.movSpeed + "\n");
		bw.write("SNR_C_P:\t\t\t\t" + this.computeSNR_C_P() + "\n");
		bw.write("SNR_B_P:\t\t\t\t" + this.computeSNR_B_P() + "\n");
		bw.write("SNR_B_G:\t\t\t\t" + this.computeSNR_B_G() + "\n");
		bw.close();
		fw.close();
	}

	/**
	 * Generate the real pixel values for the given point, handling the
	 * different cases of color and bit depth
	 * 
	 * @param x
	 * @param y
	 * @param frame
	 * @return
	 * 
	 * @since 0.0
	 */
	// private double[] generatePixel(final int x, final int y,
	// final Map<Integer, Double> frame) {
	private double[] generatePixel(final int x, final int y,
	        final Double[][] frame) {
		double[] data;
		switch (this.colors) {
		case Grey:
			data = new double[1];
			break;
		default:
			switch (this.bits) {
			case _32_Bits:
				data = new double[4];
				break;
			default:
				data = new double[3];
				break;
			}
			break;
		}

		switch (this.colors) {
		case Grey:
			// data[0] = frame.get(this.computePoint(x, y, this.width))
			// .doubleValue();
			data[0] = frame[y][x];
			return data;
		case Red:
			// data[0] = frame.get(this.computePoint(x, y, this.width))
			// .doubleValue();
			data[0] = frame[y][x];
			data[1] = 0;
			data[2] = 0;
			return data;
		case Green:
			data[0] = 0;
			// data[1] = frame.get(this.computePoint(x, y, this.width))
			// .doubleValue();
			data[1] = frame[y][x];
			data[2] = 0;
			return data;
		case Blue:
			data[0] = 0;
			data[1] = 0;
			// data[2] = frame.get(this.computePoint(x, y, this.width))
			// .doubleValue();
			data[2] = frame[y][x];
			return data;
		default:
			switch (this.bits) {
			case _32_Bits:
				// data[0] = frame.get(this.computePoint(x, y, this.width))
				// .doubleValue();
				// data[1] = frame.get(this.computePoint(x, y, this.width))
				// .doubleValue();
				// data[2] = frame.get(this.computePoint(x, y, this.width))
				// .doubleValue();
				data[0] = frame[y][x];
				data[1] = frame[y][x];
				data[2] = frame[y][x];
				data[3] = 0;
				return data;
			default:
				// data[0] = frame.get(this.computePoint(x, y, this.width))
				// .doubleValue();
				// data[1] = frame.get(this.computePoint(x, y, this.width))
				// .doubleValue();
				// data[2] = frame.get(this.computePoint(x, y, this.width))
				// .doubleValue();
				data[0] = frame[y][x];
				data[1] = frame[y][x];
				data[2] = frame[y][x];
				return data;
			}
		}
	}

	/**
	 * Generate a buffered image with the chosen color and bit depth (some cases
	 * are still missing: grey 24/32 and color 4/8/16, the grey 4 bits results
	 * as a 1 bit depth because is binary
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private BufferedImage generateBufferedImage() {
		// TODO add missing cases and fix the 4 bits grey
		switch (this.colors) {
		case Grey:
			switch (this.bits) {
			case _4_Bits:
				return new BufferedImage(this.width, this.height,
				        BufferedImage.TYPE_BYTE_BINARY);
			case _8_Bits:
				return new BufferedImage(this.width, this.height,
				        BufferedImage.TYPE_BYTE_GRAY);
			case _16_Bits:
				return new BufferedImage(this.width, this.height,
				        BufferedImage.TYPE_USHORT_GRAY);
				// case _24_Bits:
				// bi = new BufferedImage(this.width, this.height);
				// break;
				// case _32_Bits:
				// bi = new BufferedImage(this.width, this.height);
				// break;
			default:
				return new BufferedImage(this.width, this.height,
				        BufferedImage.TYPE_USHORT_GRAY);
			}
		default:
			switch (this.bits) {
			// case _4_Bits:
			// bi = new BufferedImage(this.width, this.height, BufferedImage.T);
			// break;
			// case _8_Bits:
			// bi = new BufferedImage(this.width, this.height);
			// break;
			// case _16_Bits:
			// bi = new BufferedImage(this.width, this.height);
			// break;
			case _24_Bits:
				return new BufferedImage(this.width, this.height,
				        BufferedImage.TYPE_INT_RGB);
			case _32_Bits:
				return new BufferedImage(this.width, this.height,
				        BufferedImage.TYPE_INT_ARGB);
			default:
				return new BufferedImage(this.width, this.height,
				        BufferedImage.TYPE_INT_RGB);
			}
		}
	}

	/**
	 * Create the postfix of each frame name based on the number of digit the
	 * user wants to have
	 * 
	 * @param index
	 * @return
	 * 
	 * @since 0.1
	 */
	private StringBuffer generatePostfix(final Integer numberOfDigits,
	        final Integer index) {
		final StringBuffer postfix = new StringBuffer();
		int maxValue = 1;
		for (int k = 0; k < numberOfDigits; k++) {
			postfix.append("0");
			maxValue *= 10;
		}
		int counter = 0;
		for (int k = 1; k < maxValue; k *= 10) {
			final int i = (int) Math.floor((index / 10));
			if (i < k) {
				break;
			}
			counter++;
		}

		final int start = numberOfDigits - counter - 1;
		final int end = numberOfDigits;
		postfix.replace(start, end, index.toString());
		return postfix;
	}

	/**
	 * Compute the POISSON SNR of the frame sequence using the Chezuum formula:
	 * (peakSignal - background) / sqrt(peakSignal)
	 * 
	 * @return
	 * 
	 * @since 0.2
	 */
	public Double computeSNR_C_P() {
		final Double val1 = (double) (this.currentSignalPeakValue - this.backgroundValue);
		final Double val2 = Math.sqrt(this.currentSignalPeakValue);
		return val1 / val2;
	}

	/**
	 * Compute the POISSON SNR of the frame sequence using the Bhattacharyya
	 * formula: (peakSignal - background) / sqrt(peakSignal)
	 * 
	 * @return
	 * 
	 * @since 0.4
	 */
	public Double computeSNR_B_P() {
		final Double val1 = Math.sqrt(this.currentSignalPeakValue);
		final Double val2 = Math.sqrt(this.backgroundValue);
		return 2 * (val1 - val2);
	}

	/**
	 * Compute the GAUSSIAN SNR of the frame sequence using the Bhattacharyya
	 * formula: 2* (peakSignal - background) / (sqrt(peakSignal) -
	 * sqrt(background))
	 * 
	 * @return
	 * 
	 * @since 0.4
	 */
	public Double computeSNR_B_G() {
		final Double val1 = Math.sqrt(this.currentSignalPeakValue);
		final Double val2 = Math.sqrt(this.backgroundValue);
		return (2 * (this.currentSignalPeakValue - this.backgroundValue))
		        / (val1 + val2);
	}
}
