package content.region.asgarnia.white_wolf_mountain.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Rohak dialogue.
 */
@Initializable
class RohakDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if(!hasRequirement(player, Quests.RECIPE_FOR_DISASTER, false)) {
            npcl(FaceAnim.OLD_DEFAULT, "Hello there, youngster.")
        } else {
            npcl(FaceAnim.OLD_DEFAULT, "Hello again, youngster.")
            stage = 4
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.ASKING, "Who are you calling youngster??").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Why you. Unless you have seen the small village of Falador grow into a heaving city like I have of course!").also { stage++ }
            2 -> player(FaceAnim.HALF_THINKING, "Err... no...").also { stage++ }
            3 -> npcl(FaceAnim.OLD_HAPPY, "Well then! Enjoy your stay in our mountain home.").also { stage = END_DIALOGUE }

            4 -> showTopics(
                Topic("About your son...", 5, true),
                Topic("Can I have another rock cake?", 7)
            )

            5 -> player("How's your son getting on, now that I've rescued him from the time bubble and the culinaromancer?").also { stage++ }
            6 -> npcl(FaceAnim.OLD_DEFAULT, "My son's fine. I'm not so sure about you, though. Maybe you should lay down in a dark room until the babbling stops.").also { stage = END_DIALOGUE }
            7 -> npcl(FaceAnim.OLD_HAPPY, "Aye, I've still got enough ingredients from before, but I'll want 100 coins.").also { stage++ }
            8 -> showTopics(
                Topic("Okay, 100 coins will be fine.", 9, true),
                Topic("No thanks.", 10),
            )
            9 -> {
                end()
                if(!removeItem(player, Item(Items.COINS_995, 100))) {
                    sendMessage(player, "You can not afford that.")
                } else {
                    sendItemDialogue(player, Items.DWARVEN_ROCK_CAKE_7510, "You hand over 100 gold and Rohak bakes you a rock cake.")
                    produceGroundItem(player, Items.DWARVEN_ROCK_CAKE_7510, 1, Location(2867, 9878, 0))
                }
            }
            10 -> npcl(FaceAnim.OLD_DEFAULT, "I'm not doing it for less, even for a mate.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RohakDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ROHAK_3403)
}
