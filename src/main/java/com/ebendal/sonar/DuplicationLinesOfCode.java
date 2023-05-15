package com.ebendal.sonar;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.ArrayList;
import java.util.List;

import static com.ebendal.sonar.TestMaintainabilityMetrics.DUPLICATION_LINES_OF_CODE;

public class DuplicationLinesOfCode implements MeasureComputer {

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
            .setInputMetrics(DUPLICATION_LINES_OF_CODE.key())
            .setOutputMetrics(DUPLICATION_LINES_OF_CODE.key())
            .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        if (context.getComponent().getType() != Component.Type.FILE) {
            List<Integer> result = new ArrayList<>();
            for (Measure child : context.getChildrenMeasures(DUPLICATION_LINES_OF_CODE.key())) {
                result.add(child.getIntValue());
            }
            context.addMeasure(DUPLICATION_LINES_OF_CODE.key(), result.stream().mapToInt(Integer::intValue).sum());
        }
    }
}
