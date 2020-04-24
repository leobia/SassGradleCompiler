package com.lbia.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SassCompilerPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        SassCompilerExtension sassOptions = project.getExtensions().create("sassOptions", SassCompilerExtension.class);
        project.getTasks().create("compileSass", SassCompilerTask.class, sassOptions);
    }
}
