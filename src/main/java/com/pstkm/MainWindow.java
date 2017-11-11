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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import lombok.Getter;
import com.pstkm.algorithms.BruteForce;
import com.pstkm.algorithms.Evolutionary;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.RoutingSolutionDTO;
import com.pstkm.fileManagement.FileReader;
import com.pstkm.fileManagement.FileWriter;
import com.pstkm.util.Stopwatch;

@Getter
public class MainWindow {

    private JFrame frame;
    private FileReader fileReader;
    private FileDialog fileDialog;
    private JLabel label;
    private Path path;
    private FileDTO file;
    public static JTextArea textArea;
    private JTextField chromosomesNumberTextField;
    private JTextField mutationProbabilityTextField;
    private JTextField crossoverProbabilityTextField;
    private JTextField maxTimeTextField;
    private JTextField numberOfGenerationsTextField;
    private JTextField numberOfMutationsTextField;
    private JTextField numberOfContinuousNonBetterSolutionsTextField;
    private JTextField seedTextField;
    private JTextField percentOfBestChromosomesToMutationAndCrossover;

    MainWindow() {
        frame = new JFrame();
        fileReader = new FileReader();
        buildMainFrame();
    }

    private void buildMainFrame() {
        setFrame();
        frame.add(getImportButton());
        frame.add(getBruteForceLabel());
        frame.add(getBruteForceDDAPButton());
        frame.add(getBruteForceDAPButton());
        frame.add(getEvolutionaryLabel());
        frame.add(getEvolutionaryAlgorithmDDAPButton());
        frame.add(getEvolutionaryAlgorithmDAPButton());
        frame.add(getPathLabel());
        frame.add(getChromosomesNumberLabel());
        frame.add(getChromosomesNumberTextFields());
        frame.add(getMutationProbabilityLabel());
        frame.add(getMutationProbabilityTextField());
        frame.add(getCrossoverProbabilityLabel());
        frame.add(getCrossoverProbabilityTextField());
        frame.add(getEvolutionaryAlgorithmLogTextArea());
        frame.add(getScroll());
        frame.add(getStopCriteriaLabel());
        frame.add(getMaxTimeLabel());
        frame.add(getMaxTimeTextField());
        frame.add(getNumberOfGenerationsLabel());
        frame.add(getNumberOfGenerationsTextField());
        frame.add(getNumberOfMutationsLabel());
        frame.add(getNumberOfMutationsTextField());
        frame.add(getNumberOfContinuousNonBetterSolutionsLabel());
        frame.add(getNumberOfContinuousNonBetterSolutionsTextField());
        frame.add(getSeedLabel());
        frame.add(getSeedTextField());
        frame.add(getPercentOfBestChromosomesToMutationAndCrossoverLabel());
        frame.add(getPercentOfBestChromosomesToMutationAndCrossoverTextField());
    }

