package content.region.kandarin.piscatoris.quest.phoenix.plugin

import content.data.GameAttributes
import content.region.kandarin.piscatoris.quest.phoenix.allTwigs
import content.region.kandarin.piscatoris.quest.phoenix.custcene.FuneralPyreCutscene
import content.region.kandarin.piscatoris.quest.phoenix.custcene.GetLostCutscene
import content.region.kandarin.piscatoris.quest.phoenix.custcene.WoundedPhoenixCutscene
import content.region.kandarin.piscatoris.quest.phoenix.dialogue.PhoenixEgglingDialogue
import core.api.*
import core.api.interaction.getSceneryName
import core.api.item.allInInventory
import core.api.quest.isQuestComplete
import core.api.ui.closeDialogue
import core.api.utils.PlayerCamera
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.*

private val rebornWarriors =
    intArrayOf(
        NPCs.LESSER_REBORN_WARRIOR_8557,
        NPCs.LESSER_REBORN_WARRIOR_8558,
        NPCs.GREATER_REBORN_WARRIOR_8559,
        NPCs.GREATER_REBORN_WARRIOR_8560,
        NPCs.LESSER_REBORN_RANGER_8561,
        NPCs.LESSER_REBORN_RANGER_8562,
        NPCs.GREATER_REBORN_RANGER_8563,
        NPCs.GREATER_REBORN_RANGER_8564,
        NPCs.LESSER_REBORN_MAGE_8565,
        NPCs.LESSER_REBORN_MAGE_8566,
        NPCs.GREATER_REBORN_MAGE_8567,
        NPCs.GREATER_REBORN_MAGE_8568,
    )

