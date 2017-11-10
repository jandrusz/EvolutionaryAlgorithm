package com.pstkm.dtos;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FileDTO {

    private Integer numberOfLinks;
    private List<LinkDTO> links;
    private Integer numberOfDemands;
    private List<DemandDTO> demands;
}
