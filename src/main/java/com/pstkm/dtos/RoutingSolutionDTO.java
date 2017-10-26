package com.pstkm.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoutingSolutionDTO { //TODO find better name

	private Map<PointDTO, Integer> mapOfValues;

}

