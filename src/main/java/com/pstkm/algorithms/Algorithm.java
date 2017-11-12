package com.pstkm.algorithms;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pstkm.dtos.DemandDTO;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.PathDTO;
import com.pstkm.dtos.PointDTO;
import com.pstkm.dtos.RoutingSolutionDTO;
import com.pstkm.util.Utils;

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
        List<List<Integer>> combinations = getCombinations(demand.getDemandVolume(), demand.getNumberOfPaths());
        for (int i = 0; i < numberOfCombinations; i++) {
            list.add(getMapOfValuesForOneDemand(demand, i, combinations));
        }
        return list;
    }

    private RoutingSolutionDTO getMapOfValuesForOneDemand(DemandDTO demand, Integer combinationIndex, List<List<Integer>> combinations) {
        Map<PointDTO, Integer> mapOfValuesForOneDemand = Maps.newHashMap();
        for (int j = 0; j < demand.getNumberOfPaths(); j++) {
            Integer pathId = demand.getPaths().get(j).getPathId();
            mapOfValuesForOneDemand.put(new PointDTO(demand.getDemandId(), pathId), combinations.get(combinationIndex).get(pathId - 1));
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

    private List<RoutingSolutionDTO> getAllAcceptableRoutingSolutions() {
        List<List<RoutingSolutionDTO>> allCombinations = getAllCombinations();
        List<List<Integer>> combinationOfIndexes = Lists.cartesianProduct(fillListsWithIndexes(allCombinations));

        return combinationOfIndexes.stream()
                .map(indexes -> getOneAcceptableRoutingSolution(allCombinations, indexes))
                .collect(Collectors.toList());
    }

    private List<RoutingSolutionDTO> getRandomAcceptableRoutingSolutions(Integer numberOfChromosomes, Long seed) {
        List<List<RoutingSolutionDTO>> allCombinations = getAllCombinations();
        List<RoutingSolutionDTO> listOfRandomChromosomes = Lists.newArrayList();

        Random random = new Random(seed);
        for (int i = 0; i < numberOfChromosomes; i++) {
            RoutingSolutionDTO chromosome = new RoutingSolutionDTO(Maps.newHashMap());
            for (int j = 0; j < allCombinations.size(); j++) {
                chromosome.getMapOfValues().putAll(allCombinations.get(j).get(random.nextInt(allCombinations.get(j).size())).getMapOfValues());
            }
            listOfRandomChromosomes.add(chromosome);
        }

        return listOfRandomChromosomes;
    }

    private RoutingSolutionDTO getOneAcceptableRoutingSolution(List<List<RoutingSolutionDTO>> combinations, List<Integer> indexes) {
        RoutingSolutionDTO routingSolutionDTO = new RoutingSolutionDTO(Maps.newHashMap());
        for (int i = 0; i < combinations.size(); i++) {
            routingSolutionDTO.getMapOfValues().putAll(combinations.get(i).get(indexes.get(i)).getMapOfValues());
        }
        return routingSolutionDTO;
    }

    public List<RoutingSolutionDTO> getAllAcceptableRoutingSolutionsWithLinksCapacities() {
        List<RoutingSolutionDTO> allAcceptableRoutingSolutions = getAllAcceptableRoutingSolutions();
        List<List<Integer>> linksCapacities = computeLinksCapacitiesOfAllRoutingSolutions(allAcceptableRoutingSolutions);
        for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
            allAcceptableRoutingSolutions.get(i).setLinksCapacities(linksCapacities.get(i));
        }
        return allAcceptableRoutingSolutions;
    }

    public List<RoutingSolutionDTO> getNRandomAcceptableRoutingSolutionsWithLinksCapacities(Integer numberOfChromosomes, Long seed) {
        List<RoutingSolutionDTO> allAcceptableRoutingSolutions = getRandomAcceptableRoutingSolutions(numberOfChromosomes, seed);
        List<List<Integer>> linksCapacities = computeLinksCapacitiesOfAllRoutingSolutions(allAcceptableRoutingSolutions);
        Random random = new Random(seed);
        List<RoutingSolutionDTO> list = Lists.newArrayList();

        for (int i = 0; i < numberOfChromosomes; i++) {
            Integer rand = random.nextInt(allAcceptableRoutingSolutions.size());
            allAcceptableRoutingSolutions.get(rand).setLinksCapacities(linksCapacities.get(rand));
            list.add(allAcceptableRoutingSolutions.get(rand));
        }
        return list;
    }

    public List<List<Integer>> computeLinksCapacitiesOfAllRoutingSolutions(List<RoutingSolutionDTO> listOfRoutingSolutions) {
        return listOfRoutingSolutions.stream()
                .map(this::computeLinksCapacitiesOfOneRoutingSolution)
                .collect(Collectors.toList());
    }

    private List<Integer> computeLinksCapacitiesOfOneRoutingSolution(RoutingSolutionDTO routingSolutionDTO) {
        List<Integer> linksCapacities = Utils.prepareEmptyListWithNZeroElements(file.getLinks().size());

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
            linksCapacities.set(j, (int) Math.ceil(sum / (double) file.getLinks().get(j).getNumberOfLambdasInFibre()));
        }
        return linksCapacities;
    }

}
