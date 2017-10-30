package com.pstkm.algorithms;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Setter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pstkm.dtos.DemandDTO;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.PathDTO;
import com.pstkm.dtos.PointDTO;
import com.pstkm.dtos.RoutingSolutionDTO;
import com.pstkm.util.Utils;

@Setter
public class BruteForce {

	private FileDTO file;

	public BruteForce(FileDTO fileDTO) {
		setFile(fileDTO);
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

	public RoutingSolutionDTO computeDDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, List<List<Integer>> costs) {
		Float finalCost = Float.MAX_VALUE;
		Float cost = 0F;
		Integer indexOfBestRoutingSolution = 0;

		for (int i = 0; i < costs.size(); i++) {
			for (int j = 0; j < costs.get(i).size(); j++) {
				cost += file.getLinks().get(j).getFibrePairCost() * costs.get(i).get(j);
			}
			if (cost < finalCost) {
				finalCost = cost;
				indexOfBestRoutingSolution = i;
			}
			cost = 0F;
		}
		System.out.println("DDAP minimum cost: " + finalCost);
		return allAcceptableRoutingSolutions.get(indexOfBestRoutingSolution);
	}

	public RoutingSolutionDTO computeDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions, List<List<Integer>> costs) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < costs.size(); i++) {
			for (int j = 0; j < costs.get(i).size(); j++) {
				max = Math.max(0, costs.get(i).get(j) - file.getLinks().get(j).getNumberOfFibrePairsInCable());
			}
			if (max == 0) {
				return allAcceptableRoutingSolutions.get(i);
			}
		}
		return null;
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
}
