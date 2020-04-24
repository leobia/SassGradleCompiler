package com.leobia.gradle;

public class SassCompilerExtension {

    private String inputFilePath;
    private String outputFilePath;
    private boolean minify;
    private boolean sass = true;
    private boolean omitSourceMap = true;
    private String outputStyle;
    private Integer indentSpaces = 2;

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

    public boolean isSass() {
        return sass;
    }

    public void setSass(boolean sass) {
        this.sass = sass;
    }

    public boolean isOmitSourceMap() {
        return omitSourceMap;
    }

    public void setOmitSourceMap(boolean omitSourceMap) {
        this.omitSourceMap = omitSourceMap;
    }

    public String getOutputStyle() {
        return outputStyle;
    }

    public void setOutputStyle(String outputStyle) {
        this.outputStyle = outputStyle;
    }

    public Integer getIndentSpaces() {
        return indentSpaces;
    }

    public void setIndentSpaces(Integer indentSpaces) {
        this.indentSpaces = indentSpaces;
    }
}
