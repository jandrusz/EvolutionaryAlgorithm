package com.pstkm;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.swing.*;
import lombok.Getter;

@Getter
class MainWindow {

	private JFrame frame;

	private FileDialog fileDialog;

	private JLabel label;

	private Path path;

	MainWindow() {
		buildMainFrame();
	}

	private void buildMainFrame() {
		frame = new JFrame();
		setFrameProperties(frame);
		frame.add(getImportButton());
		frame.add(getPathLabel());
	}

	private void setFrameProperties(JFrame frame) {
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

	private void setListenerForImportButton(JButton button) {
		button.addActionListener(e -> {
			try {
				importAndSaveFileInRuntime();
				label.setText("Chosen file: " + path.getParent() + "\\" + path.getFileName());
				label.setToolTipText(label.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	private JLabel getPathLabel() {
		label = new JLabel();
		label.setBounds(20, 0, 380, 50);
		label.setText("Chosen file:");
		return label;
	}

	private void importAndSaveFileInRuntime() throws IOException {
		fileDialog = new FileDialog(frame, "Choose file", FileDialog.LOAD);
		fileDialog.setVisible(true);
		if (Objects.nonNull(fileDialog.getFile())) {
			path = Paths.get(fileDialog.getDirectory() + fileDialog.getFile());
			FileReader.readFile(Files.readAllLines(path, StandardCharsets.UTF_8));
		}
	}
}
