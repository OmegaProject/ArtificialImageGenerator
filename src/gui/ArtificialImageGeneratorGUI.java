package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import core.GenerationType;
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

	private static int PANEL_ROWS = 9;

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

	private JComboBox<GenerationType> generatorTypeCombo;

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
	private JTextField particlesDist_txt;
	private JTextField radius_txt;
	private JTextField movSpeed_txt;

	private JTextField sigma_txt;

	private JComboBox<Bit> bits_cmb;
	private JComboBox<Color> colors_cmb;
	private JComboBox<MovementDirection> movDirs_cmb;

	private JCheckBox backgroundGen_checkB, particlesGen_checkB,
	        gaussianGen_checkB, poissonGen_checkB, particlesLogsGen_checkB,
	        doubleValuesGen_checkB;

	private JButton generate_btt;

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

		this.setDefaultValues(GenerationType.Standard);

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
		final Container cont = this.mainFrame.getContentPane();
		cont.setLayout(new BorderLayout());

		final JPanel folderPanel = this.createFolderPanel();
		final JPanel mainPanel = this.createImageMainInformationsPanel();
		final JPanel particlesPanel = this.createParticlesPanel();
		final JPanel optionsPanel = this.createOptionsPanel();
		final JPanel resultsPanel = this.createResultsPanel();
		final JPanel buttonsPanel = this.createButtonsPanel();

		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		topPanel.add(folderPanel, BorderLayout.NORTH);

		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 2));
		centerPanel.add(mainPanel);
		centerPanel.add(particlesPanel);
		topPanel.add(centerPanel, BorderLayout.CENTER);
		topPanel.add(optionsPanel, BorderLayout.SOUTH);

		cont.add(topPanel, BorderLayout.NORTH);

		cont.add(resultsPanel, BorderLayout.CENTER);
		cont.add(buttonsPanel, BorderLayout.SOUTH);
	}

	private JPanel createFolderPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel topSubPanel = new JPanel();
		topSubPanel.setLayout(new GridLayout(1, 2));

		topSubPanel.add(new JLabel("Choose generatore type:"));
		final GenerationType[] choices = GenerationType.values();
		this.generatorTypeCombo = new JComboBox<GenerationType>(choices);
		topSubPanel.add(this.generatorTypeCombo);

		this.dirChooserButt = new JButton("Choose the working directory");
		topSubPanel.add(this.dirChooserButt);

		panel.add(topSubPanel, BorderLayout.NORTH);

		final JPanel subPanel = new JPanel();
		subPanel.setLayout(new FlowLayout());

		subPanel.add(new JLabel("Actual folder:"));
		final String s = this.dirChooserDialog.getCurrentDirectory().getPath();
		this.currentDirLbl = new JLabel(s);
		subPanel.add(this.currentDirLbl);

		panel.add(subPanel, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Create the main informations panel
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private JPanel createImageMainInformationsPanel() {
		final JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Image options"));
		panel.setLayout(new GridLayout(ArtificialImageGeneratorGUI.PANEL_ROWS,
		        2));

		panel.add(new JLabel("Number of datasets"));
		this.numberOfDatasets_text = new JTextField();
		panel.add(this.numberOfDatasets_text);

		panel.add(new JLabel("Image name"));
		this.imageName_txt = new JTextField();
		panel.add(this.imageName_txt);

		panel.add(new JLabel("Image digits postfix"));
		this.imageDigitsPostfix_txt = new JTextField();
		panel.add(this.imageDigitsPostfix_txt);

		panel.add(new JLabel("Number of frames"));
		this.numOfFrames_txt = new JTextField();
		panel.add(this.numOfFrames_txt);

		panel.add(new JLabel("Frame height"));
		this.height_txt = new JTextField();
		panel.add(this.height_txt);

		panel.add(new JLabel("Frame width"));
		this.width_txt = new JTextField();
		panel.add(this.width_txt);

		panel.add(new JLabel("Frame bits deepth"));
		this.bits_cmb = new JComboBox<Bit>(Bit.values());
		panel.add(this.bits_cmb);

		panel.add(new JLabel("Frame colors"));
		this.colors_cmb = new JComboBox<Color>(Color.values());
		panel.add(this.colors_cmb);

		panel.add(new JLabel("Background value"));
		this.backgroundValue_txt = new JTextField();
		panel.add(this.backgroundValue_txt);

		final int diff = ArtificialImageGeneratorGUI.PANEL_ROWS
		        - panel.getComponentCount();
		for (int i = 0; i < diff; i++) {
			panel.add(new JLabel());
			panel.add(new JLabel());
		}

		return panel;
	}

	/**
	 * Create the image particles panel
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	private JPanel createParticlesPanel() {
		final JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Particles options"));
		panel.setLayout(new GridLayout(ArtificialImageGeneratorGUI.PANEL_ROWS,
		        2));

		panel.add(new JLabel("Number of particles"));
		this.numOfParticles_txt = new JTextField();
		panel.add(this.numOfParticles_txt);

		panel.add(new JLabel("Distance between particles"));
		this.particlesDist_txt = new JTextField();
		panel.add(this.particlesDist_txt);

		panel.add(new JLabel("Signal peak value"));
		this.signalPeakValue_txt = new JTextField();
		panel.add(this.signalPeakValue_txt);

		panel.add(new JLabel("Particles movement direction"));
		this.movDirs_cmb = new JComboBox<MovementDirection>(
		        MovementDirection.values());
		panel.add(this.movDirs_cmb);

		panel.add(new JLabel("Particle movement speed"));
		this.movSpeed_txt = new JTextField();
		panel.add(this.movSpeed_txt);

		panel.add(new JLabel("Radius of particles"));
		this.radius_txt = new JTextField();
		panel.add(this.radius_txt);

		panel.add(new JLabel("Sigma value"));
		this.sigma_txt = new JTextField();
		panel.add(this.sigma_txt);

		final int diff = ArtificialImageGeneratorGUI.PANEL_ROWS
		        - panel.getComponentCount();
		for (int i = 0; i < diff; i++) {
			panel.add(new JLabel());
			panel.add(new JLabel());
		}

		return panel;
	}

	private JPanel createOptionsPanel() {
		final JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Generation options"));
		panel.setLayout(new FlowLayout());

		this.backgroundGen_checkB = new JCheckBox("Has background");
		panel.add(this.backgroundGen_checkB);

		this.particlesGen_checkB = new JCheckBox("Has particles");
		panel.add(this.particlesGen_checkB);

		this.particlesLogsGen_checkB = new JCheckBox("Has particles logs");
		panel.add(this.particlesLogsGen_checkB);

		this.gaussianGen_checkB = new JCheckBox("Has gaussian blob");
		panel.add(this.gaussianGen_checkB);

		this.poissonGen_checkB = new JCheckBox("Has poisson");
		panel.add(this.poissonGen_checkB);

		this.doubleValuesGen_checkB = new JCheckBox("Has double values logs");
		panel.add(this.doubleValuesGen_checkB);

		return panel;
	}

	private JPanel createButtonsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		this.generate_btt = new JButton("Generate");
		panel.add(this.generate_btt);

		return panel;
	}

	/**
	 * Create the results panel
	 * 
	 * @return
	 * 
	 * @since 0.2
	 */
	private JPanel createResultsPanel() {
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
		ArtificialImageGeneratorGUIListeners.addGenerateButtListener(this);
		ArtificialImageGeneratorGUIListeners
		        .addChangeGeneratoreTypeListener(this);
	}

	/**
	 * Set default values in each field
	 * 
	 * @since 0.1
	 */
	public void setDefaultValues(final GenerationType genType) {
		this.numberOfDatasets_text.setText("1");
		this.imageName_txt.setText("test");
		this.imageDigitsPostfix_txt.setText("4");
		this.numOfFrames_txt.setText("100");

		this.bits_cmb.setSelectedItem(Bit._16_Bits);
		this.colors_cmb.setSelectedItem(Color.Grey);

		this.backgroundValue_txt.setText("10");
		this.signalPeakValue_txt.setText("23.9");

		this.radius_txt.setText("4");

		this.sigma_txt.setText("1");

		this.backgroundGen_checkB.setSelected(true);
		this.particlesGen_checkB.setSelected(true);
		this.particlesLogsGen_checkB.setSelected(true);
		this.gaussianGen_checkB.setSelected(true);
		this.poissonGen_checkB.setSelected(true);

		switch (genType) {
		case Special:
			this.height_txt.setText("15");
			this.width_txt.setText("15");
			this.numOfParticles_txt.setText("1");
			this.particlesDist_txt.setText("0");
			this.movDirs_cmb.setSelectedIndex(-1);
			this.movSpeed_txt.setText("");
			this.movSpeed_txt.setEnabled(false);
			this.movDirs_cmb.setEnabled(false);
			break;
		default:
			this.height_txt.setText("256");
			this.width_txt.setText("256");
			this.numOfParticles_txt.setText("10");
			this.particlesDist_txt.setText("10");
			this.movDirs_cmb.setSelectedItem(MovementDirection.Horizontal);
			this.movSpeed_txt.setText("0.27");
			this.movSpeed_txt.setEnabled(true);
			this.movDirs_cmb.setEnabled(true);
			break;
		}

	}

	/**
	 * Return the generate poisson distribution button
	 * 
	 * @return
	 * 
	 * @since 0.0
	 */
	public JButton getGenerateButt() {
		return this.generate_btt;
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

	public Integer getDistanceBetweenParticles() {
		return Integer.valueOf(this.particlesDist_txt.getText());
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
		if (this.movDirs_cmb.getSelectedIndex() == -1)
			return null;
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
		if (this.movSpeed_txt.getText().isEmpty())
			return null;
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
	public void appendOutput(final String output) {
		this.results_txtA.append(output);
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

	public JComboBox<GenerationType> getGeneratorTypeCombo() {
		return this.generatorTypeCombo;
	}

	public GenerationType getGeneratorType() {
		return (GenerationType) this.generatorTypeCombo.getSelectedItem();
	}

	public boolean getHasBackground() {
		return this.backgroundGen_checkB.isSelected();
	}

	public boolean getHasParticles() {
		return this.particlesGen_checkB.isSelected();
	}

	public boolean getHasParticlesLogs() {
		return this.particlesLogsGen_checkB.isSelected();
	}

	public boolean getHasGaussian() {
		return this.gaussianGen_checkB.isSelected();
	}

	public boolean getHasPoisson() {
		return this.poissonGen_checkB.isSelected();
	}

	public boolean getHasDoubleValuesLogs() {
		return this.doubleValuesGen_checkB.isSelected();
	}
}
