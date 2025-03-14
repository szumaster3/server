package content.region.asgarnia.handlers.guilds.warriorsguild

import core.api.hasSpaceFor
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.global.action.DoorActionHandler.handleDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class WarriorsGuildOption : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(15653).handlers["option:open"] = this
        SceneryDefinition.forId(1530).handlers["option:open"] = this
        NPCDefinition.forId(4287).handlers["option:claim-shield"] = this
        NPCDefinition.setOptionHandler("claim-tokens", this)
        definePlugin(ClaimTokenDialogue())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (node.id) {
            15653, 1530 -> {
                if (node.id == 1530 && node.location != Location(2837, 3549, 0)) {
                    handleDoor(player, (node as Scenery))
                    return true
                }
                if (canEnter(player)) {
                    player.musicPlayer.unlock(634)
                    handleAutowalkDoor(player, (node as Scenery))
                } else {
                    player.dialogueInterpreter.sendDialogues(4285, null, "You not pass. You too weedy.")
                }
            }

            else ->
                when (option) {
                    "claim-shield" -> player.dialogueInterpreter.open(4287, node, true)
                    "claim-tokens" -> player.dialogueInterpreter.open("wg:claim-tokens", node.id)
                }
        }
        return true
    }

    private fun canEnter(player: Player): Boolean {
        return player.getSkills().getStaticLevel(Skills.ATTACK) + player.getSkills().getStaticLevel(Skills.STRENGTH) >=
            130 ||
            player.getSkills().getStaticLevel(Skills.ATTACK) == 99 ||
            player.getSkills().getStaticLevel(Skills.STRENGTH) == 99
    }

    class ClaimTokenDialogue : Dialogue {
        private var npcId = 0

        constructor()

        constructor(player: Player?) : super(player)

        override fun newInstance(player: Player): Dialogue {
            return ClaimTokenDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            npcId = args[0] as Int
            player("May I claim my tokens please?")
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            val tokens = player.getSavedData().activityData.warriorGuildTokens
            when (stage) {
                0 ->
                    if (tokens < 1) {
                        npc(
                            "I'm afraid you have not earned any tokens yet. Try",
                            "some of the activities around the guild to earn some.",
                        )
                        stage = 3
                    } else {
                        npc("Of course! Here you go, you've earned $tokens tokens!")
                        stage++
                    }

                1 -> {
                    val item = Item(TOKEN, tokens)
                    if (!hasSpaceFor(player, item)) {
                        player("Sorry, I don't seem to have enough inventory space.")
                        stage++
                    }
                    player.getSavedData().activityData.warriorGuildTokens = 0
                    player.inventory.add(item)
                    player("Thanks!")
                    stage++
                }

                2 -> end()
                3 -> {
                    player("Ok, I'll go see what I can find.")
                    stage--
                }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("wg:claim-tokens"))
        }
    }

    companion object {
        const val TOKEN: Int = 8851
    }
}
