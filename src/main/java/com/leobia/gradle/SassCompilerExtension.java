package com.leobia.gradle;

public class SassCompilerExtension {

    private String inputPath;
    private String outputPath;
    private String outputStyle;
    private boolean sass = true;
    private boolean indentWithTabs = false;

    public SassCompilerExtension() {
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean isSass() {
        return sass;
    }

    public void setSass(boolean sass) {
        this.sass = sass;
    }

    public String getOutputStyle() {
        return outputStyle;
    }

    public void setOutputStyle(String outputStyle) {
        this.outputStyle = outputStyle;
    }

    public boolean isIndentWithTabs() {
        return indentWithTabs;
    }

    public void setIndentWithTabs(boolean indentWithTabs) {
        this.indentWithTabs = indentWithTabs;
    }
}
