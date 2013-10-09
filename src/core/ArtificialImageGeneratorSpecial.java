package core;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import enums.Bit;
import enums.Color;

/**
 * Image generator engine
 * 
 * @author Alex Rigano
 * @version 0.4, Beta
 */
public class ArtificialImageGeneratorSpecial extends
        ArtificialImageGeneratorStandard {

	public ArtificialImageGeneratorSpecial(final File folder,
	        final int numOfDatasets, final String imageName,
	        final int imagePostfixDigits, final int numOfFrames,
	        final int height, final int width, final Bit bits,
	        final Color colors) {
		super(folder, numOfDatasets, imageName, imagePostfixDigits,
		        numOfFrames, height, width, bits, colors);
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

		final BigDecimal startingPosition = new BigDecimal(7.0 - ctr);

		for (int frameIndex = 0; frameIndex < frames.size(); frameIndex++) {
			final List<MyPoint> frameParticles = new ArrayList<MyPoint>();
			for (int p = 0; p < numOfParticles; p++) {
				Random rand = new Random(System.nanoTime());
				final Double randomPartX = rand.nextDouble();
				rand = new Random(System.nanoTime());
				final Double randomPartY = rand.nextDouble();

				final BigDecimal x = startingPosition.add(new BigDecimal(
				        randomPartX));
				final BigDecimal y = startingPosition.add(new BigDecimal(
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
