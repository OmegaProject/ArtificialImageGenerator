package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import core.ArtificialImageGeneratorSpecial;
import core.ArtificialImageGeneratorStandard;
import core.GenerationType;
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
	 * {@link ArtificialImageGeneratorStandard#generateBackgroundLevel(Double)}
	 * and the {@link ArtificialImageGeneratorStandard#generateFiles()} methods
	 * 
	 * @param mainGUI
	 */
	public static void addGenerateButtListener(
	        final ArtificialImageGeneratorGUI mainGUI) {
		mainGUI.getGenerateButt().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent evt) {
				final File folder = mainGUI.getWorkingDirectory();
				final Integer numOfDatasets = mainGUI.getNumOfDatasets();
				final String imageName = mainGUI.getImageName();
				final int imagePostfixDigits = mainGUI.getImagePostfixDigits();
				final int numOfFrames = mainGUI.getNumOfFrames();
				final int height = mainGUI.getHeight();
				final int width = mainGUI.getWidth();
				final Bit bits = mainGUI.getBits();
				final Color colors = mainGUI.getColors();
				final Double backgroundValue = mainGUI.getBackgroundValue();
				final int numOfParticles = mainGUI.getNumOfParticles();
				final int particlesDist = mainGUI.getDistanceBetweenParticles();
				final List<Double> signalPeakValues = mainGUI
				        .getSignalPeakValue();
				final MovementDirection movDirection = mainGUI
				        .getMovementDirection();
				final Double movSpeed = mainGUI.getMovementSpeed();
				final Integer radius = mainGUI.getRadius();
				final Integer sigmaValue = mainGUI.getSigmaValue();

				final boolean hasBackGround = mainGUI.getHasBackground();
				final boolean hasParticles = mainGUI.getHasParticles();
				final boolean hasParticlesLogs = mainGUI.getHasParticlesLogs();
				final boolean hasGaussian = mainGUI.getHasGaussian();
				final boolean hasPoisson = mainGUI.getHasPoisson();
				final boolean hasDoubleValuesLogs = mainGUI
				        .getHasDoubleValuesLogs();

				final GenerationType genType = mainGUI.getGeneratorType();

				ArtificialImageGeneratorStandard aig = null;

				switch (genType) {
				case Special:
					aig = new ArtificialImageGeneratorSpecial(folder,
					        numOfDatasets, imageName, imagePostfixDigits,
					        numOfFrames, height, width, bits, colors,
					        backgroundValue, numOfParticles, particlesDist,
					        signalPeakValues, movDirection, movSpeed, radius,
					        sigmaValue, hasBackGround, hasParticles,
					        hasGaussian, hasPoisson, hasParticlesLogs,
					        hasDoubleValuesLogs, mainGUI);
					break;
				default:
					aig = new ArtificialImageGeneratorStandard(folder,
					        numOfDatasets, imageName, imagePostfixDigits,
					        numOfFrames, height, width, bits, colors,
					        backgroundValue, numOfParticles, particlesDist,
					        signalPeakValues, movDirection, movSpeed, radius,
					        sigmaValue, hasBackGround, hasParticles,
					        hasGaussian, hasPoisson, hasParticlesLogs,
					        hasDoubleValuesLogs, mainGUI);
					break;
				}

				final Thread t = new Thread(aig);
				t.start();
			}
		});
	}

	public static void addChangeGeneratoreTypeListener(
	        final ArtificialImageGeneratorGUI mainGUI) {
		mainGUI.getGeneratorTypeCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				final GenerationType genType = (GenerationType) mainGUI
				        .getGeneratorTypeCombo().getSelectedItem();
				mainGUI.setDefaultValues(genType);
			}
		});

	}
}
