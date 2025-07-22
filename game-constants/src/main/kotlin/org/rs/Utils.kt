package org.rs

import java.io.File
import org.rs.consts.*

object Utils {
    @JvmStatic
    fun duplicateDetection(vararg classes: Class<*>, report: File) {
        val rows = mutableListOf(listOf("Class", "Value", "Fields"))

        for (clazz in classes) {
            val valueMap = mutableMapOf<Int, MutableList<String>>()

            clazz.declaredFields
                .filter {
                    java.lang.reflect.Modifier.isStatic(it.modifiers) &&
                            java.lang.reflect.Modifier.isFinal(it.modifiers)
                }
                .filter {
                    it.type == Int::class.javaPrimitiveType || it.type == Integer::class.java
                }
                .forEach {
                    it.isAccessible = true
                    val value = it.getInt(null)
                    valueMap.getOrPut(value) { mutableListOf() }.add(it.name)
                }

            val duplicates = valueMap.filter { it.value.size > 1 }
            duplicates.forEach { (value, fields) ->
                rows.add(listOf(clazz.simpleName, value.toString(), fields.joinToString(", ")))
            }
        }

        if (rows.size == 1) {
            println("Report complete: no conflicts detected.")
            return
        }
        report.parentFile?.mkdirs()
        report.printWriter().use { out ->
            rows.forEach { row -> out.println(row.joinToString(";")) }
        }

        println("Report completed. Path: [${report.absolutePath}]")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val reportFile = if (args.isNotEmpty()) File(args[0]) else File("duplicates_report.csv")
        duplicateDetection(
            Graphics::class.java,
            Items::class.java,
            Music::class.java,
            Network::class.java,
            NPCs::class.java,
            Quests::class.java,
            Regions::class.java,
            Scenery::class.java,
            Sounds::class.java,
            Vars::class.java,
            Animations::class.java,
            Components::class.java,
            report = reportFile
        )
    }
}
