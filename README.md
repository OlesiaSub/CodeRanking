# Ranking of the code fragments
###### Supervisor: Reznick Sergey Alexandrovich
#### *Motivation* :

● Large project contain lots of code. Some of the code fragments are more important than other. In this project we aimed to automatically sort these fragments according to their importance.

#### *Project description* :
Functionality: extensible Gradle plugin for Java-like languages with the option of performing static and dynamic code analyses of the project.  Users are privided with a list of ranked code fragments. Methods of the project classes are used as a ranking metric.

#### *Plugin supports three modes* :

● `static:` performing static code analysis. Its result will be available in a file `CodeRankingStat.txt` in the current directory.

● `instrument_dynamic:` instrumantation of the bytecode of user's project to prepare it for dynamic analysis. Modified project files will appear in the current working directory. To gain the statistics about the project user need to launch the project or run the tests on it.

● `analyse_dynamic:` processing the data gained during the launch of the modified project on the previous step. The result of the analysis will be available in a file `DynamicCodeRankingStat.txt` in the current directory.

#### *Preparation for the usage* :

● User needs to make a .jar file of the project, to which they want to apply the plugin.

● User needs to create .properties file and specify a property called `process-packages`, which indicates names of the files user wants to process. For example, if you only want to process files from the package called ru.hse - you will need to write `process-packages=ru/hse`

#### *Launching the plugin* :

● In order to add the dependency user needs to write the following code to their build.gradle file:
```
buildscript {
    repositories {
        mavenLocal() // plugin is now published to maven local, not released yet
    }
    dependencies {
        classpath 'org.coderank:CodeRanking:0.0.1'
    }
}
```
● In order to use the plugin:
```
apply plugin: 'org.coderank.CodeRanking'

coderank {
    inputJarFileName = 'full path to the .jar file of your project'
    graphBuilderLocation = 'full path to .jar file of your custom graph builder'
    graphBuilderName = 'full name of your custom graph builder class' OR 'rank.pain.impl.graph.Graph' - default
    classFilesLocation = 'full path to the .class files directory of your project'
    mode = 'desired mode'
    propertiesFileName = 'full path to your .properties file'
}
```
● In order to launch the plugin from the console use the following command:
```
./gradlew codeRank 
```

#### *Launching directly using Launcher* :
Pass the arguments to main function in the same order as they are specified in the build.gradle file. (main is located in class Launcher in package coderank.impl.launchers).

#### *Remark* : 
The plugin was not published yet and still is under developement, therefore the only options to launch is are:
1. launch it directly from the class Launch using the main method
2. clone this repository to your local stogare and publich the plugin to mavenLocal()
