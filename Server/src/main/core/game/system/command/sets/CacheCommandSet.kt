package core.game.system.command.sets

import com.google.gson.GsonBuilder
import core.cache.Cache
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.cache.def.impl.*
//import core.cache.def.type.*
import core.game.component.ComponentDefinition
import core.game.system.command.Privilege
import core.plugin.Initializable
import java.io.*
import java.lang.reflect.Modifier
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Initializable
class CacheCommandSet : CommandSet(Privilege.ADMIN) {

    override fun defineCommands() {

        /*
         * Dumps detailed info about interface.
         */

        define(
            name = "dumpiface",
            privilege = Privilege.ADMIN,
            usage = "::dumpiface <interfaceId>",
            description = "Dumps detailed info about interface.",
        ) { player, args ->
            if (args.size < 2) {
                player.debug("Usage: ::dumpiface <interfaceId>")
                return@define
            }

            val ifaceId = args[1].toIntOrNull()
            if (ifaceId == null) {
                player.debug("Invalid interface ID: ${args[1]}")
                return@define
            }

            val ifaceDef = try {
                IfaceDefinition.forId(ifaceId)
            } catch (e: Exception) {
                player.debug("Error loading interface $ifaceId: ${e.message}")
                null
            }

            if (ifaceDef == null) {
                player.debug("Interface $ifaceId not found.")
                return@define
            }

            val children = ifaceDef.children ?: emptyArray()
            if (children.isEmpty()) {
                player.debug("Interface $ifaceId has no children.")
                return@define
            }

            val exportDir = File("dumps/components")
            if (!exportDir.exists()) exportDir.mkdirs()
            val dump = File(exportDir, "$ifaceId.txt")

            val writer = dump.bufferedWriter()
            try {
                children.forEachIndexed { index, child ->
                    if (child == null) return@forEachIndexed

                    val isEmpty = (child.type == null) && (child.baseWidth ?: 0) == 0 && (child.baseHeight ?: 0) == 0
                    if (isEmpty) return@forEachIndexed

                    writer.write("Child: $index, type: ${child.type ?: "unknown"}, width: ${child.baseWidth ?: 0}, height: ${child.baseHeight ?: 0}\n")

                    fun appendIfNotDefault(name: String, value: Any?, default: Any?) {
                        if (value != null && value != default) {
                            writer.write("  $name: $value\n")
                        }
                    }

                    appendIfNotDefault("clientCode", child.clientCode, 0)
                    appendIfNotDefault("baseX", child.baseX, 0)
                    appendIfNotDefault("baseY", child.baseY, 0)
                    appendIfNotDefault("baseWidth", child.baseWidth, 0)
                    appendIfNotDefault("baseHeight", child.baseHeight, 0)
                    appendIfNotDefault("dynWidth", child.dynWidth, 0)
                    appendIfNotDefault("dynHeight", child.dynHeight, 0)
                    appendIfNotDefault("xMode", child.xMode, 0)
                    appendIfNotDefault("yMode", child.yMode, 0)
                    appendIfNotDefault("overlayer", child.overlayer, 0)
                    appendIfNotDefault("hidden", child.hidden, false)
                    appendIfNotDefault("scrollMaxH", child.scrollMaxH, 0)
                    appendIfNotDefault("scrollMaxV", child.scrollMaxV, 0)
                    appendIfNotDefault("noClickThrough", child.noClickThrough, false)
                    appendIfNotDefault("spriteId", child.spriteId, 0)
                    appendIfNotDefault("activeSpriteId", child.activeSpriteId, 0)
                    appendIfNotDefault("angle2d", child.angle2d, 0)
                    appendIfNotDefault("hasAlpha", child.hasAlpha, false)
                    appendIfNotDefault("spriteTiling", child.spriteTiling, false)
                    appendIfNotDefault("alpha", child.alpha, 0)
                    appendIfNotDefault("outlineThickness", child.outlineThickness, 0)
                    appendIfNotDefault("shadowColor", child.shadowColor, 0)
                    appendIfNotDefault("hFlip", child.hFlip, false)
                    appendIfNotDefault("vFlip", child.vFlip, false)
                    appendIfNotDefault("modelType", child.modelType, 0)
                    appendIfNotDefault("activeModelType", child.activeModelType, 0)
                    appendIfNotDefault("modelId", child.modelId, 0)
                    appendIfNotDefault("activeModelId", child.activeModelId, 0)
                    appendIfNotDefault("unknownModelProp_1", child.unknownModelProp_1, 0)
                    appendIfNotDefault("unknownModelProp_2", child.unknownModelProp_2, 0)
                    appendIfNotDefault("modelXAngle", child.modelXAngle, 0)
                    appendIfNotDefault("modelYAngle", child.modelYAngle, 0)
                    appendIfNotDefault("modelYOffset", child.modelYOffset, 0)
                    appendIfNotDefault("modelZoom", child.modelZoom, 0)
                    appendIfNotDefault("modelAnimId", child.modelAnimId, 0)
                    appendIfNotDefault("activeModelAnimId", child.activeModelAnimId, 0)
                    appendIfNotDefault("modelOrtho", child.modelOrtho, false)
                    appendIfNotDefault("unknownModelProp_3", child.unknownModelProp_3, 0)
                    appendIfNotDefault("unknownModelProp_4", child.unknownModelProp_4, 0)
                    appendIfNotDefault("unknownModelProp_5", child.unknownModelProp_5, false)
                    appendIfNotDefault("unknownModelProp_6", child.unknownModelProp_6, 0)
                    appendIfNotDefault("unknownModelProp_7", child.unknownModelProp_7, 0)
                    appendIfNotDefault("font", child.font, 0)
                    appendIfNotDefault("text", child.text, null)
                    appendIfNotDefault("activeText", child.activeText, null)
                    appendIfNotDefault("vPadding", child.vPadding, 0)
                    appendIfNotDefault("halign", child.halign, 0)
                    appendIfNotDefault("valign", child.valign, 0)
                    appendIfNotDefault("shadowed", child.shadowed, false)
                    appendIfNotDefault("color", child.color, 0)
                    appendIfNotDefault("activeColor", child.activeColor, 0)
                    appendIfNotDefault("overColor", child.overColor, 0)
                    appendIfNotDefault("unknownColor", child.unknownColor, 0)
                    appendIfNotDefault("filled", child.filled, false)
                    appendIfNotDefault("lineWidth", child.lineWidth, 0)
                    appendIfNotDefault("unknownProp_8", child.unknownProp_8, false)
                    if (child.unknownIntArray_1 != null) writer.write("  unknownIntArray_1: ${child.unknownIntArray_1!!.joinToString(",")}\n")
                    if (child.unknownIntArray_2 != null) writer.write("  unknownIntArray_2: ${child.unknownIntArray_2!!.joinToString(",")}\n")
                    if (child.unknownByteArray_1 != null) writer.write("  unknownByteArray_1: ${child.unknownByteArray_1!!.joinToString(",")}\n")
                    if (child.unknownByteArray_2 != null) writer.write("  unknownByteArray_2: ${child.unknownByteArray_2!!.joinToString(",")}\n")
                    appendIfNotDefault("optionBase", child.optionBase, null)
                    if (child.ops != null) writer.write("  ops: ${child.ops!!.joinToString(",")}\n")
                    appendIfNotDefault("dragDeadzone", child.dragDeadzone, 0)
                    appendIfNotDefault("dragDeadtime", child.dragDeadtime, 0)
                    appendIfNotDefault("dragRenderBehavior", child.dragRenderBehavior, false)
                    appendIfNotDefault("opCircumfix", child.opCircumfix, null)
                    appendIfNotDefault("opSuffix", child.opSuffix, null)
                    appendIfNotDefault("option", child.option, null)
                    appendIfNotDefault("unknownProp_9", child.unknownProp_9, 0)
                    appendIfNotDefault("unknownProp_10", child.unknownProp_10, 0)
                    appendIfNotDefault("unknownProp_11", child.unknownProp_11, 0)
                    if (child.cs1ComparisonOperands != null) writer.write("  cs1ComparisonOperands: ${child.cs1ComparisonOperands!!.joinToString(",")}\n")
                    if (child.cs1ComparisonOpcodes != null) writer.write("  cs1ComparisonOpcodes: ${child.cs1ComparisonOpcodes!!.joinToString(",")}\n")
                    if (child.cs1Scripts != null) {
                        writer.write("  cs1Scripts:\n")
                        child.cs1Scripts!!.forEachIndexed { i, arr ->
                            writer.write("    Script $i: ${arr?.joinToString(",") ?: "null"}\n")
                        }
                    }
                    if (child.objCounts != null) writer.write("  objCounts: ${child.objCounts!!.joinToString(",")}\n")
                    if (child.objTypes != null) writer.write("  objTypes: ${child.objTypes!!.joinToString(",")}\n")
                    appendIfNotDefault("invMarginX", child.invMarginX, 0)
                    appendIfNotDefault("invMarginY", child.invMarginY, 0)
                    if (child.invOffsetX != null) writer.write("  invOffsetX: ${child.invOffsetX!!.joinToString(",")}\n")
                    if (child.invOffsetY != null) writer.write("  invOffsetY: ${child.invOffsetY!!.joinToString(",")}\n")
                    if (child.invSprite != null) writer.write("  invSprite: ${child.invSprite!!.joinToString(",")}\n")
                    appendIfNotDefault("buttonType", child.buttonType, 0)
                    if (child.invOptions != null) writer.write("  invOptions: ${child.invOptions!!.joinToString(",")}\n")
                    appendIfNotDefault("parent", child.parent, 0)

                    writer.write("\n")
                }
            } finally {
                writer.close()
            }

            player.debug("Interface $ifaceId dumped to file: $dump.")
        }

        /*
         * Dumps for educational purposes the interface id data.
         */

        define(
            name = "dumpinterfaces",
            privilege = Privilege.ADMIN,
            usage = "::dumpinterfaces",
            description = "Dumps all interface definitions to a .json file.",
        ) { p, _ ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val dump = File("dumps/interface_definitions.json")
            val interfaces = mutableListOf<Map<String, Any?>>()

            for (interfaceId in 0 until Cache.getIndexCapacity(CacheIndex.COMPONENTS)) {
                val ifaceDef =
                    try {
                        IfaceDefinition.forId(interfaceId)
                    } catch (e: Exception) {
                        println("Error loading interface ID $interfaceId: ${e.message}")
                        null
                    } ?: continue

                try {
                    val ifaceMap =
                        ifaceDef::class
                            .memberProperties
                            .filter { prop ->
                                prop.returnType.classifier !in
                                    listOf(
                                        IfaceDefinition::class,
                                        List::class,
                                        Map::class,
                                    )
                            }.associate { prop ->
                                prop.isAccessible = true
                                prop.name to (prop.getter.call(ifaceDef) ?: "null")
                            }

                    if (ifaceMap.isNotEmpty()) {
                        interfaces.add(ifaceMap)
                    }
                } catch (e: Exception) {
                    println("Error processing interface ID $interfaceId: ${e.message}")
                }
            }

            dump.writeText(gson.toJson(interfaces))
            p.debug("Interface definitions have been successfully dumped to $dump.")
        }

        /*
         * Dumps for educational purposes identity kit configurations to a .csv file.
         */

        define(
            name = "dumpidk",
            privilege = Privilege.ADMIN,
            usage = "::dumpidk",
            description = "Dumps identity kits data to a .csv file.",
        ) { p, _ ->
            val length = Cache.getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.IDK_TYPE)

            val dump = File("dumps/identity_kits.csv")
            val headers = listOf("id", "bodyPartId", "bodyModelIds", "isSelectable", "headModelIds")

            if (dump.exists()) {
                dump.delete()
            }

            val writer = dump.bufferedWriter()
            writer.appendLine(headers.joinToString(","))

            for (i in 0 until length) {
                val def = ClothDefinition.forId(i)

                val bodyModelIdsString = def.bodyModelIds?.joinToString(",") { it.toString() } ?: ""
                val headModelIdsString = def.headModelIds?.joinToString(",") { it.toString() }

                writer.appendLine("$i,${def.bodyPartId},$bodyModelIdsString,${def.notSelectable},$headModelIdsString")
            }

            writer.close()
            p.debug("Identity kits data has been successfully dumped to $dump.")
        }

