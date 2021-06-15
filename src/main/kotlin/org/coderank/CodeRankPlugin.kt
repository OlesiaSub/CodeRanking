package org.coderank

import org.gradle.api.Plugin
import org.gradle.api.Project

import coderank.impl.staticanalysis.Configuration
import coderank.impl.launchers.Launcher

class CodeRankPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<CodeRankExtension>("coderank", CodeRankExtension::class.java)
        project.tasks.create("codeRank") { task ->
            task.doLast {
                println("Starting task codeRank...")
                val inputJarFileName = extension.inputJarFileName.get()
                val graphBuilderLocation = extension.graphBuilderLocation.get()
                val graphBuilderName = extension.graphBuilderName.get()
                val classFilesLocation = extension.classFilesLocation.get()
                val mode = extension.mode.get()
                val args = arrayOf(inputJarFileName, graphBuilderLocation, graphBuilderName, classFilesLocation, mode)
                Configuration.setConfigProperty(extension.propertiesFileName.get())
                Launcher.main(args)
            }
        }.apply {
            group = "stat"
        }
    }
}