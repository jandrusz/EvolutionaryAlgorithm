package com.pstkm.fileManagement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import com.pstkm.dtos.DemandDTO;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.LinkDTO;
import com.pstkm.dtos.PathDTO;

public class FileReader {

    private static final String END_OF_LINKS_BLOCK_SIGN = "-1";
    private FileDTO fileDTO = new FileDTO();
    private List<String> file;
    private Integer numberOfDemands = 0;

    public FileDTO readFilePipeline(List<String> fileLines) {
        numberOfDemands = 0;
        file = fileLines;
        file.add("");
        fileDTO.setNumberOfLinks(Integer.valueOf(getAndRemoveFirstLineOfFile()));
        fileDTO.setLinks(getAllLinks());
        fileDTO.setNumberOfDemands(getNumberOfDemands());
        getAndRemoveFirstLineOfFile();
        fileDTO.setDemands(getAllDemands());
        return fileDTO;
    }

    private List<LinkDTO> getAllLinks() {
        List<LinkDTO> links = Lists.newArrayList();
        for (int i = 0; ; i++) {
            if (Objects.equals(file.get(0), END_OF_LINKS_BLOCK_SIGN)) {
                getAndRemoveFirstLineOfFile();
                break;
            }
            links.add(getLink(getAndRemoveFirstLineOfFile()));
        }
        return links;
    }

    private List<DemandDTO> getAllDemands() {
        List<DemandDTO> demands = Lists.newArrayList();
        for (int i = 0; i < fileDTO.getNumberOfDemands(); i++) {
            demands.add(getDemand(getDemandBlock()));
        }
        return demands;
    }

    private List<String> getDemandBlock() {
        List<String> demandBlock = Lists.newArrayList();
        while (true) {
            if (Objects.equals(file.get(0), "")) {
                getAndRemoveFirstLineOfFile();
                break;
            }
            demandBlock.add(getAndRemoveFirstLineOfFile());
        }
        return demandBlock;
    }

    private DemandDTO getDemand(List<String> demandBlock) {
        DemandDTO demandDTO = new DemandDTO();
        if (!demandBlock.isEmpty()) {
            String[] splitted = demandBlock.get(0).split("\\s+");
            demandDTO.setStartNode(Integer.valueOf(splitted[0]));
            demandDTO.setEndNode(Integer.valueOf(splitted[1]));
            demandDTO.setDemandVolume(Integer.valueOf(splitted[2]));
            splitted = demandBlock.get(1).split("\\s+");
            demandDTO.setNumberOfPaths(Integer.valueOf(splitted[0]));
            demandDTO.setDemandId(++numberOfDemands);
            demandDTO.setPaths(getPaths(demandDTO, demandBlock));
        }
        return demandDTO;
    }

    private List<PathDTO> getPaths(DemandDTO demandDTO, List<String> demandBlock) {
        List<PathDTO> paths = Lists.newArrayList();
        for (int i = 0; i < demandDTO.getNumberOfPaths(); i++) {
            String[] splitted = demandBlock.get(2 + i).split("\\s+");
            PathDTO pathDTO = new PathDTO();
            pathDTO.setPathId(i + 1);
            pathDTO.setEdges(getEdges(splitted));
            pathDTO.setDemandId(demandDTO.getDemandId());
            paths.add(pathDTO);
        }
        return paths;
    }

    private List<Integer> getEdges(String[] edges) {
        return Arrays.stream(edges)
                .skip(1)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    private LinkDTO getLink(String link) {
        String[] splitted = link.split("\\s+");
        return LinkDTO.builder()
                .startNode(Integer.valueOf(splitted[0]))
                .endNode(Integer.valueOf(splitted[1]))
                .numberOfFibrePairsInCable(Integer.valueOf(splitted[2]))
                .fibrePairCost(Float.valueOf(splitted[3]))
                .numberOfLambdasInFibre(Integer.valueOf(splitted[4]))
                .build();
    }

    private Integer getNumberOfDemands() {
        while (true) {
            if (!Objects.equals(file.get(0), "")) {
                return Integer.valueOf(getAndRemoveFirstLineOfFile());
            }
            getAndRemoveFirstLineOfFile();
        }
    }

    private String getAndRemoveFirstLineOfFile() {
        return file.remove(0);
    }
}
