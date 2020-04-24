package com.leobia.gradle;

public class SassCompilerExtension {

    private String inputFilePath;
    private String outputFilePath;
    private boolean minify;

    public SassCompilerExtension() {
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public boolean isMinify() {
        return minify;
    }

    public void setMinify(boolean minify) {
        this.minify = minify;
    }
}
