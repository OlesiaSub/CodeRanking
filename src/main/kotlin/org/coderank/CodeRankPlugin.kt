package org.coderank

import org.gradle.api.Plugin
import org.gradle.api.Project

import CodeRank.app.src.main.java.ru.hse.coderank.analysis.main.Main

class CodeRankPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<CodeRankExtension>("coderank", CodeRankExtension::class.java)
        project.tasks.create("codeRank") { task ->
            task.doLast {
                //val args = arrayOf("/home/olesya/HSE_2020-1/java/maze/out/artifacts/maze_jar/maze.jar")
                //Main.main(args)
                println("Starting task codeRank...")
                val args2 = arrayOf(extension.inputJarFileName.get())
                Main.main(args2)
            }
        }.apply {
            group = "stat"
        }
    }
}