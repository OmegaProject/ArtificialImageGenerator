package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import enums.Bit;
import enums.Color;
import enums.MovementDirection;

/**
 * GUI for the AIG
 * 
 * @author Alex Rigano
 * @version 0.4, Stable
 * @since 0.0 of AIG
 */
public class ArtificialImageGeneratorGUI {

	// TODO:
	// 1) add validators on gui fields
	// 2) add feature: selected initial distance from border
	// 3) add feature: select working directory
	// 4) add feature: movement direction variation

	public static void main(final String[] args) {
		final ArtificialImageGeneratorGUI mainGUI = new ArtificialImageGeneratorGUI();
		mainGUI.createAndShowGUI();
	}

	private JFrame mainFrame;

	private JButton dirChooserButt;
	private JFileChooser dirChooserDialog;
	private JLabel currentDirLbl;

	private JTextField numberOfDatasets_text;

	private JTextField imageName_txt;
	private JTextField imageDigitsPostfix_txt;
	private JTextField numOfFrames_txt;
	private JTextField height_txt;
	private JTextField width_txt;
	private JTextField signalPeakValue_txt;
	private JTextField backgroundValue_txt;
	private JTextField numOfParticles_txt;
	private JTextField radius_txt;
	private JTextField movSpeed_txt;

	private JTextField sigma_txt;

	private JComboBox<Bit> bits_cmb;
	private JComboBox<Color> colors_cmb;
	private JComboBox<MovementDirection> movDirs_cmb;

	private JButton generateBackground_btt;
	private JButton generateParticles_btt;
	private JButton generateGaussianBlob_btt;
	private JButton generatePoissonDistr_btt;

	private JTextArea results_txtA;

	/**
	 * Create the main frame and invoke all the needed methods
	 * 
	 * @since 0.0
	 */
	private void createAndShowGUI() {
		// Create and set up the window.
		this.mainFrame = new JFrame("Artificial Image Generator");
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.dirChooserDialog = new JFileChooser();
		final String s = System.getProperty("user.dir");
		this.dirChooserDialog.setCurrentDirectory(new File(s + "/img"));
		this.dirChooserDialog.setDialogTitle("Working directory chooser");
		this.dirChooserDialog.setMultiSelectionEnabled(false);
		this.dirChooserDialog
		        .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		this.addWidgets();

		this.addListeners();

		this.setDefaultValues();

		// Display the window.
		this.mainFrame.pack();
		this.mainFrame.setVisible(true);
	}

	/**
	 * Create all the needed panels and invoke the relative methods
	 * 
	 * @since 0.0
	 */
	private void addWidgets() {
		this.mainFrame.getContentPane()
		        .setLayout(
		                new BoxLayout(this.mainFrame.getContentPane(),
		                        BoxLayout.Y_AXIS));

		this.mainFrame.getContentPane().add(
		        this.generateImageMainInformationsPanel());
		this.addHorizontalSeparator();

		this.mainFrame.getContentPane().add(this.generateBackgroundPanel());
		this.addHorizontalSeparator();

		this.mainFrame.getContentPane().add(this.generateParticlesPanel());
		this.addHorizontalSeparator();

		this.mainFrame.getContentPane().add(this.generateGaussianBlobPanel());
		this.addHorizontalSeparator();

		this.mainFrame.getContentPane().add(
		        this.generatePoissonDistributionPanel());
		this.addHorizontalSeparator();

		this.mainFrame.getContentPane().add(this.generateResultsPanel());
	}

	/**
	 * Add an horizontal separator
	 * 
	 * @since 0.0
	 */
	private void addHorizontalSeparator() {
		this.mainFrame.getContentPane().add(Box.createVerticalStrut(5));
		this.mainFrame.getContentPane().add(new JSeparator());
		this.mainFrame.getContentPane().add(Box.createVerticalStrut(5));
	}

