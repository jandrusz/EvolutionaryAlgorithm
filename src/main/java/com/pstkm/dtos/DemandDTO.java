package com.pstkm.dtos;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DemandDTO {

    private Integer demandId;
    private Integer startNode;
    private Integer endNode;
    private Integer demandVolume;
    private Integer numberOfPaths;
    private List<PathDTO> paths;
}
