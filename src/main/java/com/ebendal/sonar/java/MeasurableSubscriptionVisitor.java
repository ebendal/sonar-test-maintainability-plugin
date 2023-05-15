package com.ebendal.sonar.java;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.java.ast.visitors.SubscriptionVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;

import java.io.Serializable;

public abstract class MeasurableSubscriptionVisitor<G extends Serializable> extends SubscriptionVisitor {

    private final SensorContext sensorContext;

    protected MeasurableSubscriptionVisitor(SensorContext sensorContext) {
        this.sensorContext = sensorContext;
    }

    private void saveMetricOnFile(InputFile inputFile, Metric<G> metric, G value) {
        sensorContext.<G>newMeasure().forMetric(metric).on(inputFile).withValue(value).save();
    }

    @Override
    public void scanFile(JavaFileScannerContext context) {
        super.scanFile(context);
        saveMetricOnFile(context.getInputFile(), metric(), value(context));
        clearMeasureContext();
    }

    abstract Metric<G> metric();

    abstract G value(JavaFileScannerContext fileScannerContext);

    void clearMeasureContext() {
        // might be overridden
    }
}
