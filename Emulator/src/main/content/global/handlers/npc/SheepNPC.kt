package content.global.handlers.npc

import content.region.misthalin.quest.sheep.SheepShearer
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.*

private val sheepIds =
    intArrayOf(
        NPCs.SHEEP_42,
        NPCs.SHEEP_43,
        NPCs.GOLDEN_SHEEP_1271,
        NPCs.GOLDEN_SHEEP_1272,
        NPCs.SHEEP_1529,
        NPCs.SHEEP_1762,
        NPCs.SHEEP_1763,
        NPCs.SHEEP_1764,
        NPCs.SHEEP_1765,
        NPCs.SICK_LOOKING_SHEEP_1_2377,
        NPCs.SICK_LOOKING_SHEEP_2_2378,
        NPCs.SICK_LOOKING_SHEEP_3_2379,
        NPCs.SICK_LOOKING_SHEEP_4_2380,
        NPCs.SHEEP_3310,
        NPCs.SHEEP_3311,
        NPCs.SHEEP_3579,
        NPCs.SHEEP_5148,
        NPCs.SHEEP_5149,
        NPCs.SHEEP_5150,
        NPCs.SHEEP_5151,
        NPCs.SHEEP_5152,
        NPCs.SHEEP_5153,
        NPCs.SHEEP_5154,
        NPCs.SHEEP_5155,
        NPCs.SHEEP_5156,
        NPCs.SHEEP_5157,
        NPCs.SHEEP_5158,
        NPCs.SHEEP_5159,
        NPCs.SHEEP_5160,
        NPCs.SHEEP_5161,
        NPCs.SHEEP_5162,
        NPCs.SHEEP_5163,
        NPCs.SHEEP_5164,
        NPCs.SHEEP_5165,
        NPCs.GOLDEN_SHEEP_5172,
        NPCs.GOLDEN_SHEEP_5173,
    )

class SheepNPC :
    NPCBehavior(*sheepIds),
    InteractionListener {
    override fun tick(self: NPC): Boolean {
        if (self.properties.combatPulse.isAttacking || DeathTask.isDead(self)) {
            return true
        }
        if (RandomFunction.random(35) == 5) {
            sendChat(self, "Baa!")
        }
        return true
    }

    override fun defineListeners() {
        on(IntType.NPC, "shear") { player, node ->
            val sheep = node as NPC
            if (sheep.id == NPCs.SHEEP_3579) {
                if (player.questRepository.getQuest(Quests.SHEEP_SHEARER).isStarted(player)) {
                    setAttribute(player, SheepShearer.ATTR_IS_PENGUIN_SHEEP_SHEARED, true)
                }
                animate(player, Animation(Animations.SHEAR_SHEEP_893))
                playAudio(player, Sounds.PENGUINSHEEP_ESCAPE_686)
                animate(sheep, Animation(3570))
                sheepBackAway(player, sheep, "The... whatever it is... manages to get away from you!")
                return@on true
            }
            if (!inInventory(player, Items.SHEARS_1735)) {
                sendMessage(player, "You need shears to shear a sheep.")
                return@on true
            }
            if (hasOption(sheep, "attack")) {
                sendMessage(player, "That one looks a little too violent to shear...")
                return@on true
            }
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough space in your inventory to carry any wool you would shear.")
                return@on true
            }
            sheep.faceTemporary(player, 1)
            lock(sheep, 3)
            animate(player, Animation(Animations.SHEAR_SHEEP_893))
            val random = RandomFunction.random(1, 5)
            if (random != 4) {
                sheep.locks.lockMovement(2)
                sheep.transform(NPCs.SHEEP_5153)
                playAudio(player, Sounds.SHEAR_SHEEP_761)
                sendMessage(player, "You get some wool.")
                addItem(player, Items.WOOL_1737)
                GameWorld.Pulser.submit(
                    object : Pulse(80, sheep) {
                        override fun pulse(): Boolean {
                            sheep.reTransform()
                            return true
                        }
                    },
                )
            } else {
                sheepBackAway(player, sheep, "The sheep manages to get away from you!")
            }
            return@on true
        }
    }

    fun sheepBackAway(
        player: Player,
        sheep: NPC,
        messagePlayer: String,
    ) {
        val playerLocation = player.location
        val sheepLocation = sheep.location
        val sheepDirection = Direction.getDirection(sheepLocation, playerLocation)
        val sheepOppositeDirection = sheepDirection.opposite
        val xWalkLocation = sheepLocation.x + (sheepOppositeDirection.stepX * 3)
        val yWalkLocation = sheepLocation.y + (sheepOppositeDirection.stepY * 3)
        val sheepWalkToLocation = Location(xWalkLocation, yWalkLocation, sheepLocation.z)
        sendMessage(player, messagePlayer)
        unlock(sheep)
        forceWalk(sheep, sheepWalkToLocation, "dumb")
    }
}
