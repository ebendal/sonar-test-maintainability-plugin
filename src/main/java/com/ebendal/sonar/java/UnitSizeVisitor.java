package com.ebendal.sonar.java;

import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.java.ast.visitors.LinesOfCodeVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.ArrayList;
import java.util.List;

import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_SIZE;
import static java.util.stream.Collectors.joining;
import static org.sonar.plugins.java.api.tree.Tree.Kind.CONSTRUCTOR;
import static org.sonar.plugins.java.api.tree.Tree.Kind.INITIALIZER;
import static org.sonar.plugins.java.api.tree.Tree.Kind.METHOD;
import static org.sonar.plugins.java.api.tree.Tree.Kind.STATIC_INITIALIZER;

public class UnitSizeVisitor extends MeasurableSubscriptionVisitor<String> {

    private final LinesOfCodeVisitor linesOfCodeVisitor;
    private List<Integer> linesOfCodePerUnit = new ArrayList<>();

    protected UnitSizeVisitor(SensorContext sensorContext) {
        super(sensorContext);
        linesOfCodeVisitor = new LinesOfCodeVisitor();
    }

    @Override
    Metric<String> metric() {
        return UNIT_SIZE;
    }

    @Override
    String value(JavaFileScannerContext fileScannerContext) {
        return linesOfCodePerUnit.stream()
            .map(Object::toString)
            .collect(joining(","));
    }

    @Override
    void clearMeasureContext() {
        linesOfCodePerUnit = new ArrayList<>();
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(METHOD, CONSTRUCTOR, INITIALIZER, STATIC_INITIALIZER);
    }

    @Override
    public void visitNode(Tree tree) {
        linesOfCodePerUnit.add(linesOfCodeVisitor.linesOfCode(tree));
    }
}
