package com.ebendal.sonar.java;

import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.java.ast.visitors.ComplexityVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.ArrayList;
import java.util.List;

import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_COMPLEXITY;
import static java.util.stream.Collectors.joining;
import static org.sonar.plugins.java.api.tree.Tree.Kind.CONSTRUCTOR;
import static org.sonar.plugins.java.api.tree.Tree.Kind.INITIALIZER;
import static org.sonar.plugins.java.api.tree.Tree.Kind.METHOD;
import static org.sonar.plugins.java.api.tree.Tree.Kind.STATIC_INITIALIZER;

public class UnitComplexityVisitor extends MeasurableSubscriptionVisitor<String> {

    private final ComplexityVisitor complexityVisitor;
    private List<Integer> complexityPerUnit = new ArrayList<>();

    protected UnitComplexityVisitor(SensorContext sensorContext) {
        super(sensorContext);
        complexityVisitor = new ComplexityVisitor();
    }

    @Override
    Metric<String> metric() {
        return UNIT_COMPLEXITY;
    }

    @Override
    String value(JavaFileScannerContext fileScannerContext) {
        return complexityPerUnit.stream()
            .map(Object::toString)
            .collect(joining(","));
    }

    @Override
    void clearMeasureContext() {
        complexityPerUnit = new ArrayList<>();
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(METHOD, CONSTRUCTOR, INITIALIZER, STATIC_INITIALIZER);
    }

    @Override
    public void visitNode(Tree tree) {
        complexityPerUnit.add(complexityVisitor.getNodes(tree).size());
    }
}
