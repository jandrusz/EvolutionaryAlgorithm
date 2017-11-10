package com.pstkm.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class LinkDTO {

    private Integer startNode;
    private Integer endNode;
    private Integer numberOfFibrePairsInCable;
    private Float fibrePairCost;
    private Integer numberOfLambdasInFibre;
}