        /*
         * Dumps for educational purposes item definitions into a .json.
         */

        define(
            name = "dumpitems",
            privilege = Privilege.ADMIN,
            usage = "::dumpitems",
            description = "Dumps item definitions data to a .json file.",
        ) { p, _ ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val dump = File("dumps/dumps/item_definitions.json")
            val items = mutableListOf<Map<String, Any?>>()

            for (itemId in 0 until Cache.getIndexCapacity(CacheIndex.ITEM_CONFIGURATION)) {
                val itemDef = ItemDefinition.forId(itemId) ?: continue
                val itemMap =
                    itemDef::class
                        .memberProperties
                        .filter { prop ->
                            prop.returnType.classifier !in listOf(ItemDefinition::class, List::class, Map::class)
                        }.associate { prop ->
                            prop.isAccessible = true
                            try {
                                prop.name to prop.getter.call(itemDef)
                            } catch (e: Exception) {
                                prop.name to "Error"
                            }
                        }

                if (itemMap.isNotEmpty()) {
                    items.add(itemMap)
                }
            }
            dump.writeText(gson.toJson(items))
            p.debug("Item data has been successfully dumped to $dump.")
        }

        /*
         * Dumps for educational purposes CS2 mapping data to a .csv file.
         */

        define(
            name = "dumpcs2",
            privilege = Privilege.ADMIN,
            usage = "::dumpcs2",
            description = "Dumps CS2 mapping data to a .csv file.",
        ) { p, _ ->
            val dump = File("dumps/clientscripts.csv")
            val gson = GsonBuilder().disableHtmlEscaping().create()

            try {
                BufferedWriter(FileWriter(dump)).use { writer ->
                    val allProperties = CS2Mapping::class.memberProperties.map { it.name }

                    writer.write(allProperties.joinToString(","))
                    writer.newLine()

                    for (itemId in 0 until 6000) {
                        val itemDef = CS2Mapping.forId(itemId) ?: continue

                        val values =
                            allProperties.map { propName ->
                                val prop = CS2Mapping::class.memberProperties.find { it.name == propName }
                                prop?.let {
                                    it.isAccessible = true
                                    try {
                                        val value = it.getter.call(itemDef)
                                        when (value) {
                                            is Array<*> -> value.joinToString(";") { it.toString() }
                                            is List<*> -> value.joinToString(";") { it.toString() }
                                            is Map<*, *> -> gson.toJson(value)
                                            null -> "null"
                                            else -> value.toString()
                                        }
                                    } catch (e: Exception) {
                                        "Error"
                                    }
                                } ?: "null"
                            }

                        writer.write(values.joinToString(", "))
                        writer.newLine()
                    }
                }

                p.debug("CS2 data has been successfully dumped to $dump.")
            } catch (e: IOException) {
                p.debug("Error writing to CSV file: ${e.message}")
                e.printStackTrace()
            }
        }

