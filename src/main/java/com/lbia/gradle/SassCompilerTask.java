package com.lbia.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;

public class SassCompilerTask extends DefaultTask {

    private SassCompilerExtension extension;

    @Inject
    public SassCompilerTask(SassCompilerExtension extension) {
        this.extension = extension;
    }

    @TaskAction
    public void compileSass() {
        System.out.println("input path: " + extension.getInputFilePath());
        System.out.println("output path: " + extension.getOutputFilePath());
        System.out.println("minify: " + extension.isMinify());
    }

}
