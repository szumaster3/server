package core.game.node.entity.player.info.login

import core.ServerConstants
import core.Util
import core.api.PersistPlayer
import core.api.log
import core.game.node.entity.combat.graves.GraveController
import core.game.node.entity.combat.graves.GraveType
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.music.MusicEntry
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import java.sql.Timestamp
import java.util.*

class PlayerSaveParser(
    val player: Player,
) {
    companion object {
        val contentHooks = ArrayList<PersistPlayer>()
    }

    var parser = JSONParser()
    var reader: FileReader? = null
    var saveFile: JSONObject? = null
    var read = true

    fun parse() {
        val JSON = File(ServerConstants.PLAYER_SAVE_PATH + player.name + ".json")
        if (JSON.exists()) {
            reader = FileReader(JSON)
        }
        reader ?: log(
            this::class.java,
            Log.WARN,
            "Couldn't find save file for ${player.name}, or save is corrupted.",
        ).also { read = false }
        if (read) {
            saveFile = parser.parse(reader) as JSONObject
        }

        if (read) {
            parseData()
        }
    }

    fun parseData() {
        parseCore()
        parseAttributes()
        parseSkills()
        parseSettings()
        parseQuests()
        parseAppearance()
        parseGrave()
        parseVarps()
        parseStates()
        parseSpellbook()
        parseSavedData()
        parseAutocastSpell()
        parseFarming()
        parseConfigs()
        parseMonitor()
        parseMusic()
        parseFamiliars()
        parseBankPin()
        parseHouse()
        parseIronman()
        parseEmoteManager()
        parseStatistics()
        parseAchievements()
        parsePouches()
        parseVersion()
    }

    fun runContentHooks() {
        if (read) contentHooks.forEach { it.parsePlayer(player, saveFile!!) }
        player.details.saveParsed = true
    }

    fun parseVarps() {
        if (saveFile!!.containsKey("varps")) player.varpManager.parse(saveFile!!["varps"] as JSONArray)
    }

    fun parsePouches() {
        if (saveFile!!.containsKey("pouches")) player.pouchManager.parse(saveFile!!["pouches"] as JSONArray)
    }

    fun parseAttributes() {
        if (saveFile!!.containsKey("attributes")) {
            val attrData = saveFile!!["attributes"] as JSONArray
            for (a in attrData) {
                val attr = a as JSONObject
                val key = attr["key"].toString()
                val type = attr["type"].toString()
                val isExpirable = attr.getOrDefault("expirable", false) as Boolean
                if (isExpirable) {
                    val expireTime = attr["expiration-time"].toString().toLong()
                    if (expireTime < System.currentTimeMillis()) continue

                    player.gameAttributes.keyExpirations[key] = expireTime
                }
                player.gameAttributes.savedAttributes.add(key)
                player.gameAttributes.attributes[key] =
                    when (type) {
                        "int" -> attr["value"].toString().toInt()
                        "str" -> attr["value"].toString()
                        "short" -> attr["value"].toString().toShort()
                        "long" -> attr["value"].toString().toLong()
                        "bool" -> attr["value"] as Boolean
                        "byte" -> Base64.getDecoder().decode(attr["value"].toString())[0]
                        "location" -> Location.fromString(attr["value"].toString())
                        "time" -> Timestamp.valueOf("time")
                        else ->
                            null.also {
                                log(
                                    this::class.java,
                                    Log.WARN,
                                    "Invalid data type for key: $key in PlayerSaveParser.kt Line 115",
                                )
                            }
                    }
            }
        } else {
            player.gameAttributes.parse(player.name + ".xml")
        }
    }

    fun parseStatistics() {
        if (saveFile!!.containsKey("statistics")) {
            val stats: JSONArray = saveFile!!["statistics"] as JSONArray
            for (stat in stats) {
                val s = stat as JSONObject
                val index = (s["index"] as String).toInt()
                val value = (s["value"] as String).toInt()
            }
        }
    }

    fun parseEmoteManager() {
        if (saveFile!!.containsKey("emoteData")) {
            val emoteData: JSONArray = saveFile!!["emoteData"] as JSONArray
            for (emote in emoteData) {
                val e = Emotes.values()[(emote as String).toInt()]
                if (!player.emoteManager.emotes.contains(e)) {
                    player.emoteManager.emotes.add(e)
                }
            }
        }
    }

    fun parseIronman() {
        if (saveFile!!.containsKey("ironManMode")) {
            val ironmanMode = (saveFile!!["ironManMode"] as String).toInt()
            player.ironmanManager.mode = IronmanMode.values()[ironmanMode]
        }
    }

    fun parseAchievements() {
        if (saveFile!!.containsKey("achievementDiaries")) {
            val achvData = saveFile!!["achievementDiaries"] as JSONArray
            player.achievementDiaryManager.parse(achvData)
        } else {
            player.achievementDiaryManager.resetRewards()
        }
    }

    fun parseHouse() {
        val houseData = saveFile!!["houseData"] as JSONObject
        player.houseManager.parse(houseData)
    }

    fun parseBankPin() {
        val bpData = saveFile!!["bankPinManager"] as JSONObject
        player.bankPinManager.parse(bpData)
    }

    fun parseStates() {
        player.states.clear()
        if (saveFile!!.containsKey("states")) {
            val states: JSONArray = saveFile!!["states"] as JSONArray
            for (state in states) {
                val s = state as JSONObject
                val stateId = s["stateKey"].toString()
                if (player.states[stateId] != null) continue
                val stateClass = player.registerState(stateId)
                stateClass?.parse(s)
                stateClass?.init()
                player.states[stateId] = stateClass
            }
        }
    }

    fun parseFamiliars() {
        val familiarData = saveFile!!["familiarManager"] as JSONObject
        player.familiarManager.parse(familiarData)
    }

    fun parseMusic() {
        val unlockedSongs = saveFile!!["unlockedMusic"] as JSONArray
        for (song in unlockedSongs) {
            val s = (song as String).toInt()
            val entry = MusicEntry.forId(s)
            player.musicPlayer.unlocked[entry.index] = entry
        }
    }

    fun parseMonitor() {
        val joinDate = saveFile!!["joinDate"] as? JSONObject ?: return
        val joinTime = joinDate["joinTime"] as? String ?: return
        val timestamp = Timestamp.valueOf(joinTime)
        player.gameAttributes.attributes["joinDate"] = timestamp
    }

    fun parseConfigs() {
        val configs = saveFile!!["configs"] as? JSONArray ?: return
        for (config in configs) {
            val c = config as JSONObject
            val index = (c["index"] as String).toInt()
            if (index == 1048) continue
            val value = (c["value"] as String).toInt()
            player.varpMap[index] = value
        }
    }

    fun parseFarming() {
    }

    fun parseAutocastSpell() {
        val autocastRaw = saveFile!!["autocastSpell"]
        autocastRaw ?: return
        val autocast = autocastRaw as JSONObject
        val book = (autocast["book"] as String).toInt()
        val spellId = (autocast["spellId"] as String).toInt()
        player.properties.autocastSpell = SpellBookManager.SpellBook.values()[book].getSpell(spellId) as CombatSpell
    }

    fun parseSavedData() {
        val activityData = saveFile!!["activityData"] as JSONObject
        val questData = saveFile!!["questData"] as JSONObject
        val globalData = saveFile!!["globalData"] as JSONObject
        player.savedData.activityData.parse(activityData)
        player.savedData.questData.parse(questData)
        player.savedData.globalData.parse(globalData)
    }

    fun parseSpellbook() {
        val spellbookData = (saveFile!!["spellbook"] as String).toInt()
        SpellBookManager.SpellBook.forInterface(spellbookData)?.let { player.spellBookManager.setSpellBook(it) }
    }

    fun parseGrave() {
        saveFile ?: return
        val graveData = (saveFile!!["grave_type"] as? String)?.toInt() ?: return
        val type = GraveType.values()[graveData]
        GraveController.updateGraveType(player, type)
    }

    fun parseAppearance() {
        saveFile ?: return
        val appearanceData = saveFile!!["appearance"] as JSONObject
        player.appearance.parse(appearanceData)
    }

    fun parseQuests() {
        saveFile ?: return
        val questData = saveFile!!["quests"] as JSONObject
        player.questRepository.parse(questData)
    }

    fun parseCore() {
        saveFile ?: return
        val coreData = saveFile!!["core_data"] as JSONObject
        val inventory = coreData["inventory"] as JSONArray
        val bank = coreData["bank"] as JSONArray
        val bankSecondary = coreData.getOrDefault("bankSecondary", JSONArray()) as JSONArray
        val equipment = coreData["equipment"] as JSONArray
        val bBars = coreData["blastBars"] as? JSONArray
        val bOre = coreData["blastOre"] as? JSONArray
        val bCoal = coreData["blastCoal"] as? JSONArray
        val varpData = coreData["varp"] as? JSONArray
        val timerData = coreData["timers"] as? JSONObject
        val location = coreData["location"] as String
        val bankTabData = coreData["bankTabs"]
        if (bankTabData != null) {
            val tabData = bankTabData as JSONArray
            for (i in tabData) {
                i ?: continue
                val tab = i as JSONObject
                val index = tab["index"].toString().toInt()
                val startSlot = tab["startSlot"].toString().toInt()
                player.bankPrimary.tabStartSlot[index] = startSlot
            }
        }
        val bankTabSecondaryData = coreData["bankTabsSecondary"]
        if (bankTabSecondaryData != null) {
            val tabData = bankTabSecondaryData as JSONArray
            for (i in tabData) {
                i ?: continue
                val tab = i as JSONObject
                val index = tab["index"].toString().toInt()
                val startSlot = tab["startSlot"].toString().toInt()
                player.bankSecondary.tabStartSlot[index] = startSlot
            }
        }
        player.useSecondaryBank = coreData.getOrDefault("useSecondaryBank", false) as Boolean
        player.inventory.parse(inventory)
        player.bankPrimary.parse(bank)
        player.bankSecondary.parse(bankSecondary)
        player.equipment.parse(equipment)
        bBars?.let { player.blastBars.parse(it) }
        bOre?.let { player.blastOre.parse(bOre) }
        bCoal?.let { player.blastCoal.parse(bCoal) }
        player.location = Util.parseLocation(location)

        if (varpData != null) {
            for (e in varpData) {
                e ?: continue
                val varp = e as JSONObject
                val index = varp["index"]?.toString()?.toInt() ?: continue
                val value = varp["value"]?.toString()?.toInt() ?: continue
                player.varpMap[index] = value
                player.saveVarp[index] = true
            }
        }

        if (timerData != null) player.timers.parseTimers(timerData)
    }

    fun parseSkills() {
        saveFile ?: return
        val skillData = saveFile!!["skills"] as JSONArray
        player.skills.parse(skillData)
        player.skills.experienceGained = saveFile!!["totalEXP"].toString().toDouble()
        player.skills.experienceMultiplier = saveFile!!["exp_multiplier"].toString().toDouble()
        if (GameWorld.settings?.default_xp_rate != 1.0) {
            player.skills.experienceMultiplier = GameWorld.settings?.default_xp_rate!!
        }
        if (saveFile!!.containsKey("milestone")) {
            val milestone: JSONObject = saveFile!!["milestone"] as JSONObject
            player.skills.combatMilestone = (milestone["combatMilestone"]).toString().toInt()
            player.skills.skillMilestone = (milestone["skillMilestone"]).toString().toInt()
        }
    }

    fun parseSettings() {
        saveFile ?: return
        val settingsData = saveFile!!["settings"] as JSONObject
        player.settings.parse(settingsData)
    }

    fun parseVersion() {
        saveFile ?: return
        player.version = 0
        if (saveFile!!.containsKey("version")) {
            player.version = saveFile!!["version"].toString().toInt()
        }
    }
}
