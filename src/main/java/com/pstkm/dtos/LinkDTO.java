package com.pstkm.dtos;

import lombok.Builder;

@Builder
public class LinkDTO {

	private Integer startNode;
	private Integer endNode;
	private Integer numberOfFibrePairsInCable;
	private Float fibrePairCost;
	private Integer numberOfLambdasInFibre;
}