	/**
	 * Create the main informations panel
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private JPanel generateImageMainInformationsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel topSubPanel = new JPanel();
		topSubPanel.setLayout(new GridLayout(3, 1));

		topSubPanel.add(new JLabel("Actual folder:"));
		final String s = this.dirChooserDialog.getCurrentDirectory().getPath();
		this.currentDirLbl = new JLabel(s);
		topSubPanel.add(this.currentDirLbl);
		this.dirChooserButt = new JButton("Choose the working directory");
		topSubPanel.add(this.dirChooserButt);

		panel.add(topSubPanel, BorderLayout.NORTH);

		final JPanel subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(8, 2));

		subPanel.add(new JLabel("Number of datasets"));
		this.numberOfDatasets_text = new JTextField();
		subPanel.add(this.numberOfDatasets_text);

		subPanel.add(new JLabel("Image name"));
		this.imageName_txt = new JTextField();
		subPanel.add(this.imageName_txt);

		subPanel.add(new JLabel("Image digits postfix"));
		this.imageDigitsPostfix_txt = new JTextField();
		subPanel.add(this.imageDigitsPostfix_txt);

		subPanel.add(new JLabel("Number of frames"));
		this.numOfFrames_txt = new JTextField();
		subPanel.add(this.numOfFrames_txt);

		subPanel.add(new JLabel("Frame height"));
		this.height_txt = new JTextField();
		subPanel.add(this.height_txt);

		subPanel.add(new JLabel("Frame width"));
		this.width_txt = new JTextField();
		subPanel.add(this.width_txt);

		subPanel.add(new JLabel("Frame bits deepth"));
		this.bits_cmb = new JComboBox<Bit>(Bit.values());
		subPanel.add(this.bits_cmb);

		subPanel.add(new JLabel("Frame colors"));
		this.colors_cmb = new JComboBox<Color>(Color.values());
		subPanel.add(this.colors_cmb);

		panel.add(subPanel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Create the image background panel
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private JPanel generateBackgroundPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(1, 2));

		subPanel.add(new JLabel("Background value"));
		this.backgroundValue_txt = new JTextField();
		subPanel.add(this.backgroundValue_txt);

		panel.add(subPanel, BorderLayout.CENTER);

		this.generateBackground_btt = new JButton(
		        "<HTML><center>Generate frames with<BR><center>background only - step 1 </HTML>");
		panel.add(this.generateBackground_btt, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Create the image particles panel
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private JPanel generateParticlesPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(4, 2));

		subPanel.add(new JLabel("Number of particles"));
		this.numOfParticles_txt = new JTextField();
		subPanel.add(this.numOfParticles_txt);

		subPanel.add(new JLabel("Signal peak value"));
		this.signalPeakValue_txt = new JTextField();
		subPanel.add(this.signalPeakValue_txt);

		subPanel.add(new JLabel("Particles movement direction"));
		this.movDirs_cmb = new JComboBox<MovementDirection>(
		        MovementDirection.values());
		subPanel.add(this.movDirs_cmb);

		subPanel.add(new JLabel("Particle movement speed"));
		this.movSpeed_txt = new JTextField();
		subPanel.add(this.movSpeed_txt);

		panel.add(subPanel, BorderLayout.CENTER);

		this.generateParticles_btt = new JButton(
		        "<HTML><center>Generate frames with<BR><center>particles - step 1 & 2 </HTML>");
		panel.add(this.generateParticles_btt, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Create the gaussian blob panel
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private JPanel generateGaussianBlobPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(2, 2));

		subPanel.add(new JLabel("Radius of particles"));
		this.radius_txt = new JTextField();
		subPanel.add(this.radius_txt);

		subPanel.add(new JLabel("Sigma value"));
		this.sigma_txt = new JTextField();
		subPanel.add(this.sigma_txt);

		panel.add(subPanel, BorderLayout.CENTER);

		this.generateGaussianBlob_btt = new JButton(
		        "<HTML><center>Generate frames with<BR><center>Gaussian blob - step 1, 2 & 3 </HTML>");
		panel.add(this.generateGaussianBlob_btt, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Create the poisson distribution panel
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private JPanel generatePoissonDistributionPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		this.generatePoissonDistr_btt = new JButton(
		        "<HTML><center>Generate frames with Gaussian blob<BR><center>and Poisson distribution - all steps </HTML>");
		panel.add(this.generatePoissonDistr_btt, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Create the results panel
	 * 
	 * @return
	 * 
	 * @since 0.2
	 */
	private JPanel generateResultsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		this.results_txtA = new JTextArea(10, 25);
		this.results_txtA.setLineWrap(true);
		this.results_txtA.setWrapStyleWord(true);
		this.results_txtA.setEditable(false);
		this.results_txtA.setRows(5);

		final JScrollPane scrollPane = new JScrollPane(this.results_txtA);

		// scrollPane.setPreferredSize(new Dimension(200, 200));

		// final DefaultCaret caret = (DefaultCaret)
		// this.results_txtA.getCaret();
		// caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		// scrollPane.add(this.results_txtA);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Add all the needed listeners
	 * 
	 * @since 0.0
	 */
	private void addListeners() {
		ArtificialImageGeneratorGUIListeners.addWorkingDirChooser(this);
		ArtificialImageGeneratorGUIListeners
		        .addGenerateBackgroundButtListener(this);
		ArtificialImageGeneratorGUIListeners
		        .addGenerateParticlesButtListener(this);
		ArtificialImageGeneratorGUIListeners
		        .addGenerateGaussianBlobButtListener(this);
		ArtificialImageGeneratorGUIListeners
		        .addGeneratePoissonDistrButtListener(this);
	}

	/**
	 * Set default values in each field
	 * 
	 * @since 0.1
	 */
	private void setDefaultValues() {
		this.numberOfDatasets_text.setText("1");
		this.imageName_txt.setText("test");
		this.imageDigitsPostfix_txt.setText("4");
		this.numOfFrames_txt.setText("100");
		this.height_txt.setText("118");
		this.width_txt.setText("118");

		this.bits_cmb.setSelectedItem(Bit._16_Bits);
		this.colors_cmb.setSelectedItem(Color.Grey);

		this.backgroundValue_txt.setText("10");

		this.numOfParticles_txt.setText("10");
		this.radius_txt.setText("4");
		this.signalPeakValue_txt.setText("23.9");
		this.movDirs_cmb.setSelectedItem(MovementDirection.Horizontal);
		this.movSpeed_txt.setText("0.27");

		this.sigma_txt.setText("1");

	}

