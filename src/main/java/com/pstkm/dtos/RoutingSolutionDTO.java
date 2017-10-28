package com.pstkm.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RoutingSolutionDTO {

	private Map<PointDTO, Integer> mapOfValues;

	@Override
	public String toString() {
		String result = "";
		for (Map.Entry<PointDTO, Integer> entry : mapOfValues.entrySet()) {
			result += "P(" + entry.getKey().getDemandId() + "," + entry.getKey().getPathId() + ") value:" + entry.getValue() + "\n";
		}
		return result;
	}

}

