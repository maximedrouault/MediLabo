package com.medilabo.microserviceclientui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskLevelDTO {

    private RiskLevelEnum level;

    public enum RiskLevelEnum {
        NONE,
        BORDERLINE,
        IN_DANGER,
        EARLY_ONSET,
        UNKNOWN_RISK_LEVEL
    }
}