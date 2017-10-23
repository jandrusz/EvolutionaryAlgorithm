package com.pstkm.dtos;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoutingSolutionDTO {

	private Map<PointDTO, Integer> map;
	private List<Integer> demandsVolumes;
}
