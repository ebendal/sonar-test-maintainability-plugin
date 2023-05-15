package com.ebendal.sonar.java;

import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

import static com.ebendal.sonar.TestMaintainabilityMetrics.LINES_OF_CODE;

public class LinesOfCodeVisitor extends MeasurableSubscriptionVisitor<Integer> {

    private final org.sonar.java.ast.visitors.LinesOfCodeVisitor nativelinesOfCodeVisitor;

    protected LinesOfCodeVisitor(SensorContext sensorContext) {
        super(sensorContext);
        nativelinesOfCodeVisitor = new org.sonar.java.ast.visitors.LinesOfCodeVisitor();
    }

    @Override
    Metric<Integer> metric() {
        return LINES_OF_CODE;
    }

    @Override
    Integer value(JavaFileScannerContext fileScannerContext) {
        return nativelinesOfCodeVisitor.linesOfCode(fileScannerContext.getTree());
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(Tree.Kind.COMPILATION_UNIT);
    }
}
