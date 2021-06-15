# Ранжирование фрагментов кода
###### Руководитель: Сергей Резник
#### *Мотивация* :

● В больших проектах много кода, есть более важные фрагменты, есть
менее важные. Хотим отранжировать их по важности, по строгому критерию и
автоматически

#### *Описание проекта* :
*Функциональность:* расширяемый gradle-плагин для Java с возможностью проведения статического и
динамического анализов кода проекта. Предоставление пользователю списка ранжированных по важности фрагментов кода. Метрика ранжирования — по методам классов.

*Возможное развитие:* инвариантность относительно языка, оптимизация анализа кода и используемых алгоритмов.

#### *Проект поддерживает 3 режима:*

● `static:` проведение статического анализа кода, результат будет записан в файл `CodeRankingStat.txt` в текущей директориию

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
В том же порядке, что и в build.gradle, передать в main (в классе coderank.impl.launchers.Launcher) те же аргументы, запустить main.

Сейчас плагин не опубликован, поэтому для запуска проекта нужно либо запускать его через Launcher (main метод),
либо локально создать копию данного проекта и опубликовать в mavenLocal().