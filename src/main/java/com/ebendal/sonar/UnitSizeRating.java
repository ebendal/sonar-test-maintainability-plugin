package com.ebendal.sonar;

import com.ebendal.sonar.util.MeasureUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.HashMap;
import java.util.Optional;

import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_SIZE_RATING;
import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_SIZE_RISK_DISTRIBUTION;
import static com.ebendal.sonar.util.JsonUtils.applyJsonProcessing;
import static com.ebendal.sonar.util.MeasureUtils.getRating;

public class UnitSizeRating implements MeasureComputer {

    private final ObjectMapper objectMapper;

    public UnitSizeRating() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
            .setInputMetrics(UNIT_SIZE_RISK_DISTRIBUTION.key())
            .setOutputMetrics(UNIT_SIZE_RATING.key())
            .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        Optional.ofNullable(context.getMeasure(UNIT_SIZE_RISK_DISTRIBUTION.key()))
            .map(Measure::getStringValue)
            .map(stringValue -> {
                TypeReference<HashMap<Risk, Integer>> typeRef = new TypeReference<>() {
                };
                return applyJsonProcessing(stringValue, s -> objectMapper.readValue(s, typeRef));
            }).ifPresent(riskDistribution -> {
                    double rating = getRating(riskDistribution, MeasureUtils.RiskStartRatingThresholdsTable.builder()
                        .moderate(MeasureUtils.StarRatingThresholds.builder()
                            .fiveStarThreshold(.123)
                            .fourStarThreshold(.276)
                            .threeStarThreshold(.354)
                            .twoStarThreshold(.540)
                            .build())
                        .high(MeasureUtils.StarRatingThresholds.builder()
                            .fiveStarThreshold(.061)
                            .fourStarThreshold(.161)
                            .threeStarThreshold(.250)
                            .twoStarThreshold(.430)
                            .build())
                        .veryHigh(MeasureUtils.StarRatingThresholds.builder()
                            .fiveStarThreshold(.008)
                            .fourStarThreshold(.070)
                            .threeStarThreshold(.140)
                            .twoStarThreshold(.242)
                            .build())
                        .build());
                    context.addMeasure(UNIT_SIZE_RATING.key(), rating);
                }
            );
    }
}
