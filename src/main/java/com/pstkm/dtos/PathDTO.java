package com.pstkm.dtos;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PathDTO {

    private Integer demandId;
    private Integer pathId;
    private List<Integer> edges;
}
