

# Sass Gradle Compiler :zap:

A simple Sass compiler for gradle (tested only on Gradle 6.0+). Given a folder containing .sass and .css files, the plugin will compile them into a single file. 

## Instructions :pencil:

As stated before the plugin was tested and developed with gradle 6 so I cannot ensure functionality with older versions.

#### Install the plugin locally

As of now Gradle has not accepted the plugin so to use it you have to download the source code put it inside your project and run the task:

    uploadArchives

This task will create a folder *repo/* in your project that contains the jar of the plugin.

#### Apply the plugin
Now that you have the plugin installed inside your project you can apply it. 
To do so add these following lines to your *build.gradle*:

    buildscript {  
      repositories {  
        maven {  
          url uri("../repo") // This basically tells gradle to search plugins inside 
                             // the repo directory that we created before
        }  
      }  
      dependencies {  
        classpath "com.leobia.gradle:sassjavacompiler:0.1.0" // this is my plugin
      }  
    }
    apply plugin: "com.leobia.gradle.sassjavacompiler"

#### Use the plugin
The usage of the plugin it's pretty simple, first you define the options (inside the *build.gradle*):

    sassOptions {  
      inputPath = file("./resources/")   
      outputPath = file("./resources/minified/")
      outputStyle = "compressed"  
      sass = true
      indentWithTabs = false
    }

 - **inputPath**: (required) it tells the plugin where to search sass and css files (it is not recursive, this means that it will only search inside this directory and not inside its subdirectory)
 - **outputPath**: where the file output should be saved (it will be saved as style.css)
 - **sass**: true consider .sass files, false consider .scss files
 - **outputStyle** : possible style are "nested" (default), "compressed" (minified), "compact" and "expanded"

and then you can call the task:

    compileSass

## What's next  :rocket:


These are the main features that I plan to include in the project :

 - Ability to compile at the same time scss file and sass file without need to change flags
 - Include unit test to provide more stability
 
 Currently I'm working on another plugin to compile and minify javascript files.

## Built With :hammer:

* [Gradle](https://gradle.org/) - Dependency Management
* [jsass](https://jsass.readthedocs.io/en/latest/) - Used to compile sass

## Author :boy:

* **Leonardo Bianco** - [leobia](https://github.com/leobia)

## License :page_facing_up:

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details

