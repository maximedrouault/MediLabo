package com.medilabo.microserviceclientui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RiskLevelDTO {
    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("In danger"),
    EARLY_ONSET("Early onset");

    private final String level;
}