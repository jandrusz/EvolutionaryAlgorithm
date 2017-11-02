package com.pstkm;

import com.pstkm.algorithms.BruteForce;
import com.pstkm.algorithms.EvolutionaryAlgorithm;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.RoutingSolutionDTO;
import com.pstkm.fileManagement.FileReader;
import com.pstkm.fileManagement.FileWriter;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Getter
class MainWindow {

	private JFrame frame;
	private FileReader fileReader;
	private FileDialog fileDialog;
	private JLabel label;
	private Path path;
	private FileDTO file;
	private JTextArea textArea;
	private JTextField chromosomesNumberTextField;
	private JTextField mutationProbabilityTextField;
	private JTextField crossoverProbabilityTextField;
	private JTextField maxTimeTextField;
	private JTextField numberOfGenerationsTextField;
	private JTextField numberOfMutationsTextField;
	private JTextField seedTextField;

	MainWindow() {
		frame = new JFrame();
		fileReader = new FileReader();
		buildMainFrame();
	}

	private void buildMainFrame() {
		setFrame();
		frame.add(getImportButton());
		frame.add(getBruteForceButton());
		frame.add(getEvolutionaryAlgorithmButton());
		frame.add(getPathLabel());
		frame.add(getChromosomesNumberLabel());
		frame.add(getChromosomesNumberTextFields());
		frame.add(getMutationProbabilityLabel());
		frame.add(getMutationProbabilityTextField());
		frame.add(getCrossoverProbabilityLabel());
		frame.add(getCrossoverProbabilityTextField());
		frame.add(getEvolutionaryAlgorithmLogTextArea());
		frame.add(getStopCriteriaLabel());
		frame.add(getMaxTimeLabel());
		frame.add(getMaxTimeTextField());
		frame.add(getNumberOfGenerationsLabel());
		frame.add(getNumberOfGenerationsTextField());
		frame.add(getNumberOfMutationsLabel());
		frame.add(getNumberOfMutationsTextField());
        frame.add(getSeedLabel());
        frame.add(getSeedTextField());
	}

	private JLabel getChromosomesNumberLabel() {
        JLabel label = new JLabel();
		label.setBounds(20, 160, 180, 20);
		label.setText("Number of chromosomes:");
		return label;
	}

	private JTextField getChromosomesNumberTextFields(){
		chromosomesNumberTextField = new JTextField();
        chromosomesNumberTextField.setBounds(240, 160, 120, 20);
		return chromosomesNumberTextField;
	}

	private JLabel getMutationProbabilityLabel() {
        JLabel label = new JLabel();
		label.setBounds(20, 180, 180, 20);
		label.setText("Mutation probability:");
		return label;
	}

	private JTextField getMutationProbabilityTextField(){
		mutationProbabilityTextField = new JTextField();
        mutationProbabilityTextField.setBounds(240, 180, 120, 20);
		return mutationProbabilityTextField;
	}

	private JLabel getCrossoverProbabilityLabel() {
        JLabel label = new JLabel();
		label.setBounds(20, 200, 180, 20);
		label.setText("Crossover probability:");
		return label;
	}

	private JTextField getCrossoverProbabilityTextField(){
		crossoverProbabilityTextField = new JTextField();
        crossoverProbabilityTextField.setBounds(240, 200, 120, 20);
		return crossoverProbabilityTextField;
	}

	private JTextArea getEvolutionaryAlgorithmLogTextArea(){
		textArea = new JTextArea();
		textArea.setBounds(400, 20, 380, 360);
		textArea.setEnabled(false);
		textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return textArea;
	}

	private JLabel getStopCriteriaLabel() {
		JLabel label = new JLabel();
		label.setBounds(160, 220, 180, 20);
		label.setText("Stop criteria");
		return label;
	}

	private JLabel getMaxTimeLabel() {
        JLabel label = new JLabel();
		label.setBounds(20, 240, 180, 20);
		label.setText("Max time:");
		return label;
	}

    private JTextField getMaxTimeTextField(){
        maxTimeTextField = new JTextField();
        maxTimeTextField.setBounds(240, 240, 120, 20);
        return maxTimeTextField;
    }

	private JLabel getNumberOfGenerationsLabel() {
        JLabel label = new JLabel();
		label.setBounds(20, 260, 180, 20);
		label.setText("Number of generations:");
		return label;
	}

