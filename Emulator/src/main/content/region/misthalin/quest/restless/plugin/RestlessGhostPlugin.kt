package content.region.misthalin.quest.restless.plugin

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class RestlessGhostPlugin : InteractionListener {
    override fun defineListeners() {
        on(COFFIN_IDS, IntType.SCENERY, "open", "close", "search") { player, node ->
            val option = getUsedOption(player)
            val obj = node.asScenery()

            if (GHOST == null) {
                GHOST = RestlessGhostNPC(NPCs.RESTLESS_GHOST_457, Location.create(3250, 3195, 0))
                GHOST!!.init()
                GHOST!!.isInvisible = true
            }

            when (option) {
                "open", "close" -> toggleCoffin(player, obj)
                "search" ->
                    when (node.id) {
                        Scenery.COFFIN_2145 -> toggleCoffin(player, obj)
                        Scenery.COFFIN_15052 ->
                            sendMessage(
                                player,
                                "You search the coffin and find some human remains.",
                            )

                        Scenery.COFFIN_15053 -> sendDialogue(player, "There's a nice and complete skeleton in here!")
                        Scenery.ALTAR_15050 -> searchAltar(player, obj)
                        Scenery.ALTAR_15051 -> {
                            if (!isQuestComplete(player, Quests.THE_RESTLESS_GHOST) &&
                                !inBank(player, Items.SKULL_964) &&
                                !inInventory(player, Items.SKULL_964)
                            ) {
                                sendMessage(player, "You find another skull.")
                                addItem(player, Items.SKULL_964)
                            }
                            player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(25), 40)
                        }
                    }
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.SKULL_964, *COFFIN_IDS) { player, _, with ->
            val coffin = with.asScenery().id
            if (coffin == Scenery.COFFIN_2145) {
                sendDialogue(player, "Maybe I should open it first.")
                return@onUseWith true
            }

            if (isQuestComplete(player, Quests.THE_RESTLESS_GHOST)) {
                sendMessage(player, "You have already put the skull in the coffin.")
                return@onUseWith true
            }

            if (removeItem(player, Item(Items.SKULL_964, 1))) {
                setVarbit(player, Vars.VARBIT_RESTLESS_GHOST_PUT_SKULL_2129, 1, true)
                animate(player, Animations.PUT_OBJECT_ON_TABLE_537)
                playAudio(player, Sounds.RG_PLACE_SKULL_1744)
                sendMessage(player, "You put the skull in the coffin.")
                RestlessGhostCutscene(player).start(true)
            }
            return@onUseWith true
        }

        on(Items.SKULL_964, IntType.ITEM, "drop") { player, _ ->
            sendMessage(player, "You can't drop this! Return it to the ghost.")
            return@on false
        }
    }

    private fun toggleCoffin(
        player: Player,
        n: Node,
    ) {
        val coffin = n.asScenery()
        val closedCoffin = n.id == Scenery.COFFIN_2145

        lock(player, 2)

        animate(
            entity = player,
            anim =
                if (closedCoffin) {
                    Animation(Animations.OPEN_CHEST_536)
                } else {
                    Animation(Animations.OPEN_POH_WARDROBE_535)
                },
        )

        if (closedCoffin) {
            playAudio(player, Sounds.COFFIN_CLOSE_53)
        } else {
            playAudio(player, Sounds.COFFIN_OPEN_54)
        }

        replaceScenery(
            toReplace = coffin,
            with = if (closedCoffin) Scenery.COFFIN_15061 else Scenery.COFFIN_2145,
            forTicks = -1,
        )

        sendMessage(
            player = player,
            message = "You " + (if (closedCoffin) "open" else "close") + " the coffin.",
        )

        if (coffin.id == Scenery.COFFIN_2145 && !isQuestComplete(player, Quests.THE_RESTLESS_GHOST)) {
            playAudio(player, Sounds.RG_GHOST_APPROACH_1743, 1)
            if (GHOST!!.isInvisible) {
                val destination = Location.create(3250, 3192, 0)
                spawnProjectile(
                    source = destination,
                    dest = destination.transform(0, 3, 0),
                    projectile = Graphics.SWIRLEY_GREY_SMOKE_668,
                    startHeight = 30,
                    endHeight = 42,
                    delay = 0,
                    speed = 30,
                    angle = 0,
                )
            }
            sendGhost()
        }
    }

    private fun searchAltar(
        player: Player,
        n: Node,
    ) {
        if (getQuestStage(player, Quests.THE_RESTLESS_GHOST) != 30) {
            sendMessage(player, "You search the altar and find nothing.")
            return
        }
        if (n.asScenery().id != Scenery.ALTAR_15051) {
            addItemOrDrop(player, Items.SKULL_964)
            setVarp(player, Vars.VARP_RESTLESS_GHOST_728, 5, true)
            queueScript(player, 1, QueueStrength.NORMAL) {
                setQuestStage(player, Quests.THE_RESTLESS_GHOST, 40)
                sendMessage(player, "The skeleton in the corner suddenly comes to life!")
                sendSkeleton(player)
                return@queueScript stopExecuting(player)
            }
        }
    }

    private fun sendGhost() {
        if (!GHOST!!.isInvisible) {
            return
        }

        GHOST!!.isInvisible = false

        Pulser.submit(
            object : Pulse(100, GHOST) {
                override fun pulse(): Boolean {
                    GHOST!!.isInvisible = true
                    return true
                }
            },
        )
    }

    private fun sendSkeleton(player: Player) {
        val skeleton =
            core.game.node.entity.npc.NPC
                .create(NPCs.SKELETON_459, Location.create(3120, 9568, 0))
        skeleton.isWalks = false
        skeleton.isRespawn = false
        skeleton.setAttribute("player", player)
        skeleton.init()
        playAudio(player, Sounds.RG_SKELETON_AWAKE_1746)
        skeleton.properties.combatPulse.style = CombatStyle.MELEE
        skeleton.properties.combatPulse.attack(player)
    }

    companion object {
        val COFFIN_IDS =
            intArrayOf(
                Scenery.COFFIN_2145,
                Scenery.ALTAR_15050,
                Scenery.ALTAR_15051,
                Scenery.COFFIN_15052,
                Scenery.COFFIN_15053,
                Scenery.COFFIN_15061,
            )
        var GHOST: RestlessGhostNPC? = null
    }
}
