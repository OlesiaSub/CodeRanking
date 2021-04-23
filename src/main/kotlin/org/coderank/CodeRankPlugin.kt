package org.coderank

import org.gradle.api.Plugin
import org.gradle.api.Project

import CodeRank.app.src.main.impl.asm.Configuration;
import CodeRank.app.src.main.impl.main.Main
import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderLoader

class CodeRankPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<CodeRankExtension>("coderank", CodeRankExtension::class.java)
        project.tasks.create("codeRank") { task ->
            task.doLast {
                //val args = arrayOf("/home/olesya/HSE_2020-1/java/maze/out/artifacts/maze_jar/maze.jar")
                //Main.main(args)
                println("Starting task codeRank snd...")
                Configuration.setConfigProperty(extension.propertiesFileName.get())
                val inputJarFileName = extension.inputJarFileName.get()
                val graphBuilderLocation = extension.graphBuilderLocation.get()
                val args2 = arrayOf(inputJarFileName, graphBuilderLocation)
                Main.main(args2)
            }
        }.apply {
            group = "stat"
        }
    }
}