    private JLabel getChromosomesNumberLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 160, 180, 20);
        label.setText("Number of chromosomes:");
        return label;
    }

    private JTextField getChromosomesNumberTextFields() {
        chromosomesNumberTextField = new JTextField();
        chromosomesNumberTextField.setBounds(240, 160, 120, 20);
        chromosomesNumberTextField.setText("5000");
        return chromosomesNumberTextField;
    }

    private JLabel getMutationProbabilityLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 180, 180, 20);
        label.setText("Mutation probability [0-1]:");
        return label;
    }

    private JTextField getMutationProbabilityTextField() {
        mutationProbabilityTextField = new JTextField();
        mutationProbabilityTextField.setBounds(240, 180, 120, 20);
        mutationProbabilityTextField.setText("0.50");
        return mutationProbabilityTextField;
    }

    private JLabel getCrossoverProbabilityLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 200, 180, 20);
        label.setText("Crossover probability [0-1]:");
        return label;
    }

    private JTextField getCrossoverProbabilityTextField() {
        crossoverProbabilityTextField = new JTextField();
        crossoverProbabilityTextField.setBounds(240, 200, 120, 20);
        crossoverProbabilityTextField.setText("0.50");
        return crossoverProbabilityTextField;
    }

    private JTextArea getEvolutionaryAlgorithmLogTextArea() {
        textArea = new JTextArea();
        textArea.setBounds(400, 20, 380, 380);
        textArea.setEnabled(false);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textArea.setDisabledTextColor(Color.BLACK);
        return textArea;
    }

    private JScrollPane getScroll() {
        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(textArea.getBounds());
        return scroll;
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
        label.setText("Max time [s]:");
        return label;
    }

    private JTextField getMaxTimeTextField() {
        maxTimeTextField = new JTextField();
        maxTimeTextField.setBounds(240, 240, 120, 20);
        maxTimeTextField.setText("60");
        return maxTimeTextField;
    }

    private JLabel getNumberOfGenerationsLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 260, 180, 20);
        label.setText("Number of generations:");
        return label;
    }

    private JTextField getNumberOfGenerationsTextField() {
        numberOfGenerationsTextField = new JTextField();
        numberOfGenerationsTextField.setBounds(240, 260, 120, 20);
        numberOfGenerationsTextField.setText("100");
        return numberOfGenerationsTextField;
    }

    private JLabel getNumberOfMutationsLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 280, 180, 20);
        label.setText("Number of mutations:");
        return label;
    }

    private JTextField getNumberOfMutationsTextField() {
        numberOfMutationsTextField = new JTextField();
        numberOfMutationsTextField.setBounds(240, 280, 120, 20);
        numberOfMutationsTextField.setText("1000000");
        return numberOfMutationsTextField;
    }

    private JLabel getNumberOfContinuousNonBetterSolutionsLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 300, 260, 20);
        label.setText("Number of continuous non better solutions:");
        return label;
    }

    private JTextField getNumberOfContinuousNonBetterSolutionsTextField() {
        numberOfContinuousNonBetterSolutionsTextField = new JTextField();
        numberOfContinuousNonBetterSolutionsTextField.setBounds(300, 300, 60, 20);
        numberOfContinuousNonBetterSolutionsTextField.setText("5");
        return numberOfContinuousNonBetterSolutionsTextField;
    }

    private JLabel getSeedLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 380, 180, 20);
        label.setText("Seed:");
        return label;
    }

    private JTextField getSeedTextField() {
        seedTextField = new JTextField();
        seedTextField.setBounds(240, 380, 120, 20);
        seedTextField.setText("123");
        return seedTextField;
    }

    private JLabel getPercentOfBestChromosomesToMutationAndCrossoverLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 360, 280, 20);
        label.setText("Chromosomes to mutation/crossover [%]:");
        return label;
    }

    private JTextField getPercentOfBestChromosomesToMutationAndCrossoverTextField() {
        percentOfBestChromosomesToMutationAndCrossover = new JTextField();
        percentOfBestChromosomesToMutationAndCrossover.setBounds(300, 360, 60, 20);
        percentOfBestChromosomesToMutationAndCrossover.setText("70");
        return percentOfBestChromosomesToMutationAndCrossover;
    }

    private void setFrame() {
        frame.setTitle("Evolutionary algorithm");
        frame.setBounds(400, 200, 800, 440);
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

    private JLabel getBruteForceLabel() {
        JLabel label = new JLabel();
        label.setBounds(20, 100, 180, 20);
        label.setText("Brute force algorithm");
        return label;
    }

    private JButton getBruteForceDDAPButton() {
        JButton button = new JButton("DDAP");
        button.setForeground(Color.BLACK);
        button.setBackground(SystemColor.menu);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBounds(20, 130, 80, 20);
        setListenerForBruteForceDDAPButton(button);
        return button;
    }

    private JButton getBruteForceDAPButton() {
        JButton button = new JButton("DAP");
        button.setForeground(Color.BLACK);
        button.setBackground(SystemColor.menu);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBounds(105, 130, 80, 20);
        setListenerForBruteForceDAPButton(button);
        return button;
    }

    private JLabel getEvolutionaryLabel() {
        JLabel label = new JLabel();
        label.setBounds(200, 100, 180, 20);
        label.setText("Evolutionary algorithm");
        return label;
    }

    private JButton getEvolutionaryAlgorithmDDAPButton() {
        JButton button = new JButton("DDAP");
        button.setForeground(Color.BLACK);
        button.setBackground(SystemColor.menu);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBounds(200, 130, 80, 20);
        setListenerForEvolutionaryAlgorithmButtonDDAP(button);
        return button;
    }


    private JButton getEvolutionaryAlgorithmDAPButton() {
        JButton button = new JButton("DAP");
        button.setForeground(Color.BLACK);
        button.setBackground(SystemColor.menu);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBounds(285, 130, 80, 20);
        setListenerForEvolutionaryAlgorithmButtonDAP(button);
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

    private void importAndLoadFile() throws IOException {
        fileDialog = new FileDialog(frame, "Choose file", FileDialog.LOAD);
        fileDialog.setVisible(true);
        if (Objects.nonNull(fileDialog.getFile())) {
            path = Paths.get(fileDialog.getDirectory() + fileDialog.getFile());
            file = fileReader.readFilePipeline(Files.readAllLines(path, StandardCharsets.UTF_8));
        }
    }

    private void setListenerForBruteForceDDAPButton(JButton button) {
        button.addActionListener(e -> {
            new Thread(() -> {
                textArea.setText("Started brute force DDAP\n");
                BruteForce bruteForce = new BruteForce(file);
                List<RoutingSolutionDTO> allAcceptableRoutingSolutions = bruteForce.getAllAcceptableRoutingSolutionsWithCosts();
                RoutingSolutionDTO routingSolutionDDAP = bruteForce.computeDDAP(allAcceptableRoutingSolutions);
                new FileWriter().writeFile("output_BF_DDAP", routingSolutionDDAP, bruteForce.getFile());
            }).start();
        });
    }

    private void setListenerForBruteForceDAPButton(JButton button) {
        button.addActionListener(e -> {
            new Thread(() -> {
                textArea.setText("Started brute force DAP\n");
                BruteForce bruteForce = new BruteForce(file);
                List<RoutingSolutionDTO> allAcceptableRoutingSolutions = bruteForce.getAllAcceptableRoutingSolutionsWithCosts();
                RoutingSolutionDTO routingSolutionDAP = bruteForce.computeDAP(allAcceptableRoutingSolutions);
                new FileWriter().writeFile("output_BF_DAP", routingSolutionDAP, bruteForce.getFile());
            }).start();
        });
    }

    private void setListenerForEvolutionaryAlgorithmButtonDDAP(JButton button) {
        button.addActionListener(e -> {
            new Thread(() -> {
                textArea.setText("Started evolutionary algorithm DDAP\n");
                Stopwatch.reset();
                Stopwatch.start();

                Integer numberOfGenerations = Integer.valueOf(numberOfGenerationsTextField.getText());
                Long seed = Long.valueOf(seedTextField.getText());
                Double crossoverProbability = Double.valueOf(crossoverProbabilityTextField.getText());
                Double mutationProbability = Double.valueOf(mutationProbabilityTextField.getText());
                Integer numberOfContinuousNonBetterSolutions = Integer.valueOf(numberOfContinuousNonBetterSolutionsTextField.getText());
                Integer numberOfChromosomes = Integer.valueOf(chromosomesNumberTextField.getText());
                Double maxTime = Double.valueOf(maxTimeTextField.getText());
                Integer maxNumberOfMutations = Integer.valueOf(numberOfMutationsTextField.getText());
                Double percentOfBestChromosomes = Double.valueOf(percentOfBestChromosomesToMutationAndCrossover.getText());

                Evolutionary evolutionaryAlgorithm = new Evolutionary(file);
                List<RoutingSolutionDTO> allAcceptableRoutingSolutions = evolutionaryAlgorithm.getNRandomAcceptableRoutingSolutionsWithCosts(numberOfChromosomes, seed);
                textArea.append("Initialized start population in " + Stopwatch.getTimeText() + "\n");

                RoutingSolutionDTO routingSolutionDDAP = evolutionaryAlgorithm.computeDDAP(allAcceptableRoutingSolutions, numberOfGenerations, seed, crossoverProbability, mutationProbability, numberOfContinuousNonBetterSolutions, maxTime, maxNumberOfMutations, percentOfBestChromosomes);
                new FileWriter().writeFile("output_EA_DDAP", routingSolutionDDAP, evolutionaryAlgorithm.getFile());

            }).start();
        });
    }

    private void setListenerForEvolutionaryAlgorithmButtonDAP(JButton button) {
        button.addActionListener(e -> {
            new Thread(() -> {
                textArea.setText("Started evolutionary algorithm DAP\n");
                Stopwatch.reset();
                Stopwatch.start();

                Integer numberOfGenerations = Integer.valueOf(numberOfGenerationsTextField.getText());
                Long seed = Long.valueOf(seedTextField.getText());
                Double crossoverProbability = Double.valueOf(crossoverProbabilityTextField.getText());
                Double mutationProbability = Double.valueOf(mutationProbabilityTextField.getText());
                Integer numberOfChromosomes = Integer.valueOf(chromosomesNumberTextField.getText());
                Double maxTime = Double.valueOf(maxTimeTextField.getText());
                Integer maxNumberOfMutations = Integer.valueOf(numberOfMutationsTextField.getText());
                Double percentOfBestChromosomes = Double.valueOf(percentOfBestChromosomesToMutationAndCrossover.getText());

                Evolutionary evolutionaryAlgorithm = new Evolutionary(file);
                List<RoutingSolutionDTO> allAcceptableRoutingSolutions = evolutionaryAlgorithm.getNRandomAcceptableRoutingSolutionsWithCosts(numberOfChromosomes, seed);
                textArea.append("Initialized start population in " + Stopwatch.getTimeText() + "\n");
                RoutingSolutionDTO routingSolutionDAP = evolutionaryAlgorithm.computeDAP(allAcceptableRoutingSolutions, numberOfGenerations, seed, crossoverProbability, mutationProbability, maxTime, maxNumberOfMutations, percentOfBestChromosomes);
                new FileWriter().writeFile("output_EA_DAP", routingSolutionDAP, evolutionaryAlgorithm.getFile());
            }).start();
        });
    }
}
