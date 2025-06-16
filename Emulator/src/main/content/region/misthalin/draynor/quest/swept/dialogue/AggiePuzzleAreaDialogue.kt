package content.region.misthalin.draynor.quest.swept.dialogue

import content.data.GameAttributes
import core.api.lock
import core.api.openInterface
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.setAttribute
import core.api.unlock
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Aggie dialogue.
 *
 * Relations:
 * - [Swept Away quest][content.region.kandarin.quest.swept.SweptAway]
 */
@Initializable
class AggiePuzzleAreaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.SWEPT_AWAY) == 3) {
            player("Wow, that was impressive.")
            stage = 12
        } else {
            player("Woah! Where are we and what are we doing here?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Oh, this is just a little place that some of us witches use", "on occasion. It's rather convenient for the occasional", "ritual or spell.").also { stage++ }
            1 -> npc("Not only is it infused with a bit of magical peace, but", "it's out of the way enough that we don't get a lot of", "unnecessary interruptions.").also { stage++ }
            2 -> player("Ah, right. Which leaves the question of what we're doing", "here.").also { stage++ }
            3 -> npc("You want that broom of yours enchanted, right?").also { stage++ }
            4 -> player("Right.").also { stage++ }
            5 -> npc("Well the best way to enchant that hunk of dead wood", "is to harness the power latent in this magical symbol", "here.").also { stage++ }
            6 -> npc("Do you see that pattern of 16 lines thrown out of sand", "on the ground?").also { stage++ }
            7 -> player("How could I miss it?").also { stage++ }
            8 -> npc("In order to enchant the broom, you need to sweep", "away 4 lines of those 16 lines, such that you leave only 4", "small triangles on the ground - and nothing else.").also { stage++ }
            9 -> npc("If you run into any trouble, let me know and I'll", "reconfigure the original pattern for you. I can also", "teleport you back to Draynor when you're ready to", "leave.").also { stage++ }
            10 -> {
                player("Okay, thanks. I'll give it a try.")
                end()
                setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LINE, 0)
                setQuestStage(player, Quests.SWEPT_AWAY, 2)
                stage = END_DIALOGUE
            }
            12 -> npc("Yes, there is a lot of power in these types of magical", "symbols.").also { stage++ }
            13 -> options("Is there anything else that needs to be done here?", "Where are we?", "I'd like to go back to Draynor, please.").also { stage++ }
            14 -> when (buttonId) {
                1 -> player("Is there anything else that needs to be done here?").also { stage = 20 }
                2 -> player("Where are we?").also { stage = 21 }
                3 -> player("I'd like to go back to Draynor, please.").also { stage++ }

            }
            15 -> npc("Sure thing! Just hold on to your hat and you'll be back", "in Draynor before you can wiggle your nose.").also { stage++ }
            16 -> {
                end()
                teleport(player)
            }
            20 -> npc(FaceAnim.HALF_GUILTY, "Not everything you were supposed to do has been done. [Transcript missing]").also { stage = 13 }
            21 -> npc("Oh, this is just a little place that some of us witches use", "on occasion. It's rather convenient for the occasional", "ritual or spell.").also { stage++ }
            22 -> npc("Not only is it infused with a bit of magical peace, but", "it's out of the way enough that we don't get a lot of", "unnecessary interruptions.").also { stage = 13 }

        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return AggiePuzzleAreaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AGGIE_8207)
    }

    /**
     * Teleports the player to secret area.
     */
    private fun teleport(player: Player) {
        lock(player, 6)
        GameWorld.Pulser.submit(
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> openInterface(player, Components.FADE_TO_BLACK_120)
                        1 ->
                            core.api.teleport(
                                player,
                                Location.create(3086, 3259, 0),
                                TeleportManager.TeleportType.NORMAL,
                            )

                        6 -> {
                            unlock(player)
                            openInterface(player, Components.FADE_FROM_BLACK_170)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }
}
