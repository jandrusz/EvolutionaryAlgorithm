package com.pstkm.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemandDTO {

	private Integer demandNumber;
	private Integer startNode;
	private Integer endNode;
	private Integer demandVolume; //h(d)
	private Integer numberOfPaths;
	private List<PathDTO> paths;
}
