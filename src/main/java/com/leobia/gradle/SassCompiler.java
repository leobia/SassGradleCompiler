package com.leobia.gradle;

import com.leobia.gradle.exception.InputPathNotProvidedException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.*;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SassCompiler {

    private static final String SCSS = "scss";
    private static final String SASS = "sass";
    private static final String CSS = "css";
    private final Logger logger;
    private final Options options;

    public SassCompiler(Logger logger) {
        options = new Options();
        this.logger = logger;
    }

    public void compile(SassCompilerExtension extension) {
        try {
            setOptionsByExtension(extension);

            List<File> inputFiles = getInputFiles(extension);
            File outputFile = getOutputFile(extension);

            StringBuilder css = getCss(inputFiles, outputFile);
            write(outputFile, minify(css));
            logger.info("Sass compilation completed");


        } catch (IOException e) {
            throw new GradleException("No privilege to create new files!", e);
        } catch (InputPathNotProvidedException e) {
            throw new GradleException("Input path not provided!", e);
        } catch (CompilationException e) {
            throw new GradleException("Sass compilation failed!", e);
        }
    }

    private StringBuilder getCss(List<File> inputFiles, File outputFile) throws CompilationException {
        StringBuilder css = new StringBuilder();

        Compiler sassCompiler = new Compiler();

        for (File inputFile : inputFiles) {
            Output output = sassCompiler.compileFile(inputFile.toURI(), outputFile.toURI(), options);
            css.append(output.getCss());
        }
        return css;
    }

    private String minify(StringBuilder css) {
        String cssString = css.toString();
        if (OutputStyle.COMPRESSED.equals(options.getOutputStyle())) {
            Pattern replace = Pattern.compile(options.getLinefeed());
            Matcher matcher = replace.matcher(cssString);
            cssString = matcher.replaceAll("");
        }
        return cssString;
    }

    private void setOptionsByExtension(SassCompilerExtension extension) {
        options.setIsIndentedSyntaxSrc(extension.isSass());
        options.setOutputStyle(retrieveStyle(extension.getOutputStyle()));
        options.setOmitSourceMapUrl(true);
        options.setIndent(retrieveIndent(extension.isIndentWithTabs()));
    }

    private String retrieveIndent(boolean useTab) {
        String indent = "  ";

        if (useTab) {
            indent = "\t";
        }

        return indent;
    }

    private OutputStyle retrieveStyle(String outputStyle) {
        OutputStyle style = OutputStyle.NESTED;

        if (outputStyle != null) {
            switch (outputStyle.toUpperCase()) {
                case "COMPACT":
                    style = OutputStyle.COMPACT;
                    break;
                case "COMPRESSED":
                    style = OutputStyle.COMPRESSED;
                    break;
                case "EXPANDED":
                    style = OutputStyle.EXPANDED;
                    break;
                default:
                    style = OutputStyle.NESTED;
                    break;
            }
        }

        return style;
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
        String inputFilePath = extension.getInputPath();
        List<File> sassFiles = new ArrayList<>();
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            throw new InputPathNotProvidedException();
        }


        File inputFile = new File(inputFilePath);
        if (inputFile.exists()) {
            if (inputFile.isFile() && isSassFile(inputFile, extension.isSass())) {
                sassFiles.add(inputFile);
            } else if (inputFile.isDirectory()) {
                File[] files = inputFile.listFiles();

                if (files != null && files.length > 0) {
                    for (File file : files) {
                        if (isSassFile(file, extension.isSass()) && !pathEqualsToOutput(file, extension)) {
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
        String outputFilePath = extension.getOutputPath();
        if (outputFilePath == null || outputFilePath.isEmpty()) {
            outputFilePath = addToPath(extension.getInputPath(), "minified");
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

    private boolean isSassFile(File file, boolean sass) {
        boolean isSass = false;

        if (file != null && file.isFile()) {
            String fileName = file.getName();
            if (fileName.contains(".")) {
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (sass) {
                    isSass = SASS.equals(extension.toLowerCase()) || CSS.equals(extension.toLowerCase());
                } else {
                    isSass = SCSS.equals(extension.toLowerCase()) || CSS.equals(extension.toLowerCase());
                }
            }
        }

        return isSass;
    }
}
