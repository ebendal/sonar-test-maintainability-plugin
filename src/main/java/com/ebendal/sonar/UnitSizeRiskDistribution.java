package com.ebendal.sonar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_SIZE;
import static com.ebendal.sonar.TestMaintainabilityMetrics.UNIT_SIZE_RISK_DISTRIBUTION;
import static com.ebendal.sonar.util.JsonUtils.applyJsonProcessing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class UnitSizeRiskDistribution implements MeasureComputer {

    private final ObjectMapper objectMapper;

    public UnitSizeRiskDistribution() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public MeasureComputer.MeasureComputerDefinition define(MeasureComputer.MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
            .setInputMetrics(UNIT_SIZE.key())
            .setOutputMetrics(UNIT_SIZE_RISK_DISTRIBUTION.key())
            .build();
    }

    @Override
    public void compute(MeasureComputer.MeasureComputerContext context) {
        if (context.getComponent().getType() == Component.Type.FILE) {
            Measure measure = context.getMeasure(UNIT_SIZE.key());
            if (measure != null) {
                Map<Risk, Integer> riskDistribution = Arrays.stream(measure.getStringValue().split(","))
                    .map(Integer::parseInt)
                    .map(value -> Map.entry(getRisk(value), value))
                    .collect(groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));

                context.addMeasure(UNIT_SIZE_RISK_DISTRIBUTION.key(), applyJsonProcessing(riskDistribution, objectMapper::writeValueAsString));

            }
        } else {
            Map<Risk, Integer> riskDistribution = StreamSupport.stream(context.getChildrenMeasures(UNIT_SIZE_RISK_DISTRIBUTION.key()).spliterator(), false)
                .map(Measure::getStringValue)
                .map(stringValue -> {
                    TypeReference<HashMap<Risk, Integer>> typeRef = new TypeReference<>() {
                    };
                    return applyJsonProcessing(stringValue, s -> objectMapper.readValue(s, typeRef));
                })
                .flatMap(map -> map.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));

            context.addMeasure(UNIT_SIZE_RISK_DISTRIBUTION.key(), applyJsonProcessing(riskDistribution, objectMapper::writeValueAsString));
        }
    }

    private Risk getRisk(int size) {
        if (size <= 24) {
            return Risk.LOW;
        } else if (size <= 31) {
            return Risk.MODERATE;
        } else if (size <= 48) {
            return Risk.HIGH;
        } else {
            return Risk.VERY_HIGH;
        }
    }
}
