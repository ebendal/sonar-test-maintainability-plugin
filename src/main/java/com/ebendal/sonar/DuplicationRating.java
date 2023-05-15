package com.ebendal.sonar;

import com.ebendal.sonar.util.MeasureUtils;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.Optional;

import static com.ebendal.sonar.TestMaintainabilityMetrics.DUPLICATION_PERCENTAGE;
import static com.ebendal.sonar.TestMaintainabilityMetrics.DUPLICATION_RATING;

public class DuplicationRating implements MeasureComputer {
    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext) {
        return defContext.newDefinitionBuilder()
            .setInputMetrics(DUPLICATION_PERCENTAGE.key())
            .setOutputMetrics(DUPLICATION_RATING.key())
            .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        Optional.ofNullable(context.getMeasure(DUPLICATION_PERCENTAGE.key()))
            .map(Measure::getDoubleValue)
            .ifPresent(measure -> {
                double rating = MeasureUtils.getRating(measure / 100, MeasureUtils.StarRatingThresholds.builder()
                    .fiveStarThreshold(.055)
                    .fourStarThreshold(.103)
                    .threeStarThreshold(.164)
                    .twoStarThreshold(.216)
                    .build());
                context.addMeasure(DUPLICATION_RATING.key(), rating);
            });
    }
}
