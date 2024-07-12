package com.coreApplication.Utils;

public class PredictionResponse {
    private final double score;

    public PredictionResponse(double score) {
        this.score = score;
    }

    // Getter
    public double getScore() { return score; }
}
