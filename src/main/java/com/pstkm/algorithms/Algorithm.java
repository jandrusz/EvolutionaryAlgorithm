package com.pstkm.algorithms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pstkm.dtos.DemandDTO;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.PathDTO;
import com.pstkm.dtos.PointDTO;
import com.pstkm.dtos.RoutingSolutionDTO;
import com.pstkm.util.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
@Setter
public class Algorithm {

    protected FileDTO file;

    private List<List<Integer>> fillListsWithIndexes(List<List<RoutingSolutionDTO>> combinations) {
        return combinations.stream()
                .map(combination -> fillOneListWithSuccessiveIndexes(combination.size()))
                .collect(Collectors.toList());
    }

    private List<Integer> fillOneListWithSuccessiveIndexes(Integer size) {
        List<Integer> oneCombination = Lists.newArrayList();
        for (int j = 0; j < size; j++) {
            oneCombination.add(j);
        }
        return oneCombination;
    }

    private List<List<RoutingSolutionDTO>> getAllCombinations() {
        return file.getDemands().stream()
                .map(this::getCombinationsOfOneDemand)
                .collect(Collectors.toList());
    }

    private List<RoutingSolutionDTO> getCombinationsOfOneDemand(DemandDTO demand) {
        List<RoutingSolutionDTO> list = Lists.newArrayList();
        Integer numberOfCombinations = Utils.newtonSymbol(demand.getNumberOfPaths() + demand.getDemandVolume() - 1, demand.getDemandVolume());
        for (int i = 0; i < numberOfCombinations; i++) {
            list.add(getMapOfValuesForOneDemand(demand, i));
        }
        return list;
    }

    private RoutingSolutionDTO getMapOfValuesForOneDemand(DemandDTO demand, Integer combinationIndex) {
        Map<PointDTO, Integer> mapOfValuesForOneDemand = Maps.newHashMap();
        for (int j = 0; j < demand.getNumberOfPaths(); j++) {
            Integer pathId = demand.getPaths().get(j).getPathId();
            mapOfValuesForOneDemand.put(new PointDTO(demand.getDemandId(), pathId), getCombinations(demand.getDemandVolume(), demand.getNumberOfPaths()).get(combinationIndex).get(pathId - 1));
        }
        return new RoutingSolutionDTO(mapOfValuesForOneDemand);
    }

    private List<List<Integer>> getCombinations(Integer sum, Integer numberOfElements) {
        List<List<Integer>> lists = Lists.newArrayList();
        List<Integer> list = Lists.newArrayList();

        for (int i = 0; i <= sum; i++) {
            list.add(i);
        }

        for (int i = 0; i < numberOfElements; i++) {
            lists.add(list);
        }

        return Lists.cartesianProduct(lists).stream()
                .filter(product -> sum.equals(product.stream().mapToInt(Integer::intValue).sum()))
                .collect(Collectors.toList());
    }

    public List<RoutingSolutionDTO> getAllAcceptableRoutingSolutions() {
        List<List<RoutingSolutionDTO>> allCombinations = getAllCombinations();
        List<List<Integer>> combinationOfIndexes = Lists.cartesianProduct(fillListsWithIndexes(allCombinations));

        return combinationOfIndexes.stream()
                .map(indexes -> getOneAcceptableRoutingSolution(allCombinations, indexes))
                .collect(Collectors.toList());
    }

    private RoutingSolutionDTO getOneAcceptableRoutingSolution(List<List<RoutingSolutionDTO>> combinations, List<Integer> indexes) {
        RoutingSolutionDTO routingSolutionDTO = new RoutingSolutionDTO(Maps.newHashMap());
        for (int i = 0; i < combinations.size(); i++) {
            routingSolutionDTO.getMapOfValues().putAll(combinations.get(i).get(indexes.get(i)).getMapOfValues());
        }
        return routingSolutionDTO;
    }

    public List<RoutingSolutionDTO> getAllAcceptableRoutingSolutionsWithCosts() {
        List<RoutingSolutionDTO> allAcceptableRoutingSolutions = getAllAcceptableRoutingSolutions();
        List<List<Integer>> costs = computeCostsOfAllRoutingSolutions(allAcceptableRoutingSolutions);
        for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
            allAcceptableRoutingSolutions.get(i).setCosts(costs.get(i));
        }
        return allAcceptableRoutingSolutions;
    }

    public List<RoutingSolutionDTO> getAllAcceptableRoutingSolutionsWithCosts(Integer numberOfChromosomes, Long seed) {
        List<RoutingSolutionDTO> allAcceptableRoutingSolutions = getAllAcceptableRoutingSolutions();
        List<List<Integer>> costs = computeCostsOfAllRoutingSolutions(allAcceptableRoutingSolutions);
        Random random = new Random(seed);
        List<RoutingSolutionDTO> list = Lists.newArrayList();

        for (int i = 0; i < numberOfChromosomes; i++) {
            Integer rand = random.nextInt(allAcceptableRoutingSolutions.size());
            allAcceptableRoutingSolutions.get(rand).setCosts(costs.get(rand));
            list.add(allAcceptableRoutingSolutions.get(rand));
        }
        return list;
    }

    public List<List<Integer>> computeCostsOfAllRoutingSolutions(List<RoutingSolutionDTO> listOfRoutingSolutions) {
        return listOfRoutingSolutions.stream()
                .map(this::computeCostOfOneRoutingSolution)
                .collect(Collectors.toList());
    }

    private List<Integer> computeCostOfOneRoutingSolution(RoutingSolutionDTO routingSolutionDTO) {
        List<Integer> cost = Utils.prepareEmptyListWithNZeroElements(file.getLinks().size());

        List<PathDTO> paths = Lists.newArrayList();
        for (DemandDTO demand : file.getDemands()) {
            paths.addAll(demand.getPaths());
        }

        for (int j = 0; j < file.getNumberOfLinks(); j++) {
            double sum = 0;
            for (PathDTO path : paths) {
                if (path.getEdges().contains(j + 1)) {
                    sum += routingSolutionDTO.getMapOfValues().get(new PointDTO(path.getDemandId(), path.getPathId()));
                }
            }
            cost.set(j, (int) Math.ceil(sum / (double) file.getLinks().get(j).getNumberOfLambdasInFibre()));
        }
        return cost;
    }

}
