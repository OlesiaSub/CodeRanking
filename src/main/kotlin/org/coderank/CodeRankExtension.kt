package org.coderank

import org.gradle.api.provider.Property

abstract class CodeRankExtension {
    abstract val inputJarFileName: Property<String>
    abstract val graphBuilderLocation : Property<String>
    abstract val graphBuilderName : Property<String>
    abstract val classFilesLocation : Property<String>
    abstract val propertiesFileName : Property<String>
    abstract val mode : Property<String>
}