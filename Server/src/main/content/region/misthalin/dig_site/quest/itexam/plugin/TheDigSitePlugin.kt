package content.region.misthalin.dig_site.quest.itexam.plugin

import content.global.skill.thieving.PickpocketPlugin
import content.region.misthalin.dig_site.quest.itexam.TheDigSite
import content.region.misthalin.dig_site.quest.itexam.dialogue.PanningGuideDialogue
import core.api.*
import core.api.getQuestStage
import core.api.setQuestStage
import core.api.utils.PlayerCamera
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import shared.consts.*

class TheDigSitePlugin : InteractionListener {

    override fun defineListeners() {
        on(NPCs.NISHA_4648, IntType.NPC, "talk-to") { player, _ ->
            //Panning_archaeologist_(female)?oldid=1169942
            return@on false
        }

        /*
         * Handles inspecting the Level 1 Certificate item.
         * Displays a certificate interface with the player name.
         */

        on(Items.LEVEL_1_CERTIFICATE_691, ITEM, "look-at") { player, _ ->
            openInterface(player, 440)
            sendString(player, player.username, 440, 5)
            return@on true
        }

        /*
         * Handles inspecting the Level 2 Certificate item.
         * Displays a certificate interface with the player name.
         */

        on(Items.LEVEL_2_CERTIFICATE_692, ITEM, "look-at") { player, _ ->
            openInterface(player, 441)
            sendString(player, player.username, 441, 5)
            return@on true
        }

        /*
         * Handles inspecting the Level 3 Certificate item.
         * Displays a certificate interface with the player name.
         */

        on(Items.LEVEL_3_CERTIFICATE_693, ITEM, "look-at") { player, _ ->
            openInterface(player, 444)
            sendString(player, player.username, 444, 5)
            return@on true
        }

        /*
         * Handles stealing from Digsite Workmen NPCs.
         */

        on(intArrayOf(NPCs.DIGSITE_WORKMAN_613, NPCs.DIGSITE_WORKMAN_4564, NPCs.DIGSITE_WORKMAN_4565),
            IntType.NPC,
            "steal-from"
        ) { player, node ->

            if (getStatLevel(player, Skills.THIEVING) < 25) {
                sendMessage(player, "You need a Thieving level of 25 to do that.")
                return@on true
            }

            val table =
                if (getQuestStage(player, Quests.THE_DIG_SITE) == 3)
                    workmanPickpocketingTable
                else
                    workmanPostQuestPickpocketingTable

            if (!table.canRoll(player)) {
                sendMessage(player, "You don't have enough inventory space to do that.")
                return@on true
            }

            sendMessage(player, "You attempt to pick the workman's pocket...")
            player.animator.animate(PickpocketPlugin.PICKPOCKET_ANIM)

            val roll = PickpocketPlugin.pickpocketRoll(player, 84.0, 240.0, table)

            fun applyFail() {
                val npc = node.asNpc()
                npc.face(player)
                npc.animator.animate(PickpocketPlugin.NPC_ANIM)
                sendMessage(player, "You fail to pick the workman's pocket.")
                sendChat(npc, "What do you think you're doing???")
                sendMessage(player, "You have been stunned.")
                playHurtAudio(player, 20)
                stun(player, 3)
                player.impactHandler.manualHit(npc, 1, ImpactHandler.HitsplatType.NORMAL)
                npc.face(null)
            }

            fun applySuccess() {
                queueScript(player, PickpocketPlugin.PICKPOCKET_ANIM.duration, QueueStrength.NORMAL) { stage ->
                    if (stage == 0) {
                        val loot = roll!!
                        if (loot.isNotEmpty()) {
                            val id = loot[0].id
                            addItemOrDrop(player, id)

                            when (id) {
                                Items.ANIMAL_SKULL_671 ->
                                    sendItemDialogue(player, id, "You steal an animal skull.")
                                else ->
                                    sendMessage(player, "You steal something.")
                            }
                        } else sendMessage(player, "You couldn't steal anything.")
                        return@queueScript stopExecuting(player)
                    } else return@queueScript stopExecuting(player)
                }
            }
            if (roll == null) applyFail() else applySuccess()
            return@on true
        }

        /*
         * Handles trying to pickpocket a student at the Digsite.
         */

        on(NPCs.STUDENT_617, NPC, "pickpocket") { player, _ ->
            sendDialogue(player, "I don't think I should try to steal from this poor student.")

            return@on true
        }

        /*
         * Handles searching a bush.
         */

        on(Scenery.BUSH_2357, SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the bush... You find nothing of interest.")
            return@on true
        }

        /*
         * Handles searching the quest bush where the teddy bear item can be found.
         */

        on(Scenery.BUSH_2358, SCENERY, "search") { player, _ ->
            if(inInventory(player, Items.TEDDY_673)) return@on false
            sendPlayerDialogue(player, "Hey, something has been dropped here...")
            runTask(player, 3){
                sendItemDialogue(player, Items.TEDDY_673, "You find... something.")
                addItemOrDrop(player, Items.TEDDY_673)
            }
            return@on true
        }

        /*
         * Handles giving a cup of tea to the Panning Guide NPC.
         * Unlocks permission for the player to pan for items.
         */

        onUseWith(NPC, Items.CUP_OF_TEA_712, NPCs.PANNING_GUIDE_620) { player, used, with ->
            if (removeItem(player, used)) {
                sendNPCDialogue(player, with.id, "Ah! Lovely! You can't beat a good cuppa! You're free to pan all you want.")
                setAttribute(player, TheDigSite.attributePanningGuideTea, true)
            }
            return@onUseWith true
        }

        /*
         * Handles using a panning tray on the panning point.
         * Allows panning only if the player gave tea to the guide.
         */

        onUseWith(SCENERY, Items.PANNING_TRAY_677, Scenery.PANNING_POINT_2363) { player, used, _ ->
            if (getAttribute(player, TheDigSite.attributePanningGuideTea, false)) {
                queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, PANNING_ANIMATION)
                            lock(player, PANNING_ANIMATION.duration)
                            return@queueScript delayScript(player, PANNING_ANIMATION.duration)
                        }

                        1 -> {
                            sendItemDialogue(player, Items.PANNING_TRAY_679, "You lift the full tray from the water")
                            if (removeItem(player, used)) {
                                addItemOrDrop(player, Items.PANNING_TRAY_679)
                            }
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                openDialogue(player, PanningGuideDialogue(), findNPC(NPCs.PANNING_GUIDE_620)!!)
            }
            return@onUseWith true
        }

