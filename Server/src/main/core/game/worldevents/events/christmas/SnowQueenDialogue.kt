package core.game.worldevents.events.christmas

import core.api.addItemOrDrop
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the (2007 Christmas event) Snow Queen dialogue.
 */
@Initializable
class SnowQueenDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Happy Christmas, noble visitor. Welcome to the Land of Snow. I have a gift for you, if you would like it.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HAPPY, "Ooh, a present?").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "It is a snow globe: a tiny model of your Lumbridge Castle enchanted with a little of this land's magical snow.").also { stage++ }
            2 -> {
                sendItemDialogue(player, Items.SNOW_GLOBE_11949, "The Queen of Snow hands you a snow globe.").also { stage++ }
                addItemOrDrop(player, Items.SNOW_GLOBE_11949)
                stage++
            }
            3 -> npcl(FaceAnim.FRIENDLY, "If you lost it, I have given Diango of Draynor Village a supply of spares.").also { stage++ }
            4 -> playerl(FaceAnim.HAPPY, "Happy Christmas, Your Majesty.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "And a Happy New Year to you.").also { stage++ }
            6 -> showTopics(
                Topic("Why have you sent snow to ${GameWorld.settings?.name}?", 7),
                Topic("What is the Land of Snow?", 8),
                Topic("What is the snow globe for?", 10),
                Topic("I want to go back to ${GameWorld.settings?.name}.", 11),
                Topic("What is this place?", 12),
            )
            7 -> npcl(FaceAnim.NEUTRAL, "A snowy Christmas is a tradition that I have sadly neglected in recent years; what is Christmas without the hope of snow? There can be no hope of snow if snow never comes.").also { stage = 5 }
            8 -> npcl(FaceAnim.NEUTRAL, "The Land of Snow was created aeons ago by Guthix, as part of his balancing of the world.").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "The coldness of this place counteracts the heat of the great deserts and volcanoes of others; ensuring the correct balance of hot and cold.").also { stage = 5 }
            10 -> npcl(FaceAnim.NEUTRAL, "Nothing, besides your idle amusement. It is imbued with a little of the snow imps' magic. When you shake it, it will call some snow to you from the Land of Snow.").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Very well. You may return here during Christmas-time, when the Land of Snow is close to ${GameWorld.settings?.name}. Just speak to a snowman or snow imp.").also { stage = END_DIALOGUE }
            12 -> npcl(FaceAnim.NEUTRAL, "This is the Land of Snow. It was created by Guthix as part of his balancing of the world. The coldness of this place counteracts the heat of the great deserts.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SnowQueenDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.QUEEN_OF_SNOW_6731)
}