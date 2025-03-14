package content.region.kandarin.quest.ikov.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GuardianOfArmadylDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun newInstance(player: Player): Dialogue {
        return GuardianOfArmadylDialogue(player)
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, GuardianOfArmadylDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARDIAN_OF_ARMADYL_274, NPCs.GUARDIAN_OF_ARMADYL_275)
    }
}

class GuardianOfArmadylDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { _ -> true }
            .branch { player ->
                return@branch if (inEquipment(player, Items.PENDANT_OF_LUCIEN_86)) {
                    1
                } else {
                    0
                }
            }.let { branch ->

                branch
                    .onValue(1)
                    .npcl("Thou is a foul agent of Lucien! Such an agent must die!")
                    .endWith { _, player ->
                        npc!!.attack(player)
                    }
                return@let branch
            }.onValue(0)
            .npcl(
                "Thou hast ventured deep into the tunnels, you have reached the temple of our master. It is many ages since a pilgrim has come here.",
            ).options()
            .let { optionBuilder ->
                val returnJoin = b.placeholder()

                optionBuilder
                    .option("I seek the Staff of Armadyl.")
                    .playerl(FaceAnim.FRIENDLY, "I seek the Staff of Armadyl.")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "We are the guardians of the staff, our fathers were guardians and our fathers' fathers before that. Why dost thou seek it?",
                    ).options()
                    .let { optionBuilder2 ->
                        optionBuilder2
                            .option_playerl("Lucien will give me a grand reward for it!")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Thou art working for that spawn of evil?! Fool! You must be cleansed to save your soul!",
                            ).goto(returnJoin)
                        optionBuilder2
                            .option_playerl("Give it to me!")
                            .npcl(FaceAnim.FRIENDLY, "The staff is sacred! You will not have it!")
                            .endWith { _, player ->
                                npc!!.attack(player)
                            }
                        optionBuilder2
                            .option_playerl("I collect rare and powerful artefacts.")
                            .npcl(FaceAnim.FRIENDLY, "Your worldly greed has darkened your soul!")
                            .endWith { _, player ->
                                npc!!.attack(player)
                            }
                    }

                optionBuilder
                    .option_playerl("Out of my way fool!")
                    .npcl(FaceAnim.FRIENDLY, "I may be a fool but I will not step aside!")
                    .options()
                    .let { optionBuilder2 ->
                        optionBuilder2
                            .option_playerl("Why not?!")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Only members of our order are allowed to handle the staff.",
                            ).end()
                        optionBuilder2
                            .option_playerl("Then you must die!")
                            .endWith { _, player ->
                                npc!!.attack(player)
                            }
                        optionBuilder2
                            .option_playerl("You're right, I will go now.")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "That is a wise decision. Stay a while and let your soul be cleansed!",
                            ).end()
                    }

                optionBuilder
                    .option_playerl("What are your kind and what are you doing here?")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "We are the Guardians of Armadyl. We have kept the temple safe for many ages. The evil in the dungeons seek what lies here. The Mahjarrat are the worst.",
                    ).options()
                    .let { optionBuilder2 ->
                        optionBuilder2
                            .option_playerl("What is the Armadyl?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "Armadyl is the god we serve. We have been charged with guarding his sacred artifacts until he requires them.",
                            ).options()
                            .let { optionBuilder3 ->
                                optionBuilder3
                                    .option_playerl("Ah ok, thanks.")
                                    .npcl(FaceAnim.FRIENDLY, "Go in peace.")
                                    .end()
                                optionBuilder3
                                    .option("Someone told me there were only three gods.")
                                    .playerl(
                                        FaceAnim.FRIENDLY,
                                        "Someone told me there were only three gods. Saradomin, Zamorak and Guthix.",
                                    ).npcl(
                                        FaceAnim.FRIENDLY,
                                        "Saradominists. Bleh. They only acknowledge those three. There are at least twenty gods!",
                                    ).end()
                            }

                        optionBuilder2
                            .option_playerl("Who are the Mahjarrat?")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "They are ancient and powerful beings! They are very evil! It is said that they once dominated this plane of existence, Zamorak was supposedly of their blood. They are far fewer in number now.",
                            ).npcl(
                                FaceAnim.FRIENDLY,
                                "Some still have presence in this world in their liche forms. Mahjarrat such as Lucien and Azzanadra would become very powerful if they came into possession of the Staff of Armadyl.",
                            ).options()
                            .let { optionBuilder3 ->
                                optionBuilder3
                                    .option("Did you say Lucien?")
                                    .playerl(
                                        FaceAnim.FRIENDLY,
                                        "Did you say Lucien? It was Lucien that asked me to get the staff!",
                                    ).npcl(
                                        FaceAnim.FRIENDLY,
                                        "You are a fool to be working for Lucien! Your soul must be cleansed to save you!",
                                    ).goto(returnJoin)
                                optionBuilder3
                                    .option_playerl("I hope you're doing a good job then!")
                                    .npcl(FaceAnim.FRIENDLY, "Do not fear! We are devoted to our charge!")
                                    .end()
                            }

                        optionBuilder2
                            .option_playerl("Wow! You must be really old!")
                            .npcl(
                                FaceAnim.ANGRY,
                                "No! I am not old! My family has guarded the staff for many generations.",
                            ).end()
                    }
                return@let returnJoin.builder()
            }.options()
            .let { optionBuilder ->
                optionBuilder
                    .option("How dare you call me a fool!")
                    .playerl(FaceAnim.ANGRY, "How dare you call me a fool! I will work for whom I want!")
                    .npcl(FaceAnim.ANGRY, "We must cleanse the temple!")
                    .endWith { _, player ->
                        npc!!.attack(player)
                    }
                optionBuilder
                    .option_playerl("I just thought of something I must do!")
                    .npcl(FaceAnim.ANGRY, "An agent of evil cannot be allowed to leave!")
                    .endWith { _, player ->
                        npc!!.attack(player)
                    }
                optionBuilder
                    .option_playerl("You're right, it's time for my yearly bath.")
                    .line("The guardian splashes holy water over you.")
                    .npcl(FaceAnim.FRIENDLY, "You have been cleansed!")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Lucien must not get hold of the staff! He would become too powerful!",
                    ).npcl(
                        FaceAnim.FRIENDLY,
                        "Hast thou come across the undead necromancer? It was he that raised an army of the undead against Varrock a generation ago. If you know where he is you can help us defeat him.",
                    ).options()
                    .let { optionBuilder2 ->
                        optionBuilder2
                            .option_playerl("Ok! I'll help!")
                            .npcl(FaceAnim.FRIENDLY, "So he is close by?")
                            .playerl(FaceAnim.FRIENDLY, "Yes!")
                            .npcl(
                                FaceAnim.FRIENDLY,
                                "He must be gaining in power again. If you can defeat him he will be banished from this plane for a while. You will need this pendant to attack him.",
                            ).item(Items.ARMADYL_PENDANT_87, "The guardian has given you a pendant.")
                            .endWith { _, player ->
                                setAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END, 1)
                                if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) == 5) {
                                    setQuestStage(player, Quests.TEMPLE_OF_IKOV, 6)
                                }
                                addItemOrDrop(player, Items.ARMADYL_PENDANT_87)
                            }
                        optionBuilder2
                            .option_playerl("No! I shall not turn against my employer!")
                            .npcl(FaceAnim.ANGRY, "Fool! You will die for your sins!")
                            .endWith { _, player ->
                                npc!!.attack(player)
                            }
                        optionBuilder2
                            .option_playerl("I need time to think.")
                            .npcl(FaceAnim.FRIENDLY, "Linger a while and be at peace.")
                            .end()
                    }
            }
    }
}