    private JTextField getNumberOfGenerationsTextField(){
        numberOfGenerationsTextField = new JTextField();
        numberOfGenerationsTextField.setBounds(240, 260, 120, 20);
        return numberOfGenerationsTextField;
    }

	private JLabel getNumberOfMutationsLabel() {
        JLabel label = new JLabel();
		label.setBounds(20, 280, 180, 20);
		label.setText("Number of mutations:");
		return label;
	}

    private JTextField getNumberOfMutationsTextField(){
        numberOfMutationsTextField = new JTextField();
        numberOfMutationsTextField.setBounds(240, 280, 120, 20);
        return numberOfMutationsTextField;
    }

    private JLabel getSeedLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 320, 180, 20);
        label.setText("Seed:");
        return label;
    }

    private JTextField getSeedTextField(){
        seedTextField = new JTextField();
        seedTextField.setBounds(240, 320, 120, 20);
        return seedTextField;
    }

	private void setFrame() {
		frame.setTitle("Evolutionary algorithm");
		frame.setBounds(400, 200, 800, 400);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private JButton getImportButton() {
		JButton button = new JButton("Choose file");
		button.setForeground(Color.BLACK);
		button.setBackground(SystemColor.menu);
		button.setFont(new Font("Arial", Font.PLAIN, 16));
		button.setBounds(20, 40, 360, 50);
		setListenerForImportButton(button);
		return button;
	}

	private JButton getBruteForceButton() {
		JButton button = new JButton("Brute force");
		button.setForeground(Color.BLACK);
		button.setBackground(SystemColor.menu);
		button.setFont(new Font("Arial", Font.PLAIN, 16));
		button.setBounds(20, 100, 180, 50);
		setListenerForBruteForceButton(button);
		return button;
	}

	private JButton getEvolutionaryAlgorithmButton() {
		JButton button = new JButton("Evolutionary alg.");
		button.setForeground(Color.BLACK);
		button.setBackground(SystemColor.menu);
		button.setFont(new Font("Arial", Font.PLAIN, 16));
		button.setBounds(200, 100, 180, 50);
		setListenerForEvolutionaryAlgorithmButton(button);
		return button;
	}

	private JLabel getPathLabel() {
		label = new JLabel();
		label.setBounds(20, 0, 380, 50);
		label.setText("Chosen file:");
		return label;
	}

	private void setListenerForImportButton(JButton button) {
		button.addActionListener(e -> {
			try {
				importAndLoadFile();
				label.setText("Chosen file: " + path.getParent() + "\\" + path.getFileName());
				label.setToolTipText(label.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	private void setListenerForBruteForceButton(JButton button) {
		button.addActionListener(e -> {
			BruteForce bruteForce = new BruteForce(file);
			List<RoutingSolutionDTO> allAcceptableRoutingSolutions = bruteForce.getAllAcceptableRoutingSolutions();
			List<List<Integer>> costs = bruteForce.computeCostsOfAllRoutingSolutions(allAcceptableRoutingSolutions);
			RoutingSolutionDTO routingSolutionDDAP = bruteForce.computeDDAP(allAcceptableRoutingSolutions, costs);
			System.out.println("DDAP RESULT:\n" + routingSolutionDDAP.toString());
			new FileWriter().writeFile("output", routingSolutionDDAP, bruteForce.getFile());
			RoutingSolutionDTO routingSolutionDAP = bruteForce.computeDAP(allAcceptableRoutingSolutions, costs);
			System.out.println("DAP RESULT:\n" + routingSolutionDAP.toString());
		});
	}

	private void setListenerForEvolutionaryAlgorithmButton(JButton button) {
		button.addActionListener(e -> {
			EvolutionaryAlgorithm evolutionaryAlgorithm = new EvolutionaryAlgorithm();
		});
	}

	private void importAndLoadFile() throws IOException {
		fileDialog = new FileDialog(frame, "Choose file", FileDialog.LOAD);
		fileDialog.setVisible(true);
		if (Objects.nonNull(fileDialog.getFile())) {
			path = Paths.get(fileDialog.getDirectory() + fileDialog.getFile());
			file = fileReader.readFilePipeline(Files.readAllLines(path, StandardCharsets.UTF_8));
		}
	}
}
