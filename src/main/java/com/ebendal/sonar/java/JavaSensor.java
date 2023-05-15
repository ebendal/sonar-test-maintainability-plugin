package com.ebendal.sonar.java;

import com.ebendal.sonar.util.SonarFileUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.java.ast.JavaAstScanner;
import org.sonar.java.model.JavaVersionImpl;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.plugins.java.Java;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.Collections;
import java.util.List;

import static org.sonar.api.batch.fs.InputFile.Type.TEST;

public class JavaSensor implements Sensor {

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnFileType(TEST).onlyOnLanguage(Java.KEY).name("test-maintainability-java-sensor");
    }

    @Override
    public void execute(SensorContext sensorContext) {
        JavaAstScanner javaAstScanner = new JavaAstScanner(null);
        List<JavaCheck> visitors = List.of(
            new UnitSizeVisitor(sensorContext),
            new UnitComplexityVisitor(sensorContext),
            new DuplicatedLinesOfCodeVisitor(sensorContext),
            new LinesOfCodeVisitor(sensorContext)
        );
        VisitorsBridge visitorBridge = new VisitorsBridge(visitors, Collections.singletonList(sensorContext.fileSystem().baseDir()), null);
        visitorBridge.setJavaVersion(new JavaVersionImpl(11));
        javaAstScanner.setVisitorBridge(visitorBridge);
        FileSystem fileSystem = sensorContext.fileSystem();
        javaAstScanner.scan(fileSystem.inputFiles(SonarFileUtils::isJavaTestFile));
    }
}
