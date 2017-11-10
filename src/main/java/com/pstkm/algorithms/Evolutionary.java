package com.pstkm.algorithms;

import static com.pstkm.util.Utils.prepareEmptyListWithSequenceElements;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pstkm.MainWindow;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.RoutingSolutionDTO;

public class Evolutionary extends Algorithm {

    public Evolutionary(FileDTO fileDTO) {
        setFile(fileDTO);
    }

    public RoutingSolutionDTO computeDDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfGenerations, Long seed, Double probabilityOfCrossover, Integer numberOfSolutions) {
        Float finalCost = Float.MAX_VALUE;
        Float cost = 0F;
        Integer indexOfBestRoutingSolution = 0;
        List<RoutingSolutionDTO> bestSolutions = Lists.newArrayList();
        RoutingSolutionDTO bestSolution;

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
            MainWindow.textArea.append("DDAP minimum cost of " + (generation + 1) + " generation: " + finalCost + "\n");
            bestSolution = allAcceptableRoutingSolutions.get(indexOfBestRoutingSolution);
            bestSolutions.add(bestSolution);
            if (checkIfLastNSolutionsWasWorseOrEqual(bestSolutions, numberOfSolutions)) {
                break;
            }
            indexOfBestRoutingSolution = 0;
            finalCost = Float.MAX_VALUE;

            if (generation != 0) {
                allAcceptableRoutingSolutions = getBestSetOfAcceptableRoutingSolutionsForDDAP(allAcceptableRoutingSolutions, 97.0);
            }
            allAcceptableRoutingSolutions = crossover(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfCrossover);
//            allAcceptableRoutingSolutions = mutation(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfCrossover);
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

        List<List<Integer>> costs = computeCostsOfAllRoutingSolutions(children);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).setCosts(costs.get(i));
        }

        return children;
    }

    private List<RoutingSolutionDTO> mutation(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfChromosomes, Long seed, Double probabilityOfCrossover) {
        return Lists.newArrayList(); //TODO
    }

    private List<RoutingSolutionDTO> produceOffspring(RoutingSolutionDTO firstParent, RoutingSolutionDTO secondParent, Random random, Double probabilityOfCrossover) {
        List<RoutingSolutionDTO> children;

        if ((double) random.nextInt(100) / 100 < probabilityOfCrossover) {
            children = Lists.newArrayList(new RoutingSolutionDTO(Maps.newHashMap()), new RoutingSolutionDTO(Maps.newHashMap()));
            for (int i = 0; i < firstParent.getNumberOfChromosomes(); i++) {
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

    public RoutingSolutionDTO computeDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfGenerations, Long seed, Double probabilityOfCrossover, Integer numberOfSolutions) {
        for (int generation = 0; generation < numberOfGenerations; generation++) {
            for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
                List<Integer> maxValues = Lists.newArrayList();
                for (int j = 0; j < allAcceptableRoutingSolutions.get(i).getCosts().size(); j++) {
                    maxValues.add(Math.max(0, allAcceptableRoutingSolutions.get(i).getCosts().get(j) - file.getLinks().get(j).getNumberOfFibrePairsInCable()));
                }
                if (Collections.max(maxValues) == 0) {
                    return allAcceptableRoutingSolutions.get(i);
                }
            }

            if (generation != 0) {
                allAcceptableRoutingSolutions = getBestSetOfAcceptableRoutingSolutionsForDAP(allAcceptableRoutingSolutions, 97.0);
            }
            allAcceptableRoutingSolutions = crossover(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfCrossover);
//            allAcceptableRoutingSolutions = mutation(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size(), seed, probabilityOfCrossover); //TODO
        }
        return new RoutingSolutionDTO();
    }

    private List<RoutingSolutionDTO> getBestSetOfAcceptableRoutingSolutionsForDAP(List<RoutingSolutionDTO> routingSolutions, Double percent) {
        Integer size = routingSolutions.size();
        List<RoutingSolutionDTO> list = routingSolutions.stream()
//                .sorted(Comparator.comparing(RoutingSolutionDTO::getFinalCost)) //TODO
                .collect(Collectors.toList())
                .subList(0, ((int) (routingSolutions.size() * (percent / 100.0))));

        list.addAll(list.subList(0, size - (int) (routingSolutions.size() * (percent / 100.0))));
        return list;
    }

}