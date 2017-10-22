package com.pstkm.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathDTO {

	private Integer pathNumber;
	private List<Integer> edges;
}
