package com.pstkm.fileManagement;

import com.google.common.collect.Lists;
import com.pstkm.dtos.*;
import com.pstkm.util.Utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class FileWriter {

    private FileDTO file;

    public void writeFile(String fileName, RoutingSolutionDTO routingSolutionDTO, FileDTO file) {
        this.file = file;
        String text = printCostOfOneRoutingSolution(routingSolutionDTO);
        try (PrintWriter writer = new PrintWriter(fileName + ".txt", "UTF-8")) {
            writer.println(text);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String printCostOfOneRoutingSolution(RoutingSolutionDTO routingSolutionDTO) {
        List<Integer> cost = Utils.prepareEmptyListWithNZeroElements(file.getLinks().size());
        List<Integer> signals = Utils.prepareEmptyListWithNZeroElements(file.getLinks().size());

        List<PathDTO> paths = Lists.newArrayList();
        for (DemandDTO demand : file.getDemands()) {
            paths.addAll(demand.getPaths());
        }

        for (int j = 0; j < file.getNumberOfLinks(); j++) {
            double sum = 0;
            for (PathDTO path : paths) {
                if (path.getEdges().contains(j + 1)) {
                    sum += routingSolutionDTO.getMapOfValues().get(new PointDTO(path.getDemandId(), path.getPathId()));
                }
            }
            signals.set(j, (int) sum);
            cost.set(j, (int) Math.ceil(sum / (double) file.getLinks().get(j).getNumberOfLambdasInFibre()));
        }
        return getRoutingSolutionText(signals, cost, routingSolutionDTO);
    }

    private String getRoutingSolutionText(List<Integer> signals, List<Integer> cost, RoutingSolutionDTO routingSolutionDTO) {
        String routingSolutionText = signals.size() + "\n\n";
        for (int i = 0; i < signals.size(); i++) {
            routingSolutionText += i + 1 + " " + signals.get(i) + " " + cost.get(i) + "\n";
        }
        routingSolutionText += "\n" + file.getNumberOfDemands() + "\n\n";

        for (int i = 0; i < file.getDemands().size(); i++) {
            Integer demandId = file.getDemands().get(i).getDemandId();
            routingSolutionText += demandId + " " + getNumberOfNonZeroPathForDemand(routingSolutionDTO, demandId) + "\n";
            routingSolutionText += getPaths(routingSolutionDTO, demandId);
        }

        return routingSolutionText;
    }

    private Integer getNumberOfNonZeroPathForDemand(RoutingSolutionDTO routingSolutionDTO, Integer demandId) {
        Integer count = 0;
        for (Map.Entry<PointDTO, Integer> entry : routingSolutionDTO.getMapOfValues().entrySet()) {
            if (entry.getKey().getDemandId().equals(demandId) && entry.getValue() != 0)
                count++;
        }
        return count;
    }

    private String getPaths(RoutingSolutionDTO routingSolutionDTO, Integer demandId) {
        String result = "";
        for (Map.Entry<PointDTO, Integer> entry : routingSolutionDTO.getMapOfValues().entrySet()) {
            if (entry.getKey().getDemandId().equals(demandId) && entry.getValue() != 0)
                result += entry.getKey().getPathId() + " " + entry.getValue() + "\n";
        }
        return result + "\n";
    }

}
