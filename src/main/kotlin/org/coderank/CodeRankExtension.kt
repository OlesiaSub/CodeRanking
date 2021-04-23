package org.coderank

import org.gradle.api.provider.Property

abstract class CodeRankExtension {
    abstract val inputJarFileName: Property<String>
    abstract val propertiesFileName : Property<String>
    abstract val graphBuilderLocation : Property<String>
}