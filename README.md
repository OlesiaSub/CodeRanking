# Ranking of the code fragments
###### Supervisor: Reznick Sergey Alexandrovich
#### *Motivation* :

● Large projects contain lots of code. Some of the code fragments are more important than others. In this project we aimed to automatically sort these fragments according to their importance.

#### *Project description* :
CodeRanking is an extensible Gradle plugin for JVM languages with the option of performing static and dynamic code analyses of the project. After the execution users will receive a list of ranked code fragments. Methods of classes are used as a ranking metric.

#### *Plugin supports three modes* :

● `static:` performs static code analysis. Its result will be available in a file `CodeRankingStat.txt` in the current directory.

● `instrument_dynamic:` performs instrumantation of the bytecode of user's project to prepare it for dynamic analysis. Modified project files will appear in the current working directory. To gain the statistics about the project user needs to launch the project and perform actions, which will cover its functionality or run the tests.

● `analyse_dynamic:` processing the data gained during the launch of the modified project on the previous step. The result of the analysis will be available in a file `DynamicCodeRankingStat.txt` in the current directory.

#### *Preparation for the usage* :

● User needs to make a .jar file of the project, which they want to apply the plugin to.

● User needs to create .properties file and specify property called `process-packages`, which indicates names of files user wants to process. For example, if you only want to process files from the package called ru.hse - you will need to add a property `process-packages=ru/hse`

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
● In order to launch plugin from the command line use the following command:
```
./gradlew codeRank 
```

#### *Launching directly using Launcher* :
Pass the arguments to `main` method in the same order as they are specified in the build.gradle file. (`main` is located in class Launcher in package coderank.impl.launchers).

#### *Remark* : 
The plugin was not published yet and still is under developement, therefore the only options to launch is are:
1. launch it directly from the class Launch using `main` method
2. clone this repository to your local stogare and publich plugin to mavenLocal()
