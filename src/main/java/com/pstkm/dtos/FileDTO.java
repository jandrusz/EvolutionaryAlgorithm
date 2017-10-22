package com.pstkm.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {

	private Integer numberOfLinks;
	private List<LinkDTO> links;
	private Integer numberOfDemands;
	private List<DemandDTO> demands;

}
