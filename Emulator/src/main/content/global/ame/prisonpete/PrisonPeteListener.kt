package content.global.ame.prisonpete

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Graphics
import org.rs.consts.*

class PrisonPeteListener :
    InteractionListener,
    MapArea {
    init {
        addScenery(4408, Location(2085, 4457, 0), 0, 22)
        addScenery(26191, Location(2083, 4460, 0), 0, 10)
        addScenery(26184, Location(2084, 4459, 0), 1, 0)
        addScenery(26186, Location(2085, 4459, 0), 1, 0)
        addScenery(26188, Location(2086, 4459, 0), 1, 0)
        addScenery(26184, Location(2087, 4459, 0), 1, 0)
    }

    override fun defineListeners() {
        on(NPCs.PRISON_PETE_3118, IntType.NPC, "talk-to") { player, _ ->
            face(player, findNPC(NPCs.PRISON_PETE_3118)!!.location)
            openDialogue(player, PrisonPeteDialogue(0))
            return@on true
        }

        on(Items.PRISON_KEY_6966, IntType.ITEM, "return") { player, _ ->
            openDialogue(player, PrisonPeteDialogue(1))
            return@on true
        }

        on(Scenery.LEVER_26191, IntType.SCENERY, "pull") { player, _ ->
            animate(player, Animations.PULL_LEVER_798)
            openInterface(player, Components.PRISONPETE_273)
            return@on true
        }

        on(PrisonPeteUtils.ANIMAL_ID, IntType.NPC, "pop") { player, node ->
            if (getUsedOption(player) == "pop") {
                if (node.id == 3119 && getAttribute(player, PrisonPeteUtils.POP_KEY_VALUE, -1) == 0) {
                    player.incrementAttribute(PrisonPeteUtils.POP_KEY)
                } else if (node.id == 3120 && getAttribute(player, PrisonPeteUtils.POP_KEY_VALUE, -1) == 1) {
                    player.incrementAttribute(PrisonPeteUtils.POP_KEY)
                } else if (node.id == 3121 && getAttribute(player, PrisonPeteUtils.POP_KEY_VALUE, -1) == 2) {
                    player.incrementAttribute(PrisonPeteUtils.POP_KEY)
                } else if (node.id == 3122 && getAttribute(player, PrisonPeteUtils.POP_KEY_VALUE, -1) == 3) {
                    player.incrementAttribute(PrisonPeteUtils.POP_KEY)
                } else {
                    setAttribute(player, PrisonPeteUtils.POP_KEY_FALSE, true)
                }
            }
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        lock(player, 3)
                        visualize(
                            player,
                            Animations.STOMP_BALLOON_794,
                            Graphics(
                                org.rs.consts.Graphics.WHITE_SPIKE_BALL_POPS_524,
                                0,
                                10,
                            ),
                        )
                        playAudio(player, Sounds.POP3_3252, 5)
                        return@queueScript delayScript(player, 4)
                    }

                    1 -> {
                        animate(player, Animations.MULTI_BEND_OVER_827)
                        addItem(player, Items.PRISON_KEY_6966)
                        node.asNpc().clear()
                        face(player, findNPC(NPCs.PRISON_PETE_3118)!!.location)
                        openDialogue(player, PrisonPeteDialogue(dialOpt = 1))
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(PrisonPeteUtils.PRISON_ZONE)
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.PRISON_PETE_3118), "talk-to") { _, _ ->
            return@setDest Location.create(2084, 4461, 0)
        }
    }
}
