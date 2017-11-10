package com.pstkm.algorithms;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
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
        System.out.println("DDAP minimum cost: " + finalCost);
        return allAcceptableRoutingSolutions.get(indexOfBestRoutingSolution);
    }

    public RoutingSolutionDTO computeDAP(List<RoutingSolutionDTO> allAcceptableRoutingSolutions) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < allAcceptableRoutingSolutions.size(); i++) {
            for (int j = 0; j < allAcceptableRoutingSolutions.get(i).getCosts().size(); j++) {
                max = Math.max(0, allAcceptableRoutingSolutions.get(i).getCosts().get(j) - file.getLinks().get(j).getNumberOfFibrePairsInCable());
            }
            if (max == 0) {
                return allAcceptableRoutingSolutions.get(i);
            }
        }
        return null;
    }
}
