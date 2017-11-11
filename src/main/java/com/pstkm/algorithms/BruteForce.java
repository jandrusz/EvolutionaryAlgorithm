package com.pstkm.algorithms;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import com.google.common.collect.Lists;
import com.pstkm.MainWindow;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.RoutingSolutionDTO;

@Setter
@Getter
public class BruteForce extends Algorithm {

    public BruteForce(FileDTO fileDTO) {
        setFile(fileDTO);
    }

    public RoutingSolutionDTO computeDDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions) {
        Float finalCost = Float.MAX_VALUE;
        Float cost = 0F;
        Integer indexOfBestRoutingSolution = 0;

        for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
            List<Integer> costsOfLinks = allAcceptableRoutingSolutions.get(i).getCosts();
            for (int j = 0; j < costsOfLinks.size(); j++) {
                cost += file.getLinks().get(j).getFibrePairCost() * costsOfLinks.get(j);
            }
            if (cost < finalCost) {
                finalCost = cost;
                indexOfBestRoutingSolution = i;
            }
            cost = 0F;
        }
        MainWindow.textArea.append("DDAP minimum cost: " + finalCost);
        return allAcceptableRoutingSolutions.get(indexOfBestRoutingSolution);
    }

    public RoutingSolutionDTO computeDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions) {
        for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
            List<Integer> maxValues = Lists.newArrayList();
            for (int j = 0; j < allAcceptableRoutingSolutions.get(i).getCosts().size(); j++) {
                maxValues.add(Math.max(0, allAcceptableRoutingSolutions.get(i).getCosts().get(j) - file.getLinks().get(j).getNumberOfFibrePairsInCable()));
            }
            allAcceptableRoutingSolutions.get(i).setNonZeroSomething(maxValues.stream().filter(p -> p > 0).collect(Collectors.toList()).size());
            if (Collections.max(maxValues) == 0) {
                return allAcceptableRoutingSolutions.get(i);
            }
        }
        return null;
    }
}
