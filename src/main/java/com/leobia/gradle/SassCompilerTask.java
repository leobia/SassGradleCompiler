package com.leobia.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;

public class SassCompilerTask extends DefaultTask {

    private SassCompilerExtension extension;
    private SassCompiler compiler;

    @Inject
    public SassCompilerTask(SassCompilerExtension extension) {
        this.extension = extension;
    }

    @TaskAction
    public void compileSass() {
        compiler = new SassCompiler(getLogger());
        compiler.compile(extension);
    }

}
