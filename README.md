# Ранжирование фрагментов кода
###### Руководитель: Резник Сергей Александрович
#### *Мотивация* :

● В больших проектах много кода, есть более важные фрагменты, есть
менее важные. Хотим отранжировать их по важности, по строгому критерию и
автоматически.

#### *Описание проекта* :
*Функциональность:* расширяемый gradle-плагин для Java с возможностью проведения статического и
динамического анализов кода проекта. Предоставление пользователю списка ранжированных по важности фрагментов кода. Метрика ранжирования — по методам классов.

#### *Проект поддерживает 3 режима:*

● `static:` проведение статического анализа кода, результат будет записан в файл `CodeRankingStat.txt` в текущей директории.

● `instrument_dynamic:` инструментирование байткода проекта, подготовка к динамическому анализу. Модифицированные файлы проекта появятся в текущей рабочей директории. 
Нужно запустить модифицированный проект или запустить тесты для него (тоже на инструментированном коде) для сбора статистики.

● `analyse_dynamic:` обработка собранной на этапе запуска проекта статистики. Результат будет записан в файл `DynamicCodeRankingStat.txt` в текущей директории.

#### *Подготовка к использованию* :

● Проект, на котором вы хотите запустить ранжирование, нужно собрать в .jar файл.

● Нужно создать .properties файл и указать в нём `process-packages`, эта property отвечает за то, какие файлы проекта вы хотите обработать.
Например, если вы хотите обработать только файлы из пакета ru.hse, то нужно указать `process-packages=ru/hse`

#### *Запуск плагина* :

● Для подключения зависимости в build.gradle вашего проекта нужно дописать:
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
● Для использования плагина:
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
● Для запуска плагина из консоли нужно вызвать:
```
./gradlew codeRank 
```

#### *Запуск напрямую через Launcher* :
В том же порядке, что и в build.gradle, передать в main (который находится в классе coderank.impl.launchers.Launcher) те же аргументы, запустить main.

Сейчас плагин не опубликован, поэтому для запуска проекта нужно либо запускать его через Launcher (main метод),
либо локально создать копию данного проекта и опубликовать в mavenLocal().



## English Version:

# Ranking of the code fragments
###### Supervisor: Reznick Sergey Alexandrovich
#### *Motivation* :

● Large project contain lots of code. Some of the code fragments are more important than other. In this project we aimed to automatically sort these fragments according to their importance.

#### *Project description* :
Functionality: extensible Gradle plugin for Java-like languages with the option of performing static and dynamic code analyses of the project.  Users are privided with a list of ranked code fragments. Methods of the project classes are used as a ranking metric.(???)

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
Pass the arguments to main in the same order as they are specified in the build.gradle file. (main is located in class Launcher in package coderank.impl.launchers).

#### *Remark* : 
The plugin was not published yet, therefore the only options to launch is are:
1. launch it directly from the class Launch using the main method
2. clone this repository to your local stogare and publich the plugin to mavenLocal()
