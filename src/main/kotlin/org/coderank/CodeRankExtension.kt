package org.coderank

import org.gradle.api.provider.Property

abstract class CodeRankExtension {
    abstract val inputJarFileName: Property<String>
}