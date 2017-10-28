package com.pstkm;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import lombok.Getter;
import com.pstkm.algorithms.BruteForce;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.RoutingSolutionDTO;

@Getter
class MainWindow {

	private JFrame frame;
	private FileReader fileReader;
	private FileDialog fileDialog;
	private JLabel label;
	private Path path;
	private FileDTO file;

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
	}

	private void setFrame() {
		frame.setTitle("Evolutionary algorithm");
		frame.setBounds(400, 200, 400, 400);
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
			List<java.util.List<Integer>> costs = bruteForce.computeCostsOfAllRoutingSolutions(allAcceptableRoutingSolutions);
			RoutingSolutionDTO routingSolutionDDAP = bruteForce.computeDDAP(allAcceptableRoutingSolutions, costs);
			System.out.println("DDAP RESULT:\n" + routingSolutionDDAP.toString());
			RoutingSolutionDTO routingSolutionDAP = bruteForce.computeDAP(allAcceptableRoutingSolutions, costs);
			System.out.println("DAP RESULT:\n" + routingSolutionDAP.toString());
		});
	}

	private void setListenerForEvolutionaryAlgorithmButton(JButton button) {
		button.addActionListener(e -> {
			//TODO in next part of project
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
