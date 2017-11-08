package com.pstkm.dtos;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RoutingSolutionDTO {

    private Float finalCost;
    private Integer numberOfChromosomes;
    private Map<PointDTO, Integer> mapOfValues;
    private List<Integer> costs;

    public RoutingSolutionDTO(Map<PointDTO, Integer> mapOfValues) {
        this.mapOfValues = mapOfValues;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<PointDTO, Integer> entry : mapOfValues.entrySet()) {
            result += "P(" + entry.getKey().getDemandId() + "," + entry.getKey().getPathId() + ") value:" + entry.getValue() + "\n";
        }
        return result;
    }

    public Map<PointDTO, Integer> getGene(Integer geneId) {
        Map<PointDTO, Integer> chromosome = Maps.newHashMap();
        for (Map.Entry<PointDTO, Integer> entry : mapOfValues.entrySet()) {
            if (entry.getKey().getDemandId().equals(geneId)) {
                chromosome.put(entry.getKey(), entry.getValue());
            }
        }
        return chromosome;
    }

    public Integer getNumberOfChromosomes() {
        return mapOfValues.entrySet().stream()
                .map(entry -> entry.getKey().getDemandId())
                .collect(Collectors.toSet())
                .size();
    }
}

