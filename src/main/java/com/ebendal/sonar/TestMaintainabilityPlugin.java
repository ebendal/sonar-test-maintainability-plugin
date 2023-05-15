package com.ebendal.sonar;

import com.ebendal.sonar.java.JavaSensor;
import org.sonar.api.Plugin;

public class TestMaintainabilityPlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtensions(
            TestMaintainabilityMetrics.class,
            JavaSensor.class,
            UnitSizeRiskDistribution.class,
            UnitSizeRating.class,
            UnitComplexityRiskDistribution.class,
            UnitComplexityRating.class,
            LinesOfCode.class,
            DuplicationLinesOfCode.class,
            DuplicationPercentage.class,
            DuplicationRating.class,
            Maintainability.class
        );
    }
}
