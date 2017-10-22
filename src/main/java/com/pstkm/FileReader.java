package com.pstkm;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import com.google.common.collect.Lists;
import com.pstkm.dtos.DemandDTO;
import com.pstkm.dtos.FileDTO;
import com.pstkm.dtos.LinkDTO;
import com.pstkm.dtos.PathDTO;

@Getter
@Setter
public class FileReader {

	private static final String END_OF_LINKS_BLOCK_SIGN = "-1";

	private static FileDTO fileDTO = new FileDTO();

	private static List<String> file;

	public static void readFile(List<String> fileLines) {
		file = fileLines;
		fileDTO.setNumberOfLinks(Integer.valueOf(file.remove(0)));
		fileDTO.setLinks(getAllLinks());
		fileDTO.setNumberOfDemands(getNumberOfDemands());
		file.remove(0);
		fileDTO.setDemands(getAllDemands());
		getFileDTO();
	}

	private static List<LinkDTO> getAllLinks() {
		List<LinkDTO> links = Lists.newArrayList();
		for (int i = 0; ; i++) {
			if (Objects.equals(file.get(0), END_OF_LINKS_BLOCK_SIGN)) {
				file.remove(0);
				break;
			}
			links.add(getLink(file.remove(0)));
		}
		return links;
	}

	private static List<DemandDTO> getAllDemands() {
		List<DemandDTO> demands = Lists.newArrayList();
		for (int i = 0; i < fileDTO.getNumberOfDemands(); i++) {
			demands.add(getDemand(getDemandBlock()));
		}

		return demands;
	}

	private static List<String> getDemandBlock() {
		List<String> demandBlock = Lists.newArrayList();
		for (int i = 0; ; i++) {
			if (Objects.equals(file.get(0), "")) {
				file.remove(0);
				break;
			}
			demandBlock.add(file.remove(0));
		}
		return demandBlock;
	}

	private static DemandDTO getDemand(List<String> demandBlock) {
		DemandDTO demandDTO = new DemandDTO();
		List<PathDTO> paths = Lists.newArrayList();
		if (!demandBlock.isEmpty()) {
			String[] splited = demandBlock.get(0).split("\\s+");
			demandDTO.setStartNode(Integer.valueOf(splited[0]));
			demandDTO.setEndNode(Integer.valueOf(splited[1]));
			demandDTO.setDemandVolume(Integer.valueOf(splited[2]));
			splited = demandBlock.get(1).split("\\s+");
			demandDTO.setNumberOfPaths(Integer.valueOf(splited[0]));

			for (int i = 0; i < demandDTO.getNumberOfPaths(); i++) {
				splited = demandBlock.get(2 + i).split("\\s+");
				PathDTO pathDTO = new PathDTO();
				pathDTO.setPathNumber(Integer.valueOf(splited[0]));
				pathDTO.setEdges(getEdges(splited));
				paths.add(pathDTO);
			}
		}
		demandDTO.setPaths(paths);
		return demandDTO;
	}

	private static List<Integer> getEdges(String[] edges) {
		List<Integer> list = Lists.newArrayList();
		for (String s : edges) {
			list.add(Integer.valueOf(s));
		}
		list.remove(0);
		return list;
	}

	private static LinkDTO getLink(String link) {
		LinkDTO linkDTO = new LinkDTO();
		String[] splited = link.split("\\s+");
		linkDTO.setStartNode(Integer.valueOf(splited[0]));
		linkDTO.setEndNode(Integer.valueOf(splited[1]));
		linkDTO.setNumberOfFibrePairsInCable(Integer.valueOf(splited[2]));
		linkDTO.setFibrePairCost(Float.valueOf(splited[3]));
		linkDTO.setNumberOfLambdasInFibre(Integer.valueOf(splited[4]));
		return linkDTO;
	}


	private static Integer getNumberOfDemands() {

		for (int i = 0; ; i++) {
			if (!Objects.equals(file.get(0), "")) {
				return Integer.valueOf(file.remove(0));
			}
			file.remove(0);
		}
	}

	public static FileDTO getFileDTO() {
		return fileDTO;
	}
}
