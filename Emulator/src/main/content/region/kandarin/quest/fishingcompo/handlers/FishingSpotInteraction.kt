package content.region.kandarin.quest.fishingcompo.handlers

import core.game.dialogue.FaceAnim
import core.game.interaction.MovementPulse
import core.game.interaction.Option
import core.game.interaction.PluginInteraction
import core.game.interaction.PluginInteractionManager
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

@Initializable
class FishingSpotInteraction : PluginInteraction() {
    override fun handle(
        player: Player,
        npc: NPC,
        option: Option,
    ): Boolean {
        val npcLocation = npc.location
        if (!player.getAttribute("fishing_contest:fee-paid", false)) {
            if (npcLocation == Location(2637, 3444, 0)) {
                player.pulseManager.run(
                    object : MovementPulse(player, npc.location.transform(1, 0, 0)) {
                        override fun pulse(): Boolean {
                            player.dialogueInterpreter.sendDialogues(
                                3677,
                                FaceAnim.NEUTRAL,
                                "I think you will find that is",
                                "my spot.",
                            )
                            return true
                        }
                    },
                    PulseType.STANDARD,
                )
                return true
            } else if (npcLocation.equals(2630, 3435, 0)) {
                player.pulseManager.run(
                    object : MovementPulse(player, npc.location.transform(1, 0, 0)) {
                        override fun pulse(): Boolean {
                            player.dialogueInterpreter.sendDialogues(
                                225,
                                FaceAnim.NEUTRAL,
                                "Hey, you need to pay to enter the",
                                "competition first! Only 5gp entrance fee!",
                            )
                            return true
                        }
                    },
                    PulseType.STANDARD,
                )
                return true
            } else if (npcLocation == Location.create(2632, 3427, 0)) {
                player.pulseManager.run(
                    object : MovementPulse(player, npc.location.transform(1, 0, 0)) {
                        override fun pulse(): Boolean {
                            player.dialogueInterpreter.sendDialogues(
                                228,
                                FaceAnim.NEUTRAL,
                                "I think you will find that is my spot.",
                            )
                            return true
                        }
                    },
                    PulseType.STANDARD,
                )
                return true
            } else if (npcLocation == Location.create(2627, 3415, 0)) {
                player.pulseManager.run(
                    object : MovementPulse(player, npc.location.transform(1, 0, 0)) {
                        override fun pulse(): Boolean {
                            player.dialogueInterpreter.sendDialogues(
                                228,
                                FaceAnim.NEUTRAL,
                                "I think you will find that is my spot.",
                            )
                            return true
                        }
                    },
                    PulseType.STANDARD,
                )
                return true
            }
        }
        return false
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        setIds(intArrayOf(NPCs.FISHING_SPOT_309))
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.NPC)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }
}
