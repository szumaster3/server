package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FatherLawrenceDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val quest = player.getQuestRepository().getQuest(Quests.ROMEO_JULIET)
        if (quest.getStage(player) < 30) {
            npc(FaceAnim.HALF_GUILTY, "Oh to be a father in the times of whiskey.")
            stage = 0
        }
        when (quest.getStage(player)) {
            30 -> {
                npc("...and let Saradomin light the way for you...", "Urgh!")
                stage = 41
            }

            40 -> {
                npc("Ah, have you found the Apothecary yet? Remember,", "Cadava potion, for Juliet.")
                stage = 30
            }

            50 -> {
                npc("Did you find the Apothecary?")
                stage = 820
            }

            60, 70 -> {
                npc("Did you find the Apothecary?")
                stage = 820
            }

            100 -> {
                player("Hi again!")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ROMEO_JULIET)
        when (stage) {
            0 -> {
                npc(FaceAnim.HALF_GUILTY, "I sing and I drink and I wake up in gutters.")
                stage = 1
            }

            1 -> {
                npc(FaceAnim.HALF_GUILTY, "Top of the morning to you.")
                stage = 2
            }

            2 -> end()
            41 -> {
                npc("Can't you see that I'm in the middle of a Sermon?!")
                stage = 42
            }

            42 -> {
                player("But Romeo sent me!")
                stage = 43
            }

            43 -> {
                npc("But I'm busy delivering a sermon to my congregation!")
                stage = 44
            }

            44 -> {
                player("Yes, well, it certainly seems like you have a captive", "audience!")
                stage = 45
            }

            45 -> {
                npc("Ok, ok...what do you want so I can get rid of you and", "continue with my sermon?")
                stage = 46
            }

            46 -> {
                player("Romeo sent me. He says you may be able to help.")
                stage = 47
            }

            47 -> {
                npc("Ah Romeo, yes. A fine lad, but a little bit confused.")
                stage = 48
            }

            48 -> {
                player(
                    "Yes, very confused....Anyway, Romeo wishes to be",
                    "married to Juliet! She must be rescued from her",
                    "father's control!",
                )
                stage = 49
            }

            49 -> {
                npc("I agree, and I think I have an idea! A potion to make", "her appear dead...")
                stage = 50
            }

            50 -> {
                player("Dead! Sounds a bit creepy to me...but please, continue.")
                stage = 51
            }

            51 -> {
                npc("The potion will only make Juliet 'appear' dead...then", "she'll be taken to the crypt...")
                stage = 52
            }

            52 -> {
                player("Crypt! Again...very creepy! You must have some", "strange hobbies.")
                stage = 53
            }

            53 -> {
                npc(
                    "Then Romeo can collect her from the crypt! Go to the",
                    "Apothecary, tell him I sent you and that you'll need a",
                    "'Cadava' potion.",
                )
                stage = 54
            }

            54 -> {
                player("Apart from the strong overtones of death, this is", "turning out to be a real love story.")
                stage = 55
            }

            55 -> {
                quest.setStage(player, 40)
                end()
            }

            30 -> end()
            820 -> {
                player("Yes I did. He's told me I must find some Cadava", "berries.")
                stage = 821
            }

            821 -> {
                npc("Well, good luck with that...they're quite tricky to find.")
                stage = 822
            }

            822 -> {
                player("Any clues where I can start to look?")
                stage = 823
            }

            823 -> {
                npc(
                    "I heard some kids saying they saw some the other day.",
                    "They were visiting the mining place to the south east",
                    "Varrock.",
                )
                stage = 824
            }

            824 -> {
                player("Ok, that's as good a place to start looking as any.")
                stage = 825
            }

            825 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FatherLawrenceDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FATHER_LAWRENCE_640)
    }
}
