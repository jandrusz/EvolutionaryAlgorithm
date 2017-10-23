package com.pstkm.bruteForce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.pstkm.dtos.DemandDTO;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.PointDTO;
import com.pstkm.dtos.RoutingSolutionDTO;

public class BruteForce {

	private Map<PointDTO, Integer> map = new HashMap<>();
	private FileDTO file;
	private List<Integer> demandsVolumes = Lists.newArrayList(); //h(d)

	public RoutingSolutionDTO produceRoutingSolution(FileDTO fileDTO) {
		file = fileDTO;
		for (DemandDTO demand : file.getDemands()) {
			demandsVolumes.add(demand.getDemandVolume());
			for (int i = 0; i < demand.getNumberOfPaths(); i++) {
				map.put(new PointDTO(demand.getDemandNumber(), demand.getPaths().get(i).getPathNumber()), 0); //TODO how to split h(d) to sum of N non-negative numbers?
			}
		}
		return new RoutingSolutionDTO(map, demandsVolumes);
	}
}
