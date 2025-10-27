package core.game.system.command.sets

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.ServerConstants
import core.cache.Cache
import core.cache.Group
import core.cache.Archive
import core.cache.def.impl.*
import core.game.system.command.Privilege
import core.game.world.map.RegionManager
import core.game.world.map.RegionPlane
import core.plugin.Initializable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Paths
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
            name = "dumprenderanim",
            privilege = Privilege.ADMIN,
            usage = "::dumprenderanim",
            description = "Dumps all render animations to dumps/render_anim.txt",
        ) { player, _ ->
            val path = Paths.get("dumps/render_anim.txt")
            Files.createDirectories(path.parent)

            try {
                BufferedWriter(Files.newBufferedWriter(path)).use { bw ->
                    val capacity = Cache.getArchiveCapacity(Archive.JS5_CONFIG, Group.BAS_TYPE)
                    for (i in 0 until capacity) {
                        val def = RenderAnimationDefinition.forId(i) ?: continue
                        bw.append("RenderAnim $i -> ")
                        bw.append("stand=${def.standAnimationId}, ")
                        bw.append("walk=${def.walkAnimationId}, ")
                        bw.append("run=${def.runAnimationId}, ")
                        bw.append("turn180=${def.turn180Animation}, ")
                        bw.append("turnCW=${def.turnCWAnimation}, ")
                        bw.append("turnCCW=${def.turnCCWAnimation}")
                        bw.newLine()
                    }
                }
                player.debug("Render animations dumped successfully to dumps/render_anim.txt")
            } catch (e: Exception) {
                player.debug("Failed to dump render animations: ${e.message}")
                e.printStackTrace()
            }

            return@define
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
            val index = Archive.JS5_CONFIG_ENUM
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
            name = "dumpiface",
            privilege = Privilege.ADMIN,
            usage = "::dumpiface <id>",
            description = "Dumps interface definitions as JSON to a file.",
        ) { p, args ->

            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
            val dumps = File("dumps")
            if (!dumps.exists()) dumps.mkdirs()

            if (args.size < 2) {
                p.debug("Usage: ::dumpiface <id>")
                return@define
            }

            val i = args[1].toIntOrNull()
            if (i == null) {
                p.debug("Invalid interface id=$i.")
                return@define
            }

            val iface = IfaceDefinition.forId(i)
            if (iface == null) {
                p.debug("Interface for id=$i not found.")
                return@define
            }

            fun dumpIface(def: IfaceDefinition, isRoot: Boolean = false): Map<String, Any?>? {
                val map = mutableMapOf<String, Any?>()

                fun <T> addIfNotNull(key: String, value: T?) {
                    when (value) {
                        null -> return
                        is Int -> if (value == 0) return
                        is Boolean -> if (!value) return
                        is String -> if (value.isEmpty()) return
                        is IntArray -> {
                            val filtered = value.filter { it != 0 }
                            if (filtered.isEmpty()) return
                            map[key] = filtered
                            return
                        }
                        is Array<*> -> {
                            val filtered = value.mapNotNull {
                                when (it) {
                                    is IfaceDefinition -> dumpIface(it)
                                    is IntArray -> it.filter { v -> v != 0 }.takeIf { it.isNotEmpty() }
                                    is String -> if (it.isEmpty()) null else it
                                    else -> it
                                }
                            }.filterNotNull()
                            if (filtered.isEmpty()) return
                            map[key] = filtered
                            return
                        }
                    }
                    map[key] = value
                }

                if (isRoot) {
                    addIfNotNull("id", def.id)
                    addIfNotNull("parent", def.parent)
                }

                addIfNotNull("type", def.type?.name?.lowercase())
                addIfNotNull("version", def.version)
                addIfNotNull("clientCode", def.clientCode)
                addIfNotNull("baseX", def.baseX)
                addIfNotNull("baseY", def.baseY)
                addIfNotNull("baseWidth", def.baseWidth)
                addIfNotNull("baseHeight", def.baseHeight)
                addIfNotNull("dynWidth", def.dynWidth)
                addIfNotNull("dynHeight", def.dynHeight)
                addIfNotNull("xMode", def.xMode)
                addIfNotNull("yMode", def.yMode)
                addIfNotNull("overlayer", def.overlayer)
                addIfNotNull("hidden", def.hidden)
                addIfNotNull("scrollMaxH", def.scrollMaxH)
                addIfNotNull("scrollMaxV", def.scrollMaxV)
                addIfNotNull("noClickThrough", def.noClickThrough)
                addIfNotNull("spriteId", def.spriteId)
                addIfNotNull("activeSpriteId", def.activeSpriteId)
                addIfNotNull("angle2d", def.angle2d)
                addIfNotNull("hasAlpha", def.hasAlpha)
                addIfNotNull("spriteTiling", def.spriteTiling)
                addIfNotNull("alpha", def.alpha)
                addIfNotNull("outlineThickness", def.outlineThickness)
                addIfNotNull("shadowColor", def.shadowColor)
                addIfNotNull("hFlip", def.hFlip)
                addIfNotNull("vFlip", def.vFlip)
                addIfNotNull("modelType", def.modelType)
                addIfNotNull("activeModelType", def.activeModelType)
                addIfNotNull("modelId", def.modelId)
                addIfNotNull("activeModelId", def.activeModelId)
                addIfNotNull("unknownModelProp_1", def.unknownModelProp_1)
                addIfNotNull("unknownModelProp_2", def.unknownModelProp_2)
                addIfNotNull("modelXAngle", def.modelXAngle)
                addIfNotNull("modelYAngle", def.modelYAngle)
                addIfNotNull("modelYOffset", def.modelYOffset)
                addIfNotNull("modelZoom", def.modelZoom)
                addIfNotNull("modelAnimId", def.modelAnimId)
                addIfNotNull("activeModelAnimId", def.activeModelAnimId)
                addIfNotNull("modelOrtho", def.modelOrtho)
                addIfNotNull("unknownModelProp_3", def.unknownModelProp_3)
                addIfNotNull("unknownModelProp_4", def.unknownModelProp_4)
                addIfNotNull("unknownModelProp_5", def.unknownModelProp_5)
                addIfNotNull("unknownModelProp_6", def.unknownModelProp_6)
                addIfNotNull("unknownModelProp_7", def.unknownModelProp_7)
                addIfNotNull("font", def.font)
                addIfNotNull("text", def.text)
                addIfNotNull("activeText", def.activeText)
                addIfNotNull("vPadding", def.vPadding)
                addIfNotNull("halign", def.halign)
                addIfNotNull("valign", def.valign)
                addIfNotNull("shadowed", def.shadowed)
                addIfNotNull("color", def.color)
                addIfNotNull("activeColor", def.activeColor)
                addIfNotNull("overColor", def.overColor)
                addIfNotNull("unknownColor", def.unknownColor)
                addIfNotNull("filled", def.filled)
                addIfNotNull("lineWidth", def.lineWidth)
                addIfNotNull("unknownProp_8", def.unknownProp_8)
                addIfNotNull("unknownIntArray_1", def.unknownIntArray_1)
                addIfNotNull("unknownIntArray_2", def.unknownIntArray_2)
                addIfNotNull("unknownByteArray_1", def.unknownByteArray_1)
                addIfNotNull("unknownByteArray_2", def.unknownByteArray_2)
                addIfNotNull("optionBase", def.optionBase)
                addIfNotNull("ops", def.ops)
                addIfNotNull("dragDeadzone", def.dragDeadzone)
                addIfNotNull("dragDeadtime", def.dragDeadtime)
                addIfNotNull("dragRenderBehavior", def.dragRenderBehavior)
                addIfNotNull("opCircumfix", def.opCircumfix)
                addIfNotNull("opSuffix", def.opSuffix)
                addIfNotNull("option", def.option)
                addIfNotNull("unknownProp_9", def.unknownProp_9)
                addIfNotNull("unknownProp_10", def.unknownProp_10)
                addIfNotNull("unknownProp_11", def.unknownProp_11)
                addIfNotNull("cs1ComparisonOperands", def.cs1ComparisonOperands)
                addIfNotNull("cs1ComparisonOpcodes", def.cs1ComparisonOpcodes)
                addIfNotNull("cs1Scripts", def.cs1Scripts)
                addIfNotNull("objCounts", def.objCounts)
                addIfNotNull("objTypes", def.objTypes)
                addIfNotNull("invMarginX", def.invMarginX)
                addIfNotNull("invMarginY", def.invMarginY)
                addIfNotNull("invOffsetX", def.invOffsetX)
                addIfNotNull("invOffsetY", def.invOffsetY)
                addIfNotNull("invSprite", def.invSprite)
                addIfNotNull("buttonType", def.buttonType)
                addIfNotNull("invOptions", def.invOptions)

                def.scripts?.let { s ->
                    val sMap = mutableMapOf<String, Any?>()
                    s.unknown?.let { sMap["unknown"] = listOf(it.id, it.args) }
                    s.onMouseOver?.let { sMap["onMouseOver"] = listOf(it.id, it.args) }
                    s.onMouseLeave?.let { sMap["onMouseLeave"] = listOf(it.id, it.args) }
                    s.onUseWith?.let { sMap["onUseWith"] = listOf(it.id, it.args) }
                    s.onUse?.let { sMap["onUse"] = listOf(it.id, it.args) }
                    s.onVarpTransmit?.let { sMap["onVarpTransmit"] = listOf(it.id, it.args) }
                    s.onInvTransmit?.let { sMap["onInvTransmit"] = listOf(it.id, it.args) }
                    s.onStatTransmit?.let { sMap["onStatTransmit"] = listOf(it.id, it.args) }
                    s.onTimer?.let { sMap["onTimer"] = listOf(it.id, it.args) }
                    s.onOptionClick?.let { sMap["onOptionClick"] = listOf(it.id, it.args) }
                    s.onMouseRepeat?.let { sMap["onMouseRepeat"] = listOf(it.id, it.args) }
                    s.onClickRepeat?.let { sMap["onClickRepeat"] = listOf(it.id, it.args) }
                    s.onDrag?.let { sMap["onDrag"] = listOf(it.id, it.args) }
                    s.onRelease?.let { sMap["onRelease"] = listOf(it.id, it.args) }
                    s.onHold?.let { sMap["onHold"] = listOf(it.id, it.args) }
                    s.onDragStart?.let { sMap["onDragStart"] = listOf(it.id, it.args) }
                    s.onDragRelease?.let { sMap["onDragRelease"] = listOf(it.id, it.args) }
                    s.onScroll?.let { sMap["onScroll"] = listOf(it.id, it.args) }
                    s.onVarcTransmit?.let { sMap["onVarcTransmit"] = listOf(it.id, it.args) }
                    s.onVarcstrTransmit?.let { sMap["onVarcstrTransmit"] = listOf(it.id, it.args) }
                    if (sMap.isNotEmpty()) map["scripts"] = sMap
                }

                def.triggers?.let { t ->
                    val tMap = mutableMapOf<String, Any?>()
                    t.varpTriggers?.let { tMap["varpTriggers"] = it }
                    t.inventoryTriggers?.let { tMap["inventoryTriggers"] = it }
                    t.statTriggers?.let { tMap["statTriggers"] = it }
                    t.varcTriggers?.let { tMap["varcTriggers"] = it }
                    t.varcstrTriggers?.let { tMap["varcstrTriggers"] = it }
                    if (tMap.isNotEmpty()) map["triggers"] = tMap
                }

                def.children?.let { children ->
                    val childMap = children.mapNotNull { it?.let { dumpIface(it) } }
                    if (childMap.isNotEmpty()) map["children"] = childMap
                }

                return if (map.isEmpty()) null else map
            }

            val dumpMap = dumpIface(iface, isRoot = true)
            if (dumpMap != null) {
                val dump = File(dumps, "$i.json")
                dump.writeText(gson.toJson(dumpMap))
                p.debug("Iface id=$i dumped to $dump.")
            } else {
                p.debug("Iface id=$i has no dumpable data.")
            }
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

            val length = Cache.getArchiveCapacity(Archive.JS5_CONFIG, Group.IDK_TYPE)
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

            for (itemId in 0 until Cache.getIndexCapacity(Archive.JS5_CONFIG_OBJ)) {
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
         * Command to dump all CS2 mappings to a file.
         */

        define(
            name = "dumpcs2",
            privilege = Privilege.ADMIN,
            usage = "::dumpcs2",
            description = "Dumps all CS2 mappings to ./cs2.txt",
        ) { player, _ ->
            val path = Paths.get("dumps/cs2.txt")
            Files.createDirectories(path.parent)
            try {
                BufferedWriter(Files.newBufferedWriter(path)).use { bw ->
                    for (i in 0 until 10000) {
                        val mapping = CS2Mapping.forId(i) ?: continue
                        val mappingMap = mapping.map ?: continue
                        bw.append("ScriptAPI - $i [")
                        for ((index, value) in mappingMap) {
                            bw.append("$value: $index ")
                        }
                        bw.append("]")
                        bw.newLine()
                    }
                }
                player.debug("CS2 mappings dumped successfully to dumps/cs2.txt")
            } catch (e: Exception) {
                player.debug("Failed to dump CS2 mappings: ${e.message}")
                e.printStackTrace()
            }

            return@define
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

                val archiveFileCount = Cache.getArchiveFileCount(Archive.JS5_CONFIG, 26)
                for (fID in 0 until archiveFileCount) {
                    val file = Cache.getData(Archive.JS5_CONFIG, Group.STRUCT_TYPE, fID)
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

            for (npcId in 0 until Cache.getIndexCapacity(Archive.JS5_CONFIG_NPC)) {
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
