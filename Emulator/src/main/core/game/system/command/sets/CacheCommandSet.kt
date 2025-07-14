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

            val exportDir = File("components")
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
            val dump = File("interface_definitions.json")
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

            val dump = File("identity_kits.csv")
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
            val dump = File("item_definitions.json")
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
            val dump = File("clientscripts.csv")
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
                val dump = File("structs.csv")
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
                val dump = File("datamaps.json")
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

            val dump = File("npc_definitions.json")
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

        /*
         * Dumps the component definitions into a .json file.
         */

        define(
            name = "dumpcomponents",
            privilege = Privilege.ADMIN,
            usage = "::dumpcomponents",
            description = "Dumps component definitions data to a .json file.",
        ) { p, _ ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val dump = File("component_definitions.json")
            val components = mutableListOf<Map<String, Any?>>()

            for ((id, componentDef) in ComponentDefinition.definitions) {
                val componentMap =
                    mapOf(
                        "id" to id,
                        "type" to componentDef.type.name,
                        "walkable" to componentDef.isWalkable,
                        "tabIndex" to componentDef.tabIndex,
                        "plugin" to componentDef.plugin?.javaClass?.simpleName,
                    )
                components.add(componentMap)
            }

            dump.writeText(gson.toJson(components))
            p.debug("Component data successfully dumped to $dump.")
        }

        /*
         * Dumps the animation definitions into a .json file.
         */

        define(
            name = "dumpanimations",
            privilege = Privilege.ADMIN,
            usage = "::dumpanimations",
            description = "Dumps animation definitions data to a .json file.",
        ) { p, _ ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val dump = File("animation_definitions.json")
            val animations = mutableListOf<Map<String, Any?>>()

            for ((id, animDef) in AnimationDefinition.getDefinition()) {
                val animationMap =
                    mapOf(
                        "id" to id,
                        "duration" to animDef.getDuration(),
                        "cycles" to animDef.getCycles(),
                        "duration" to animDef.getDurationTicks(),
                        "rightHandItem" to animDef.rightHandItem,
                        "hasSoundEffect" to animDef.hasSoundEffect,
                        "effect2Sound" to animDef.effect2Sound,
                    )
                animations.add(animationMap)
            }

            dump.writeText(gson.toJson(animations))
            p.debug("Animation data has been successfully dumped to $dump.")
        }

        /*
         * Dumps the varbit definitions into a .json file.
         */

        define(
            name = "dumpvarbits",
            privilege = Privilege.ADMIN,
            usage = "::dumpvarbits",
            description = "Dumps all varbit definitions to a .json file.",
        ) { p, _ ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val varbits = mutableListOf<Map<String, Any?>>()
            val dump = File("varbit_definitions.json")
            val maxId = Cache.getIndexCapacity(CacheIndex.CONFIGURATION)

            var startId: Int? = null
            var last: VarbitDefinition? = null

            fun addGroup(start: Int, end: Int, def: VarbitDefinition) {
                val group = if (start != end)
                    mapOf(
                        "idRange" to listOf(start, end),
                        "varpId" to def.varpId,
                        "startBit" to VarbitDefinition.forId(start).startBit,
                        "endBit" to VarbitDefinition.forId(end).endBit
                    )
                else
                    mapOf(
                        "id" to start,
                        "varpId" to def.varpId,
                        "startBit" to def.startBit,
                        "endBit" to def.endBit
                    )
                varbits.add(group)
            }

            for (id in 0 until maxId) {
                val def = runCatching { VarbitDefinition.forId(id) }.getOrNull() ?: continue
                if (last != null && def.varpId == last.varpId && def.startBit == last.endBit + 1 && def.endBit == def.startBit) {
                    last = def
                } else {
                    if (startId != null && last != null) addGroup(startId, id - 1, last)
                    startId = id
                    last = def
                }
            }

            if (startId != null && last != null) addGroup(startId, maxId - 1, last)

            dump.writeText(gson.toJson(varbits))
            p.debug("Varbit definitions have been successfully dumped to $dump.")
        }

        /*
         * Dumps the CS2Mapping definitions into a .txt file.
         */

        define(
            name = "dumpcs2txt",
            privilege = Privilege.ADMIN,
            usage = "::dumpcs2txt",
            description = "Dumps CS2Mapping data to a .txt file.",
        ) { p, _ ->
            val dump = File("cs2_mappings.txt")
            val sb = StringBuilder()

            for (scriptId in 0 until 10000) {
                val mapping = CS2Mapping.forId(scriptId) ?: continue
                val map = mapping.map ?: continue

                sb.append("Script ID: ${mapping.scriptId}\n")
                sb.append("Unknown: ${mapping.unknown}\n")
                sb.append("Unknown1: ${mapping.unknown1}\n")
                sb.append("Default String: ${mapping.defaultString}\n")
                sb.append("Default Int: ${mapping.defaultInt}\n")
                sb.append("Map:\n")
                for ((key, value) in map) {
                    sb.append("  Key: $key, Value: $value\n")
                }
                mapping.array?.let { array ->
                    sb.append("Array: ${array.joinToString(", ")}\n")
                }
                sb.append("\n")
            }

            dump.writeText(sb.toString())
            p.debug("CS2Mapping data has been successfully dumped to $dump.")
        }

        /*
         * Commands to prints and display the region name based on the given region ID.
         */

        define(
            name = "regioncache",
            privilege = Privilege.ADMIN,
            usage = "::regioncache <region_id>",
            description = "Display the region name based on the given region ID.",
        ) { player, args ->

            if (args.isEmpty()) {
                player.debug("Please specify a region ID. Usage: ::regioncache <region_id>")
                return@define
            }
            val regionId = args[1].toIntOrNull()

            if (regionId == null) {
                player.debug("Invalid region ID. Please provide a valid number.")
                return@define
            }

            val x = (regionId shr 8) and 0xFF
            val y = regionId and 0xFF

            val regionName = "l${x}_$y"

            player.debug("The region name for region ID [$regionId] is: [$regionName].")
        }

        /*
         * Commands to prints cache item values.
         */

        define(
            name = "itemprices",
            privilege = Privilege.ADMIN,
            usage = "::itemprices",
            description = "Creates an item price index.",
        ) { player, _ ->
            val file = File("530_cache_item_values_from_client.csv")
            PrintWriter(file).use { writer ->
                writer.println("id,name,cost")
                for (item in ItemDefinition.definitions.values) {
                    writer.println("${item.id},${item.name},${item.value}")
                }
            }
            player.debug("Item price index dumped to $file.")
        }

        /*
         * Commands to prints Animation definitions.
         */

        define(
            name = "animdefs",
            privilege = Privilege.ADMIN,
            usage = "::animdefs",
            description = "Dumps animation definitions to a file.",
        ) { player, _ ->
            val file = File("animation_dump.txt")
            BufferedWriter(FileWriter(file)).use { writer ->
                for ((animationId, animationDef) in AnimationDefinition.getDefinition()) {
                    val frameIds = animationDef.frames
                    val frameLengths = animationDef.duration ?: intArrayOf()
                    val cycleLength = animationDef.getDurationTicks()
                    writer.write("Animation ID: $animationId\n")
                    writer.write("Cycle Length: $cycleLength\n")
                    writer.write("Frame IDs: ${frameIds!!.joinToString(",")}\n")
                    writer.write("Frame Lengths: ${frameLengths.joinToString(",")}\n")
                    writer.write("-----\n")
                }
            }
            player.debug("Animation definitions have been dumped to $file.")
            return@define
        }
    }
}
