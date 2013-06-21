import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * 
 */

/**
 * @author Alex Rigano
 * 
 */
public class Listeners {

	public static void addGenerateListener(
			final ArtificialImageGeneratorGUI artificialImageGeneratorGUI) {
		artificialImageGeneratorGUI.generateButt
				.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent arg0) {
						boolean validator = artificialImageGeneratorGUI
								.validateNamePrefix();
						final String imageNamePrefix = artificialImageGeneratorGUI
								.getNamePrefix();
						validator = artificialImageGeneratorGUI
								.validatePostfixDigits();
						final int imageNamePostifxDigits = artificialImageGeneratorGUI
								.getPostfixDigits();
						validator = artificialImageGeneratorGUI
								.validateNumberOfImages();
						final int numberOfImages = artificialImageGeneratorGUI
								.getNumberOfImages();
						validator = artificialImageGeneratorGUI
								.validateImageWidth();
						final int sizeW = artificialImageGeneratorGUI
								.getImageWidth();
						validator = artificialImageGeneratorGUI
								.validateImageHeight();
						final int sizeH = artificialImageGeneratorGUI
								.getImageHeight();
						validator = artificialImageGeneratorGUI
								.validateImageBackground();
						final int backgroundValue = artificialImageGeneratorGUI
								.getImageBackground();
						validator = artificialImageGeneratorGUI.validateColor();
						final int color = artificialImageGeneratorGUI
								.getColor();
						validator = artificialImageGeneratorGUI
								.validatePeakSignal();
						final int intensityPeakValue = artificialImageGeneratorGUI
								.getPeakSignal();
						validator = artificialImageGeneratorGUI
								.validateNumberfOfParticles();
						final int numberOfParticles = artificialImageGeneratorGUI
								.getNumberfOfParticles();
						validator = artificialImageGeneratorGUI
								.validateParticlesMovDirection();
						final int movDirection = artificialImageGeneratorGUI
								.getParticlesMovDirection();
						validator = artificialImageGeneratorGUI
								.validateParticlesMovSpeed();
						final int movSpeed = artificialImageGeneratorGUI
								.getParticlesMovSpeed();
						validator = artificialImageGeneratorGUI
								.validateParticlesMovOscillation();
						final int movOscillation = artificialImageGeneratorGUI
								.getParticlesMovOscillation();
						final ArtificialImageGenerator gen = new ArtificialImageGenerator(
								imageNamePrefix, imageNamePostifxDigits,
								numberOfImages, sizeW, sizeH, backgroundValue,
								color, intensityPeakValue, numberOfParticles,
								movDirection, movSpeed, movOscillation);
						try {
							gen.generate();
						} catch (final IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
}
