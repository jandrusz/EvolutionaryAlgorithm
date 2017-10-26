package com.pstkm.bruteForce;

import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pstkm.dtos.DemandDTO;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.PointDTO;
import com.pstkm.dtos.RoutingSolutionDTO;

public class BruteForce {

	private FileDTO file;

	public List<List<RoutingSolutionDTO>> getAllCombinations(FileDTO fileDTO) {
		file = fileDTO;
		Map<PointDTO, Integer> mapOfValuesForOneDemand = Maps.newHashMap();
		List<List<RoutingSolutionDTO>> outerList = Lists.newArrayList();
		List<RoutingSolutionDTO> innerList = Lists.newArrayList();

		for (DemandDTO demand : file.getDemands()) {
			Integer numberOfCombinations = newtonSymbol(demand.getNumberOfPaths() + demand.getDemandVolume() - 1, demand.getDemandVolume());
			Integer demandId = demand.getDemandId();
			for (int i = 0; i < numberOfCombinations; i++) {
				for (int j = 0; j < demand.getNumberOfPaths(); j++) {
					Integer pathId = demand.getPaths().get(j).getPathId();
					mapOfValuesForOneDemand.put(new PointDTO(demandId, pathId), getCombinations(demand.getDemandVolume(), demand.getNumberOfPaths()).get(i)[pathId - 1]);
				}
				innerList.add(new RoutingSolutionDTO(mapOfValuesForOneDemand));
				mapOfValuesForOneDemand = Maps.newHashMap();
			}
			outerList.add(innerList);
			innerList = Lists.newArrayList();
		}
		return outerList;
	}

	public List<RoutingSolutionDTO> getAllRoutingSolutions(List<List<RoutingSolutionDTO>> combinations) {
		List<RoutingSolutionDTO> listOfRoutingSolutions = Lists.newArrayList(); //TODO find all permutations
		return listOfRoutingSolutions;
	}

	private static List<int[]> getCombinations(int targetSum, int numberOfElements) {
		List<int[]> results = Lists.newArrayList();
		for (int i = 0; i < numberOfElements; i++) {
			int tmpTargetSum = targetSum;
			for (int s = tmpTargetSum; tmpTargetSum > 0; tmpTargetSum--) {
				int[] tmpTable = new int[numberOfElements];
				int t = 0;
				for (t = 0; t < i; t++) {
					tmpTable[t] = 0;
				}

				tmpTable[t] = tmpTargetSum;
				t++;

				if (i == numberOfElements - 1) {
					tmpTargetSum = 0;
					results.add(tmpTable);

				} else {
					int diff = targetSum - tmpTargetSum;
					if (diff == 0) {
						for (int j = 1; j < numberOfElements - 1; j++) {
							tmpTable[t] = 0;
						}
						t++;
						results.add(tmpTable);
					} else {
						List<int[]> tmpResults = getCombinations(diff, numberOfElements - 1 - i);
						for (int k = 0; k < tmpResults.size(); k++) {
							int[] tmp3 = tmpResults.get(k);
							int p = t;
							for (int l = 0; l < tmp3.length; l++) {
								tmpTable[p] = tmp3[l];
								p++;
							}
							int[] tableToAdd = new int[tmpTable.length];
							for (int q = 0; q < tmpTable.length; q++) {
								tableToAdd[q] = tmpTable[q];
							}
							results.add(tableToAdd);
						}
					}
				}
			}
		}
		return results;
	}

	private Integer newtonSymbol(Integer n, Integer k) {
		Integer result = 1;
		for (int i = 1; i <= k; i++)
			result = result * (n - i + 1) / i;
		return result;
	}
}
