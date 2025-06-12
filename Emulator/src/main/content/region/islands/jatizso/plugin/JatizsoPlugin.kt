package content.region.islands.jatizso.plugin

import content.region.islands.jatizso.dialogue.LeftieRightieDialogue
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class JatizsoPlugin : InteractionListener {

    override fun defineListeners() {
        on(GATES_CLOSED, IntType.SCENERY, "open") { player, node ->
            if (NORTH_GATE_ZONE.insideBorder(player)) {
                if (node.id == GATES_CLOSED.first()) {
                    val other = getScenery(node.location.transform(1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.EAST)
                    replaceScenery(other.asScenery(), other.id - 1, -1, Direction.NORTH_EAST)
                } else {
                    val other = getScenery(node.location.transform(-1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH_EAST)
                    replaceScenery(other.asScenery(), other.id, -1, Direction.EAST)
                }
            } else if (WEST_GATE_ZONE.insideBorder(player)) {
                if (node.id == GATES_CLOSED.first()) {
                    val other = getScenery(node.location.transform(0, 1, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.WEST)
                    replaceScenery(other.asScenery(), other.id + 1, -1, Direction.NORTH)
                } else {
                    val other = getScenery(node.location.transform(0, -1, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH)
                    replaceScenery(other.asScenery(), other.id + 1, -1, Direction.WEST)
                }
            } else if (SOUTH_GATE_ZONE.insideBorder(player)) {
                if (node.id == GATES_CLOSED.first()) {
                    val other = getScenery(node.location.transform(-1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.SOUTH)
                    replaceScenery(other.asScenery(), other.id - 1, -1, Direction.EAST)
                } else {
                    val other = getScenery(node.location.transform(1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.EAST)
                    replaceScenery(other.asScenery(), other.id, -1, Direction.SOUTH)
                }
            }
            playAudio(player, Sounds.NICEDOOR_OPEN_81)
            return@on true
        }

        on(TOWER_GUARDS, IntType.NPC, "watch-shouting") { player, _ ->
            val local = findLocalNPC(player, NPCs.GUARD_5489)
            lock(player, 200)
            face(local!!, Location.create(2371, 3801, 2))
            local.asNpc().isRespawn = false
            submitIndividualPulse(
                player,
                object : Pulse(4) {
                    var id = NPCs.GUARD_5489
                    var counter = 0
                    val other = findLocalNPC(player, getOther(NPCs.GUARD_5489))

                    override fun start() {
                        other?.isRespawn = false
                    }

                    override fun pulse(): Boolean {
                        val npc = findLocalNPC(player, id) ?: return false
                        val index =
                            when (id) {
                                NPCs.GUARD_5489 -> 0
                                else -> 1
                            }
                        if (index == 1 && counter == 5) return true
                        sendChat(npc, LINES[index][counter])
                        if (npc.id != NPCs.GUARD_5489) counter++
                        id = getOther(id)
                        return false
                    }

                    override fun stop() {
                        unlock(player)
                        other?.isRespawn = true
                        local.asNpc().isRespawn = true
                        super.stop()
                    }

                    fun getOther(npc: Int): Int =
                        when (npc) {
                            NPCs.GUARD_5489 -> NPCs.GUARD_5490
                            NPCs.GUARD_5490 -> NPCs.GUARD_5489
                            else -> NPCs.GUARD_5489
                        }
                },
            )
            return@on true
        }

        on(BELL, IntType.SCENERY, "ring-bell") { player, _ ->
            playAudio(player, Sounds.STEEL_15)
            sendMessage(player, "You ring the warning bell, but everyone ignores it!")
            return@on true
        }

        on(GUARDS, IntType.NPC, "talk-to") { player, _ ->
            player.dialogueInterpreter.open(LeftieRightieDialogue(), NPC(NPCs.GUARD_5491))
            return@on true
        }

        on(KING_CHEST, IntType.SCENERY, "open") { player, _ ->
            sendPlayerDialogue(player, "I probably shouldn't mess with that.", FaceAnim.HALF_THINKING)
            return@on true
        }

        setDest(IntType.NPC, MAGNUS_NPC) { _, _ ->
            return@setDest Location.create(2416, 3801, 0)
        }
    }

    companion object {
        const val BELL = Scenery.BELL_21394
        const val MAGNUS_NPC = NPCs.MAGNUS_GRAM_5488

        val GATES_CLOSED = intArrayOf(Scenery.GATE_21403, Scenery.GATE_21405)
        val TOWER_GUARDS = intArrayOf(NPCs.GUARD_5490, NPCs.GUARD_5489)
        val GUARDS = intArrayOf(NPCs.GUARD_5491, NPCs.GUARD_5492)
        val KING_CHEST = intArrayOf(Scenery.CHEST_21299, Scenery.CHEST_21300)

        val NORTH_GATE_ZONE = ZoneBorders(2414, 3822, 2417, 3825)
        val WEST_GATE_ZONE = ZoneBorders(2386, 3797, 2390, 3801)
        val SOUTH_GATE_ZONE = ZoneBorders(2411, 3795, 2414, 3799)

        val LINES =
            arrayOf(
                arrayOf(
                    "YOU WOULDN'T KNOW HOW TO FIGHT A TROLL IF IT CAME UP AND BIT YER ARM OFF",
                    "YAK LOVERS",
                    "CALL THAT A TOWN? I'VE SEEN BETTER HAMLETS!",
                    "AND YOUR FATHER SMELLED OF WINTERBERRIES!",
                    "WOODEN BRIDGES ARE RUBBISH!",
                    "OUR KING'S BETTER THAN YOUR BURGHER!",
                ),
                arrayOf(
                    "YOU WOULDN'T KNOW HOW TO SHAVE A YAK IF YOUR LIFE DEPENDED ON IT",
                    "MISERABLE LOSERS",
                    "YOUR MOTHER WAS A HAMSTER!",
                    "AT LEAST WE HAVE SOMETHING OTHER THAN SMELLY FISH!",
                    "AT LEAST WE CAN COOK!",
                ),
            )
    }
}
