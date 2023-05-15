package com.ebendal.sonar.java;

import com.ebendal.sonar.util.SonarFileUtils;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ebendal.sonar.TestMaintainabilityMetrics.DUPLICATION_LINES_OF_CODE;
import static com.ebendal.sonar.util.IOUtils.applyIOFunction;

public class DuplicatedLinesOfCodeVisitor extends MeasurableSubscriptionVisitor<Integer> {

    private final SensorContext sensorContext;

    protected DuplicatedLinesOfCodeVisitor(SensorContext sensorContext) {
        super(sensorContext);
        this.sensorContext = sensorContext;
    }

    @Override
    Metric<Integer> metric() {
        return DUPLICATION_LINES_OF_CODE;
    }

    @Override
    Integer value(JavaFileScannerContext fileScannerContext) {
        InputFile inputFile = fileScannerContext.getInputFile();
        List<String> codeLines = codeLines(inputFile);
        Boolean[] resultArray = new Boolean[codeLines.size()];
        Arrays.fill(resultArray, Boolean.FALSE);
        for (int i = 0; i < codeLines.size() - 6; i++) {
            List<String> codeBlock = codeLines.subList(i, i + 6);
            for (InputFile otherInputFile : sensorContext.fileSystem().inputFiles(SonarFileUtils::isJavaTestFile)) {
                if (!otherInputFile.uri().equals(inputFile.uri())) {
                    List<String> otherCodeLines = codeLines(otherInputFile);
                    for (int j = 0; j < otherCodeLines.size() - 6; j++) {
                        List<String> otherCodeBlock = otherCodeLines.subList(j, j + 6);
                        if (codeBlock.equals(otherCodeBlock)) {
                            for (int k = i; k < i + 6; k++) {
                                resultArray[k] = Boolean.TRUE;
                            }
                        }
                    }
                }
            }
        }
        return Math.toIntExact(Arrays.stream(resultArray).filter(Boolean::booleanValue).count());
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(Tree.Kind.COMPILATION_UNIT);
    }

    private List<String> codeLines(InputFile inputFile) {
        return Arrays.stream(applyIOFunction(inputFile, InputFile::contents).split("\n"))
            .filter(line -> !line.isBlank())
            .map(String::stripLeading)
            .filter(line -> !line.startsWith("/") && !line.startsWith("*") && !line.startsWith("import") && !line.startsWith("package"))
            .collect(Collectors.toList());
    }
}
