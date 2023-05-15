package com.ebendal.sonar.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IOUtils {

    public static <I, O> O applyIOFunction(I input, IOFunction<I, O> function) {
        try {
            return function.apply(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface IOFunction<I, O> {
        O apply(I i) throws IOException;
    }
}
