package content.region.misthalin.dialogue.lumbridge

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Sigmund dialogue (The Lost Tribe quest).
 */
@Initializable
class SigmundDialogue : Dialogue {
    var TLTNPCS: IntArray =
        intArrayOf(NPCs.COOK_278, NPCs.HANS_0, NPCs.BOB_519, NPCs.LUMBRIDGE_GUIDE_2244, NPCs.DOOMSAYER_3777)

    constructor()

    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "Can I help you?")
        if (player
                .getQuestRepository()
                .getQuest(Quests.THE_LOST_TRIBE)
                .getStage(player) > 0 &&
            player
                .getQuestRepository()
                .getQuest(Quests.THE_LOST_TRIBE)
                .getStage(player) < 100
        ) {
            npc("Have you found out what it was?")
            stage = 34
            return true
        }
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                interpreter.sendOptions("Select an Option", "Do you have any quests for me?", "Who are you?")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "Do you have any quests for me?")
                        stage = 10
                    }

                    2 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "Who are you?")
                        stage = 20
                    }
                }

            20 -> {
                interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "I'm the Duke's advisor.")
                stage = 21
            }

            21 -> {
                interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "Can you give me any advice then?")
                stage = 22
            }

            22 -> {
                interpreter.sendDialogues(
                    npc,
                    FaceAnim.HALF_GUILTY,
                    "I only advise the Duke. But if you want to make",
                    "yourself useful, there are evil goblins to slay on the",
                    "other side of the river.",
                )
                stage = 23
            }

            23 -> end()
            10 -> {
                if (player.getQuestRepository().hasStarted(Quests.THE_LOST_TRIBE) &&
                    !player
                        .getQuestRepository()
                        .isComplete(Quests.THE_LOST_TRIBE)
                ) {
                    npc("No, not right now.")
                    stage = 12
                    return true
                }
                if (player.getQuestRepository().isComplete(Quests.GOBLIN_DIPLOMACY) &&
                    player.getQuestRepository().isComplete(Quests.RUNE_MYSTERIES) &&
                    !player.getQuestRepository().hasStarted(Quests.THE_LOST_TRIBE)
                ) {
                    npc("There was recently some damage to the castle cellar.", "Part of the wall has collapsed.")
                    stage = 30
                    return true
                }
                interpreter.sendDialogues(
                    npc,
                    FaceAnim.HALF_GUILTY,
                    "I hear the Duke has a task for an adventurer.",
                    "Otherwise, if you want to make yourself useful, there",
                    "are always evil monsters to slay.",
                )
                stage = 11
            }

            11 -> {
                interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "Okay, I might just do that.")
                stage = 12
            }

            12 -> end()
            30 -> {
                npc("The Duke insists that it was an earthquake, but I think", "some kind of monsters are to blame.")
                stage++
            }

            31 -> {
                npc("You should ask other people around the town if they", "saw anything.")
                stage = END_DIALOGUE
                player.getQuestRepository().getQuest(Quests.THE_LOST_TRIBE).start(player)
                player.setAttribute("/save:tlt-witness", TLTNPCS[0])
            }

            34 -> {
                player("No...")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue = SigmundDialogue(player)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.SIGMUND_2082,
            NPCs.SIGMUND_2083,
            NPCs.SIGMUND_2090,
            NPCs.SIGMUND_3713,
            NPCs.SIGMUND_3716,
            NPCs.SIGMUND_3717,
            NPCs.SIGMUND_3718,
            NPCs.SIGMUND_3719,
            NPCs.SIGMUND_3720,
            NPCs.SIGMUND_4328,
            NPCs.SIGMUND_4331,
            NPCs.SIGMUND_4332,
            NPCs.SIGMUND_4333,
            NPCs.SIGMUND_4334,
            NPCs.SIGMUND_4335,
        )
}