        /*
         * Handles direct panning interaction at the panning point.
         * Requires permission and an empty panning tray in inventory.
         */

        on(Scenery.PANNING_POINT_2363, SCENERY, "pan") { player, _ ->
            if (getAttribute(player, TheDigSite.attributePanningGuideTea, false)) {
                if (inInventory(player, Items.PANNING_TRAY_677)) {
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                playAudio(player, Sounds.DIGSITE_PANNING_2377)
                                animate(player, PANNING_ANIMATION)
                                return@queueScript delayScript(player, PANNING_ANIMATION.duration)
                            }

                            1 -> {
                                sendItemDialogue(
                                    player,
                                    Items.PANNING_TRAY_679,
                                    "You lift the full tray from the water.",
                                )
                                if (removeItem(player, Items.PANNING_TRAY_677)) {
                                    addItemOrDrop(player, Items.PANNING_TRAY_679)
                                }
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else if (inInventory(player, Items.PANNING_TRAY_679)) {
                    sendPlayerDialogue(player, "I already have a full panning tray; perhaps I should search it first.")
                } else {
                    sendMessage(player, "I need a panning tray to pan the panning point.")
                }
            } else {
                openDialogue(player, PanningGuideDialogue(), findNPC(NPCs.PANNING_GUIDE_620)!!)
            }
            return@on true
        }

        /*
         * Handles searching an empty panning tray.
         */

        on(Items.PANNING_TRAY_677, ITEM, "search") { player, _ ->
            sendMessage(player, "The panning tray is empty.")
            return@on true
        }

        /*
         * Handles searching a full panning tray.
         */

        on(Items.PANNING_TRAY_679, ITEM, "search") { player, used ->
            sendMessage(player, "You search the contents of the tray.")
            if (removeItem(player, used)) {
                addItemOrDrop(player, Items.PANNING_TRAY_677)
                val tableRoll = panningTable.roll()
                if (tableRoll.size > 0) {
                    addItemOrDrop(player, tableRoll[0].id)
                    when (tableRoll[0].id) {
                        Items.COINS_995 -> sendItemDialogue(player, Items.COINS_995, "You find some coins within the mud.")
                        Items.NUGGETS_680 -> sendItemDialogue(player, Items.PANNING_TRAY_678, "You find some gold nuggets within the mud.")
                        Items.OYSTER_407 -> sendItemDialogue(player, Items.OYSTER_407, "You find an oyster within the mud.")
                        Items.UNCUT_OPAL_1625 -> sendItemDialogue(player, Items.UNCUT_OPAL_1625, "You find a gem within the mud!")
                        Items.UNCUT_JADE_1627 -> sendItemDialogue(player, Items.UNCUT_JADE_1627, "You find a gem within the mud!")
                        Items.SPECIAL_CUP_672 -> sendItemDialogue(player, Items.SPECIAL_CUP_672, "You find a shiny cup covered in mud.")
                    }
                } else {
                    sendItemDialogue(player, Items.PANNING_TRAY_679, "The tray contains only plain mud.")
                }
            }
            return@on true
        }

        /*
          * Handles digging in soil spots using a trowel at different Digsite levels.
          */

        onUseWith(SCENERY, Items.TROWEL_676, Scenery.SOIL_2376, Scenery.SOIL_2377, Scenery.SOIL_2378) { player, _, _ ->
            val level3DigRight = ZoneBorders(3370, 3437, 3377, 3442)
            val level3DigLeft = ZoneBorders(3350, 3404, 3357, 3412)
            if (level3DigRight.insideBorder(player.location) || level3DigLeft.insideBorder(player.location)) {
                if (getQuestStage(player, Quests.THE_DIG_SITE) >= 6) {
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth...")
                                playAudio(player, Sounds.DIGSITE_DIG_TROWEL_2376)
                                animate(player, TROWEL_ANIMATION)
                                lock(player, TROWEL_ANIMATION.duration)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = level3DigTable.roll()
                                sendMessage(player, "You carefully clean your find with the specimen brush.")
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                    sendMessage(player, "You find a ${tableRoll[0].name.lowercase()}.")
                                }
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    sendNPCDialogue(
                        player,
                        NPCs.DIGSITE_WORKMAN_613,
                        "Oi! What do you think you're doing? There's fragile specimens around here!",
                        FaceAnim.ANGRY,
                    )
                }
            }

            val level2Dig = ZoneBorders(3350, 3424, 3363, 3430)
            if (level2Dig.insideBorder(player.location)) {
                if (getQuestStage(player, Quests.THE_DIG_SITE) >= 5) {
                    lock(player, TROWEL_ANIMATION.duration)
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth...")
                                playAudio(player, Sounds.DIGSITE_DIG_TROWEL_2376)
                                animate(player, TROWEL_ANIMATION)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = level2DigTable.roll()
                                sendMessage(player, "You carefully clean your find with the specimen brush.")
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                    sendMessage(player, "You find a ${tableRoll[0].name.lowercase()}.")
                                }
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    sendNPCDialogue(
                        player,
                        NPCs.DIGSITE_WORKMAN_613,
                        "Oi! What do you think you're doing? There's fragile specimens around here!",
                        FaceAnim.ANGRY,
                    )
                }
            }

