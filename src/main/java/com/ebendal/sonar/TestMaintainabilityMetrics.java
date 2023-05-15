package com.ebendal.sonar;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.List;

import static java.util.Arrays.asList;

public class TestMaintainabilityMetrics implements Metrics {

    public static final Metric<String> UNIT_SIZE = new Metric.Builder("test-unit-size", "Test Unit Size", Metric.ValueType.DATA)
        .setQualitative(false)
        .setDomain(CoreMetrics.DOMAIN_SIZE)
        .create();

    public static final Metric<String> UNIT_SIZE_RISK_DISTRIBUTION = new Metric.Builder("test-unit-size-risk-distribution", "Test Unit Size distribution", Metric.ValueType.DATA)
        .setQualitative(false)
        .setDomain(CoreMetrics.DOMAIN_SIZE)
        .create();

    public static final Metric<Double> UNIT_SIZE_RATING = new Metric.Builder("test-unit-size-rating", "Test Unit Size rating", Metric.ValueType.FLOAT)
        .setQualitative(true)
        .setDomain(CoreMetrics.DOMAIN_SIZE)
        .setDirection(Metric.DIRECTION_BETTER)
        .setBestValue(5.5)
        .setWorstValue(0.5)
        .create();

    public static final Metric<String> UNIT_COMPLEXITY = new Metric.Builder("test-unit-complexity", "Test Unit Complexity", Metric.ValueType.DATA)
        .setQualitative(false)
        .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
        .create();

    public static final Metric<String> UNIT_COMPLEXITY_RISK_DISTRIBUTION = new Metric.Builder("test-unit-complexity-risk-distribution", "Test Unit Complexity Distribution", Metric.ValueType.DATA)
        .setQualitative(false)
        .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
        .create();

    public static final Metric<Double> UNIT_COMPLEXITY_RATING = new Metric.Builder("test-unit-complexity-rating", "Test Unit Complexity Rating", Metric.ValueType.FLOAT)
        .setQualitative(true)
        .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
        .setDirection(Metric.DIRECTION_BETTER)
        .setBestValue(5.5)
        .setWorstValue(0.5)
        .create();

    public static final Metric<Integer> LINES_OF_CODE = new Metric.Builder("test-lines-of-code", "Test Lines of Code", Metric.ValueType.INT)
        .setQualitative(false)
        .setDomain(CoreMetrics.DOMAIN_SIZE)
        .create();

    public static final Metric<Integer> DUPLICATION_LINES_OF_CODE = new Metric.Builder("test-duplication-lines-of-code", "Test Duplication lines of code", Metric.ValueType.INT)
        .setQualitative(false)
        .setDomain(CoreMetrics.DOMAIN_DUPLICATIONS)
        .create();

    public static final Metric<Double> DUPLICATION_PERCENTAGE = new Metric.Builder("test-duplication-percentage", "Test Duplication percentage", Metric.ValueType.PERCENT)
        .setQualitative(false)
        .setDomain(CoreMetrics.DOMAIN_DUPLICATIONS)
        .create();

    public static final Metric<Double> DUPLICATION_RATING = new Metric.Builder("test-duplication-rating", "Test Duplication rating", Metric.ValueType.FLOAT)
        .setQualitative(true)
        .setDomain(CoreMetrics.DOMAIN_DUPLICATIONS)
        .setDirection(Metric.DIRECTION_BETTER)
        .setBestValue(5.5)
        .setWorstValue(0.5)
        .create();

    public static final Metric<Double> ANALYSABILITY = new Metric.Builder("test-analysability", "Test Analysability", Metric.ValueType.FLOAT)
        .setQualitative(true)
        .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
        .setDirection(Metric.DIRECTION_BETTER)
        .setBestValue(5.5)
        .setWorstValue(0.5)
        .create();

    public static final Metric<Double> CHANGEABILITY = new Metric.Builder("test-changeability", "Test Changeability", Metric.ValueType.FLOAT)
        .setQualitative(true)
        .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
        .setDirection(Metric.DIRECTION_BETTER)
        .setBestValue(5.5)
        .setWorstValue(0.5)
        .create();

    public static final Metric<Double> STABILITY = new Metric.Builder("test-stability", "Test Stability", Metric.ValueType.FLOAT)
        .setQualitative(true)
        .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
        .setDirection(Metric.DIRECTION_BETTER)
        .setBestValue(5.5)
        .setWorstValue(0.5)
        .create();

    public static final Metric<Double> MAINTAINABILITY = new Metric.Builder("test-maintainability", "Test Maintainability", Metric.ValueType.FLOAT)
        .setQualitative(true)
        .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
        .setDirection(Metric.DIRECTION_BETTER)
        .setBestValue(5.5)
        .setWorstValue(0.5)
        .create();

    @Override
    public List<Metric> getMetrics() {
        return asList(
            LINES_OF_CODE,
            UNIT_SIZE,
            UNIT_SIZE_RISK_DISTRIBUTION,
            UNIT_SIZE_RATING,
            UNIT_COMPLEXITY,
            UNIT_COMPLEXITY_RISK_DISTRIBUTION,
            UNIT_COMPLEXITY_RATING,
            DUPLICATION_RATING,
            DUPLICATION_LINES_OF_CODE,
            DUPLICATION_PERCENTAGE,
            ANALYSABILITY,
            CHANGEABILITY,
            STABILITY,
            MAINTAINABILITY);
    }
}
