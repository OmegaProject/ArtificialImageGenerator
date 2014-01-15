package core;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class ArtificialImageGeneratorSpecial extends
        ArtificialImageGeneratorStandard {

	private final Random rand;

	public ArtificialImageGeneratorSpecial(final File folder,
	        final int numOfDatasets, final String imageName,
	        final int imagePostfixDigits, final int numOfFrames,
	        final int height, final int width, final Bit bits,
	        final Color colors, final Double backgroundValue,
	        final int numOfParticles, final int particlesDist,
	        final List<Double> signalPeakValues,
	        final MovementDirection movDirection, final Double movSpeed,
	        final int radius, final int sigmaValue,
	        final boolean hasBackgroundGen, final boolean hasParticleGen,
	        final boolean hasGaussianGen, final boolean hasPoissonGen,
	        final boolean hasParticleLogGen,
	        final boolean hasDoubleValueLogGen,
	        final ArtificialImageGeneratorGUI gui) {
		super(folder, numOfDatasets, imageName, imagePostfixDigits,
		        numOfFrames, height, width, bits, colors, backgroundValue,
		        numOfParticles, particlesDist, signalPeakValues, movDirection,
		        movSpeed, radius, sigmaValue, hasBackgroundGen, hasParticleGen,
		        hasGaussianGen, hasPoissonGen, hasParticleLogGen,
		        hasDoubleValueLogGen, gui);
		this.rand = new Random(System.nanoTime());
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
	@Override
	public void generateParticles() {
		this.generateSpecialParticles();
	}

	private void generateSpecialParticles() {
		final Double signalPeakValue = this.getSignalPeakValue();
		final int ctr = this.getCTR();
		this.initParticles();
		final int numOfParticles = this.getNumOfParticle();
		this.getParticles();
		final List<Double[][]> frames = this.getFrames();

		final BigDecimal halfX = new BigDecimal(this.getHeight()).divide(
		        new BigDecimal(2)).setScale(0, BigDecimal.ROUND_HALF_UP);
		final BigDecimal halfY = new BigDecimal(this.getWidth()).divide(
		        new BigDecimal(2)).setScale(0, BigDecimal.ROUND_HALF_UP);
		final BigDecimal startingPositionX = new BigDecimal(halfX.intValue()
		        - ctr);
		final BigDecimal startingPositionY = new BigDecimal(halfY.intValue()
		        - ctr);

		for (int frameIndex = 0; frameIndex < frames.size(); frameIndex++) {
			final List<MyPoint> frameParticles = new ArrayList<MyPoint>();
			for (int p = 0; p < numOfParticles; p++) {
				// Random rand = new Random(System.nanoTime());
				final Double randomPartX = this.rand.nextDouble();
				// rand = new Random(System.nanoTime());
				final Double randomPartY = this.rand.nextDouble();

				final BigDecimal x = startingPositionX.add(new BigDecimal(
				        randomPartX));
				final BigDecimal y = startingPositionY.add(new BigDecimal(
				        randomPartY));

				final MyPoint newParticle = new MyPoint(x.doubleValue(),
				        y.doubleValue());

				final MyPoint particleCopy = new MyPoint();
				particleCopy.xD = newParticle.xD.subtract(new BigDecimal(1.50))
				        .add(new BigDecimal(this.getCTR()));
				particleCopy.yD = newParticle.yD.subtract(new BigDecimal(1.50))
				        .add(new BigDecimal(this.getCTR()));
				particleCopy.computeIntegerValues();

				// firstFrame.put(this.computePoint(actualPoint.x,
				// actualPoint.y,
				// this.width), this.signalPeakValue);
				frames.get(frameIndex)[particleCopy.y][particleCopy.x] = signalPeakValue;
				frameParticles.add(newParticle);
			}
			this.addFrameParticles(frameParticles);
			this.setLastAvailPoints(frameParticles);
		}
	}
}
