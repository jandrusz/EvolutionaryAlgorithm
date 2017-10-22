package com.pstkm.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkDTO {

	private Integer startNode;
	private Integer endNode;
	private Integer numberOfFibrePairsInCable;
	private Float fibrePairCost;
	private Integer NumberOfLambdasInFibre;

}
