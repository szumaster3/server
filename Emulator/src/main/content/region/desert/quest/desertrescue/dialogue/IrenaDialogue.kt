package content.region.desert.quest.desertrescue.dialogue

import content.region.desert.quest.desertrescue.TouristTrap
import core.api.sendNPCDialogueLines
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import org.rs.consts.Quests

class IrenaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        if (quest!!.getStage(player) == 95 && player.inventory.containsItem(TouristTrap.ANNA_BARREL)) {
            npc(FaceAnim.HAPPY, "Hey, great you've found Ana!")
            stage = 900
            return true
        }
        when (quest!!.getStage(player)) {
            98 -> {
                npc(
                    FaceAnim.HAPPY,
                    "Thank you very much for returning my daughter to",
                    "me. I'm really very grateful... I would like to reward",
                    "you for your bravery and daring.",
                )
                stage++
            }

            0 -> interpreter.sendDialogue("Irena seems to be very upset and cries as you approach her.")
            100, 95, 99 -> {
                player.packetDispatch.sendMessage("Irena seems happy now that her daughter has returned home.")
                npc(
                    FaceAnim.FRIENDLY,
                    "Thanks so much for returning my daughter to me.",
                    "I expect that she will go on another trip soon though.",
                    "She is the adventurous type... a bit like yourself really!",
                    "Okay, see you around then!",
                )
            }

            else -> npc(FaceAnim.HALF_CRYING, "*Sob* Have you found my daughter yet?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            900 -> {
                sendNPCDialogueLines(
                    player,
                    823,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "<col=08088A>-- You show Irena the barrel with Ana in it. --",
                    "Hey great, there's my Mum!",
                )
                stage++
                return true
            }

            901 -> {
                player.inventory.remove(TouristTrap.ANNA_BARREL)
                end()
                quest!!.setStage(player, 98)
                player.dialogueInterpreter.open(822)
                return true
            }
        }
        when (quest!!.getStage(player)) {
            98 ->
                when (stage) {
                    1 -> {
                        npc("I can offer you increased knowledge in two of the", "following areas.")
                        stage++
                    }

                    2 -> {
                        options("Fletching.", "Agility.", "Smithing.", "Thieving.")
                        stage++
                    }

                    3 -> {
                        setAttribute(player, "first-skill", buttonId)
                        npc("Okay, now choose your second skills.")
                        stage++
                    }

                    4 -> {
                        options("Fletching.", "Agility.", "Smithing.", "Thieving.")
                        stage++
                    }

                    5 -> {
                        setAttribute(player, "second-skill", buttonId)
                        npc("Okay, that's all the skills I can teach you!")
                        stage++
                    }

                    6 -> {
                        val skills =
                            intArrayOf(
                                SKILLS[player.getAttribute("first-skill", 0) - 1],
                                SKILLS[player.getAttribute("second-skill", 0) - 1],
                            )
                        for (i in skills) {
                            player.getSkills().addExperience(i, 4650.0)
                        }
                        quest!!.finish(player)
                        end()
                    }
                }

            0 ->
                when (stage) {
                    0 ->
                        if (quest!!.hasRequirements(player)) {
                            npc(FaceAnim.CRYING, "Boo hoo! Oh dear, my only daughter....")
                            stage++
                        } else {
                            npc(FaceAnim.CRYING, "Boo hoo! Go away!")
                            stage = 99
                        }

                    1 -> {
                        player("What's the matter?")
                        stage++
                    }

                    2 -> {
                        npc(
                            FaceAnim.SAD,
                            "Oh dear... my daughter, Ana, has gone missing in the",
                            "desert. I fear that she is lost, or perhaps... *sob* even",
                            "worse.",
                        )
                        stage++
                    }

                    3 -> {
                        player("When did she go into the desert?")
                        stage++
                    }

                    4 -> {
                        npc(
                            FaceAnim.SAD,
                            "*Sob* she went in there just a few days ago, *Sob*",
                            "she said she would be back yesterday.",
                        )
                        stage++
                    }

                    5 -> {
                        npc(FaceAnim.SAD, "*Sob* And she's not...")
                        stage++
                    }

                    6 -> {
                        player("Is there a reward if I get her back?")
                        stage++
                    }

                    7 -> {
                        npc(FaceAnim.SAD, "Well, yes, you'll have my gratitude young man.")
                        stage++
                    }

                    8 -> {
                        npc(
                            FaceAnim.HALF_CRYING,
                            "And I'm sure that Ana will also be very pleased! And I",
                            "may see if I can get a small reward together... But I",
                            "cannot promise anything. So does that mean that you'll",
                            "look for her then?",
                        )
                        stage++
                    }

                    9 -> {
                        player("Okay Irena, calm down. I'll get your daughter back for", "you.")
                        stage++
                    }

                    10 -> {
                        npc(
                            FaceAnim.HAPPY,
                            "That would be great! That's really very nice of you!",
                            "She was wearing a red silk scarf when she left.",
                        )
                        stage++
                    }

                    11 -> {
                        npc(
                            FaceAnim.HALF_ASKING,
                            "Are you 'really' sure you want to do this? I mean, go",
                            "on this quest?",
                        )
                        stage++
                    }

                    12 -> {
                        player("Yes, I'll go on this quest!")
                        stage++
                    }

                    13 -> {
                        npc(
                            FaceAnim.HAPPY,
                            "Oh thank you! You've made me a very happy mother, I",
                            "just hope it's not too late!",
                        )
                        stage++
                    }

                    14 -> {
                        player("Do you have any other hints on where she may have", "gone?")
                        stage++
                    }

                    15 -> {
                        npc(
                            FaceAnim.NEUTRAL,
                            "I did go looking for her myself and I came across some",
                            "footprints just a little way south. I'm worried that they",
                            "lead to the desert mining camp.",
                        )
                        stage++
                    }

                    16 -> {
                        quest!!.start(player)
                        end()
                    }

                    99 -> end()
                }

            10 ->
                when (stage) {
                    0 -> {
                        player("No, not yet.")
                        stage++
                    }

                    1 -> {
                        npc(FaceAnim.HALF_CRYING, "Please! Hurry up.")
                        stage++
                    }

                    2 -> end()
                }

            100, 95, 99 -> {
                player.packetDispatch.sendMessage("Irena goes back to work.")
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(4986)
    }

    companion object {
        private val SKILLS = intArrayOf(Skills.FLETCHING, Skills.AGILITY, Skills.SMITHING, Skills.THIEVING)
    }
}
