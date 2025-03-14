package content.region.morytania.handlers.phasmatys

import content.global.skill.prayer.Bones
import content.global.skill.prayer.Bones.Companion.forBoneMeal
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds
import java.util.*

@Initializable
class PhasmatysPlugin :
    MapZone("Port phasmatys", true),
    Plugin<Any?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            val option = getUsedOption(player!!)

            if (target is NPC) {
                if (target.id == NPCs.GHOST_BANKER_1702) return false

                if ((
                        target.name.lowercase().contains("ghost") ||
                            target.name.equals(
                                "velorina",
                                ignoreCase = true,
                            ) ||
                            target.name.contains("husband")
                    ) &&
                    !inEquipment(player, Items.GHOSTSPEAK_AMULET_552)
                ) {
                    player.dialogueInterpreter.open(target.getId(), target)
                    return true
                }
            }

            when (target.id) {
                5267 -> {
                    animate(player, Animations.OPEN_CHEST_536)
                    sendMessage(player, "The trapdoor opens...")
                    SceneryBuilder.replace(target as Scenery, target.transform(5268))
                    return true
                }

                5268 -> {
                    if (option == "close") {
                        animate(player, Animations.OPEN_POH_WARDROBE_535)
                        sendMessage(player, "You close the trapdoor.")
                        SceneryBuilder.replace(target as Scenery, target.transform(5267))
                    } else {
                        sendMessage(player, "You climb down through the trapdoor...")
                        player.properties.teleportLocation = Location.create(3669, 9888, 3)
                    }
                    return true
                }

                7434 ->
                    if (option == "open") {
                        SceneryBuilder.replace(target.asScenery(), target.asScenery().transform(7435))
                    }

                7435 ->
                    if (option == "close") {
                        SceneryBuilder.replace(target.asScenery(), target.asScenery().transform(7434))
                    }

                9308 -> {
                    if (getStatLevel(player, Skills.AGILITY) < 58) {
                        sendMessage(player, "You need an agility level of at least 58 to climb down this wall.")
                        return true
                    }
                    player.properties.teleportLocation = Location.create(3671, 9888, 2)
                    return true
                }

                9307 -> {
                    if (getStatLevel(player, Skills.AGILITY) < 58) {
                        sendMessage(player, "You need an agility level of at least 58 to climb up this wall.")
                        return true
                    }
                    player.properties.teleportLocation = Location.create(3670, 9888, 3)
                    return true
                }

                5264 -> {
                    ClimbActionHandler.climb(player, Animation.create(828), Location.create(3654, 3519, 0))
                    return true
                }

                5282 -> {
                    worship(player)
                    return true
                }
            }
        }
        return super.interact(e, target, option)
    }

    private fun worship(player: Player?) {
        var bone: Bones? = null
        for (i in player!!.inventory.toArray()) {
            if (i == null) {
                continue
            }
            val b = forBoneMeal(i.id)
            if (b != null) {
                bone = b
                break
            }
        }

        if (!inInventory(player, Items.BUCKET_OF_SLIME_4286, 1) && bone == null) {
            sendDialogueLines(player, "You don't have any ectoplasm or crushed bones to put into the", "Ectofuntus.")
            return
        }
        if (bone == null) {
            sendDialogueLines(player, "You need a pot of crushed bones to put into the Ectofuntus.")
            return
        }
        if (!inInventory(player, Items.BUCKET_OF_SLIME_4286, 1)) {
            sendDialogueLines(player, "You need ectoplasm to put into the Ectofuntus.")
            return
        }

        if (removeItem(player, Item(bone.bonemealId!!)) && removeItem(player, Item(Items.BUCKET_OF_SLIME_4286, 1))) {
            lock(player, 1)
            animate(player, Animations.PRAY_ECTOFUNTUS_1651)
            playAudio(player, Sounds.PRAYER_BOOST_2671)
            addItem(player, Items.BUCKET_1925)
            addItem(player, Items.EMPTY_POT_1931)
            rewardXP(player, Skills.PRAYER, bone.experience * 4)
            sendMessage(player, "You put some ectoplasm and bonemeal into the Ectofuntus, and worship it.")
            player.getSavedData().globalData.setEctoCharges(player.getSavedData().globalData.getEctoCharges() + 1)
        }
    }

    override fun configure() {
        register(ZoneBorders(3583, 3456, 3701, 3552))
        register(ZoneBorders(3667, 9873, 3699, 9914))
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }
}
