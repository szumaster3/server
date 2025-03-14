package core.game.node.entity.player.info.login

import com.google.gson.GsonBuilder
import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.pet.Pet
import core.ServerConstants
import core.api.PersistPlayer
import core.api.log
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.tools.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Math.ceil
import java.sql.Timestamp
import java.util.*
import javax.script.ScriptEngineManager

class PlayerSaver(
    val player: Player,
) {
    companion object {
        val contentHooks = ArrayList<PersistPlayer>()
    }

    fun populate(): JSONObject {
        val saveFile = JSONObject()
        saveCoreData(saveFile)
        saveSkills(saveFile)
        saveSettings(saveFile)
        saveQuests(saveFile)
        saveAppearance(saveFile)
        saveSpellbook(saveFile)
        saveSavedData(saveFile)
        saveAutocast(saveFile)
        savePlayerMonitor(saveFile)
        saveMusicPlayer(saveFile)
        saveFamiliarManager(saveFile)
        saveStateManager(saveFile)
        saveBankPinData(saveFile)
        saveHouseData(saveFile)
        saveAchievementData(saveFile)
        saveIronManData(saveFile)
        saveEmoteData(saveFile)
        saveStatManager(saveFile)
        saveAttributes(saveFile)
        savePouches(saveFile)
        saveVersion(saveFile)
        contentHooks.forEach { it.savePlayer(player, saveFile) }
        return saveFile
    }

    fun save() =
        runBlocking {
            if (!player.details.saveParsed) return@runBlocking
            val json: String
            if (ServerConstants.JAVA_VERSION < 11) {
                val manager = ScriptEngineManager()
                val scriptEngine = manager.getEngineByName("JavaScript")
                if (scriptEngine == null) {
                    log(
                        this::class.java,
                        Log.ERR,
                        "Cannot save: Failed to load ScriptEngineManager, this is a known issue on non Java-11 versions. Set your Java version to 11 to avoid further bugs!",
                    )
                    return@runBlocking
                }
                scriptEngine.put("jsonString", populate().toJSONString())
                scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)")
                json = scriptEngine["result"] as String
            } else {
                json = GsonBuilder().setPrettyPrinting().create().toJson(populate())
            }

            try {
                if (!File("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json").exists()) {
                    File("${ServerConstants.PLAYER_SAVE_PATH}").mkdirs()
                    withContext(Dispatchers.IO) {
                        File("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json").createNewFile()
                    }
                }
                withContext(Dispatchers.IO) {
                    FileWriter("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json").use { file ->
                        file.write(json)
                        file.flush()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    fun savePouches(root: JSONObject) {
        player.pouchManager.save(root)
    }

    fun saveVersion(root: JSONObject) {
        root["version"] = player.version
    }

    fun saveAttributes(root: JSONObject) {
        if (player.gameAttributes.savedAttributes.isNotEmpty()) {
            val attrs = JSONArray()
            for (key in player.gameAttributes.savedAttributes) {
                val value = player.gameAttributes.attributes[key]
                value ?: continue
                val isExpirable = player.gameAttributes.keyExpirations.containsKey(key)
                val attr = JSONObject()
                val type =
                    when (value) {
                        is Int -> "int"
                        is Boolean -> "bool"
                        is Long -> "long"
                        is Short -> "short"
                        is String -> "str"
                        is Byte -> "byte"
                        is Location -> "location"
                        else ->
                            "null".also {
                                log(
                                    this::class.java,
                                    Log.WARN,
                                    "Invalid attribute type for key: $key in PlayerSaver.kt Line 115",
                                )
                            }
                    }
                attr["type"] = type
                attr["key"] = key
                if (value is Byte) {
                    val asString = Base64.getEncoder().encodeToString(byteArrayOf(value))
                    attr["value"] = asString
                } else {
                    attr["value"] = if (value is Boolean) value else value.toString()
                }
                if (isExpirable) {
                    attr["expirable"] = true
                    attr["expiration-time"] = player.gameAttributes.keyExpirations[key].toString()
                }
                attrs.add(attr)
            }
            root["attributes"] = attrs
        }
    }

    fun saveStatManager(root: JSONObject) {
        val statistics = JSONArray()
        var index = 0
        root["statistics"] = statistics
    }

    fun saveEmoteData(root: JSONObject) {
        if (player.emoteManager.isSaveRequired) {
            val emoteData = JSONArray()
            player.emoteManager.emotes.map {
                emoteData.add(it.ordinal.toString())
            }
            root["emoteData"] = emoteData
        }
    }

    fun saveIronManData(root: JSONObject) {
        if (player.ironmanManager.mode != IronmanMode.NONE) {
            root["ironManMode"] =
                player.ironmanManager.mode.ordinal
                    .toString()
        }
    }

    fun saveAchievementData(root: JSONObject) {
        val achievementData = JSONArray()
        player.achievementDiaryManager.diarys.map {
            val diary = JSONObject()
            val startedLevels = JSONArray()
            it.levelStarted.map {
                startedLevels.add(it)
            }
            diary["startedLevels"] = startedLevels
            val completedLevels = JSONArray()
            it.taskCompleted.map {
                val level = JSONArray()
                it.map {
                    level.add(it)
                }
                completedLevels.add(level)
            }
            diary["completedLevels"] = completedLevels
            val rewardedLevels = JSONArray()
            it.levelRewarded.map {
                rewardedLevels.add(it)
            }
            diary["rewardedLevels"] = rewardedLevels
            val diaryCollector = JSONObject()
            diaryCollector[it.type.name] = diary
            achievementData.add(diaryCollector)
        }
        root["achievementDiaries"] = achievementData
    }

    fun saveHouseData(root: JSONObject) {
        val manager = player.houseManager
        val houseData = JSONObject()
        houseData["location"] = manager.location.ordinal.toString()
        houseData["style"] = manager.style.ordinal.toString()
        if (manager.hasServant()) {
            val servant = JSONObject()
            servant["type"] =
                manager.servant.type.ordinal
                    .toString()
            servant["uses"] = manager.servant.uses.toString()
            if (manager.servant.item != null) {
                val item = JSONObject()
                item["id"] =
                    manager.servant.item.id
                        .toString()
                item["amount"] =
                    manager.servant.item.amount
                        .toString()
                servant["item"] = item
            }
            servant["greet"] = manager.servant.isGreet
            houseData["servant"] = servant
        }
        val rooms = JSONArray()
        var z = 0
        for (room in player.houseManager.rooms) {
            var x = 0
            for (xr in room) {
                var y = 0
                for (yr in xr) {
                    if (yr != null) {
                        val r = JSONObject()
                        r["z"] = z.toString()
                        r["x"] = x.toString()
                        r["y"] = y.toString()
                        r["properties"] = yr.properties.ordinal.toString()
                        r["rotation"] = yr.rotation.toInteger().toString()
                        val hotspots = JSONArray()
                        var hotspotIndex = 0
                        yr.hotspots.map {
                            if (it.decorationIndex > -1) {
                                val hotspot = JSONObject()
                                hotspot["hotspotIndex"] = hotspotIndex.toString()
                                hotspot["decorationIndex"] = it.decorationIndex.toString()
                                hotspots.add(hotspot)
                            }
                            hotspotIndex++
                        }
                        r["hotspots"] = hotspots
                        rooms.add(r)
                    }
                    y++
                }
                x++
            }
            z++
        }
        houseData["rooms"] = rooms
        root["houseData"] = houseData
    }

    fun saveBankPinData(root: JSONObject) {
        val bankPinManager = JSONObject()
        if (player.bankPinManager.hasPin()) {
            bankPinManager["pin"] = player.bankPinManager.pin.toString()
        }
        bankPinManager["longRecovery"] = player.bankPinManager.isLongRecovery
        if (player.bankPinManager.status.ordinal != 0) {
            bankPinManager["status"] =
                player.bankPinManager.status.ordinal
                    .toString()
        }
        if (player.bankPinManager.pendingDelay != -1L &&
            player.bankPinManager.pendingDelay > System.currentTimeMillis()
        ) {
            bankPinManager["pendingDelay"] = player.bankPinManager.pendingDelay.toString()
        }
        if (player.bankPinManager.tryDelay > System.currentTimeMillis()) {
            bankPinManager["tryDelay"] = player.bankPinManager.tryDelay.toString()
        }
        root["bankPinManager"] = bankPinManager
    }

    fun saveStateManager(root: JSONObject) {
        val states = JSONArray()
        player.states.forEach { key, clazz ->
            if (clazz != null && clazz.pulse != null) {
                val stateObj = JSONObject()
                stateObj["stateKey"] = key
                clazz.save(stateObj)
                states.add(stateObj)
            }
        }
        root["states"] = states
    }

    fun saveFamiliarManager(root: JSONObject) {
        val familiarManager = JSONObject()
        val petDetails = JSONArray()
        player.familiarManager.petDetails.map {
            val detail = JSONObject()
            detail["petId"] = it.key.toString()
            detail["hunger"] = it.value.hunger.toString()
            detail["growth"] = it.value.growth.toString()
            petDetails.add(detail)
        }
        familiarManager["petDetails"] = petDetails
        if (player.familiarManager.hasPet()) {
            familiarManager["currentPet"] = (player.familiarManager.familiar as Pet).getItemIdHash().toString()
        } else if (player.familiarManager.hasFamiliar()) {
            val familiar = JSONObject()
            familiar["originalId"] =
                player.familiarManager.familiar.originalId
                    .toString()
            familiar["ticks"] =
                player.familiarManager.familiar.ticks
                    .toString()
            familiar["specialPoints"] =
                player.familiarManager.familiar.specialPoints
                    .toString()
            if (player.familiarManager.familiar.isBurdenBeast &&
                !(player.familiarManager.familiar as BurdenBeast).container.isEmpty
            ) {
                val familiarInventory = saveContainer((player.familiarManager.familiar as BurdenBeast).container)
                familiar["inventory"] = familiarInventory
            }
            familiar["lifepoints"] = player.familiarManager.familiar.skills.lifepoints
            familiarManager["familiar"] = familiar
        }
        root["familiarManager"] = familiarManager
    }

    fun saveMusicPlayer(root: JSONObject) {
        val unlockedMusic = JSONArray()
        player.musicPlayer.unlocked.values.map {
            unlockedMusic.add(it.id.toString())
        }
        root["unlockedMusic"] = unlockedMusic
    }

    fun savePlayerMonitor(root: JSONObject) {
        val joinDate = JSONObject()
        val updatedJoinTimestamp = player.gameAttributes.attributes["joinDate"] as? Timestamp
        joinDate["joinTime"] = updatedJoinTimestamp?.toString()
        root["joinDate"] = joinDate
    }

    fun saveAutocast(root: JSONObject) {
        player.properties.autocastSpell ?: return
        val spell = JSONObject()
        spell["book"] =
            player.properties.autocastSpell.book.ordinal
                .toString()
        spell["spellId"] =
            player.properties.autocastSpell.spellId
                .toString()
        root["autocastSpell"] = spell
    }

    fun saveSavedData(root: JSONObject) {
        saveActivityData(root)
        saveQuestData(root)
        saveGlobalData(root)
    }

    fun saveGlobalData(root: JSONObject) {
        val globalData = JSONObject()
        globalData["tutorialStage"] =
            player.savedData.globalData
                .getTutorialStage()
                .toString()
        globalData["homeTeleportDelay"] =
            player.savedData.globalData
                .getHomeTeleportDelay()
                .toString()
        globalData["lumbridgeRope"] = player.savedData.globalData.hasTiedLumbridgeRope()
        globalData["apprentice"] = player.savedData.globalData.hasSpokenToApprentice()
        globalData["assistTime"] =
            player.savedData.globalData
                .getAssistTime()
                .toString()
        val assistExperience = JSONArray()
        player.savedData.globalData.getAssistExperience().map {
            assistExperience.add(it.toString())
        }
        globalData["assistExperience"] = assistExperience
        val strongholdRewards = JSONArray()
        player.savedData.globalData.getStrongHoldRewards().map {
            strongholdRewards.add(it)
        }
        globalData["strongHoldRewards"] = strongholdRewards
        globalData["chatPing"] =
            player.savedData.globalData
                .getChatPing()
                .toString()
        globalData["tutorClaim"] =
            player.savedData.globalData
                .getTutorClaim()
                .toString()
        globalData["luthasTask"] = player.savedData.globalData.isLuthasTask()
        globalData["karamjaBananas"] =
            player.savedData.globalData
                .getKaramjaBananas()
                .toString()
        globalData["silkSteal"] =
            player.savedData.globalData
                .getSilkSteal()
                .toString()
        globalData["teaSteal"] =
            player.savedData.globalData
                .getTeaSteal()
                .toString()
        globalData["zafAmount"] =
            player.savedData.globalData
                .getZaffAmount()
                .toString()
        globalData["zafTime"] =
            player.savedData.globalData
                .getZaffTime()
                .toString()
        globalData["fritzGlass"] = player.savedData.globalData.isFritzGlass()
        globalData["wydinEmployee"] = player.savedData.globalData.isWydinEmployee()
        globalData["draynorRecording"] = player.savedData.globalData.isDraynorRecording()
        globalData["geTutorial"] = player.savedData.globalData.isGeTutorial()
        globalData["essenceTeleporter"] =
            player.savedData.globalData
                .getEssenceTeleporter()
                .toString()
        globalData["recoilDamage"] =
            player.savedData.globalData
                .getRecoilDamage()
                .toString()
        globalData["doubleExpDelay"] =
            player.savedData.globalData
                .getDoubleExpDelay()
                .toString()
        globalData["joinedMonastery"] = player.savedData.globalData.isJoinedMonastery()
        val readPlaques = JSONArray()
        player.savedData.globalData.readPlaques.map {
            readPlaques.add(it)
        }
        globalData["readPlaques"] = readPlaques
        globalData["forgingUses"] =
            player.savedData.globalData
                .getForgingUses()
                .toString()
        globalData["ectoCharges"] =
            player.savedData.globalData
                .getEctoCharges()
                .toString()
        globalData["dropDelay"] =
            player.savedData.globalData
                .getDropDelay()
                .toString()
        val abyssData = JSONArray()
        player.savedData.globalData.getAbyssData().map {
            abyssData.add(it)
        }
        globalData["abyssData"] = abyssData
        val rcDecays = JSONArray()
        player.savedData.globalData.getRcDecays().map {
            rcDecays.add(it.toString())
        }
        globalData["rcDecays"] = rcDecays
        globalData["disableDeathScreen"] = player.savedData.globalData.isDeathScreenDisabled()
        globalData["playerTestStage"] =
            player.savedData.globalData
                .getPlayerTestStage()
                .toString()
        globalData["charmingDelay"] =
            player.savedData.globalData
                .getCharmingDelay()
                .toString()
        val travelLogs = JSONArray()
        player.savedData.globalData.getTravelLogs().map {
            travelLogs.add(it)
        }
        globalData["travelLogs"] = travelLogs
        val godBooks = JSONArray()
        player.savedData.globalData.getGodBooks().map {
            godBooks.add(it)
        }
        globalData["godBooks"] = godBooks
        globalData["disableNews"] = player.savedData.globalData.isDisableNews()
        val godPages = JSONArray()
        player.savedData.globalData.getGodPages().map {
            godPages.add(it)
        }
        globalData["godPages"] = godPages
        globalData["overChargeDelay"] =
            player.savedData.globalData
                .getOverChargeDelay()
                .toString()
        val bossCounters = JSONArray()
        player.savedData.globalData.getBossCounters().map {
            bossCounters.add(it.toString())
        }
        globalData["bossCounters"] = bossCounters
        globalData["barrowsLoots"] =
            player.savedData.globalData
                .getBarrowsLoots()
                .toString()
        globalData["lootSharePoints"] =
            player.savedData.globalData
                .getLootSharePoints()
                .toString()
        globalData["lootShareDelay"] =
            player.savedData.globalData
                .getLootShareDelay()
                .toString()
        globalData["doubleExp"] =
            player.savedData.globalData
                .getDoubleExp()
                .toString()
        globalData["globalTeleporterDelay"] =
            player.savedData.globalData
                .getGlobalTeleporterDelay()
                .toString()
        globalData["starSpriteDelay"] =
            player.savedData.globalData.starSpriteDelay
                .toString()
        globalData["runReplenishDelay"] =
            player.savedData.globalData
                .getRunReplenishDelay()
                .toString()
        globalData["runReplenishCharges"] =
            player.savedData.globalData
                .getRunReplenishCharges()
                .toString()
        globalData["lowAlchemyCharges"] =
            player.savedData.globalData
                .getLowAlchemyCharges()
                .toString()
        globalData["lowAlchemyDelay"] =
            player.savedData.globalData
                .getLowAlchemyDelay()
                .toString()
        globalData["magicSkillCapeDelay"] =
            player.savedData.globalData
                .getMagicSkillCapeDelay()
                .toString()
        globalData["hunterCapeDelay"] =
            player.savedData.globalData
                .getHunterCapeDelay()
                .toString()
        globalData["hunterCapeCharges"] =
            player.savedData.globalData
                .getHunterCapeCharges()
                .toString()
        globalData["taskAmount"] =
            player.savedData.globalData
                .getTaskAmount()
                .toString()
        globalData["taskPoints"] =
            player.savedData.globalData
                .getTaskPoints()
                .toString()
        globalData["macroDisabled"] = player.savedData.globalData.getMacroDisabled()
        root["globalData"] = globalData
    }

    fun saveQuestData(root: JSONObject) {
        val questData = JSONObject()
        val draynorLever = JSONArray()
        player.savedData.questData.draynorLever.map {
            draynorLever.add(it)
        }
        questData["draynorLever"] = draynorLever
        val dslayer = JSONArray()
        player.savedData.questData.dragonSlayer.map {
            dslayer.add(it)
        }
        questData["dragonSlayer"] = dslayer
        questData["dragonSlayerPlanks"] =
            player.savedData.questData.dragonSlayerPlanks
                .toString()
        val demonSlayer = JSONArray()
        player.savedData.questData.demonSlayer.map {
            demonSlayer.add(it)
        }
        questData["demonSlayer"] = demonSlayer
        val cooksAssistant = JSONArray()
        player.savedData.questData.cooksAssistant.map {
            cooksAssistant.add(it)
        }
        questData["cooksAssistant"] = cooksAssistant
        questData["gardenerAttack"] = player.savedData.questData.isGardenerAttack
        questData["talkedDrezel"] = player.savedData.questData.isTalkedDrezel
        val desertTreasureNode = JSONArray()
        player.savedData.questData.desertTreasure.map {
            val item = JSONObject()
            item["id"] = it.id.toString()
            item["amount"] = it.amount.toString()
            desertTreasureNode.add(item)
        }
        questData["desertTreasureNode"] = desertTreasureNode
        questData["witchsExperimentStage"] =
            player.savedData.questData.witchsExperimentStage
                .toString()
        questData["witchsExperimentKilled"] = player.savedData.questData.isWitchsExperimentKilled
        root["questData"] = questData
    }

    fun saveActivityData(root: JSONObject) {
        val activityData = JSONObject()
        activityData["pestPoints"] =
            player.savedData.activityData.pestPoints
                .toString()
        activityData["warriorGuildTokens"] =
            player.savedData.activityData.warriorGuildTokens
                .toString()
        activityData["bountyHunterRate"] =
            player.savedData.activityData.bountyHunterRate
                .toString()
        activityData["bountyRogueRate"] =
            player.savedData.activityData.bountyRogueRate
                .toString()
        activityData["barrowKills"] =
            player.savedData.activityData.barrowKills
                .toString()
        val barrowBrothers = JSONArray()
        player.savedData.activityData.barrowBrothers.map {
            barrowBrothers.add(it)
        }
        activityData["barrowBrothers"] = barrowBrothers
        activityData["barrowTunnelIndex"] =
            player.savedData.activityData.barrowTunnelIndex
                .toString()
        activityData["kolodionStage"] =
            player.savedData.activityData.kolodionStage
                .toString()
        val godCasts = JSONArray()
        player.savedData.activityData.godCasts.map {
            godCasts.add(it.toString())
        }
        activityData["godCasts"] = godCasts
        activityData["kolodionBoss"] =
            player.savedData.activityData.kolodionBoss
                .toString()
        activityData["elnockSupplies"] = player.savedData.activityData.isElnockSupplies
        activityData["lastBorkBattle"] =
            player.savedData.activityData.lastBorkBattle
                .toString()
        activityData["startedMta"] = player.savedData.activityData.isStartedMta
        activityData["lostCannon"] = player.savedData.activityData.isLostCannon
        val pizazzPoints = JSONArray()
        player.savedData.activityData.pizazzPoints.map {
            pizazzPoints.add(it.toString())
        }
        activityData["pizazzPoints"] = pizazzPoints
        activityData["bonesToPeaches"] = player.savedData.activityData.isBonesToPeaches
        activityData["solvedMazes"] =
            player.savedData.activityData.solvedMazes
                .toString()
        activityData["fogRating"] =
            player.savedData.activityData.fogRating
                .toString()
        activityData["borkKills"] =
            player.savedData.activityData.borkKills
                .toString()
        activityData["hardcoreDeath"] = player.savedData.activityData.hardcoreDeath
        activityData["topGrabbed"] = player.savedData.activityData.isTopGrabbed
        root["activityData"] = activityData
    }

    fun saveSpellbook(root: JSONObject) {
        root["spellbook"] = player.spellBookManager.spellBook.toString()
    }

    fun saveAppearance(root: JSONObject) {
        val appearance = JSONObject()
        appearance["gender"] =
            player.appearance.gender
                .toByte()
                .toString()
        val appearanceCache = JSONArray()
        player.appearance.appearanceCache.map {
            val bodyPart = JSONObject()
            bodyPart["look"] = it.look.toString()
            bodyPart["color"] = it.color.toString()
            appearanceCache.add(bodyPart)
        }
        appearance["appearance_cache"] = appearanceCache
        root["appearance"] = appearance
    }

    fun saveQuests(root: JSONObject) {
        val quests = JSONObject()
        quests["points"] = player.questRepository.points.toString()
        val questStages = JSONArray()
        player.questRepository.questList.map {
            val quest = JSONObject()
            quest["questId"] = it.key.toString()
            quest["questStage"] = it.value.toString()
            questStages.add(quest)
        }
        quests["questStages"] = questStages
        root["quests"] = quests
    }

    fun saveSettings(root: JSONObject) {
        val settings = JSONObject()
        settings["brightness"] = player.settings.brightness.toString()
        settings["musicVolume"] = player.settings.musicVolume.toString()
        settings["soundEffectVolume"] = player.settings.soundEffectVolume.toString()
        settings["areaSoundVolume"] = player.settings.areaSoundVolume.toString()
        settings["publicChatSetting"] = player.settings.publicChatSetting.toString()
        settings["privateChatSetting"] = player.settings.privateChatSetting.toString()
        settings["clanChatSetting"] = player.settings.clanChatSetting.toString()
        settings["tradeSetting"] = player.settings.tradeSetting.toString()
        settings["assistSetting"] = player.settings.assistSetting.toString()
        settings["runEnergy"] = player.settings.runEnergy.toString()
        settings["specialEnergy"] = player.settings.specialEnergy.toString()
        settings["attackStyle"] = player.settings.attackStyleIndex.toString()
        settings["singleMouse"] = player.settings.isSingleMouseButton
        settings["disableChatEffects"] = player.settings.isDisableChatEffects
        settings["splitPrivate"] = player.settings.isSplitPrivateChat
        settings["acceptAid"] = player.settings.isAcceptAid
        settings["runToggled"] = player.settings.isRunToggled
        settings["retaliation"] = player.properties.isRetaliating
        root["settings"] = settings
    }

    fun saveSkills(root: JSONObject) {
        val skills = JSONArray()
        for (i in 0 until 24) {
            val skill = JSONObject()
            skill["id"] = i.toString()
            skill["static"] = player.skills.staticLevels[i].toString()
            if (i == Skills.HITPOINTS) {
                skill["dynamic"] = player.skills.lifepoints.toString()
            } else if (i == Skills.PRAYER) {
                skill["dynamic"] = ceil(player.skills.prayerPoints).toInt().toString()
            } else {
                skill["dynamic"] = player.skills.dynamicLevels[i].toString()
            }
            skill["experience"] = player.skills.getExperience(i).toString()
            skills.add(skill)
        }
        root["skills"] = skills
        root["totalEXP"] = player.skills.experienceGained.toString()
        root["exp_multiplier"] = player.skills.experienceMultiplier.toString()
        if (player.skills.combatMilestone > 0 || player.skills.skillMilestone > 0) {
            val milestone = JSONObject()
            milestone["combatMilestone"] = player.skills.combatMilestone.toString()
            milestone["skillMilestone"] = player.skills.skillMilestone.toString()
            root["milestone"] = milestone
        }
    }

    fun saveContainer(container: Container): JSONArray {
        val json = JSONArray()
        container.toArray().map {
            if (it != null) {
                val item = JSONObject()
                item["slot"] = it.slot.toString()
                item["id"] = it.id.toString()
                item["amount"] = it.amount.toString()
                item["charge"] = it.charge.toString()
                json.add(item)
            }
        }
        return json
    }

    fun saveCoreData(root: JSONObject) {
        val coreData = JSONObject()
        val inventory = saveContainer(player.inventory)
        coreData["inventory"] = inventory

        val bank = saveContainer(player.bankPrimary)
        coreData["bank"] = bank

        val bankSecondary = saveContainer(player.bankSecondary)
        coreData["bankSecondary"] = bankSecondary

        val bBars = saveContainer(player.blastBars)
        coreData["blastBars"] = bBars

        val bOre = saveContainer(player.blastOre)
        coreData["blastOre"] = bOre

        val bCoal = saveContainer(player.blastCoal)
        coreData["blastCoal"] = bCoal

        val bankTabs = JSONArray()
        for (i in player.bankPrimary.tabStartSlot.indices) {
            val tab = JSONObject()
            tab["index"] = i.toString()
            tab["startSlot"] = player.bankPrimary.tabStartSlot[i].toString()
            bankTabs.add(tab)
        }
        coreData["bankTabs"] = bankTabs

        val bankTabsSecondary = JSONArray()
        for (i in player.bankSecondary.tabStartSlot.indices) {
            val tab = JSONObject()
            tab["index"] = i.toString()
            tab["startSlot"] = player.bankSecondary.tabStartSlot[i].toString()
            bankTabsSecondary.add(tab)
        }
        coreData["bankTabsSecondary"] = bankTabsSecondary

        coreData["useSecondaryBank"] = player.useSecondaryBank

        val equipment = saveContainer(player.equipment)
        coreData["equipment"] = equipment

        val loctemp = player.location
        val locStr = "${loctemp.x},${loctemp.y},${loctemp.z}"
        coreData["location"] = locStr

        val varpData = JSONArray()
        for ((index, value) in player.varpMap) {
            if (player.saveVarp[index] != true) continue
            if (value == 0) continue

            val varpObj = JSONObject()
            varpObj["index"] = index.toString()
            varpObj["value"] = value.toString()
            varpData.add(varpObj)
        }
        coreData["varp"] = varpData

        val timerData = JSONObject()
        player.timers.saveTimers(timerData)
        coreData["timers"] = timerData

        root["core_data"] = coreData
    }
}
