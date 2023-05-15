package com.ebendal.sonar;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static com.ebendal.sonar.TestMaintainabilityMetrics.ANALYSABILITY;
import static com.ebendal.sonar.TestMaintainabilityMetrics.CHANGEABILITY;
import static com.ebendal.sonar.TestMaintainabilityMetrics.DUPLICATION_RATING;
import static com.ebendal.sonar.TestMaintainabilityMetrics.MAINTAINABILITY;
import static com.ebendal.sonar.TestMaintainabilityMetrics.STABILITY;
import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_COMPLEXITY_RATING;
import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_SIZE_RATING;

public class Maintainability implements MeasureComputer {

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext context) {
        return context.newDefinitionBuilder()
            .setInputMetrics(UNIT_SIZE_RATING.key(), UNIT_COMPLEXITY_RATING.key(), DUPLICATION_RATING.key())
            .setOutputMetrics(ANALYSABILITY.key(), CHANGEABILITY.key(), STABILITY.key(), MAINTAINABILITY.key())
            .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        Measure unitSizeMeasure = context.getMeasure(UNIT_SIZE_RATING.key());
        Measure unitComplexityMeasure = context.getMeasure(UNIT_COMPLEXITY_RATING.key());
        Measure duplicationMeasure = context.getMeasure(DUPLICATION_RATING.key());
        if (unitSizeMeasure != null && unitComplexityMeasure != null && duplicationMeasure != null) {
            double analysability = (unitSizeMeasure.getDoubleValue() + unitComplexityMeasure.getDoubleValue()) / 2;
            double changeability = (duplicationMeasure.getDoubleValue() + unitComplexityMeasure.getDoubleValue()) / 2;
            double stability = duplicationMeasure.getDoubleValue();
            context.addMeasure(ANALYSABILITY.key(), analysability);
            context.addMeasure(CHANGEABILITY.key(), changeability);
            context.addMeasure(STABILITY.key(), stability);
            context.addMeasure(MAINTAINABILITY.key(), (analysability + changeability + stability) / 3);
        }
    }
}
