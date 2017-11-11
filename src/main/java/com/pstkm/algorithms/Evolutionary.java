package com.pstkm.algorithms;

import static com.pstkm.util.Utils.prepareEmptyListWithSequenceElements;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pstkm.MainWindow;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.PointDTO;
import com.pstkm.dtos.RoutingSolutionDTO;
import com.pstkm.util.Stopwatch;

public class Evolutionary extends Algorithm {

    private Integer numberOfMutations;

    public Evolutionary(FileDTO fileDTO) {
        setFile(fileDTO);
    }

    public RoutingSolutionDTO computeDDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfGenerations, Long seed, Double probabilityOfCrossover, Double probabilityOfMutation, Integer numberOfSolutions, Double maxTime, Integer maxNumberOfMutations, Double percentOfBestChromosomes) {
        Float finalCost = Float.MAX_VALUE;
        Float cost = 0F;
        Integer indexOfBestRoutingSolution = 0;
        List<RoutingSolutionDTO> bestSolutions = Lists.newArrayList();
        RoutingSolutionDTO bestSolution;
        numberOfMutations = 0;

        for (int generation = 0; generation < numberOfGenerations; generation++) {
            for (int indexOfRoutingSolution = 0; indexOfRoutingSolution < allAcceptableRoutingSolutions.size(); indexOfRoutingSolution++) {
                List<Integer> costsOfLinks = allAcceptableRoutingSolutions.get(indexOfRoutingSolution).getCosts();
                for (int j = 0; j < allAcceptableRoutingSolutions.get(indexOfRoutingSolution).getCosts().size(); j++) {
                    cost += file.getLinks().get(j).getFibrePairCost() * costsOfLinks.get(j);
                }
                allAcceptableRoutingSolutions.get(indexOfRoutingSolution).setFinalCost(cost);
                if (cost < finalCost) {
                    finalCost = cost;
                    indexOfBestRoutingSolution = indexOfRoutingSolution;
                }
                cost = 0F;
            }

            if (numberOfMutations >= maxNumberOfMutations) {
                MainWindow.textArea.append("Exceeded maximum number of mutations.");
                break;
            }
            if (Stopwatch.getTime() >= maxTime) {
                MainWindow.textArea.append("Exceeded maximum time.");
                break;
            }

            MainWindow.textArea.append(Stopwatch.getTimeText() + " Minimum cost of " + (generation + 1) + " generation: " + finalCost + "\n");
            bestSolution = allAcceptableRoutingSolutions.get(indexOfBestRoutingSolution);
            bestSolutions.add(bestSolution);
            if (checkIfLastNSolutionsWasWorseOrEqual(bestSolutions, numberOfSolutions)) {
                break;
            }
            indexOfBestRoutingSolution = 0;
            finalCost = Float.MAX_VALUE;

            if (generation != 0) {
                allAcceptableRoutingSolutions = getBestSetOfAcceptableRoutingSolutionsForDDAP(allAcceptableRoutingSolutions, percentOfBestChromosomes);
            }
            allAcceptableRoutingSolutions = crossover(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfCrossover);
            allAcceptableRoutingSolutions = mutation(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfMutation);
            allAcceptableRoutingSolutions = addCostsToRoutingSolutions(allAcceptableRoutingSolutions);

        }
        return getBestSolution(bestSolutions);
    }

    private List<RoutingSolutionDTO> getBestSetOfAcceptableRoutingSolutionsForDDAP(List<RoutingSolutionDTO> routingSolutions, Double percent) {
        Integer size = routingSolutions.size();
        List<RoutingSolutionDTO> list = routingSolutions.stream()
                .sorted(Comparator.comparing(RoutingSolutionDTO::getFinalCost))
                .collect(Collectors.toList())
                .subList(0, ((int) (routingSolutions.size() * (percent / 100.0))));

        list.addAll(list.subList(0, size - (int) (routingSolutions.size() * (percent / 100.0))));
        return list;
    }

    private boolean checkIfLastNSolutionsWasWorseOrEqual(List<RoutingSolutionDTO> bestSolutions, Integer numberOfSolutions) {
        int counter = 0;
        RoutingSolutionDTO best = Iterables.getFirst(bestSolutions, null);
        for (int i = 1; i < bestSolutions.size(); i++) {
            if (bestSolutions.get(i).getFinalCost() >= best.getFinalCost()) {
                counter++;
            } else {
                best = bestSolutions.get(i);
                counter = 0;
            }
        }
        return counter >= numberOfSolutions;
    }

    private RoutingSolutionDTO getBestSolution(List<RoutingSolutionDTO> bestSolutions) {
        RoutingSolutionDTO bestSolution = bestSolutions.get(0);
        for (RoutingSolutionDTO bs : bestSolutions) {
            if (bs.getFinalCost() <= bestSolution.getFinalCost()) {
                bestSolution = bs;
            }
        }
        return bestSolution;
    }

    private List<RoutingSolutionDTO> crossover(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfChromosomes, Long seed, Double probabilityOfCrossover) {
        List<RoutingSolutionDTO> children = Lists.newArrayList();
        Random random = new Random(seed);
        Random randomIndex = new Random(seed);

        List<Integer> listOfIndexes = prepareEmptyListWithSequenceElements(numberOfChromosomes);

        for (int i = 0; i < allAcceptableRoutingSolutions.size() / 2; i++) {
            children.addAll(produceOffspring(allAcceptableRoutingSolutions.get(listOfIndexes.remove(randomIndex.nextInt(listOfIndexes.size()))), allAcceptableRoutingSolutions.get(listOfIndexes.remove(randomIndex.nextInt(listOfIndexes.size()))), random, probabilityOfCrossover));
        }
        return children;
    }

    private List<RoutingSolutionDTO> addCostsToRoutingSolutions(List<RoutingSolutionDTO> solutions) {
        List<List<Integer>> costs = computeCostsOfAllRoutingSolutions(solutions);
        for (int i = 0; i < solutions.size(); i++) {
            solutions.get(i).setCosts(costs.get(i));
        }
        return solutions;
    }

    private List<RoutingSolutionDTO> mutation(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfChromosomes, Long seed, Double probabilityOfMutation) {
        List<RoutingSolutionDTO> afterMutationSolutions = Lists.newArrayList();
        Random random = new Random(seed);
        for (int i = 0; i < numberOfChromosomes; i++) {
            if ((double) random.nextInt(100) / 100 < probabilityOfMutation) {
                numberOfMutations++;
                Map<PointDTO, Integer> genes = Maps.newHashMap();
                for (int j = 0; j < allAcceptableRoutingSolutions.get(i).getNumberOfGenes(); j++) {
                    genes.putAll(mutateGene(allAcceptableRoutingSolutions.get(i).getGene(j + 1), seed));
                }
                afterMutationSolutions.add(new RoutingSolutionDTO(genes));
            } else {
                afterMutationSolutions.add(allAcceptableRoutingSolutions.get(i));
            }
        }
        return afterMutationSolutions;
    }

    private Map<PointDTO, Integer> mutateGene(Map<PointDTO, Integer> gene, Long seed) {

        Map<PointDTO, Integer> mutatedGene = Maps.newHashMap();
        List<PointDTO> points = Lists.newArrayList();
        List<Integer> values = Lists.newArrayList();
        for (Map.Entry<PointDTO, Integer> entry : gene.entrySet()) {
            points.add(entry.getKey());
            values.add(entry.getValue());
        }

        Random random = new Random(seed);

        for (int i = 0; i < values.size(); i++) {
            Integer index = random.nextInt(values.size());
            Integer index2 = random.nextInt(values.size());
            if (values.get(index) != 0) {
                values.set(index, values.get(index) - 1);
                values.set(index2, values.get(index2) + 1);
                break;
            }
        }

        for (int i = 0; i < gene.size(); i++) {
            mutatedGene.put(points.remove(0), values.remove(0));
        }
        return mutatedGene;
    }

    private List<RoutingSolutionDTO> produceOffspring(RoutingSolutionDTO firstParent, RoutingSolutionDTO secondParent, Random random, Double probabilityOfCrossover) {
        List<RoutingSolutionDTO> children;

        if ((double) random.nextInt(100) / 100 < probabilityOfCrossover) {
            children = Lists.newArrayList(new RoutingSolutionDTO(Maps.newHashMap()), new RoutingSolutionDTO(Maps.newHashMap()));
            for (int i = 0; i < firstParent.getNumberOfGenes(); i++) {
                Double rand = (double) random.nextInt(100) / 100;
                if (rand > 0.5) {
                    children.get(0).getMapOfValues().putAll(firstParent.getGene(i + 1));
                    children.get(1).getMapOfValues().putAll(secondParent.getGene(i + 1));
                } else {
                    children.get(1).getMapOfValues().putAll(firstParent.getGene(i + 1));
                    children.get(0).getMapOfValues().putAll(secondParent.getGene(i + 1));
                }
            }
            return children;
        }

        return Lists.newArrayList(firstParent, secondParent);
    }

    public RoutingSolutionDTO computeDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfGenerations, Long seed, Double probabilityOfCrossover, Double probabilityOfMutation, Double maxTime, Integer maxNumberOfMutations, Double percentOfBestChromosomes) {
        numberOfMutations = 0;
        for (int generation = 0; generation < numberOfGenerations; generation++) {
            for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
                List<Integer> maxValues = Lists.newArrayList();
                for (int j = 0; j < allAcceptableRoutingSolutions.get(i).getCosts().size(); j++) {
                    maxValues.add(Math.max(0, allAcceptableRoutingSolutions.get(i).getCosts().get(j) - file.getLinks().get(j).getNumberOfFibrePairsInCable()));
                }
                allAcceptableRoutingSolutions.get(i).setNonZeroSomething(maxValues.stream().filter(p -> p > 0).collect(Collectors.toList()).size());
                if (Collections.max(maxValues) == 0) {
                    return allAcceptableRoutingSolutions.get(i);
                }
            }

            if (numberOfMutations >= maxNumberOfMutations) {
                MainWindow.textArea.append("Exceeded maximum number of mutations.");
                break;
            }
            if (Stopwatch.getTime() >= maxTime) {
                MainWindow.textArea.append("Exceeded maximum time.");
                break;
            }

            if (generation != 0) {
                allAcceptableRoutingSolutions = getBestSetOfAcceptableRoutingSolutionsForDAP(allAcceptableRoutingSolutions, percentOfBestChromosomes);
            }
            allAcceptableRoutingSolutions = crossover(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfCrossover);
            allAcceptableRoutingSolutions = mutation(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfMutation);
            allAcceptableRoutingSolutions = addCostsToRoutingSolutions(allAcceptableRoutingSolutions);
        }
        return null;
    }

    private List<RoutingSolutionDTO> getBestSetOfAcceptableRoutingSolutionsForDAP(List<RoutingSolutionDTO> routingSolutions, Double percent) {
        Integer size = routingSolutions.size();
        List<RoutingSolutionDTO> list = routingSolutions.stream()
                .sorted(Comparator.comparing(RoutingSolutionDTO::getNonZeroSomething))
                .collect(Collectors.toList())
                .subList(0, ((int) (routingSolutions.size() * (percent / 100.0))));

        list.addAll(list.subList(0, size - (int) (routingSolutions.size() * (percent / 100.0))));
        return list;
    }

}