	/**
	 * Return the generate background button
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public JButton getGenerateBackgroundButt() {
		return this.generateBackground_btt;
	}

	/**
	 * Return the generate particles button
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public JButton getGenerateParticlesButt() {
		return this.generateParticles_btt;
	}

	/**
	 * Return the generate gaussian blob button
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public JButton getGenerateGaussianBlobButt() {
		return this.generateGaussianBlob_btt;
	}

	/**
	 * Return the generate poisson distribution button
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public JButton getGeneratePoissonDistrButt() {
		return this.generatePoissonDistr_btt;
	}

	/**
	 * Return the number of datasets
	 * 
	 * @return
	 * 
	 * @since 0.4
	 */
	public Integer getNumOfDatasets() {
		return Integer.valueOf(this.numberOfDatasets_text.getText());
	}

	/**
	 * Return the image name
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public String getImageName() {
		return this.imageName_txt.getText();
	}

	/**
	 * Return the number of digits for the name postfix
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Integer getImagePostfixDigits() {
		return Integer.valueOf(this.imageDigitsPostfix_txt.getText());
	}

	/**
	 * Return the number of frames
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Integer getNumOfFrames() {
		return Integer.valueOf(this.numOfFrames_txt.getText());
	}

	/**
	 * Return the height of each frame
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Integer getHeight() {
		return Integer.valueOf(this.height_txt.getText());
	}

	/**
	 * Return the width of each frame
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Integer getWidth() {
		return Integer.valueOf(this.width_txt.getText());
	}

	/**
	 * Return the radius of each particles
	 * 
	 * @return
	 * 
	 * @since 0.3
	 */
	public Integer getRadius() {
		return Integer.valueOf(this.radius_txt.getText());
	}

	/**
	 * Return the bit depth of each frame
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Bit getBits() {
		return (Bit) this.bits_cmb.getSelectedItem();
	}

	/**
	 * Return the color of each frame
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Color getColors() {
		return (Color) this.colors_cmb.getSelectedItem();
	}

	/**
	 * Return the background value
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Double getBackgroundValue() {
		return Double.valueOf(this.backgroundValue_txt.getText());
	}

	/**
	 * Return the number of particles per frame
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Integer getNumOfParticles() {
		return Integer.valueOf(this.numOfParticles_txt.getText());
	}

	/**
	 * Return the peak signal value
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public List<Double> getSignalPeakValue() {
		// return Double.valueOf(this.signalPeakValue_txt.getText());
		final List<Double> peakValues = new ArrayList<Double>();
		final List<String> parsedString = this
		        .parseString(this.signalPeakValue_txt.getText());
		for (final String s : parsedString) {
			peakValues.add(Double.valueOf(s));
		}
		return peakValues;
	}

	/**
	 * Return the movement direction
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public MovementDirection getMovementDirection() {
		return (MovementDirection) this.movDirs_cmb.getSelectedItem();
	}

	/**
	 * Return the movement speed
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Double getMovementSpeed() {
		return Double.valueOf(this.movSpeed_txt.getText());
	}

	/**
	 * Return the sigma value
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public Integer getSigmaValue() {
		return Integer.valueOf(this.sigma_txt.getText());
	}

	public List<String> parseString(final String s) {
		String tmp = s;
		final List<String> parsed = new ArrayList<String>();
		while (tmp.contains(";") | tmp.contains(",")) {
			if (tmp.contains(";")) {
				final String subString = tmp.substring(0, tmp.indexOf(";"));
				parsed.add(subString);
				tmp = tmp.substring(tmp.indexOf(";") + 1, tmp.length());
			} else {
				final String subString = tmp.substring(0, tmp.indexOf(","));
				parsed.add(subString);
				tmp = tmp.substring(tmp.indexOf(",") + 1, tmp.length());
			}
		}
		parsed.add(tmp);
		return parsed;
	}

	/**
	 * Return the given results string to the results text area. A newline is
	 * inserted after each results string
	 * 
	 * @return
	 * 
	 * @since 0.2
	 */
	public void appendResultsText(final String results) {
		this.results_txtA.append(results);
		this.results_txtA.append("\n");
		this.results_txtA.setCaretPosition(this.results_txtA.getDocument()
		        .getLength());
	}

	public File getWorkingDirectory() {
		if (this.dirChooserDialog.getSelectedFile() != null)
			return this.dirChooserDialog.getSelectedFile();
		else
			return this.dirChooserDialog.getCurrentDirectory();
	}

	public JButton getDirChooserButton() {
		return this.dirChooserButt;
	}

	public JFileChooser getDirChooserDialog() {
		return this.dirChooserDialog;
	}

	public void setNewCurrentDirLbl(final String s) {
		this.currentDirLbl.setText(s);
	}

}
