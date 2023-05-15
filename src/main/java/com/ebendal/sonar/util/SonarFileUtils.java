package com.ebendal.sonar.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.java.Java;

import static org.sonar.api.batch.fs.InputFile.Type.TEST;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SonarFileUtils {

    public static boolean isJavaTestFile(InputFile inputFile) {
        return Java.KEY.equals(inputFile.language()) && TEST == inputFile.type();
    }
}
