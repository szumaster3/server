package content.global.travel

import content.global.skill.magic.TeleportMethod
import core.api.*
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
import org.rs.consts.*

/**
 * Handles the Rune Essence teleportation mechanics via NPC wizards.
 */
object EssenceTeleport {

    private val LOCATIONS = arrayOf(Location.create(2911, 4832, 0), Location.create(2913, 4837, 0), Location.create(2930, 4850, 0), Location.create(2894, 4811, 0), Location.create(2896, 4845, 0), Location.create(2922, 4820, 0), Location.create(2931, 4813, 0))
    private const val CURSE_PROJECTILE = org.rs.consts.Graphics.CURSE_PROJECTILE_109
    private val ANIMATION = Animation(Animations.ATTACK_437)
    private val OLD_ANIMATION = Animation(Animations.BALLER_CLAP_198)
    private val GLOWING_HANDS_GFX = Graphics(org.rs.consts.Graphics.CURSE_CAST_108)
    private val TELEPORT_GFX = Graphics(org.rs.consts.Graphics.CURSE_IMPACT_110, 150)

    /**
     * Teleports to the Rune Essence mine via NPC.
     */
    fun teleport(npc: NPC, player: Player) {
        if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
            player.sendMessage("You need to complete Rune Mysteries to enter the Rune Essence mine.")
            return
        }

        npc.animate(if (npc.id == NPCs.BRIMSTAIL_171) OLD_ANIMATION else ANIMATION)
        npc.faceTemporary(player, 1)
        npc.graphics(GLOWING_HANDS_GFX)
        player.lock(4)
        playAudio(player, Sounds.CURSE_ALL_125, 0, 1)
        Projectile.create(npc, player, CURSE_PROJECTILE).send()
        npc.sendChat("Senventior Disthine Molenko!")

        GameWorld.Pulser.submit(object : Pulse(1) {
            var counter = 0

            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> player.graphics(TELEPORT_GFX)
                    1 -> {
                        handleScryingOrb(player, npc)
                        player.savedData.globalData.setEssenceTeleporter(npc.id)
                        player.graphics(TELEPORT_GFX)
                        val loc = LOCATIONS.random()
                        player.teleport(loc)
                        player.dispatch(TeleportEvent(TeleportManager.TeleportType.TELE_OTHER, TeleportMethod.NPC, npc, loc))
                    }
                    2 -> {
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
    }

    /**
     * Teleports from essence mine.
     */
    fun home(player: Player, node: Node) {
        val wizard = Wizard.forNPC(player.savedData.globalData.getEssenceTeleporter())
        Projectile.create(node.location, player.location, CURSE_PROJECTILE, 15, 10, 0, 10, 0, 2).send()

        GameWorld.Pulser.submit(object : Pulse(1) {
            var counter = 0

            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> {
                        player.lock(2)
                        player.graphics(TELEPORT_GFX)
                    }
                    1 -> {
                        player.teleport(wizard.location)
                        player.graphics(TELEPORT_GFX)
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
    }

    /**
     * Handles the scrying orb mechanics during tp.
     *
     * Relations:
     * - [RuneMysteries quest][content.region.misthalin.quest.runemysteries.RuneMysteries]
     */
    private fun handleScryingOrb(player: Player, npc: NPC) {
        if (getStage(player) != 2) return
        val slot = player.inventory.getSlot(Item(Items.SCRYING_ORB_5519))
        val item = player.inventory.get(slot) ?: return

        val wizard = Wizard.forNPC(npc.id)
        if (item.charge == 1000) player.savedData.globalData.resetAbyss()

        if (!player.savedData.globalData.hasAbyssCharge(wizard.ordinal)) {
            player.savedData.globalData.setAbyssCharge(wizard.ordinal)
            item.charge += 1

            if (item.charge == 1003) {
                player.sendMessage("Your scrying orb has absorbed enough teleport information.")
                player.inventory.replace(Item(Items.SCRYING_ORB_5518), slot)
            }
        }
    }

    /**
     * Gets the current Rune Mysteries quest data.
     */
    fun getStage(player: Player): Int = getVarp(player, 492)

    /**
     * Returns a random essence mine location.
     */
    val location: Location
        get() = LOCATIONS.random()

    /**
     * Represents a wizards available teleports.
     */
    private enum class Wizard(val npc: Int, val mask: Int, val location: Location) {
        BRIMSTAIL(NPCs.BRIMSTAIL_171, 0x1, Location.create(2409, 9815, 0)),
        AUBURY(NPCs.AUBURY_553, 0x2, Location(3253, 3401, 0)),
        SEDRIDOR(NPCs.SEDRIDOR_300, 0x4, Location(3107, 9573, 0)),
        DISTENTOR(NPCs.WIZARD_DISTENTOR_462, 0x8, Location(2591, 3085, 0)),
        CROMPERTY(NPCs.WIZARD_CROMPERTY_2328, 0x12, Location.create(2682, 3323, 0));

        companion object {
            /**
             * Gets the [Wizard] enum constant for the given npc id.
             */
            fun forNPC(npc: Int): Wizard =
                values().find { it.npc == npc || (npc == 844 && it == CROMPERTY) } ?: AUBURY
        }
    }
}