            val level1DigCentre = ZoneBorders(3360, 3402, 3363, 3414)
            val level1DigRight = ZoneBorders(3367, 3403, 3372, 3414)
            if (level1DigCentre.insideBorder(player.location) || level1DigRight.insideBorder(player.location)) {
                if (getQuestStage(player, Quests.THE_DIG_SITE) >= 4) {
                    lock(player, TROWEL_ANIMATION.duration)
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth...")
                                playAudio(player, Sounds.DIGSITE_DIG_TROWEL_2376)
                                animate(player, TROWEL_ANIMATION)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = level1DigTable.roll()
                                sendMessage(player, "You carefully clean your find with the specimen brush.")
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                    sendMessage(player, "You find a ${tableRoll[0].name.lowercase()}.")
                                }
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    sendNPCDialogue(
                        player,
                        NPCs.DIGSITE_WORKMAN_613,
                        "Oi! What do you think you're doing? There's fragile specimens around here!",
                        FaceAnim.ANGRY,
                    )
                }
            }

            val trainingDigLeft = ZoneBorders(3352, 3396, 3357, 3400)
            val trainingDigRight = ZoneBorders(3367, 3397, 3372, 3400)
            if (trainingDigLeft.insideBorder(player.location) || trainingDigRight.insideBorder(player.location)) {
                if (getQuestStage(player, Quests.THE_DIG_SITE) >= 3) {
                    lock(player, TROWEL_ANIMATION.duration)
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth...")
                                playAudio(player, Sounds.DIGSITE_DIG_TROWEL_2376)
                                animate(player, TROWEL_ANIMATION)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = trainingDigTable.roll()
                                sendMessage(player, "You carefully clean your find with the specimen brush.")
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                    sendMessage(player, "You find a ${tableRoll[0].name.lowercase()}.")
                                }
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
            }

            return@onUseWith true
        }

        /*
         * Handles operating the winch in the Digsite area.
         */

        on(Scenery.WINCH_2350, SCENERY, "operate") { player, _ ->
            if (getQuestStage(player, Quests.THE_DIG_SITE) >= 11) {
                sendMessage(player, "You try to climb down the rope...")
                sendMessage(player, "You lower yourself into the shaft...")
                teleport(player, Location(3369, 9763))
                sendMessage(player, "You find yourself in a cavern...")
            } else if (getQuestStage(player, Quests.THE_DIG_SITE) >= 8) {
                if (getAttribute(player, TheDigSite.attributeRopeNorthEastWinch, false)) {
                    sendMessage(player, "You try to climb down the rope...")
                    sendMessage(player, "You lower yourself into the shaft...")
                    teleport(player, Location(3369, 9827))
                    sendMessage(player, "You find yourself in a cavern...")
                } else {
                    sendMessage(player, "You operate the winch...")
                    queueScript(player, 2, QueueStrength.NORMAL) {
                        sendPlayerDialogue(player, "Hey, I think I could fit down here. I need something to help me get all the way down.")
                        sendMessage(player, "The bucket descends, but does not reach the bottom.")
                        return@queueScript stopExecuting(player)
                    }
                }
            } else {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also { stage++ }
                                1 -> npc(NPCs.DIGSITE_WORKMAN_613, "Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                            }
                        }
                    },
                )
            }

            return@on true
        }

        /*
         * Handles using a rope on the northeast winch during The Dig Site quest.
         * Allows the player to tie a rope if the quest stage is 8 or higher.
         * Otherwise, a workman blocks access.
         */

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.WINCH_2350) { player, used, _ ->
            if (removeItem(player, used)) {
                if (getQuestStage(player, Quests.THE_DIG_SITE) >= 8) {
                    setAttribute(player, TheDigSite.attributeRopeNorthEastWinch, true)
                    sendMessage(player, "You tie the rope to the bucket.")
                } else {
                    openDialogue(
                        player,
                        object : DialogueFile() {
                            override fun handle(componentID: Int, buttonID: Int) {
                                when (stage) {
                                    0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also { stage++ }
                                    1 -> npc(NPCs.DIGSITE_WORKMAN_613, "Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                                }
                            }
                        },
                    )
                }
            }
            return@onUseWith true
        }

        /*
         * Handles climbing up the northeast winch rope from the cave below.
         */

        on(Scenery.ROPE_2352, SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(3370, 3427))
            return@on true
        }

        /*
         * Handles operating the west winch.
         */

        on(Scenery.WINCH_2351, SCENERY, "operate") { player, _ ->
            if (getQuestStage(player, Quests.THE_DIG_SITE) >= 11) {
                sendMessage(player, "You try to climb down the rope...")
                sendMessage(player, "You lower yourself into the shaft...")
                teleport(player, Location(3352, 9753))
                sendMessage(player, "You find yourself in a cavern...")
            } else if (getQuestStage(player, Quests.THE_DIG_SITE) >= 8) {
                if (getAttribute(player, TheDigSite.attributeRopeWestWinch, false)) {
                    sendMessage(player, "You try to climb down the rope...")
                    sendMessage(player, "You lower yourself into the shaft...")
                    teleport(player, Location(3352, 9818))
                    sendMessage(player, "You find yourself in a cavern...")
                } else {
                    sendMessage(player, "You operate the winch...")
                    queueScript(player, 2, QueueStrength.NORMAL) {
                        sendPlayerDialogue(
                            player,
                            "Hey, I think I could fit down here. I need something to help me get all the way down.",
                        )
                        sendMessage(player, "The bucket descends, but does not reach the bottom.")
                        return@queueScript stopExecuting(player)
                    }
                }
            } else {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also { stage++ }
                                1 -> npc(NPCs.DIGSITE_WORKMAN_613, "Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                            }
                        }
                    },
                )
            }
            return@on true
        }

        /*
         * Handles using a rope on the west winch.
         */

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.WINCH_2351) { player, used, _ ->
            if (removeItem(player, used)) {
                if (getQuestStage(player, Quests.THE_DIG_SITE) >= 8) {
                    setAttribute(player, TheDigSite.attributeRopeWestWinch, true)
                    sendMessage(player, "You tie the rope to the bucket.")
                } else {
                    openDialogue(
                        player,
                        object : DialogueFile() {
                            override fun handle(componentID: Int, buttonID: Int) {
                                when (stage) {
                                    0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also { stage++ }
                                    1 -> npc(NPCs.DIGSITE_WORKMAN_613, "Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                                }
                            }
                        },
                    )
                }
            }
            return@onUseWith true
        }

        /*
         * Handles climbing up the west winch rope from the underground cavern.
         */

        on(Scenery.ROPE_2353, SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(3354, 3417))
            return@on true
        }

        /*
         * Handles reading the invitation letter item.
         */

        on(Items.INVITATION_LETTER_696, ITEM, "read") { player, _ ->
            sendPlayerDialogue(player, "It says, 'I give permission for the bearer... to use the mine shafts on site. - signed Terrance Balando, Archaeological Expert, City of Varrock.")
            return@on true
        }

        /*
         * Handles searching the brick wall near the Digsite cave.
         */

        on(Scenery.BRICK_2362, SCENERY, "search") { player, _ ->
            if (getQuestStage(player, Quests.THE_DIG_SITE) == 8) {
                sendPlayerDialogue(player, "Hmmm, there's a room past these bricks. If I could move them out of the way then I could find out what's inside. Maybe there's someone around here who can help...", FaceAnim.THINKING)
                setQuestStage(player, Quests.THE_DIG_SITE, 9)
            }
            if (getQuestStage(player, Quests.THE_DIG_SITE) == 9) {
                sendPlayerDialogue(player, "Hmmm, there's a room past these bricks. If I could move them out of the way then I could find out what's inside. Maybe there's someone around here who can help...", FaceAnim.THINKING)
            }
            if (getQuestStage(player, Quests.THE_DIG_SITE) == 10) {
                sendPlayerDialogue(player, "The brick is covered with the chemicals I made.", FaceAnim.THINKING)
            }
            return@on true
        }

        /*
         * Handles searching the locked chest before obtaining the key.
         */

        on(Scenery.CHEST_2361, SCENERY, "search") { player, _ ->
            sendMessage(player, "The chest is locked.")
            return@on true
        }

        /*
         * Handles using the chest key on the locked chest to unlock it.
         */

        onUseWith(IntType.SCENERY, Items.CHEST_KEY_709, Scenery.CHEST_2361) { player, used, with ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            sendMessage(player, "You use the key in the chest.")
            animate(player, 536)
            replaceScenery(with.asScenery(), Scenery.CHEST_2360, 100)
            return@onUseWith true
        }

        /*
         * Handles searching specimen trays for archaeological finds.
         * Requires specimen jar in inventory.
         */

        on(Scenery.SPECIMEN_TRAY_2375, SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.SPECIMEN_JAR_669)) {
                sendMessage(player, "You sift through the earth in the tray.")
                animate(player, BENDING_DOWN_ANIMATION)
                val tableRoll = specimenTrayTable.roll()
                if (tableRoll.size > 0) {
                    addItemOrDrop(player, tableRoll[0].id)
                }
            } else {
                sendMessage(player, "You need to have a specimen jar when you are searching the tray.")
            }
            return@on true
        }

        /*
         * Handles searching the now-unlocked chest
         * to find the chemical powder.
         */

        on(Scenery.CHEST_2360, SCENERY, "search") { player, _ ->
            addItemOrDrop(player, Items.CHEMICAL_POWDER_700)
            sendItemDialogue(player, Items.CHEMICAL_POWDER_700, "You find some unusual powder inside...")
            return@on true
        }

        /*
         * Handles filling a vial with the
         * unidentified liquid from the open barrel.
         */

        onUseWith(SCENERY, Items.VIAL_229, Scenery.BARREL_17297) { player, used, _ ->
            if (removeItem(player, used)) {
                addItemOrDrop(player, Items.UNIDENTIFIED_LIQUID_702)
                openDialogue(
                    player,
                    object : DialogueBuilderFile() {
                        override fun create(b: DialogueBuilder) {
                            b
                                .onPredicate { _ -> true }
                                .item(Items.UNIDENTIFIED_LIQUID_702, "You fill the vial with the liquid.")
                                .player(
                                    "I'm not sure what this stuff is. I had better be VERY",
                                    "careful with it; I had better not drop it either...",
                                )
                        }
                    },
                )
                sendMessage(player, "You put the lid back on the barrel just in case it's dangerous.")
                setVarbit(player, TheDigSite.barrelVarbit, 0)
            }
            return@onUseWith true
        }

        /*
         * Handles attempting to open a sealed barrel without tools.
         */

        on(Scenery.BARREL_17296, SCENERY, "search", "open") { player, _ ->
            sendPlayerDialogue(
                player,
                "Mmmm... The lid is shut tight; I'll have to find something to lever it off.",
                FaceAnim.THINKING,
            )
            return@on true
        }

        /*
         * Handles prying open the sealed barrel using a trowel.
         */

        onUseWith(SCENERY, Items.TROWEL_676, Scenery.BARREL_17296) { player, _, _ ->
            sendPlayerDialogue(player, "Great! It's opened it.")
            setVarbit(player, TheDigSite.barrelVarbit, 1)
            return@onUseWith true
        }

        /*
         * Handles searching the open barrel before obtaining a vial.
         */

        on(Scenery.BARREL_17297, SCENERY, "search") { player, _ ->
            sendPlayerDialogue(player, "I can't pick this up with my bare hands! I'll need something to put it in. It looks and smells rather dangerous though, so it'll need to be something small and capable of containing dangerous chemicals.", FaceAnim.THINKING)
            return@on true
        }

        /*
         * Handles mixing ammonium nitrate with nitroglycerin.
         */

        onUseWith(ITEM, Items.AMMONIUM_NITRATE_701, Items.NITROGLYCERIN_703) { player, used, with ->
            if (getStatLevel(player, Skills.HERBLORE) < 10) {
                sendMessage(player, "You need level 10 Herblore to combine the chemicals.")
                return@onUseWith true
            }
            sendMessage(player, "You mix the nitrate powder into the liquid.")
            sendMessage(player, "It has produced a foul mixture.")
            if (removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.MIXED_CHEMICALS_705)
            }
            return@onUseWith true
        }

        /*
         * Handles mixing mixed chemicals with ground charcoal.
         */

        onUseWith(ITEM, Items.MIXED_CHEMICALS_705, Items.GROUND_CHARCOAL_704) { player, used, with ->
            if (getStatLevel(player, Skills.HERBLORE) < 10) {
                sendMessage(player, "You need level 10 Herblore to combine the chemicals.")
                return@onUseWith true
            }
            sendMessage(player, "You mix the charcoal into the liquid.")
            sendMessage(player, "It has produced an even fouler mixture.")
            if (removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.MIXED_CHEMICALS_706)
            }
            return@onUseWith true
        }

        /*
         * Handles adding arceniaroot to the foul mixture
         * to create the explosive compound.
         */

        onUseWith(ITEM, Items.MIXED_CHEMICALS_706, Items.ARCENIA_ROOT_708) { player, used, with ->
            if (getStatLevel(player, Skills.HERBLORE) < 10) {
                sendMessage(player, "You need level 10 Herblore to combine the chemicals.")
                return@onUseWith true
            }
            sendMessage(player, "You mix the root into the mixture.")
            sendMessage(player, "You produce a potentially explosive compound.")
            sendPlayerDialogue(player, "Excellent! This looks just right!", FaceAnim.HAPPY)
            if (removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.CHEMICAL_COMPOUND_707)
            }
            return@onUseWith true
        }

        /*
         * Handles using the chemical compound on the brick wall.
         * Updates quest progress to stage 10.
         */

        onUseWith(SCENERY, Items.CHEMICAL_COMPOUND_707, Scenery.BRICK_2362) { player, used, _ ->
            if (getQuestStage(player, Quests.THE_DIG_SITE) == 9) {
                if (removeItem(player, used)) {
                    addItemOrDrop(player, Items.VIAL_229)
                    sendMessage(player, "You pour the compound over the bricks...")
                    sendPlayerDialogue(
                        player,
                        "Ok, the mixture is all over the bricks. I need some way to ignite this compound.",
                        FaceAnim.THINKING,
                    )
                    setQuestStage(player, Quests.THE_DIG_SITE, 10)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles igniting the chemical compound with a tinderbox
         * to blast open the bricks.
         */

        onUseWith(SCENERY, Items.TINDERBOX_590, Scenery.BRICK_2362) { player, _, _ ->
            if (getQuestStage(player, Quests.THE_DIG_SITE) == 10) {
                setQuestStage(player, Quests.THE_DIG_SITE, 11)
                lock(player, 15)
                queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, BENDING_DOWN_ANIMATION)
                            sendMessage(player, "You strike the tinderbox...")
                            return@queueScript delayScript(player, 2)
                        }

                        1 -> {
                            sendMessage(player, "Fizz..")
                            sendPlayerDialogue(player, "Woah! This is going to blow! I'd better run!", FaceAnim.EXTREMELY_SHOCKED)
                            return@queueScript delayScript(player, BENDING_DOWN_ANIMATION.duration)
                        }

                        2 -> {
                            player.walkingQueue.reset()
                            player.walkingQueue.addPath(3366, 9830)
                            return@queueScript delayScript(player, 8)
                        }

                        3 -> {
                            PlayerCamera(player).shake(0, 20, 8, 128, 40)
                            return@queueScript delayScript(player, 4)
                        }

                        4 -> {
                            PlayerCamera(player).reset()
                            teleport(player, Location(3366, 9766))
                            unlock(player)
                            sendPlayerDialogue(player, "Wow, that was a big explosion! What's that noise I can hear? Sounds like bones moving or something...", FaceAnim.EXTREMELY_SHOCKED)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles picking up the stone tablet in the underground chamber.
         */

        on(Scenery.STONE_TABLET_17367, SCENERY, "take") { player, _ ->
            setVarbit(player, TheDigSite.tabletVarbit, 1)
            addItemOrDrop(player, Items.STONE_TABLET_699)
            sendMessage(player, "You pick the stone tablet up.")
            return@on true
        }

        /*
         * Handles reading the stone tablet item.
         */

        on(Items.STONE_TABLET_699, ITEM, "read") { player, _ ->
            sendPlayerDialogue(player, "It says: Tremble mortal, before the altar of our dread lord Zaros.")
            return@on true
        }

        /*
         * Handles emptying the unidentified liquid from a vial.
         */

        on(Items.UNIDENTIFIED_LIQUID_702, ITEM, "empty") { player, node ->
            if (removeItem(player, node)) {
                addItemOrDrop(player, Items.VIAL_229)
            }
            sendChat(player, "You very carefully empty out the liquid.")
            return@on true
        }

        /*
         * Handles dropping dangerous liquid items,
         * causing them to explode and damage the player.
         */

        on(Items.UNIDENTIFIED_LIQUID_702, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 25)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.NITROGLYCERIN_703, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 35)
            sendChat(player, "Ow! The nitroglycerin exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.MIXED_CHEMICALS_705, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 45)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.MIXED_CHEMICALS_706, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 55)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.CHEMICAL_COMPOUND_707, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 65)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        /*
         * Handles searching cupboards for
         * quest-related items (specimen jars, rock picks).
         */

        on(Scenery.CUPBOARD_17303, SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.SPECIMEN_JAR_669, "You find a specimen jar.")
            addItemOrDrop(player, Items.SPECIMEN_JAR_669)
            return@on true
        }

        on(Scenery.CUPBOARD_35223, SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.ROCK_PICK_675, "You find a rock pick.")
            addItemOrDrop(player, Items.ROCK_PICK_675)
            return@on true
        }

        /*
         * Handles searching sacks for specimen jars.
         */

        on(intArrayOf(Scenery.SACKS_2354, Scenery.SACKS_2355, Scenery.SACKS_2356), SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.SPECIMEN_JAR_669, "You find a specimen jar.")
            addItemOrDrop(player, Items.SPECIMEN_JAR_669)
            return@on true
        }

        /*
         * Handles searching the bookcase to obtain the Book on Chemicals.
         */

        on(Scenery.BOOKCASE_35224, SCENERY, "search") { player, _ ->
            sendMessage(player, "You search through the bookcase...")
            sendItemDialogue(player, Items.BOOK_ON_CHEMICALS_711, "You find a book on chemicals.")
            addItemOrDrop(player, Items.BOOK_ON_CHEMICALS_711)
            return@on true
        }

        /*
         * Handles reading the various signposts around the Digsite.
         */

        on(Scenery.SIGNPOST_2366, SCENERY, "read") { player, _ ->
            sendMessage(player, "This site is for training purposes only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2367, SCENERY, "read") { player, _ ->
            sendMessage(player, "Level 1 digs only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2368, SCENERY, "read") { player, _ ->
            sendMessage(player, "Level 2 digs only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2369, SCENERY, "read") { player, _ ->
            sendMessage(player, "Level 3 digs only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2370, SCENERY, "read") { player, _ ->
            sendMessage(player, "Private dig.")
            return@on true
        }

        on(Scenery.SIGNPOST_2371, SCENERY, "read") { player, _ ->
            sendMessage(player, "Digsite educational centre.")
            return@on true
        }
    }

    companion object {
        val BENDING_DOWN_ANIMATION = Animation(827)
        val TROWEL_ANIMATION = Animation(2272)
        val PANNING_ANIMATION = Animation(4593)

        val trainingDigTable =
            WeightBasedTable.create(
                WeightedItem(0, 0, 0, 8.0, false),
                WeightedItem(Items.COINS_995, 1, 1, 1.0, false),
                WeightedItem(Items.CHARCOAL_973, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARROW_687, 1, 1, 1.0, false),
                WeightedItem(Items.CRACKED_SAMPLE_674, 1, 1, 1.0, false),
                WeightedItem(Items.VASE_710, 1, 1, 1.0, false),
            )
        val level1DigTable =
            WeightBasedTable.create(
                WeightedItem(0, 0, 0, 2.0, false),
                WeightedItem(Items.BUTTONS_688, 1, 1, 1.0, false),
                WeightedItem(Items.VASE_710, 1, 1, 1.0, false),
                WeightedItem(Items.COPPER_ORE_436, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_BOOTS_1061, 1, 1, 1.0, false),
                WeightedItem(Items.OPAL_1609, 1, 1, 1.0, false),
                WeightedItem(Items.OLD_TOOTH_695, 1, 1, 1.0, false),
                WeightedItem(Items.ROTTEN_APPLE_1984, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_GLASS_1469, 1, 1, 1.0, false),
                WeightedItem(Items.RUSTY_SWORD_686, 1, 1, 1.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 1.0, false),
            )
        val level2DigTable =
            WeightBasedTable.create(
                WeightedItem(0, 0, 0, 3.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 2.0, false),
                WeightedItem(Items.DAMAGED_ARMOUR_697, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_BOOTS_1061, 1, 1, 1.0, false),
                WeightedItem(Items.BOWL_1923, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_STAFF_689, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARMOUR_698, 1, 1, 1.0, false),
                WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_GLASS_1469, 1, 1, 1.0, false),
                WeightedItem(Items.JUG_1935, 1, 1, 1.0, false),
                WeightedItem(Items.EMPTY_POT_1931, 1, 1, 1.0, false),
                WeightedItem(Items.CLAY_434, 1, 1, 1.0, false),
                WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 1.0, false),
            )
        val level3DigTable =
            WeightBasedTable.create(
                WeightedItem(0, 0, 0, 1.0, false),
                WeightedItem(Items.COINS_995, 10, 10, 2.0, false),
                WeightedItem(Items.ANCIENT_TALISMAN_681, 1, 1, 1.0, false),
                WeightedItem(Items.BELT_BUCKLE_684, 1, 1, 2.0, false),
                WeightedItem(Items.BLACK_MED_HELM_1151, 1, 1, 1.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARMOUR_698, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARROW_687, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_STAFF_689, 1, 1, 1.0, false),
                WeightedItem(Items.BRONZE_SPEAR_1237, 1, 1, 1.0, false),
                WeightedItem(Items.BUTTONS_688, 1, 1, 1.0, false),
                WeightedItem(Items.CERAMIC_REMAINS_694, 1, 1, 1.0, false),
                WeightedItem(Items.CLAY_434, 1, 1, 1.0, false),
                WeightedItem(Items.DAMAGED_ARMOUR_697, 1, 1, 1.0, false),
                WeightedItem(Items.IRON_KNIFE_863, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_BOOTS_1061, 1, 1, 1.0, false),
                WeightedItem(Items.NEEDLE_1733, 1, 1, 1.0, false),
                WeightedItem(Items.OLD_TOOTH_695, 1, 1, 1.0, false),
                WeightedItem(Items.PIE_DISH_2313, 1, 1, 1.0, false),
            )

        val specimenTrayTable =
            WeightBasedTable.create(
                WeightedItem(0, 0, 0, 2.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 2.0, false),
                WeightedItem(Items.COINS_995, 1, 1, 1.0, false),
                WeightedItem(Items.IRON_DAGGER_1203, 1, 1, 1.0, false),
                WeightedItem(Items.CHARCOAL_973, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARROW_687, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_GLASS_1469, 1, 1, 1.0, false),
                WeightedItem(Items.CERAMIC_REMAINS_694, 1, 1, 1.0, false),
                WeightedItem(Items.CRACKED_SAMPLE_674, 1, 1, 1.0, false),
            )

        val workmanPickpocketingTable =
            WeightBasedTable.create(
                WeightedItem(Items.SPECIMEN_BRUSH_670, 1, 1, 3.0, false),
                WeightedItem(Items.ANIMAL_SKULL_671, 1, 1, 3.0, false),
                WeightedItem(Items.COINS_995, 10, 10, 1.0, false),
                WeightedItem(Items.ROPE_954, 1, 1, 1.0, false),
                WeightedItem(Items.BUCKET_1925, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_GLOVES_1059, 1, 1, 1.0, false),
                WeightedItem(Items.SPADE_952, 1, 1, 1.0, false),
            )

        val workmanPostQuestPickpocketingTable =
            WeightBasedTable.create(
                WeightedItem(Items.SPECIMEN_BRUSH_670, 1, 1, 3.0, false),
                WeightedItem(Items.COINS_995, 10, 10, 4.0, false),
                WeightedItem(Items.ROPE_954, 1, 1, 1.0, false),
                WeightedItem(Items.BUCKET_1925, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_GLOVES_1059, 1, 1, 1.0, false),
                WeightedItem(Items.SPADE_952, 1, 1, 1.0, false),
            )

        val panningTable =
            WeightBasedTable.create(
                WeightedItem(0, 0, 0, 20.0, false),
                WeightedItem(Items.COINS_995, 1, 1, 4.0, false),
                WeightedItem(Items.NUGGETS_680, 1, 1, 4.0, false),
                WeightedItem(Items.OYSTER_407, 1, 1, 3.0, false),
                WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 3.0, false),
                WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 3.0, false),
                WeightedItem(Items.SPECIAL_CUP_672, 1, 1, 3.0, false),
            )
    }
}
