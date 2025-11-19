package com.meta.stock.materials.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MaterialDTO {
    private Long materialsId;
    private String materialName;
    private Long lotsId;
    private LocalDate sampleDate;
    private LocalDate checkDate;
    private Boolean checkResult;
}