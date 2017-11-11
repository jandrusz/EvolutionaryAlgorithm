package com.pstkm.dtos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.google.common.collect.Maps;

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
    private Integer nonZeroSomething;

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
        Map<PointDTO, Integer> gene = Maps.newHashMap();
        for (Map.Entry<PointDTO, Integer> entry : mapOfValues.entrySet()) {
            if (entry.getKey().getDemandId().equals(geneId)) {
                gene.put(entry.getKey(), entry.getValue());
            }
        }
        return gene;
    }

    public Integer getNumberOfGenes() {
        return mapOfValues.entrySet().stream()
                .map(entry -> entry.getKey().getDemandId())
                .collect(Collectors.toSet())
                .size();
    }
}

