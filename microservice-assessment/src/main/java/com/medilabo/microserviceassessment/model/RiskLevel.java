package com.medilabo.microserviceassessment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RiskLevel {

    private RiskLevelEnum level;

    public enum RiskLevelEnum {
        NONE,
        BORDERLINE,
        IN_DANGER,
        EARLY_ONSET,
        UNKNOWN_RISK_LEVEL
    }
}