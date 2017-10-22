package com.pstkm.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemandDTO {

	private Integer startNode;
	private Integer endNode;
	private Integer demandVolume;
	private Integer numberOfPaths;
	private List<PathDTO> paths;

}
