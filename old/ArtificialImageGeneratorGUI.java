import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 */

/**
 * @author Alex Rigano
 * 
 */
public class ArtificialImageGeneratorGUI extends JFrame {

	private static final long serialVersionUID = 5986814575391110121L;
	JPanel mainPanel, buttonPanel;

	JTextField imagePrefix_txt, imagePostfixDigit_txt, imageNumber_txt,
			imageHeight_txt, imageWidth_txt, imageColor_txt,
			imagePeakSignal_txt, imageBackground_txt,
			imageNumberOfParticles_txt, imageParticlesMovementDirection_txt,
			imageParticlesMovementSpeed_txt,
			imageParticlesMovementOscillation_txt;

	JButton generateButt;

	public ArtificialImageGeneratorGUI() {
		super("ArtificialImageGenerator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createAndPopulateMainPanel();
		createAndPopulateButtonPanel();

		addWidgetsToContentPane();

		pack();
		setVisible(true);
	}

	public void addWidgetsToContentPane() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public void createAndPopulateMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(12, 2));
		// Prefix row1
		final JLabel imagePrefix_lbl = new JLabel("Image prefix");
		imagePrefix_txt = new JTextField();
		imagePrefix_txt.setText("test");
		mainPanel.add(imagePrefix_lbl);
		mainPanel.add(imagePrefix_txt);
		// Postfix row2
		final JLabel imagePostfixDigit_lbl = new JLabel("Image postfix digit");
		imagePostfixDigit_txt = new JTextField();
		imagePostfixDigit_txt.setText("0");
		mainPanel.add(imagePostfixDigit_lbl);
		mainPanel.add(imagePostfixDigit_txt);
		// # of images row3
		final JLabel imageNumber_lbl = new JLabel("Number of images");
		imageNumber_txt = new JTextField();
		imageNumber_txt.setText("5");
		mainPanel.add(imageNumber_lbl);
		mainPanel.add(imageNumber_txt);
		// Height row4
		final JLabel imageHeight_lbl = new JLabel("Image height");
		imageHeight_txt = new JTextField();
		imageHeight_txt.setText("480");
		mainPanel.add(imageHeight_lbl);
		mainPanel.add(imageHeight_txt);
		// Width row5
		final JLabel imageWidth_lbl = new JLabel("Image width");
		imageWidth_txt = new JTextField();
		imageWidth_txt.setText("320");
		mainPanel.add(imageWidth_lbl);
		mainPanel.add(imageWidth_txt);
		final JLabel imageColor_lbl = new JLabel("Image color");
		imageColor_txt = new JTextField();
		imageColor_txt.setText("0");
		mainPanel.add(imageColor_lbl);
		mainPanel.add(imageColor_txt);
		// Peak signal row6
		final JLabel imagePeakSignal_lbl = new JLabel("Image peak signal");
		imagePeakSignal_txt = new JTextField();
		imagePeakSignal_txt.setText("240");
		mainPanel.add(imagePeakSignal_lbl);
		mainPanel.add(imagePeakSignal_txt);
		// Background row7
		final JLabel imageBackground_lbl = new JLabel("Image background");
		imageBackground_txt = new JTextField();
		imageBackground_txt.setText("30");
		mainPanel.add(imageBackground_lbl);
		mainPanel.add(imageBackground_txt);
		// # of particles row8
		final JLabel imageNumberOfParticles_lbl = new JLabel(
				"Image number of particles");
		imageNumberOfParticles_txt = new JTextField();
		imageNumberOfParticles_txt.setText("8");
		mainPanel.add(imageNumberOfParticles_lbl);
		mainPanel.add(imageNumberOfParticles_txt);
		// Particles movement orientation row9
		final JLabel imageParticlesMovementDirection_lbl = new JLabel(
				"Image particles movement direction");
		imageParticlesMovementDirection_txt = new JTextField();
		imageParticlesMovementDirection_txt.setText("1");
		mainPanel.add(imageParticlesMovementDirection_lbl);
		mainPanel.add(imageParticlesMovementDirection_txt);
		// Particles movement between frames row10
		final JLabel imageParticlesMovementSpeed_lbl = new JLabel(
				"Image particles movement speed");
		imageParticlesMovementSpeed_txt = new JTextField();
		imageParticlesMovementSpeed_txt.setText("10");
		mainPanel.add(imageParticlesMovementSpeed_lbl);
		mainPanel.add(imageParticlesMovementSpeed_txt);
		final JLabel imageParticlesMovementOscillation_lbl = new JLabel(
				"Image particles movement speed");
		imageParticlesMovementOscillation_txt = new JTextField();
		imageParticlesMovementOscillation_txt.setText("5");
		mainPanel.add(imageParticlesMovementOscillation_lbl);
		mainPanel.add(imageParticlesMovementOscillation_txt);
	}

	public void createAndPopulateButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		generateButt = new JButton("Generate");
		Listeners.addGenerateListener(this);
		buttonPanel.add(generateButt, BorderLayout.CENTER);
	}

	public boolean validateNamePrefix() {
		// TODO
		return true;
	}

	public String getNamePrefix() {
		return imagePrefix_txt.getText();
	}

	public boolean validatePostfixDigits() {
		// TODO
		return true;
	}

	public int getPostfixDigits() {
		int digits = 0;
		try {
			digits = Integer.valueOf(imagePostfixDigit_txt.getText());
		} catch (final NumberFormatException ex) {
			digits = -1;
		}
		return digits;
	}

	public boolean validateNumberOfImages() {
		// TODO
		return true;
	}

	public int getNumberOfImages() {
		int numImg = 0;
		try {
			numImg = Integer.valueOf(imageNumber_txt.getText());
		} catch (final NumberFormatException ex) {
			numImg = -1;
		}
		return numImg;
	}

	public boolean validateImageHeight() {
		// TODO
		return true;
	}

	public int getImageHeight() {
		int height = 0;
		try {
			height = Integer.valueOf(imageHeight_txt.getText());
		} catch (final NumberFormatException ex) {
			height = -1;
		}
		return height;
	}

	public boolean validateImageWidth() {
		// TODO
		return true;
	}

	public int getImageWidth() {
		int width = 0;
		try {
			width = Integer.valueOf(imageWidth_txt.getText());
		} catch (final NumberFormatException ex) {
			width = -1;
		}
		return width;
	}

	public boolean validateColor() {
		// TODO
		return true;
	}

	public int getColor() {
		int color = 0;
		try {
			color = Integer.valueOf(imageColor_txt.getText());
		} catch (final NumberFormatException ex) {
			color = -1;
		}
		return color;
	}

	public boolean validatePeakSignal() {
		// TODO
		return true;
	}

	public int getPeakSignal() {
		int peakSig = 0;
		try {
			peakSig = Integer.valueOf(imagePeakSignal_txt.getText());
		} catch (final NumberFormatException ex) {
			peakSig = -1;
		}
		return peakSig;
	}

	public boolean validateImageBackground() {
		// TODO
		return true;
	}

	public int getImageBackground() {
		int bg = 0;
		try {
			bg = Integer.valueOf(imageBackground_txt.getText());
		} catch (final NumberFormatException ex) {
			bg = -1;
		}
		return bg;
	}

	public boolean validateNumberfOfParticles() {
		// TODO
		return true;
	}

	public int getNumberfOfParticles() {
		int numParticles = 0;
		try {
			numParticles = Integer
					.valueOf(imageNumberOfParticles_txt.getText());
		} catch (final NumberFormatException ex) {
			numParticles = -1;
		}
		return numParticles;
	}

	public boolean validateParticlesMovDirection() {
		// TODO
		return true;
	}

	public int getParticlesMovDirection() {
		int movDir = 0;
		try {
			movDir = Integer.valueOf(imageParticlesMovementDirection_txt
					.getText());
		} catch (final NumberFormatException ex) {
			movDir = -1;
		}
		return movDir;
	}

	public boolean validateParticlesMovSpeed() {
		// TODO
		return true;
	}

	public int getParticlesMovSpeed() {
		int speed = 0;
		try {
			speed = Integer.valueOf(imageParticlesMovementSpeed_txt.getText());
		} catch (final NumberFormatException ex) {
			speed = -1;
		}
		return speed;
	}

	public boolean validateParticlesMovOscillation() {
		// TODO
		return true;
	}

	public int getParticlesMovOscillation() {
		int speed = 0;
		try {
			speed = Integer.valueOf(imageParticlesMovementOscillation_txt
					.getText());
		} catch (final NumberFormatException ex) {
			speed = -1;
		}
		return speed;
	}

	public static void main(final String[] args) {
		new ArtificialImageGeneratorGUI();
	}
}
