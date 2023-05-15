package com.ebendal.sonar;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.ArrayList;
import java.util.List;

import static com.ebendal.sonar.TestMaintainabilityMetrics.LINES_OF_CODE;

public class LinesOfCode implements MeasureComputer {

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
            .setInputMetrics(LINES_OF_CODE.key())
            .setOutputMetrics(LINES_OF_CODE.key())
            .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        if (context.getComponent().getType() != Component.Type.FILE) {
            List<Integer> result = new ArrayList<>();
            for (Measure child : context.getChildrenMeasures(LINES_OF_CODE.key())) {
                result.add(child.getIntValue());
            }
            context.addMeasure(LINES_OF_CODE.key(), result.stream().mapToInt(Integer::intValue).sum());
        }
    }
}