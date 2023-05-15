package com.ebendal.sonar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static com.ebendal.sonar.util.JsonUtils.applyJsonProcessing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class UnitComplexityRiskDistribution implements MeasureComputer {

    private final ObjectMapper objectMapper;

    public UnitComplexityRiskDistribution() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
            .setInputMetrics(TestMaintainabilityMetrics.UNIT_SIZE.key(), TestMaintainabilityMetrics.UNIT_COMPLEXITY.key())
            .setOutputMetrics(TestMaintainabilityMetrics.UNIT_COMPLEXITY_RISK_DISTRIBUTION.key())
            .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        if (context.getComponent().getType() == Component.Type.FILE) {
            Measure sizeMeasure = context.getMeasure(TestMaintainabilityMetrics.UNIT_SIZE.key());
            Measure complexityMeasure = context.getMeasure(TestMaintainabilityMetrics.UNIT_COMPLEXITY.key());

            if (sizeMeasure != null && complexityMeasure != null) {
                List<Integer> sizeValues = Arrays.stream(sizeMeasure.getStringValue().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> complexityValues = Arrays.stream(complexityMeasure.getStringValue().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                if (sizeValues.size() == complexityValues.size()) {
                    Map<Risk, Integer> riskDistribution = IntStream.range(0, sizeValues.size())
                        .mapToObj(index -> Map.entry(getRisk(complexityValues.get(index)), sizeValues.get(index)))
                        .collect(groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));

                    context.addMeasure(TestMaintainabilityMetrics.UNIT_COMPLEXITY_RISK_DISTRIBUTION.key(), applyJsonProcessing(riskDistribution, objectMapper::writeValueAsString));
                }


            }
        } else {
            Map<Risk, Integer> riskDistribution = StreamSupport.stream(context.getChildrenMeasures(TestMaintainabilityMetrics.UNIT_COMPLEXITY_RISK_DISTRIBUTION.key()).spliterator(), false)
                .map(Measure::getStringValue)
                .map(stringValue -> {
                    TypeReference<HashMap<Risk, Integer>> typeRef = new TypeReference<>() {
                    };
                    return applyJsonProcessing(stringValue, s -> objectMapper.readValue(s, typeRef));
                })
                .flatMap(map -> map.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));

            context.addMeasure(TestMaintainabilityMetrics.UNIT_COMPLEXITY_RISK_DISTRIBUTION.key(), applyJsonProcessing(riskDistribution, objectMapper::writeValueAsString));
        }
    }

    private Risk getRisk(int size) {
        if (size <= 1) {
            return Risk.LOW;
        } else if (size <= 2) {
            return Risk.MODERATE;
        } else if (size <= 4) {
            return Risk.HIGH;
        } else {
            return Risk.VERY_HIGH;
        }
    }
}
