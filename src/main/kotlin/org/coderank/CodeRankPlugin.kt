package org.coderank

import org.gradle.api.Plugin
import org.gradle.api.Project

import CodeRank.app.src.main.impl.asm.Configuration;
import CodeRank.app.src.main.impl.main.Main

class CodeRankPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<CodeRankExtension>("coderank", CodeRankExtension::class.java)
        project.tasks.create("codeRank") { task ->
            task.doLast {
                println("Starting task codeRank...")
                Configuration.setConfigProperty(extension.propertiesFileName.get())
                val inputJarFileName = extension.inputJarFileName.get()
                val graphBuilderLocation = extension.graphBuilderLocation.get()
                val graphBuilderName = extension.graphBuilderName.get()
                val args = arrayOf(inputJarFileName, graphBuilderLocation, graphBuilderName)
                Main.main(args)
            }
        }.apply {
            group = "stat"
        }
    }
}