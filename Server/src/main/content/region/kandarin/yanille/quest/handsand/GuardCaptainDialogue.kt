package content.region.kandarin.yanille.quest.handsand

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Handles the Guard Captain dialogue in The Hand in the Sand quest.
 */
@Initializable
class GuardCaptainDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)
        when {
            questStage == 0 -> player(FaceAnim.FRIENDLY, "Excuse me...")
            !allInInventory(player, Items.BEER_1917, Items.SANDY_HAND_6945) -> npc(FaceAnim.OLD_DRUNK_LEFT, "Need more beer...").also { stage = END_DIALOGUE }
            questStage == 2 && !inInventory(player, Items.BEER_SOAKED_HAND_6946) -> player("Hello Sir!").also { stage = 11 }
            questStage == 3 -> npc(FaceAnim.OLD_DRUNK_LEFT, "I don' need a hand drinkin me beer, go 'way!").also { stage = 14 }
            else -> player(FaceAnim.HALF_ASKING, "Sir? I have some more beer for you...").also { stage = 3 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "I don' need a hand drinkin me beer, go 'way!").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "But...").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Talk to tha' hand coz thish face ain't lishtnin'!").also { stage = END_DIALOGUE }

            /*
             * The hand in the sand dialogue.
             */

            3 -> player.dialogueInterpreter.sendItemMessage(Items.BEER_1917, "You give the beer to the Guard Captain who takes a", "large gulp.").also {
                removeItem(player, Items.BEER_1917)
                stage++
            }
            4 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Ahh... jus' wha' I need, now, wha' did you wanna know?").also { stage++ }
            5 -> playerl(FaceAnim.THINKING, "I've come to report that Bert, the sandman, found a hand in the sand pit.").also { stage++ }
            6 -> npc(FaceAnim.OLD_DRUNK_LEFT, "Lucky for him, means he can get even more work", "done.").also { stage++ }
            7 -> player("But aren't you going to find out who it ... belonged to?").also { stage++ }
            8 -> {
                player.dialogueInterpreter.sendItemMessage(Items.BEER_SOAKED_HAND_6946, "You hand the... hand... to the Guard Captain, he", "fumbles with it, drops it in his beer, fishes it out and", "hands it back.")
                removeItem(player, Items.SANDY_HAND_6945)
                addItem(player, Items.BEER_SOAKED_HAND_6946, 1)
                stage++
            }
            9 -> npc(FaceAnim.OLD_DRUNK_LEFT, "Oops, No 'arm done. S'prob'ly a wizard, i's always the", "wizards fault, go ask them, jus' ring the bell outshide the", "guild and talk to the first pointy hatted ninny you shee!").also { stage++ }
            10 -> player("Err... ok, I'll go ring the bell and talk to a wizard then.").also {
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 2)
                stage = END_DIALOGUE
            }
            11 -> npc(FaceAnim.OLD_DRUNK_LEFT, "Go 'way! This pint'sh nearly finished! Unlessh you got more that ish....? Wizards, s'all the wizard's fault...prob'ly that Zavistic one, he'sh the worsht!").also { stage++ }
            12 -> npc(FaceAnim.OLD_DRUNK_LEFT, "E're, you left this 'and in me beer!").also { stage++ }
            13 -> {
                end()
                sendItemDialogue(player, Items.BEER_SOAKED_HAND_6946, "The Guard Captain fished the hand out of his beer and hands it to you.")
                addItem(player, Items.BEER_SOAKED_HAND_6946, 1)
            }

            14 -> player(FaceAnim.THINKING, "But...").also { stage++ }
            15 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Talk to tha' hand coz thish face ain't lishtnin'!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GuardCaptainDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GUARD_CAPTAIN_3109)
}
