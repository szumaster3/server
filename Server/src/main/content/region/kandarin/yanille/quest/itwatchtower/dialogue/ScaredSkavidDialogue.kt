package content.region.kandarin.yanille.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Scared Skavid dialogue.
 *
 * Sources
 * - [08.04.2009](https://youtu.be/xuFxEioCojs?si=LD0PdsJM48mggLXx&t=122)
 *
 * # Relations
 * - [Watchtower Quest][content.region.kandarin.yanille.quest.itwatchtower.Watchtower]
 */
@Initializable
class ScaredSkavidDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(getQuestStage(player, Quests.WATCHTOWER) in 25..40) {
            npcl(FaceAnim.OLD_DEFAULT, "Master, how are you doing learning our language?")
            stage = 18
            return true
        }

        if(getQuestStage(player, Quests.WATCHTOWER) >= 45) {
            npc(FaceAnim.OLD_DEFAULT, "Master, my kinsmen tell me you have learned skavid.", "You should speak to the ad ones in their cave.")
            return true
        }

        npc(FaceAnim.OLD_DEFAULT, "Tanath cur, tanath cur!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.NEUTRAL, " ???").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "Don't hurt me, don't hurt me!").also { stage++ }
            2 -> player(FaceAnim.NEUTRAL, "Stop moaning, creature. I know about you skavids -", "you serve those monsters, the ogres.").also { stage++ }
            3 -> npc(FaceAnim.OLD_DEFAULT, "Please don't touch me!").also { stage++ }
            4 -> player(FaceAnim.ANNOYED, "You have something that belongs to me...").also { stage++ }
            5 -> npc(FaceAnim.OLD_DEFAULT, "I don't have anything; please believe me!").also { stage++ }
            6 -> player(FaceAnim.NEUTRAL, "Somehow, I find your words hard to believe.").also { stage++ }
            7 -> npc(FaceAnim.OLD_DEFAULT, "I'm begging your kindness; I don't have it!").also { stage++ }
            8 -> showTopics(
                Topic("I don't believe you, hand it over!", 9),
                Topic(FaceAnim.HAPPY, "Okay, okay, I'm not going to hurt you.", 11),
            )
            9 -> npc(FaceAnim.OLD_DEFAULT, "Ahhhhh, help!").also { stage++ }
            10 -> player(FaceAnim.HALF_WORRIED, "Oh great, I'm not getting anywhere.").also { stage = END_DIALOGUE }

            11 -> npc(FaceAnim.OLD_DEFAULT, "Thank you, kind sir.").also { stage++ }
            12 -> npc(FaceAnim.OLD_DEFAULT, "I'll tells you where that things you wants is. The mad", "skavids have it in their cave in the city.").also { stage++ }
            13 -> npc(FaceAnim.OLD_NEUTRAL, "You will have to learn skavid, otherwise they will not", "talks to you. Here, I'll tell you what you need to", "know...").also { stage++ }
            14 -> {
                lock(player, 5)
                val ogreChieftain = NPC.create(NPCs.OGRE_CHIEFTAIN_5174, Location.create(2500, 9434, 0), Direction.SOUTH_WEST)
                submitIndividualPulse(
                    player,
                    object : Pulse() {
                        var counter = 0
                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> ogreChieftain.init()
                                2 -> {
                                    visualize(ogreChieftain, 359, 86) // 2101,2102
                                    sendChat(ogreChieftain, "You shut your mouth! Don't tell them anything!")
                                }
                                3 -> {
                                    animate(npc!!, 5356)
                                    sendChat(npc,"Tanath cur, tanath cur!")
                                }
                                4 -> {
                                    sendGraphics(86, ogreChieftain.location)
                                    setQuestStage(player, Quests.WATCHTOWER, 25)
                                    ogreChieftain.clear()
                                    npc(FaceAnim.OLD_NEUTRAL, "I cannot help you, but the others can.").also { stage = 15 }
                                    return true
                                }
                            }
                            return false
                        }
                    }
                )
            }
            15 -> npc(FaceAnim.OLD_NEUTRAL, "Let me tells you the most common skavid words:").also { stage++ }
            16 -> npc(FaceAnim.OLD_NEUTRAL, "Ar, nod, gor, ig and cur.").also { stage++ }
            17 -> npc(FaceAnim.OLD_NEUTRAL, "Those will gets you started.").also { stage = END_DIALOGUE }
            18 -> player(FaceAnim.NEUTRAL, "I am studying the speech of your kind...").also {
                setAttribute(player, GameAttributes.WATCHTOWER_SKAVID_UPSET, 0)
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SCARED_SKAVID_863)
}