class PhoenixLairPlugin : InteractionListener, MapArea {
    private val spawnedNPC = mutableListOf<NPC>()

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders.forRegion(13905), ZoneBorders.forRegion(14161))

    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            val npcIdsToSpawn =
                if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {
                    listOf(
                        NPCs.LESSER_REBORN_MAGE_8573,
                        NPCs.LESSER_REBORN_RANGER_8571,
                        NPCs.LESSER_REBORN_WARRIOR_8569,
                    )
                } else {
                    rebornWarriors.toList()
                }

            respawnPoints.forEach { location ->
                val npcId = npcIdsToSpawn.random()
                val npc = core.game.node.entity.npc.NPC.create(npcId, location)

                npc.apply {
                    isNeverWalks = false
                    isWalks = true
                    isRespawn = false
                    isAggressive = true
                }

                npc.init()

                registerLogoutListener(player, LOGOUT_LISTENER) {
                    npc.clear()
                    spawnedNPC.remove(npc)
                }

                player.incrementAttribute(GameAttributes.PHOENIX_LAIR_VISITED)
                spawnedNPC.add(npc)
            }
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)

        spawnedNPC.forEach { it.clear() }
        spawnedNPC.clear()

        if (logout) {
            clearLogoutListener(entity.asPlayer(), LOGOUT_LISTENER)
        }
    }

    init {
        woundedPhoenix.init()
    }

    companion object {
        private val respawnPoints =
            arrayOf(
                Location.create(3464, 5242, 0),
                Location.create(3460, 5237, 0),
                Location.create(3484, 5234, 0),
                Location.create(3479, 5225, 0),
                Location.create(3475, 5220, 0),
                Location.create(3460, 5222, 0),
                Location.create(3494, 5241, 0),
                Location.create(3498, 5243, 0),
                Location.create(3504, 5241, 0),
                Location.create(3502, 5233, 0),
                Location.create(3492, 5224, 0),
                Location.create(3496, 5222, 0),
                Location.create(3509, 5223, 0),
                Location.create(3516, 5227, 0),
                Location.create(3515, 5242, 0),
                Location.create(3526, 5240, 0),
                Location.create(3526, 5244, 0),
                Location.create(3531, 5236, 0),
                Location.create(3534, 5233, 0),
                Location.create(3527, 5225, 0),
                Location.create(3535, 5220, 0),
                Location.create(3547, 5222, 0),
                Location.create(3547, 5232, 0),
                Location.create(3545, 5240, 0),
                Location.create(3465, 5210, 0),
                Location.create(3462, 5205, 0),
                Location.create(3466, 5204, 0),
                Location.create(3466, 5211, 0),
                Location.create(3471, 5210, 0),
                Location.create(3480, 5212, 0),
                Location.create(3482, 5205, 0),
                Location.create(3484, 5196, 0),
                Location.create(3475, 5190, 0),
                Location.create(3494, 5211, 0),
                Location.create(3498, 5202, 0),
                Location.create(3497, 5192, 0),
                Location.create(3493, 5188, 0),
                Location.create(3515, 5188, 0),
                Location.create(3513, 5196, 0),
                Location.create(3514, 5208, 0),
                Location.create(3505, 5203, 0),
                Location.create(3504, 5210, 0),
                Location.create(3474, 5242, 0),
                Location.create(3480, 5243, 0),
            )

        private const val LOGOUT_LISTENER = "phoenix-lair"
        val weavingRibbons =
            intArrayOf(
                Items.CINNAMON_WEAVING_RIBBON_14611,
                Items.CEDAR_WEAVING_RIBBON_14614,
                Items.AILANTHUS_WEAVING_RIBBON_14613,
                Items.SASSAFRAS_WEAVING_RIBBON_14612,
                Items.MASTIC_WEAVING_RIBBON_14615,
            )

        private val treeItemMap =
            mapOf(
                Scenery.CINNAMON_TREE_41903 to Items.CINNAMON_TWIGS_14606,
                Scenery.SASSAFRAS_TREE_41904 to Items.SASSAFRAS_TWIGS_14607,
                Scenery.AILANTHUS_TREE_41905 to Items.AILANTHUS_TWIGS_14608,
                Scenery.CEDAR_TREE_41906 to Items.CEDAR_TWIGS_14609,
                Scenery.MASTIC_TREE_41907 to Items.MASTIC_TWIGS_14610,
            )
        val phoenixEggling = intArrayOf(NPCs.LARGE_EGG_8552, NPCs.PHOENIX_EGGLING_8550)
        val woundedPhoenix: NPC = NPC.create(NPCs.WOUNDED_PHOENIX_8547, Location.create(3534, 5196, 0), Direction.NORTH)
    }

    override fun defineListeners() {
        /*
         * Handles entering the Phoenix Lair.
         */

        on(Scenery.CAVE_ENTRANCE_41900, IntType.SCENERY, "enter") { player, _ ->
            val familiar = player.familiarManager.familiar

            if (player.familiarManager.hasFamiliar() && familiar != null) {
                if (familiar.id == NPCs.PHOENIX_8575 || familiar.id == NPCs.PHOENIX_8576) {
                    sendNPCDialogue(player, familiar.id, "${player.username}, this is my lair. You must dismiss my summoned form if you wish to enter; I will only duel you when I am at full strength.", FaceAnim.CHILD_NORMAL)
                    return@on true
                }
                if (familiar.id == NPCs.PHOENIX_EGGLING_8577 || familiar.id == NPCs.PHOENIX_EGGLING_8578) {
                    sendNPCDialogue(player, familiar.id, "Why am you bringing me here? I no want to see you fight my mummy! Put me in your bag if you want to go in.", FaceAnim.CHILD_NORMAL)
                    return@on true
                }
            }

            if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) < 1 || !isQuestComplete(player, Quests.IN_PYRE_NEED)) {
                sendNPCDialogue(player, NPCs.PRIEST_OF_GUTHIX_8555, "Hey, wait! I have something to ask you before going down there.", FaceAnim.FRIENDLY)
                addDialogueAction(player) { p, _ ->
                    sendPlayerDialogue(p, "Maybe I should see what he has to say before entering this dark, scary, and unreasonably warm cave.", FaceAnim.THINKING)
                }
            } else {
                teleport(player, Location.create(3461, 5218, 0), TeleportManager.TeleportType.INSTANT)
            }
            return@on true
        }

        /*
         * Handles random cave teleportation logic.
         */

        on(
            intArrayOf(Scenery.CAVE_ENTRANCE_41901, Scenery.CAVE_ENTRANCE_41902),
            IntType.SCENERY,
            "look-at",
            "enter",
        ) { player, _ ->
            val locations =
                listOf(
                    Location.create(3466, 5213, 0),
                    Location.create(3493, 5186, 0), // Sassafras
                    Location.create(3543, 5218, 0),
                    Location.create(3476, 5186, 0),
                    Location.create(3516, 5224, 0), // Authentic
                )
            val phoenixChamber = Location(3566, 5224, 0)
            val outsideCave = Location.create(2294, 3626, 0)
            val randomRoll = RandomFunction.random(100)

            registerLogoutListener(player, "phoenix-lair") { p ->
                player.location = outsideCave
            }

            if (player.location == phoenixChamber || player.location == Location.create(3535, 5186, 0)) {
                setTitle(player, 2)
                sendDialogueOptions(player, "Are you sure you want to leave?", "Yes", "No")
                addDialogueAction(player) { _, button ->
                    when (button) {
                        2 -> {
                            openInterface(player, Components.FADE_TO_BLACK_120)
                            player.animate(Animation.create(Animations.HUMAN_CRAWL_INTO_CAVE_11042), 1)
                            lock(player, 3)
                            queueScript(player, 3, QueueStrength.SOFT) {
                                teleport(player, Location(3566, 5224, 0))
                                resetAnimator(player)
                                openInterface(player, Components.FADE_FROM_BLACK_170)
                                return@queueScript stopExecuting(player)
                            }
                        }

                        3 -> {
                            openInterface(player, Components.FADE_TO_BLACK_120)
                            player.animate(Animation.create(Animations.HUMAN_CRAWL_INTO_CAVE_11042), 1)
                            lock(player, 3)
                            queueScript(player, 3, QueueStrength.SOFT) {
                                teleport(player, locations.random())
                                resetAnimator(player)
                                openInterface(player, Components.FADE_FROM_BLACK_170)
                                return@queueScript stopExecuting(player)
                            }
                        }
                        else -> closeDialogue(player)
                    }
                }
                return@on true
            }

            if (randomRoll == 0) {
                val largeEgg = core.game.node.entity.npc.NPC.create(NPCs.LARGE_EGG_8552, Location.create(3567, 5230, 0))
                largeEgg.init().also {
                    GetLostCutscene(player).start()
                }
                return@on true
            }

            val newLocation =
                when {
                    getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) == 10 -> outsideCave
                    allInInventory(player, *allTwigs) -> Location.create(3535, 5186, 0)
                    else -> locations.random()
                }

            openInterface(player, Components.FADE_TO_BLACK_120)
            player.animate(Animation.create(Animations.HUMAN_CRAWL_INTO_CAVE_11042), 1)
            lock(player, 3)
            queueScript(player, 3, QueueStrength.SOFT) {
                teleport(player, newLocation)
                if (newLocation == Location.create(3535, 5186, 0)) {
                    player.musicPlayer?.let { music ->
                        if (!music.hasUnlocked(Music.THE_PHOENIX_596)) {
                            music.unlock(Music.THE_PHOENIX_596)
                        }
                    }
                }
                resetAnimator(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
                return@queueScript stopExecuting(player)
            }

            return@on true
        }

        /*
         * Handles escaping from the Phoenix lair.
         */

        on(
            intArrayOf(Scenery.CAVE_ENTRANCE_41901, Scenery.CAVE_ENTRANCE_41902),
            IntType.SCENERY,
            "escape",
        ) { player, _ ->
            lock(player, 3)
            openInterface(player, Components.FADE_TO_BLACK_120)
            animate(player, Animations.HUMAN_CRAWL_INTO_CAVE_11042)
            runTask(player, 3) {
                resetAnimator(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
                teleport(player, Location.create(2294, 3626, 0))
                if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {
                    player.dialogueInterpreter.open(NPCs.BRIAN_TWITCHER_8556)
                }
            }
            return@on true
        }

        /*
         * Handles harvesting twigs from trees.
         */

        on(treeItemMap.keys.toIntArray(), IntType.SCENERY, "harvest-twigs") { player, node ->
            val item = treeItemMap[node.id] ?: Items.MASTIC_TWIGS_14610
            if (!player.inventory.contains(item, 1)) {
                lock(player, 3)
                queueScript(player, 1, QueueStrength.SOFT) {
                    animate(
                        player,
                        if (inInventory(player, Items.MAGIC_SECATEURS_7409))
                            Animations.PRUNE_WITH_MAGIC_SECATEURS_11089
                        else
                            Animations.PRUNE_WITH_SECATEURS_11088
                    )
                    addItemOrDrop(player, item)
                    val twigs = getSceneryName(node.id).lowercase().replace("tree", "twigs")
                    sendMessage(player, "You harvest some $twigs.")
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendMessage(player, "You already have ${node.name.lowercase()}.")
            }
            return@on true
        }

        /*
         * Handles weaving ribbons into a basket.
         */

        onUseWith(IntType.SCENERY, weavingRibbons, Scenery.PYRE_41908) { player, _, _ ->
            if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) >= 5) {
                animate(player, if(inInventory(player, Items.MAGIC_SECATEURS_7409)) Animations.PRUNE_WITH_MAGIC_SECATEURS_11089 else Animations.PRUNE_WITH_SECATEURS_11088)
                sendMessage(
                    player,
                    "You weave a large basket from the five wooden ribbons and add it to the pyre base.",
                )
                WoundedPhoenixCutscene(player).start(true)
                weavingRibbons.forEach {
                    removeItem(player, Item(it, 1), Container.INVENTORY)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles lighting the pyre.
         */

        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.PYRE_41908) { player, _, _ ->
            if (getStatLevel(player, Skills.FIREMAKING) < 55) {
                sendDialogue(player, "You need at least 55 firemaking to light the funeral pyre.")
                return@onUseWith true
            }
            if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) >= 5) {
                lock(player, 3)
                animate(player, Animations.TINDERBOX_733)
                FuneralPyreCutscene(player).start()
            }
            return@onUseWith true
        }

        /*
         * Prevents players from lighting the wounded Phoenix.
         */

        onUseWith(IntType.NPC, Items.TINDERBOX_590, NPCs.WOUNDED_PHOENIX_8547) { player, _, _ ->
            sendNPCDialogue(player, NPCs.WOUNDED_PHOENIX_8547, "Light the pyre, not me!", FaceAnim.OLD_NORMAL)
            return@onUseWith true
        }

        /*
         * Handles fletch twigs into ribbons.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, *allTwigs) { player, _, with ->
            if (allTwigs.isNotEmpty() && allTwigs.all { hasAnItem(player, it).container == player.inventory }) {
                lock(player, 3)
                animate(player, Animations.FLETCH_LOGS_4433)
                queueScript(player, 2, QueueStrength.SOFT) {
                    allTwigs.forEach { removeItem(player, Item(it, 1), Container.INVENTORY) }
                    setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 5, true)
                    weavingRibbons.forEach { addItem(player, it, 1, Container.INVENTORY) }
                    sendItemDialogue(player, with.id, "You fetch the ${with.name.lowercase()} into wooden ribbons.")
                    stopExecuting(player)
                }
                return@onUseWith true
            }
            sendMessage(player, "You don't have the required twigs to fletch.")
            return@onUseWith false
        }

        /*
         * Handles interactions with large egg & hatching.
         */

        on(phoenixEggling, IntType.NPC, "Investigate", "Interact") { player, node ->
            val phoenixEggling = core.game.node.entity.npc.NPC.create(NPCs.PHOENIX_EGGLING_8550, Location.create(3567, 5230, 0))
            if (node.id == NPCs.LARGE_EGG_8552) {
                sendDialogueOptions(
                    player,
                    "What would you like to do?",
                    "Clap at the egg to make it hatch.",
                    "Kick the egg to make it hatch."
                )
                addDialogueAction(player) { _, button ->
                    lock(player, 12)
                    queueScript(player, 1, QueueStrength.SOFT) { stage : Int ->
                        when(stage) {
                            0 -> {
                                animate(player, if(button == 2) Animations.CLAP_11091 else Animations.KICK_423)
                                sendChat(player, "*Gasp!* It's...it's hatching!", 2)
                                return@queueScript delayScript(player, 3)
                            }
                            1 -> {
                                // Camera-independent zoom.
                                PlayerCamera(player).panTo(3567, 5226, 300, 100)
                                PlayerCamera(player).rotateTo(3567, 5228, 300, 100)
                                return@queueScript delayScript(player, 4)
                            }
                            2 -> {
                                sendGraphics(1974, findNPC(NPCs.LARGE_EGG_8552)!!.location)
                                return@queueScript delayScript(player, 3)
                            }
                            3 -> {
                                node.asNpc().clear()
                                phoenixEggling.init()
                                // sendGraphics(0000, phoenixEggling.location)
                                sendChat(phoenixEggling, if(button == 2) "Cheeeep!" else "Bwaaark!", 2)
                                return@queueScript delayScript(player, 4)
                            }
                            4 -> {
                                resetCamera(player)
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
            } else {
                val cutePet = getAttribute(player, GameAttributes.PHOENIX_LAIR_EGGLING_CUTE, false)
                val meanPet = getAttribute(player, GameAttributes.PHOENIX_LAIR_EGGLING_MEAN, false)
                if(cutePet && meanPet) {
                    sendMessage(player, "You already catch this pet.")
                    if(findLocalNPC(player, node.id) != null) { runTask(player, 1) { node.asNpc().clear() } }
                    sendMessage(player, "The phoenix eggling disappears.")
                    return@on true
                }
                openDialogue(player, PhoenixEgglingDialogue())
            }
            return@on true
        }
    }
}

private class RebornWarriorBehavior : NPCBehavior(*rebornWarriors) {
    override fun tick(self: NPC): Boolean {
        super.tick(self)
        if (RandomFunction.random(300) < 3) {
            lock(self, 3)
            stopWalk(self)
            self.impactHandler.disabledTicks = 3
            queueScript(self, 0, QueueStrength.STRONG) { stage ->
                when (stage) {
                    0 -> {
                        visualize(self, 11133, 1990)
                        teleport(
                            self,
                            Location.getRandomLocation(self.location, 5, true),
                        )
                        return@queueScript delayScript(self, 6)
                    }

                    1 -> {
                        visualize(self, 11136, 1991)
                        return@queueScript stopExecuting(self)
                    }

                    else -> return@queueScript stopExecuting(self)
                }
            }
        }
        return true
    }
}