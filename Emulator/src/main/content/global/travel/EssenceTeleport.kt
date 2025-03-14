package content.global.travel

import content.global.skill.magic.TeleportMethod
import core.api.*
import core.api.quest.isQuestComplete
import core.game.event.TeleportEvent
import core.game.node.Node
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Sounds

object EssenceTeleport {
    val LOCATIONS =
        arrayOf(
            Location.create(2911, 4832, 0),
            Location.create(2913, 4837, 0),
            Location.create(2930, 4850, 0),
            Location.create(2894, 4811, 0),
            Location.create(2896, 4845, 0),
            Location.create(2922, 4820, 0),
            Location.create(2931, 4813, 0),
        )

    private const val CURSE_PROJECTILE = 109
    private val ANIMATION = Animation(437)
    private val OLD_ANIMATION = Animation(198)
    private val GLOWING_HANDS_GFX = Graphics(108)
    private val TELEPORT_GFX = Graphics(110, 150)

    @JvmStatic
    fun teleport(
        npc: NPC,
        player: Player,
    ) {
        if (!isQuestComplete(
                player,
                Quests.RUNE_MYSTERIES,
            )
        ) {
            return sendMessage(player, "You need to complete Rune Mysteries to enter the Rune Essence mine.")
        }
        if (npc.id != 171) npc.animate(ANIMATION) else npc.animate(OLD_ANIMATION)
        npc.faceTemporary(player, 1)
        npc.graphics(GLOWING_HANDS_GFX)
        lock(player, 4)
        playAudio(player, Sounds.CURSE_ALL_125, 0, 1)
        Projectile.create(npc, player, CURSE_PROJECTILE).send()
        npc.sendChat("Senventior Disthine Molenko!")
        GameWorld.Pulser.submit(
            object : Pulse(1) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> player.graphics(TELEPORT_GFX)
                        1 -> {
                            if (getStage(player) == 2 && inInventory(player, 5519, 1)) {
                                val item = player.inventory[player.inventory.getSlot(Item(5519))]
                                if (item != null) {
                                    if (item.charge == 1000) {
                                        player.savedData.globalData.resetAbyss()
                                    }
                                    val wizard = Wizard.forNPC(npc.id)
                                    if (!player.savedData.globalData.hasAbyssCharge(wizard.ordinal)) {
                                        player.savedData.globalData.setAbyssCharge(wizard.ordinal)
                                        item.charge += 1
                                        if (item.charge == 1003) {
                                            sendMessage(
                                                player,
                                                "Your scrying orb has absorbed enough teleport information.",
                                            )
                                            removeItem(player, 5519)
                                            addItemOrDrop(player, 5518)
                                        }
                                    }
                                }
                            }
                            player.savedData.globalData.setEssenceTeleporter(npc.id)
                            player.graphics(TELEPORT_GFX)
                            val loc = LOCATIONS[RandomFunction.random(0, LOCATIONS.size)]
                            teleport(player, loc)
                            player.dispatch(
                                TeleportEvent(TeleportManager.TeleportType.TELE_OTHER, TeleportMethod.NPC, npc, loc),
                            )
                        }

                        2 -> {
                            unlock(player)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    @JvmStatic
    fun home(
        player: Player,
        node: Node,
    ) {
        val wizard = Wizard.forNPC(player.savedData.globalData.getEssenceTeleporter())
        Projectile.create(node.location, player.location, CURSE_PROJECTILE, 15, 10, 0, 10, 0, 2).send()
        GameWorld.Pulser.submit(
            object : Pulse(1) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            lock(player, 2)
                            player.graphics(TELEPORT_GFX)
                        }

                        1 -> {
                            teleport(player, wizard.location)
                            player.graphics(TELEPORT_GFX)
                            unlock(player)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    fun getStage(player: Player): Int {
        return getVarp(player, 492)
    }

    val location: Location
        get() {
            val count = RandomFunction.random(LOCATIONS.size)
            return LOCATIONS[count]
        }

    enum class Wizard(
        val npc: Int,
        val mask: Int,
        val location: Location,
    ) {
        BRIMSTAIL(NPCs.BRIMSTAIL_171, 0x1, Location.create(2409, 9815, 0)),
        AUBURY(NPCs.AUBURY_553, 0x2, Location(3253, 3401, 0)),
        SEDRIDOR(NPCs.SEDRIDOR_300, 0x4, Location(3107, 9573, 0)),
        DISTENTOR(NPCs.WIZARD_DISTENTOR_462, 0x8, Location(2591, 3085, 0)),
        CROMPERTY(NPCs.WIZARD_CROMPERTY_2328, 0x12, Location.create(2682, 3323, 0)),
        ;

        companion object {
            fun forNPC(npc: Int): Wizard {
                for (wizard in values()) {
                    if (npc == 844) {
                        return CROMPERTY
                    }
                    if (wizard.npc == npc) {
                        return wizard
                    }
                }
                return AUBURY
            }
        }
    }
}
