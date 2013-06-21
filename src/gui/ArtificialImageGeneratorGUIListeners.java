package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import core.ArtificialImageGenerator;
import enums.Bit;
import enums.Color;
import enums.MovementDirection;

/**
 * Listeners container for the AIGGUI
 * 
 * @author Alex Rigano
 * @version 0.4, Stable
 * @since 0.0 of AIGGUI
 */
public class ArtificialImageGeneratorGUIListeners {

	public static void addWorkingDirChooser(
	        final ArtificialImageGeneratorGUI mainGUI) {
		mainGUI.getDirChooserButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent evt) {
				final JFileChooser fc = mainGUI.getDirChooserDialog();
				fc.showOpenDialog(fc);
				if (fc.getSelectedFile() != null) {
					mainGUI.setNewCurrentDirLbl(fc.getSelectedFile().getPath());
				} else {
					mainGUI.setNewCurrentDirLbl(fc.getCurrentDirectory()
					        .getPath());
				}
			}
		});
	}

	/**
	 * Add an action performed listener on the background button. On each click
	 * it will invoke the
	 * {@link ArtificialImageGenerator#generateBackgroundLevel(Double)} and the
	 * {@link ArtificialImageGenerator#generateFiles()} methods
	 * 
	 * @param mainGUI
	 */
	public static void addGenerateBackgroundButtListener(
	        final ArtificialImageGeneratorGUI mainGUI) {
		mainGUI.getGenerateBackgroundButt().addActionListener(
		        new ActionListener() {

			        @Override
			        public void actionPerformed(final ActionEvent evt) {
				        final File directory = mainGUI.getWorkingDirectory();
				        final Integer numOfDatasets = mainGUI
				                .getNumOfDatasets();
				        final String imageName = mainGUI.getImageName();
				        final int imagePostfixDigits = mainGUI
				                .getImagePostfixDigits();
				        final int numOfFrames = mainGUI.getNumOfFrames();
				        final int height = mainGUI.getHeight();
				        final int width = mainGUI.getWidth();
				        final Bit bits = mainGUI.getBits();
				        final Color colors = mainGUI.getColors();
				        final ArtificialImageGenerator aig = new ArtificialImageGenerator(
				                directory, numOfDatasets, imageName,
				                imagePostfixDigits, numOfFrames, height, width,
				                bits, colors);

				        final Double backgroundValue = mainGUI
				                .getBackgroundValue();
				        aig.setBackgroundValue(backgroundValue);

				        final List<Double> signalPeakValues = mainGUI
				                .getSignalPeakValue();

				        final int radius = mainGUI.getRadius();
				        aig.setRadius(radius);
				        final Integer sigmaValue = mainGUI.getSigmaValue();
				        aig.setSigmaValue(sigmaValue);
				        aig.computeCTRAndW();

				        for (final Double d : signalPeakValues) {
					        aig.setSignalPeakValue(d);
					        aig.generateFolders();

					        for (int i = 0; i < numOfDatasets; i++) {
						        aig.generateBackgroundLevel();

						        try {
							        aig.generateFiles(i);
						        } catch (final FileNotFoundException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        } catch (final IOException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        }
					        }
					        try {
						        aig.generateResultsFile();
					        } catch (final IOException e) {
						        // TODO Auto-generated catch block
						        e.printStackTrace();
					        }
				        }

				        final Double SNR_C_P = aig.computeSNR_C_P();
				        mainGUI.appendResultsText("SNR_C_P value: " + SNR_C_P);
				        final Double SNR_B_P = aig.computeSNR_B_P();
				        mainGUI.appendResultsText("SNR_B_P value: " + SNR_B_P);
				        final Double SNR_B_G = aig.computeSNR_B_G();
				        mainGUI.appendResultsText("SNR_B_G value: " + SNR_B_G);
				        mainGUI.appendResultsText("###############");
			        }
		        });
	}

	/**
	 * Add an action performed listener on the particles button. On each click
	 * it will invoke the
	 * {@link ArtificialImageGenerator#generateBackgroundLevel(Double)}, the
	 * {@link ArtificialImageGenerator#generateParticles(int, Double, MovementDirection, Double)}
	 * and the {@link ArtificialImageGenerator#generateFiles()} methods
	 * 
	 * @param mainGUI
	 */
	public static void addGenerateParticlesButtListener(
	        final ArtificialImageGeneratorGUI mainGUI) {
		mainGUI.getGenerateParticlesButt().addActionListener(
		        new ActionListener() {

			        @Override
			        public void actionPerformed(final ActionEvent evt) {
				        final File directory = mainGUI.getWorkingDirectory();
				        final Integer numOfDatasets = mainGUI
				                .getNumOfDatasets();
				        final String imageName = mainGUI.getImageName();
				        final int imagePostfixDigits = mainGUI
				                .getImagePostfixDigits();
				        final int numOfFrames = mainGUI.getNumOfFrames();
				        final int height = mainGUI.getHeight();
				        final int width = mainGUI.getWidth();
				        final Bit bits = mainGUI.getBits();
				        final Color colors = mainGUI.getColors();
				        final ArtificialImageGenerator aig = new ArtificialImageGenerator(
				                directory, numOfDatasets, imageName,
				                imagePostfixDigits, numOfFrames, height, width,
				                bits, colors);

				        final Double backgroundValue = mainGUI
				                .getBackgroundValue();
				        aig.setBackgroundValue(backgroundValue);
				        final int numOfParticles = mainGUI.getNumOfParticles();
				        aig.setNumOfParticle(numOfParticles);

				        final MovementDirection movDir = mainGUI
				                .getMovementDirection();
				        aig.setMovDir(movDir);
				        final Double movSpeed = mainGUI.getMovementSpeed();
				        aig.setMovSpeed(movSpeed);

				        final List<Double> signalPeakValues = mainGUI
				                .getSignalPeakValue();

				        final int radius = mainGUI.getRadius();
				        aig.setRadius(radius);
				        final Integer sigmaValue = mainGUI.getSigmaValue();
				        aig.setSigmaValue(sigmaValue);
				        aig.computeCTRAndW();

				        for (final Double d : signalPeakValues) {
					        aig.setSignalPeakValue(d);
					        aig.generateFolders();
					        for (int i = 0; i < numOfDatasets; i++) {
						        aig.generateEmptyFrames();
						        aig.generateBackgroundLevel();
						        aig.generateParticles();
						        try {
							        aig.generateFiles(i);
						        } catch (final FileNotFoundException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        } catch (final IOException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        }
					        }
					        try {
						        aig.generateResultsFile();
					        } catch (final IOException e) {
						        // TODO Auto-generated catch block
						        e.printStackTrace();
					        }

					        final Double SNR_C_P = aig.computeSNR_C_P();
					        mainGUI.appendResultsText("SNR_C_P value: "
					                + SNR_C_P);
					        final Double SNR_B_P = aig.computeSNR_B_P();
					        mainGUI.appendResultsText("SNR_B_P value: "
					                + SNR_B_P);
					        final Double SNR_B_G = aig.computeSNR_B_G();
					        mainGUI.appendResultsText("SNR_B_G value: "
					                + SNR_B_G);
					        mainGUI.appendResultsText("###############");
				        }
			        }
		        });
	}

	/**
	 * Add an action performed listener on the particles button. On each click
	 * it will invoke the
	 * {@link ArtificialImageGenerator#generateBackgroundLevel(Double)}, the
	 * {@link ArtificialImageGenerator#generateParticles(int, Double, MovementDirection, Double)}
	 * , the
	 * {@link ArtificialImageGenerator#generateGaussianBlobs(int, Integer)} and
	 * the {@link ArtificialImageGenerator#generateFiles()} methods
	 * 
	 * @param mainGUI
	 */
	public static void addGenerateGaussianBlobButtListener(
	        final ArtificialImageGeneratorGUI mainGUI) {
		mainGUI.getGenerateGaussianBlobButt().addActionListener(
		        new ActionListener() {

			        @Override
			        public void actionPerformed(final ActionEvent evt) {
				        final File directory = mainGUI.getWorkingDirectory();
				        final Integer numOfDatasets = mainGUI
				                .getNumOfDatasets();
				        final String imageName = mainGUI.getImageName();
				        final int imagePostfixDigits = mainGUI
				                .getImagePostfixDigits();
				        final int numOfFrames = mainGUI.getNumOfFrames();
				        final int height = mainGUI.getHeight();
				        final int width = mainGUI.getWidth();
				        final Bit bits = mainGUI.getBits();
				        final Color colors = mainGUI.getColors();
				        final ArtificialImageGenerator aig = new ArtificialImageGenerator(
				                directory, numOfDatasets, imageName,
				                imagePostfixDigits, numOfFrames, height, width,
				                bits, colors);

				        final Double backgroundValue = mainGUI
				                .getBackgroundValue();
				        aig.setBackgroundValue(backgroundValue);
				        final int numOfParticles = mainGUI.getNumOfParticles();
				        aig.setNumOfParticle(numOfParticles);
				        final MovementDirection movDir = mainGUI
				                .getMovementDirection();
				        aig.setMovDir(movDir);
				        final Double movSpeed = mainGUI.getMovementSpeed();
				        aig.setMovSpeed(movSpeed);
				        final int radius = mainGUI.getRadius();
				        aig.setRadius(radius);
				        final Integer sigmaValue = mainGUI.getSigmaValue();
				        aig.setSigmaValue(sigmaValue);
				        aig.computeCTRAndW();

				        final List<Double> signalPeakValues = mainGUI
				                .getSignalPeakValue();

				        for (final Double d : signalPeakValues) {
					        aig.setSignalPeakValue(d);

					        aig.generateFolders();
					        for (int i = 0; i < numOfDatasets; i++) {
						        aig.generateEmptyFrames();
						        aig.generateBackgroundLevel();
						        aig.generateParticles();
						        aig.generateGaussianBlobs_V2();
						        try {
							        aig.generateFiles(i);
						        } catch (final FileNotFoundException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        } catch (final IOException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        }
					        }
					        try {
						        aig.generateResultsFile();
					        } catch (final IOException e) {
						        // TODO Auto-generated catch block
						        e.printStackTrace();
					        }

					        final Double SNR_C_P = aig.computeSNR_C_P();
					        mainGUI.appendResultsText("SNR_C_P value: "
					                + SNR_C_P);
					        final Double SNR_B_P = aig.computeSNR_B_P();
					        mainGUI.appendResultsText("SNR_B_P value: "
					                + SNR_B_P);
					        final Double SNR_B_G = aig.computeSNR_B_G();
					        mainGUI.appendResultsText("SNR_B_G value: "
					                + SNR_B_G);
					        mainGUI.appendResultsText("###############");
				        }
			        }
		        });
	}

	/**
	 * Add an action performed listener on the particles button. On each click
	 * it will invoke the
	 * {@link ArtificialImageGenerator#generateBackgroundLevel(Double)}, the
	 * {@link ArtificialImageGenerator#generateParticles(int, Double, MovementDirection, Double)}
	 * , the
	 * {@link ArtificialImageGenerator#generateGaussianBlobs(int, Integer)}, the
	 * {@link ArtificialImageGenerator#generatePoissonDistribution()} and the
	 * {@link ArtificialImageGenerator#generateFiles()} methods
	 * 
	 * @param mainGUI
	 */
	public static void addGeneratePoissonDistrButtListener(
	        final ArtificialImageGeneratorGUI mainGUI) {
		mainGUI.getGeneratePoissonDistrButt().addActionListener(
		        new ActionListener() {

			        @Override
			        public void actionPerformed(final ActionEvent evt) {
				        final File directory = mainGUI.getWorkingDirectory();
				        final Integer numOfDatasets = mainGUI
				                .getNumOfDatasets();
				        final String imageName = mainGUI.getImageName();
				        final int imagePostfixDigits = mainGUI
				                .getImagePostfixDigits();
				        final int numOfFrames = mainGUI.getNumOfFrames();
				        final int height = mainGUI.getHeight();
				        final int width = mainGUI.getWidth();
				        final Bit bits = mainGUI.getBits();
				        final Color colors = mainGUI.getColors();
				        final ArtificialImageGenerator aig = new ArtificialImageGenerator(
				                directory, numOfDatasets, imageName,
				                imagePostfixDigits, numOfFrames, height, width,
				                bits, colors);

				        final Double backgroundValue = mainGUI
				                .getBackgroundValue();
				        aig.setBackgroundValue(backgroundValue);
				        final int numOfParticles = mainGUI.getNumOfParticles();
				        aig.setNumOfParticle(numOfParticles);
				        final MovementDirection movDir = mainGUI
				                .getMovementDirection();
				        aig.setMovDir(movDir);
				        final Double movSpeed = mainGUI.getMovementSpeed();
				        aig.setMovSpeed(movSpeed);
				        final int radius = mainGUI.getRadius();
				        aig.setRadius(radius);
				        final Integer sigmaValue = mainGUI.getSigmaValue();
				        aig.setSigmaValue(sigmaValue);
				        aig.computeCTRAndW();

				        final List<Double> signalPeakValues = mainGUI
				                .getSignalPeakValue();

				        for (final Double d : signalPeakValues) {

					        aig.setSignalPeakValue(d);

					        aig.generateFolders();
					        for (int i = 0; i < numOfDatasets; i++) {
						        aig.generateEmptyFrames();
						        aig.generateBackgroundLevel();
						        aig.generateParticles();
						        aig.generateGaussianBlobs_V2();
						        aig.generatePoissonDistribution();
						        try {
							        aig.generateFiles(i);
						        } catch (final FileNotFoundException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        } catch (final IOException e) {
							        // TODO Auto-generated catch block
							        e.printStackTrace();
						        }
					        }
					        try {
						        aig.generateResultsFile();
					        } catch (final IOException e) {
						        // TODO Auto-generated catch block
						        e.printStackTrace();
					        }

					        final Double SNR_C_P = aig.computeSNR_C_P();
					        mainGUI.appendResultsText("SNR_C_P value: "
					                + SNR_C_P);
					        final Double SNR_B_P = aig.computeSNR_B_P();
					        mainGUI.appendResultsText("SNR_B_P value: "
					                + SNR_B_P);
					        final Double SNR_B_G = aig.computeSNR_B_G();
					        mainGUI.appendResultsText("SNR_B_G value: "
					                + SNR_B_G);
					        mainGUI.appendResultsText("###############");
				        }
			        }
		        });
	}
}