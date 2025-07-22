package content.region.kandarin.ardougne.quest.tol.dialogue

import content.data.GameAttributes
import core.api.addItemOrDrop
import core.api.getAttribute
import core.api.inInventory
import core.api.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TheGunsDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.TOWER_OF_LIFE)) {
            in 0..1 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Wow, that looks pretty heavy.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Umpf! Yeah, mate...argghh...this is...umpf...hard work...",
                        ).also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "What would happen if I were to say...").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "135").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "25").also { stage++ }
                    5 -> playerl(FaceAnim.FRIENDLY, "234").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "2351....").also { stage++ }
                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Stop! Stop! Humpf...I'll beat you to...urghh...a pulp!",
                        ).also { stage++ }
                    8 -> playerl(FaceAnim.FRIENDLY, "Okay, okay! Don't cry!").also { stage++ }
                    9 -> playerl(FaceAnim.FRIENDLY, "Why, I oughta...").also { stage = END_DIALOGUE }
                }

            2 ->
                if (!inInventory(player, Items.BEER_1917)) {
                    when (stage) {
                        0 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Hi, I'm looking to get some kit to look like one of you guys.",
                            ).also { stage++ }
                        1 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Anything you might be able to help me out with?",
                            ).also { stage++ }
                        2 -> npcl(FaceAnim.FRIENDLY, "Well...Urmpf! Ye can 'ave me shirt...").also { stage++ }
                        3 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Hmmm. Has it been cleaned? I don't want some sweaty hand-me-down.",
                            ).also { stage++ }
                        4 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "What? Huhmf! It ain't ever been...uurrgg...worn.",
                            ).also { stage++ }
                        5 -> playerl(FaceAnim.FRIENDLY, "Well, that sounds good. Can I have it then?").also { stage++ }
                        6 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Can't...hurrr... see why not. This is firsty work doh. Could do wiv a beer.",
                            ).also {
                                stage++
                            }
                        7 -> playerl(FaceAnim.FRIENDLY, "Sure. What's your 'usual'?").also { stage++ }
                        8 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "I'm a simple...umph...man. I like the bar in Yanille and their cheap bear.",
                            ).also {
                                stage++
                            }
                        9 -> playerl(FaceAnim.FRIENDLY, "Simple, cheap beer. No problem.").also { stage = END_DIALOGUE }
                    }
                } else {
                    when (stage) {
                        START_DIALOGUE ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Hi, I'm looking to get some kit to look like one of you guys.",
                            ).also {
                                stage++
                            }
                        1 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Anything you might be able to help me out with?",
                            ).also { stage++ }
                        2 -> npcl(FaceAnim.FRIENDLY, "Well...Urmpf! Ye can 'ave me shirt...").also { stage++ }
                        3 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Hmmm. Has it been cleaned? I don't want some sweaty hand-me-down.",
                            ).also { stage++ }
                        4 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "What? Huhmf! It ain't ever been...uurrgg...worn.",
                            ).also { stage++ }
                        5 -> playerl(FaceAnim.FRIENDLY, "Well, that sounds good. Can I have it then?").also { stage++ }
                        6 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Can't...hurrr... see why not. This is firsty work doh. Could do wiv a beer.",
                            ).also {
                                stage++
                            }
                        7 -> playerl(FaceAnim.FRIENDLY, "Well, today's your lucky day!").also { stage++ }
                        8 -> npcl(FaceAnim.FRIENDLY, "Ahhh! Smashin...urrghh...deal!").also { stage++ }
                        9 -> {
                            end()
                            if (!inInventory(player, Items.BUILDERS_SHIRT_10863)) {
                                addItemOrDrop(player, Items.BUILDERS_SHIRT_10863)
                                sendMessage(player, "Try the beckon emote while wearing an item of builders' clothing!")
                            }
                            stage = END_DIALOGUE
                        }
                    }
                }

            3 -> if (getAttribute(player, GameAttributes.TOL_TOWER_ACCESS, 0) == 1) {
                    when (stage) {
                        START_DIALOGUE ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "What do you do when you're not lifting logs above your head?",
                            ).also {
                                stage++
                            }
                        1 -> npcl(FaceAnim.FRIENDLY, "In my free time?").also { stage++ }
                        2 -> playerl(FaceAnim.FRIENDLY, "Yes.").also { stage++ }
                        3 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "All sorts. I like...hummft...a good kebab and a cold beer down...urrghh...the pub. And for those quiet evenings...hurrr... A bit of needlepoint is always welcome.",
                            ).also {
                                stage++
                            }
                        4 ->
                            playerl(FaceAnim.FRIENDLY, "I can honestly say you're a very original guy.").also {
                                stage =
                                    END_DIALOGUE
                            }
                    }
                }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.THE_GUNS_5592)
}