        /*
         * Dumps for educational purposes struct data to a .csv file.
         */

        define(
            name = "dumpstructs",
            privilege = Privilege.ADMIN,
            usage = "::dumpstructs",
            description = "Dumps structs data to a .csv file.",
        ) { player, _ ->
            try {
                val dump = File("dumps/structs.csv")
                val headers = listOf("id", "data")
                if (dump.exists()) {
                    dump.delete()
                }
                val writer = dump.bufferedWriter()
                writer.appendLine(headers.joinToString(", "))
                for (fID in 0 until Cache.getArchiveFileCount(CacheIndex.CONFIGURATION, 26)) {
                    val file = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.STRUCT_TYPE, fID)
                    if (file != null) {
                        val def = Struct.decode(fID, file)
                        if (def.dataStore.isNotEmpty()) {
                            val structData = def.dataStore.map { it.toString() }.joinToString(", ")
                            writer.appendLine("${def.id}, $structData")
                        }
                    }
                }
                writer.close()
                player.debug("Struct data has been successfully dumped to $dump.")
            } catch (e: IOException) {
                e.printStackTrace()
                reject(player, "Error writing to file: ${e.message}")
            }
        }

        /*
         * Dumps for educational purposes data map configurations to a .json file.
         */

        define(
            name = "dumpdatamaps",
            privilege = Privilege.ADMIN,
            usage = "::dumpdatamaps",
            description = "Dumps data maps configurations to a JSON file.",
        ) { player, _ ->
            try {
                val dump = File("dumps/datamaps.json")
                val gson = GsonBuilder().setPrettyPrinting().create()
                val dataMapsList = mutableListOf<Map<String, Any>>()
                val archiveIds = Cache.getIndexCapacity(CacheIndex.CONFIGURATION)

                if (archiveIds == 0) {
                    reject(player, "No archives found in index CONFIGURATION.")
                    return@define
                }

                for (archiveId in 0 until archiveIds) {
                    val archive = Cache.getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.STRUCT_TYPE)

                    for (fileId in 0 until archive) {
                        val fileData = Cache.getData(CacheIndex.CONFIGURATION, archiveId, fileId)

                        if (fileData == null || fileData.isEmpty()) {
                            // Skip invalid or empty files
                            continue
                        }

                        // Parse the file data
                        try {
                            val def = DataMap.decode(archiveId shl 8 or fileId, fileData)

                            val dataMap =
                                mapOf(
                                    "id" to def.id,
                                    "keyType" to def.keyType,
                                    "valueType" to
                                        when (def.valueType) {
                                            'K' -> "Normal"
                                            'J' -> "Struct Pointer"
                                            else -> "Unknown"
                                        },
                                    "defaultString" to (def.defaultString ?: "N/A"),
                                    "defaultInt" to def.defaultInt,
                                    "dataStore" to def.dataStore,
                                )

                            dataMapsList.add(dataMap)
                        } catch (e: Exception) {
                        }
                    }
                }

                // Write the data to a JSON file
                dump.writeText(gson.toJson(dataMapsList))
                player.debug("Data maps successfully dumped to $dump.")
            } catch (e: IOException) {
                e.printStackTrace()
                reject(player, "Error writing to file: ${e.message}")
            }
        }

        /*
         * Dumps for educational purposes the NPC definitions into a .json file.
         */

        define(
            name = "dumpnpcs",
            privilege = Privilege.ADMIN,
            usage = "::dumpnpcs",
            description = "Dumps NPC definitions data to a .json file.",
        ) { p, _ ->
            val gson =
                GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create()

            val dump = File("dumps/npc_definitions.json")
            val items = mutableListOf<Map<String, Any?>>()

            val excludedFields = setOf("handlers")

            for (npcId in 0 until Cache.getIndexCapacity(CacheIndex.NPC_CONFIGURATION)) {
                val npcDef = NPCDefinition.forId(npcId) ?: continue
                val npcMap = mutableMapOf<String, Any?>()

                for (prop in npcDef::class.memberProperties) {
                    if (prop.name in excludedFields) {
                        continue
                    }

                    prop.isAccessible = true
                    try {
                        val value = prop.getter.call(npcDef)

                        npcMap[prop.name] =
                            when (value) {
                                is String -> {
                                    if (prop.name == "combat_audio") {
                                        value
                                    } else {
                                        value
                                            .contains("Animation [priority=")
                                            .toString()
                                            .replace("Animation [priority=", "priority=")
                                            .replace("]", " ")
                                    }
                                }

                                is Number, is Boolean -> value
                                is List<*> -> value.map { it.toString() }
                                is Map<*, *> -> value.mapValues { it.value.toString() }
                                is ShortArray -> value.toList().map { it.toInt().toString() }
                                is ByteArray -> value.toList().map { it.toString() }
                                is IntArray -> value.toList().map { it.toInt().toString() }
                                is Array<*> ->
                                    when (value) {
                                        is ShortArray -> value.toList().map { it.toInt().toString() }
                                        is ByteArray -> value.toList().map { it.toString() }
                                        is IntArray -> value.toList().map { it.toInt().toString() }
                                        else -> value.toList().joinToString(",") { it.toString() }
                                    }

                                null -> "null"
                                else -> value.toString()
                            }
                    } catch (e: Exception) {
                        p.debug("Err read prop '${prop.name}': ${e.message}")
                        npcMap[prop.name] = "Err: ${e.message}"
                    }
                }

                if (npcMap.isNotEmpty()) {
                    items.add(npcMap)
                }
            }

            try {
                dump.writeText(gson.toJson(items))
                p.debug("NPC data has been successfully dumped to $dump.")
            } catch (e: Exception) {
                p.debug("Error saving JSON file: ${e.message}")
            }
        }
    }
}
