package com.leobia.gradle;

import com.leobia.gradle.exception.InputPathNotProvidedException;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SassCompiler {

    private static final String SASS = "sass";
    private final Logger logger;
    private final Options options;


    public SassCompiler(Logger logger) {
        options = new Options();
        options.setIsIndentedSyntaxSrc(true);
        this.logger = logger;
    }

    public void compile(SassCompilerExtension extension) {

        try {
            List<File> inputFiles = getInputFiles(extension);
            File outputFile = getOutputFile(extension);

            StringBuilder css = new StringBuilder();

            Compiler sassCompiler = new Compiler();

            for (File inputFile : inputFiles) {
                Output output = sassCompiler.compileFile(inputFile.toURI(), outputFile.toURI(), options);
                css.append(output.getCss());
            }

            write(outputFile, css.toString());
            logger.info("Sass compilation completed");
        } catch (IOException e) {
            throw new GradleException("No privilege to create new files!", e);
        } catch (InputPathNotProvidedException e) {
            throw new GradleException("Input path not provided!", e);
        } catch (CompilationException e) {
            throw new GradleException("Sass compilation failed!", e);
        }
    }

    private void write(File file, String css) {
        file.getParentFile().mkdirs();
        try (OutputStream stream = new FileOutputStream(file, false)) {
            stream.write(css.getBytes());
        } catch (IOException e) {
            logger.error("Error writing file: " + file.getName(), e);
        }
    }

    /**
     * Returns all sass files contained in input path files and its subdirectory
     *
     * @param extension defined by user
     */
    private List<File> getInputFiles(SassCompilerExtension extension) throws InputPathNotProvidedException {
        String inputFilePath = extension.getInputFilePath();
        List<File> sassFiles = new ArrayList<>();
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            throw new InputPathNotProvidedException();
        }


        File inputFile = new File(inputFilePath);
        if (inputFile.exists()) {
            if (inputFile.isFile() && isSassFile(inputFile)) {
                sassFiles.add(inputFile);
            } else if (inputFile.isDirectory()) {
                File[] files = inputFile.listFiles();

                if (files != null && files.length > 0) {
                    for (File file : files) {
                        if (isSassFile(file) && !pathEqualsToOutput(file, extension)) {
                            sassFiles.add(file);
                        }
                    }
                }
            }
        }

        return sassFiles;
    }

    private boolean pathEqualsToOutput(File file, SassCompilerExtension extension) {
        return file.isFile() && getOutputFilePath(extension, false).equals(file.getAbsolutePath());
    }

    /**
     * Returns a new or existing java.io.File that will contain all the sass compiled
     *
     * @param extension defined by user
     * @return the output file style.css
     * @throws IOException if it can not create a new file
     */
    private File getOutputFile(SassCompilerExtension extension) throws IOException {
        String outputFilePath = getOutputFilePath(extension, true);
        File output = new File(outputFilePath);
        if (!output.exists()) {
            output.createNewFile();
        }
        return output;
    }

    private String getOutputFilePath(SassCompilerExtension extension, boolean shouldLog) {
        String outputFilePath = extension.getOutputFilePath();
        if (outputFilePath == null || outputFilePath.isEmpty()) {
            outputFilePath = addToPath(extension.getInputFilePath(), "minified");
            if (shouldLog) {
                logger.warn("outputFilePath was not provided... saving files in {}", outputFilePath);
            }
        }
        outputFilePath = addToPath(outputFilePath, "style.css");
        return outputFilePath;
    }

    private String addToPath(String path, String toAdd) {
        String output = path;
        String lastChar = output.substring(output.length() - 1);

        if (File.separator.equals(lastChar)) {
            output += toAdd;
        } else {
            output += File.separator + toAdd;
        }

        return output;
    }

    private boolean isSassFile(File file) {
        boolean isSass = false;

        if (file != null && file.isFile()) {
            String fileName = file.getName();
            isSass = fileName.contains(".") && SASS.equals(fileName.substring(fileName.lastIndexOf(".") + 1));
        }

        return isSass;
    }
}
