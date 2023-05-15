package com.ebendal.sonar.util;

import com.ebendal.sonar.Risk;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MeasureUtils {

    public static double getRating(Map<Risk, Integer> riskDistribution, RiskStartRatingThresholdsTable riskStartRatingThresholdsTable) {

        int total = riskDistribution.values().stream()
            .mapToInt(Integer::intValue)
            .sum();
        if (total == 0) {
            return 5.5;
        }

        double moderateRating = getRating(calculatePercentage(riskDistribution, Risk.MODERATE, total), riskStartRatingThresholdsTable.getModerate());
        double highRating = getRating(calculatePercentage(riskDistribution, Risk.HIGH, total), riskStartRatingThresholdsTable.getHigh());
        double veryHighRating = getRating(calculatePercentage(riskDistribution, Risk.VERY_HIGH, total), riskStartRatingThresholdsTable.getVeryHigh());
        return Math.min(moderateRating, Math.min(highRating, veryHighRating));
    }

    public static double getRating(double percentage, StarRatingThresholds starRatingThresholds) {
        if (percentage <= starRatingThresholds.getFiveStarThreshold()) {
            return 5.5 - (1.0 / starRatingThresholds.getFiveStarThreshold()) * percentage;
        } else if (percentage <= starRatingThresholds.getFourStarThreshold()) {
            return 4.5 - (1.0 / (starRatingThresholds.getFourStarThreshold() - starRatingThresholds.getFiveStarThreshold())) * (percentage - starRatingThresholds.getFiveStarThreshold());
        } else if (percentage <= starRatingThresholds.getThreeStarThreshold()) {
            return 3.5 - (1.0 / (starRatingThresholds.getThreeStarThreshold() - starRatingThresholds.getFourStarThreshold())) * (percentage - starRatingThresholds.getFourStarThreshold());
        } else if (percentage <= starRatingThresholds.getTwoStarThreshold()) {
            return 2.5 - (1.0 / (starRatingThresholds.getTwoStarThreshold() - starRatingThresholds.getThreeStarThreshold())) * (percentage - starRatingThresholds.getThreeStarThreshold());
        } else {
            return 1.5 - (1.0 / (1.0 - starRatingThresholds.getTwoStarThreshold())) * (percentage - starRatingThresholds.getTwoStarThreshold());
        }
    }

    private static double calculatePercentage(Map<Risk, Integer> riskDistribution, Risk risk, int total) {
        return ((double) Optional.ofNullable(riskDistribution.get(risk)).orElse(0)) / total;
    }

    @Getter
    @Builder
    public static class StarRatingThresholds {

        private final double fiveStarThreshold;
        private final double fourStarThreshold;
        private final double threeStarThreshold;
        private final double twoStarThreshold;

    }

    @Getter
    @Builder
    public static class RiskStartRatingThresholdsTable {

        private final StarRatingThresholds moderate;
        private final StarRatingThresholds high;
        private final StarRatingThresholds veryHigh;
    }
}
