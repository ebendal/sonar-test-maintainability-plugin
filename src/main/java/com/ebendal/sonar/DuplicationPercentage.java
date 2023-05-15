package com.ebendal.sonar;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static com.ebendal.sonar.TestMaintainabilityMetrics.DUPLICATION_LINES_OF_CODE;
import static com.ebendal.sonar.TestMaintainabilityMetrics.DUPLICATION_PERCENTAGE;
import static com.ebendal.sonar.TestMaintainabilityMetrics.LINES_OF_CODE;

public class DuplicationPercentage implements MeasureComputer {

    @Override
    public MeasureComputer.MeasureComputerDefinition define(MeasureComputer.MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
            .setInputMetrics(DUPLICATION_LINES_OF_CODE.key(), LINES_OF_CODE.key())
            .setOutputMetrics(DUPLICATION_PERCENTAGE.key())
            .build();
    }

    @Override
    public void compute(MeasureComputer.MeasureComputerContext context) {
        Measure linesOfCode = context.getMeasure(LINES_OF_CODE.key());
        Measure duplicatedLinesOfCode = context.getMeasure(DUPLICATION_LINES_OF_CODE.key());
        if (linesOfCode != null && duplicatedLinesOfCode != null) {
            int linesOfCodeIntValue = linesOfCode.getIntValue();
            if (linesOfCodeIntValue != 0) {
                double value = calculatePercentage(linesOfCode, duplicatedLinesOfCode);
                context.addMeasure(DUPLICATION_PERCENTAGE.key(), value);
            }
        }
    }

    private static double calculatePercentage(Measure linesOfCode, Measure duplicatedLinesOfCode) {
        return ((double) duplicatedLinesOfCode.getIntValue()) * 100 / linesOfCode.getIntValue();
    }
}
