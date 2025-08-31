package core.game.system.command.sets

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.ServerConstants
import core.cache.Cache
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.cache.def.impl.*
import core.game.system.command.Privilege
import core.game.world.map.RegionManager
import core.game.world.map.RegionPlane
import core.plugin.Initializable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Initializable
class CacheCommandSet : CommandSet(Privilege.ADMIN) {

    override fun defineCommands() {
        define(
            name = "findobjects",
            privilege = Privilege.ADMIN,
            usage = "::findobjects <option>",
            description = "Scans loaded regions for all objects with the given option. Toggle preload maps to true."
        ) { player, args ->

            val option = args?.getOrNull(1)?.lowercase() ?: run {
                player.debug("Usage: ::findobjects <option>")
                return@define
            }

            val exportDir = File("dumps")
            if (!exportDir.exists()) exportDir.mkdirs()

            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM_HH-mm"))
            val dump = File(exportDir, "object_dump_$timestamp.txt")

            GlobalScope.launch {
                var found = 0
                dump.bufferedWriter().use { writer ->
                    for (region in RegionManager.regionCache.values) {
                        for (z in 0..3) {
                            val plane = region.planes[z] ?: continue
                            val grid = plane.objects ?: continue
                            for (x in 0 until RegionPlane.REGION_SIZE) {
                                for (y in 0 until RegionPlane.REGION_SIZE) {
                                    val scenery = grid[x][y] ?: continue
                                    val def = SceneryDefinition.forId(scenery.id) ?: continue
                                    if (def.options?.any { it.equals(option, ignoreCase = true) } == true) {
                                        found++
                                        val loc = scenery.location
                                        writer.write("${scenery.id} - (${loc.x}, ${loc.y}, ${loc.z}) [region=${region.id}]\n")
                                    }
                                }
                            }
                        }
                    }
                }
                player.debug("Saved $found objects with option '$option' -> ${dump.path}")
            }
        }

        define(
            name = "droptabledesc",
            privilege = Privilege.ADMIN,
            usage = "::droptabledesc",
            description = "Fill descriptions for drop tables."
        ) { player, _ ->

            val gson = GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()

            val sourceFile = File(ServerConstants.CONFIG_PATH + "drop_tables.json")
            val outputFile = File(ServerConstants.CONFIG_PATH + "drop_table_descriptions.json")

            if (!sourceFile.exists()) {
                player.debug("drop_tables.json not found.")
                return@define
            }

            val configs = gson.fromJson(FileReader(sourceFile), JsonArray::class.java)
            var filledCount = 0

            for (element in configs) {
                val e = element.asJsonObject
                val currentDescription = e.get("description")?.asString ?: ""

                if (currentDescription.isEmpty()) {
                    val ids = e.get("ids")?.asString
                        ?.split(",")
                        ?.mapNotNull { it.trim().toIntOrNull() }
                        ?: continue

                    val firstNpcDescription = ids.mapNotNull { npcId ->
                        NPCDefinition.forId(npcId)
                    }.firstOrNull()?.let { "${it.name} (level: ${it.combatLevel})" }

                    if (firstNpcDescription != null) {
                        e.addProperty("description", firstNpcDescription)
                        filledCount++
                    }
                }
            }

            FileWriter(outputFile).use { writer ->
                gson.toJson(configs, writer)
            }

            player.debug("Filled empty descriptions for $filledCount drop table entries. Output: ${outputFile.name}")
        }

        /*
         * Print xteas to txt.
         */

        define(
            name = "dumpxteas",
            privilege = Privilege.ADMIN,
            usage = "::dumpxteas",
            description = "Dumps XTEA keys from xteas.json into individual txt files."
        ) { player, _ ->

            val gson = GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()

            val source = File(ServerConstants.CONFIG_PATH + "xteas.json")
            val output = File(ServerConstants.CONFIG_PATH + "xteas_txt")

            if (!output.exists())
                output.mkdirs()

            val data = gson.fromJson(FileReader(source), JsonObject::class.java)
            val xteas = data.getAsJsonArray("xteas")

            for (entry in xteas) {
                val obj = entry.asJsonObject
                val regionId = obj.get("regionId").asInt
                val keysString = obj.get("keys").asString
                val keys = keysString.split(",").map { it.trim() }

                val filePath = File(output, "$regionId.txt")

                filePath.printWriter().use { writer ->
                    keys.forEach { key ->
                        writer.println(key)
                    }
                }
                player.debug("Dumped XTEA keys to txt=${filePath.name}")
            }
        }

        /*
         * Modify object_configs.json.
         */

        define(
            name = "dumpobjects",
            privilege = Privilege.ADMIN,
            usage = "::dumpobjects",
            description = "A dump that allows you to add data to objects_configs."
        ) { player, _ ->

            val gson = GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()

            val sourceFile = File(ServerConstants.CONFIG_PATH + "object_configs.json")
            val outputFile = File(ServerConstants.CONFIG_PATH + "object_configs_dump.json")

            if (!sourceFile.exists()) {
                player.debug("object_configs.json not found.")
                return@define
            }

            val configs = gson.fromJson(FileReader(sourceFile), JsonArray::class.java)

            val dataMap = linkedMapOf<Pair<String, String>, MutableSet<Int>>()

            for (element in configs) {
                val e = element.asJsonObject
                val ids = e.get("ids")?.asString
                    ?.split(",")
                    ?.mapNotNull { it.trim().toIntOrNull() }
                    ?: continue

                val firstId = ids.firstOrNull() ?: continue
                val objDef = SceneryDefinition.forId(firstId)

                val name = objDef?.name ?: "null"
                val examine = objDef?.examine ?: e.get("examine")?.asString ?: "null"

                dataMap.computeIfAbsent(examine to name) { linkedSetOf() }.addAll(ids)
            }

            val outputArray = JsonArray()
            for ((key, ids) in dataMap) {
                val (examine, name) = key
                val ordered = JsonObject().apply {
                    addProperty("name", name)
                    addProperty("ids", ids.joinToString(","))
                    addProperty("examine", examine)
                }
                outputArray.add(ordered)
            }

            FileWriter(outputFile).use { writer ->
                gson.toJson(outputArray, writer)
            }

            player.debug("Dumped ${outputArray.size()} grouped object configs to ${outputFile.name}.")
        }

        /*
         * Split npc spawns.
         */

        define(
            name = "splitspawns",
            privilege = Privilege.ADMIN,
            usage = "::splitspawns",
            description = "Splits npc_spawns.json into region-based json (merged loc_data)."
        ) { player, _ ->

            val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
            val sourceFile = File(ServerConstants.CONFIG_PATH + "npc_spawns.json")
            val outputDir = File(ServerConstants.CONFIG_PATH + "npc_spawns/")
            outputDir.mkdirs()

            if (!sourceFile.exists()) {
                player.debug("npc_spawns.json not found.")
                return@define
            }

            val configs = gson.fromJson(FileReader(sourceFile), JsonArray::class.java)
            val regionMap = mutableMapOf<Int, MutableMap<Int, JsonObject>>() // region -> npc_id -> object

            for (configElement in configs) {
                val e = configElement.asJsonObject
                val npcId = e.get("npc_id").asInt
                val npcName = NPCDefinition.forId(npcId)?.name ?: "null"

                val locData = e.get("loc_data").asString
                val points = locData.split("-").map { it.trim() }.filter { it.isNotEmpty() }

                for (point in points) {
                    val tokens = point.removePrefix("{").removeSuffix("}").split(",").map { it.trim() }
                    if (tokens.size < 5) continue

                    val x = tokens[0].toInt()
                    val y = tokens[1].toInt()
                    val regionId = (x shr 6 shl 8) or (y shr 6)

                    val regionEntries = regionMap.computeIfAbsent(regionId) { mutableMapOf() }
                    val npcEntry = regionEntries.computeIfAbsent(npcId) {
                        JsonObject().apply {
                            addProperty("name", npcName)
                            addProperty("npc_id", npcId.toString())
                            addProperty("loc_data", "")
                        }
                    }

                    val updatedLocData = npcEntry.get("loc_data").asString + "$point-"
                    npcEntry.addProperty("loc_data", updatedLocData)
                }
            }

            for ((regionId, npcMap) in regionMap) {
                val outFile = File(outputDir, "region_$regionId.json")
                FileWriter(outFile).use { writer ->
                    gson.toJson(npcMap.values.toList(), writer)
                }
            }

            player.debug("Split ${configs.size()} NPC spawns into ${regionMap.size} regions.")
        }

        /*
         * Dump RangeWeapon definitions.
         */

        define(
            name = "dumpranged",
            privilege = Privilege.ADMIN,
            usage = "::dumpranged",
            description = "Dumps all RangeWeapon definitions to a .txt file."
        ) { player, _ ->

            val dump = File("dumps/ranged_weapons_config.txt")
            dump.parentFile.mkdirs()

            val builder = StringBuilder()

            core.game.node.entity.combat.equipment.RangeWeapon.getAll().forEach { weapon ->
                builder.appendLine(weapon.toString())
            }

            dump.writeText(builder.toString())

            player.debug("RangeWeapon definitions dumped to $dump.")
        }

        /*
         * Dump ammunition definitions.
         */

        define(
            name = "dumpammo",
            privilege = Privilege.ADMIN,
            usage = "::dumpammo",
            description = "Dumps all Ammunition definitions to a .txt file."
        ) { player, _ ->

            val dump = File("dumps/ammunition_config.txt")
            dump.parentFile.mkdirs()

            val builder = StringBuilder()

            core.game.node.entity.combat.equipment.Ammunition.getAll().forEach { ammo ->
                builder.appendLine(ammo.toString())
            }

            dump.writeText(builder.toString())

            player.debug("Ammunition definitions dumped to $dump.")
        }

        /*
         * Dump render animations.
         */

        define(
            name = "dumprenders",
            privilege = Privilege.ADMIN,
            usage = "::dumprenders",
            description = "Dumps all RenderAnimationDefinition definitions to a .txt file.",
        ) { p, _ ->
            val dumpFile = File("dumps/render_animations.txt")

            dumpFile.parentFile?.let { parent ->
                if (!parent.exists()) parent.mkdirs()
            }

            val result = mutableListOf<String>()
            val index = CacheIndex.CONFIGURATION

            val fileCount = Cache.getArchiveFileCount(index, 32)
            if (fileCount <= 0) {
                p.debug("No BAS_TYPE archive found.")
                return@define
            }

            for (id in 0 until fileCount) {
                val data = Cache.getData(index, 32, id) ?: continue

                try {
                    val def = RenderAnimationDefinition.forId(id) ?: continue
                    val builder = StringBuilder()
                    builder.append("RenderAnimationDefinition ID: $id\n")

                    RenderAnimationDefinition::class.java.declaredFields
                        .filter { !Modifier.isStatic(it.modifiers) }
                        .forEach { field ->
                            field.isAccessible = true
                            val value = field.get(def)

                            if (value is Array<*>) {
                                builder.append("${field.name} = [${value.joinToString()}]\n")
                            } else {
                                builder.append("${field.name} = $value\n")
                            }
                        }

                    builder.append("\n")
                    result.add(builder.toString())
                } catch (e: Exception) {
                    println("Error parsing render animation id $id: ${e.message}")
                }
            }

            dumpFile.writeText(result.joinToString(separator = "\n"))
            p.debug("Render animation definitions dumped to ${dumpFile.path}.")
        }

        /*
         * Dump datamaps.
         */

        define(
            name = "dumpdatamaps",
            privilege = Privilege.ADMIN,
            usage = "::dumpdatamaps",
            description = "Dumps all DataMap definitions to a .txt file.",
        ) { p, _ ->
            val dump = File("dumps/datamaps_config.txt")

            dump.parentFile?.let { parent ->
                if (!parent.exists()) parent.mkdirs()
            }

            val dataMapStrings = mutableListOf<String>()
            val index = CacheIndex.ENUM_CONFIGURATION
            val archiveCount = Cache.getIndex(index).archives().size

            for (archiveId in 0 until archiveCount) {
                val fileCount = Cache.getArchiveFileCount(index, archiveId)
                if (fileCount <= 0) continue

                for (fileId in 0 until fileCount) {
                    val id = (archiveId shl 8) or fileId
                    val data = Cache.getData(index, archiveId, fileId) ?: continue

                    try {
                        val def = DataMap.get(id)
                        dataMapStrings.add(def.toString())
                    } catch (e: Exception) {
                        println("Error parsing DataMap ID $id: ${e.message}")
                    }
                }
            }

            dump.printWriter().use { writer ->
                dataMapStrings.forEach { writer.println(it) }
            }

            p.debug("DataMap have been successfully dumped to ${dump.path}.")
        }

        /*
         * Show icons.
         */

        define(
            name = "dumpicons",
            privilege = Privilege.ADMIN,
            usage = "::dumpicons",
            description = "Shows all icons available with ids.",
        ) { p, _ ->
            val maxIconId = 4
            for (iconId in 0..maxIconId) {
                p.debug("Icon sprite: <img=$iconId> Icon ID: $iconId")
            }
        }

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

                    writer.write("Child: $index")

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
         * Dumps for educational purposes identity kit configurations to a .csv file.
         */

        define(
            name = "dumpidk",
            privilege = Privilege.ADMIN,
            usage = "::dumpidk",
            description = "Dumps identity kits data to a .json file.",
        ) { p, _ ->

            val length = Cache.getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.IDK_TYPE)
            val dump = File("dumps/identity_kits.json")
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

            try {
                dump.parentFile?.let { parent ->
                    if (!parent.exists()) parent.mkdirs()
                }
                val dataList = mutableListOf<Map<String, Any?>>()

                for (i in 0 until length) {
                    val def = ClothDefinition.forId(i) ?: continue

                    val bodyModelIdsString = def.bodyModelIds?.joinToString(";") { it.toString() }
                    val headModelIdsString = def.headModelIds?.joinToString(";") { it.toString() }

                    val map = mapOf(
                        "id" to i,
                        "bodyPartId" to def.bodyPartId,
                        "bodyModelIds" to bodyModelIdsString,
                        "isSelectable" to !def.notSelectable,
                        "headModelIds" to headModelIdsString
                    )

                    dataList.add(map)
                }

                dump.bufferedWriter().use { writer ->
                    gson.toJson(dataList, writer)
                }

                p.debug("Identity kits data has been successfully dumped to $dump.")
            } catch (e: IOException) {
                e.printStackTrace()
                p.debug("Error writing to JSON file: ${e.message}")
            }
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
            val dump = File("dumps/item_definitions.json")
            dump.parentFile?.let { parent ->
                if (!parent.exists()) parent.mkdirs()
            }
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
            description = "Dumps CS2 mapping data to a .json file.",
        ) { p, _ ->

            val dump = File("dumps/clientscripts.json")
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

            try {
                val allProperties = CS2Mapping::class.memberProperties.map { it.name }
                val dataList = mutableListOf<Map<String, Any?>>()

                for (itemId in 0 until 6000) {
                    val itemDef = CS2Mapping.forId(itemId) ?: continue

                    val map = allProperties.associateWith { propName ->
                        val prop = CS2Mapping::class.memberProperties.find { it.name == propName }
                        prop?.let {
                            it.isAccessible = true
                            try {
                                val value = it.getter.call(itemDef)
                                when (value) {
                                    is Array<*> -> value.joinToString(";") { it.toString() }
                                    is List<*> -> value.joinToString(";") { it.toString() }
                                    is Map<*, *> -> value
                                    null -> null
                                    else -> value
                                }
                            } catch (e: Exception) {
                                "Error"
                            }
                        } ?: null
                    }

                    dataList.add(map)
                }
                dump.bufferedWriter().use { writer ->
                    gson.toJson(dataList, writer)
                }

                p.debug("CS2 data has been successfully dumped to $dump.")
            } catch (e: IOException) {
                p.debug("Error writing to JSON file: ${e.message}")
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
            description = "Dumps structs data to a .json file.",
        ) { player, _ ->

            val dump = File("dumps/structs.json")
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

            try {
                dump.parentFile?.let { parent ->
                    if (!parent.exists()) {
                        parent.mkdirs()
                    }
                }

                val dataList = mutableListOf<Map<String, Any?>>()

                val archiveFileCount = Cache.getArchiveFileCount(CacheIndex.CONFIGURATION, 26)
                for (fID in 0 until archiveFileCount) {
                    val file = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.STRUCT_TYPE, fID)
                    if (file != null) {
                        val def = Struct.decode(fID, file)
                        val map = mapOf(
                            "id" to def.id,
                            "data" to when (val data = def.dataStore) {
                                is Array<*> -> data.joinToString(";") { it.toString() }
                                is Iterable<*> -> data.joinToString(";") { it.toString() }
                                null -> null
                                else -> data.toString()
                            }
                        )
                        dataList.add(map)
                    }
                }

                dump.bufferedWriter().use { writer ->
                    gson.toJson(dataList, writer)
                }

                player.debug("Struct data has been successfully dumped to $dump.")
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
            dump.parentFile?.let { parent ->
                if (!parent.exists()) parent.mkdirs()
            }
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
