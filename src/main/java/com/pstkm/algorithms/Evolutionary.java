package com.pstkm.algorithms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.RoutingSolutionDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Evolutionary extends Algorithm {

    private RoutingSolutionDTO bestSolution;

    public Evolutionary(FileDTO fileDTO) {
        setFile(fileDTO);
    }

    public RoutingSolutionDTO computeDDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfGenerations) {
        Float finalCost = Float.MAX_VALUE;
        Float cost = 0F;
        Integer indexOfBestRoutingSolution = 0;

        for (int generation = 0; generation < numberOfGenerations; generation++) {
            for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
                List<Integer> costsOfLinks = allAcceptableRoutingSolutions.get(i).getCosts();
                for (int j = 0; j < allAcceptableRoutingSolutions.get(i).getCosts().size(); j++) {
                    cost += file.getLinks().get(j).getFibrePairCost() * costsOfLinks.get(j);
                }
                allAcceptableRoutingSolutions.get(i).setFinalCost(cost);
                if (cost < finalCost) {
                    finalCost = cost;
                    indexOfBestRoutingSolution = i;
                }
                cost = 0F;
            }
            System.out.println("DDAP minimum cost: " + finalCost);
            bestSolution = allAcceptableRoutingSolutions.get(indexOfBestRoutingSolution);
            indexOfBestRoutingSolution = 0;
            finalCost = Float.MAX_VALUE;

            if (generation != 0) {
                allAcceptableRoutingSolutions = allAcceptableRoutingSolutions.stream()
                        .sorted(Comparator.comparing(RoutingSolutionDTO::getFinalCost))
                        .collect(Collectors.toList())
                        .subList(0, ((int) (allAcceptableRoutingSolutions.size() * 0.97)));
            }
            allAcceptableRoutingSolutions = crossover(allAcceptableRoutingSolutions, allAcceptableRoutingSolutions.size());
        }
        return bestSolution;
    }

    private List<RoutingSolutionDTO> crossover(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, Integer numberOfChromosomes) {
        Random random = new Random();
        List<RoutingSolutionDTO> children = Lists.newArrayList();

        Random random2 = new Random();

        List<Integer> list = Lists.newArrayList();
        while (list.size() <= numberOfChromosomes) {
            Integer rand2 = random2.nextInt(numberOfChromosomes);
            list.add(rand2);
        }

        for (int i = 0; i < allAcceptableRoutingSolutions.size() / 2; i++) {
            children.addAll(produceOffspring(allAcceptableRoutingSolutions.get(list.remove(0)), allAcceptableRoutingSolutions.get(list.remove(1)), random));
        }

        List<List<Integer>> costs = computeCostsOfAllRoutingSolutions(children);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).setCosts(costs.get(i));
        }

        return children;
    }

    public List<RoutingSolutionDTO> produceOffspring(RoutingSolutionDTO firstParent, RoutingSolutionDTO secondParent, Random random) {
        List<RoutingSolutionDTO> children = Lists.newArrayList(new RoutingSolutionDTO(Maps.newHashMap()), new RoutingSolutionDTO(Maps.newHashMap()));

        for (int i = 0; i < firstParent.getNumberOfChromosomes(); i++) {
            Double rand = (double) random.nextInt(100) / 100;
            if (rand > 0.5) {
                children.get(0).getMapOfValues().putAll(firstParent.getChromosome(i + 1));
                children.get(1).getMapOfValues().putAll(secondParent.getChromosome(i + 1));
            } else {
                children.get(1).getMapOfValues().putAll(firstParent.getChromosome(i + 1));
                children.get(0).getMapOfValues().putAll(secondParent.getChromosome(i + 1));
            }
        }

        return children;
    }

//    public RoutingSolutionDTO computeDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, List<List<Integer>> costs) { //TODO
//        double max = Double.MIN_VALUE;
//        for (int i = 0; i < costs.size(); i++) {
//            for (int j = 0; j < costs.get(i).size(); j++) {
//                max = Math.max(0, costs.get(i).get(j) - file.getLinks().get(j).getNumberOfFibrePairsInCable());
//            }
//            if (max == 0) {
//                return allAcceptableRoutingSolutions.get(i);
//            }
//        }
//        return null;
//    }

}