package com.ebendal.sonar.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    public static <I, O> O applyJsonProcessing(I input, JsonProcessingFunction<I, O> function) {
        try {
            return function.apply(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface JsonProcessingFunction<I, O> {
        O apply(I t) throws JsonProcessingException;
    }
}
