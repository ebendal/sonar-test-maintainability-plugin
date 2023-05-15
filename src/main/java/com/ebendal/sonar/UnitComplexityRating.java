package com.ebendal.sonar;

import com.ebendal.sonar.util.MeasureUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.HashMap;
import java.util.Optional;

import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_COMPLEXITY_RATING;
import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_COMPLEXITY_RISK_DISTRIBUTION;
import static com.ebendal.sonar.util.JsonUtils.applyJsonProcessing;
import static com.ebendal.sonar.util.MeasureUtils.getRating;

public class UnitComplexityRating implements MeasureComputer {

    private final ObjectMapper objectMapper;

    public UnitComplexityRating() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
            .setInputMetrics(UNIT_COMPLEXITY_RISK_DISTRIBUTION.key())
            .setOutputMetrics(UNIT_COMPLEXITY_RATING.key())
            .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        Optional.ofNullable(context.getMeasure(UNIT_COMPLEXITY_RISK_DISTRIBUTION.key()))
            .map(Measure::getStringValue)
            .map(stringValue -> {
                TypeReference<HashMap<Risk, Integer>> typeRef = new TypeReference<>() {
                };
                return applyJsonProcessing(stringValue, s -> objectMapper.readValue(s, typeRef));
            }).ifPresent(riskDistribution -> {
                    double rating = getRating(riskDistribution, MeasureUtils.RiskStartRatingThresholdsTable.builder()
                        .moderate(MeasureUtils.StarRatingThresholds.builder()
                            .fiveStarThreshold(.112)
                            .fourStarThreshold(.216)
                            .threeStarThreshold(.397)
                            .twoStarThreshold(.623)
                            .build())
                        .high(MeasureUtils.StarRatingThresholds.builder()
                            .fiveStarThreshold(.013)
                            .fourStarThreshold(.081)
                            .threeStarThreshold(.223)
                            .twoStarThreshold(.384)
                            .build())
                        .veryHigh(MeasureUtils.StarRatingThresholds.builder()
                            .fiveStarThreshold(.003)
                            .fourStarThreshold(.025)
                            .threeStarThreshold(.099)
                            .twoStarThreshold(.224)
                            .build())
                        .build());
                    context.addMeasure(UNIT_COMPLEXITY_RATING.key(), rating);
                }
            );
    }
}
