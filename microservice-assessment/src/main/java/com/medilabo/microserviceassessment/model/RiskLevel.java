package com.medilabo.microserviceassessment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RiskLevel {
    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("In danger"),
    EARLY_ONSET("Early onset");

    private final String level;
}