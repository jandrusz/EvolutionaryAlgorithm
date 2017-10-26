package com.pstkm.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathDTO {

	private Integer pathId;
	private List<Integer> edges;
}
