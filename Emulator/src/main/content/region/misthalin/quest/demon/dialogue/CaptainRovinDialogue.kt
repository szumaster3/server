package content.region.misthalin.quest.demon.dialogue

import content.region.misthalin.quest.demon.handlers.DemonSlayerUtils
import core.api.freeSlots
import core.api.inBank
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import org.rs.consts.NPCs
import org.rs.consts.Quests

class CaptainRovinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DEMON_SLAYER)
        when (quest!!.getStage(player)) {
            else -> {
                npc(FaceAnim.HALF_GUILTY, "What are you doing up here? Only the palace guards", "are allowed up here.")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            else -> defaultDialogue(buttonId)
        }
        return true
    }

    private fun defaultDialogue(buttonId: Int) {
        when (stage) {
            0 -> {
                options("I am one of the palace guards.", "What about the King?", "Yes I know, but this is important.")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "I am one of the palace guards.")
                        stage = 10
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "What about the King? Surely you'd let him up here.")
                        stage = 20
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "Yes, I know but this is important.")
                        stage = 30
                    }
                }

            30 -> {
                npc(FaceAnim.HALF_GUILTY, "Ok, I'm listening. Tell me what's so important.")
                stage = 31
            }

            31 -> {
                if (quest!!.getStage(player) == 20) {
                    player("There's a demon who wants to invade the city.")
                    stage = 600
                } else {
                    player(FaceAnim.HALF_GUILTY, "Erm... I forgot.")
                    stage = 32
                }
            }

            10 -> {
                npc(FaceAnim.HALF_GUILTY, "No, you're not! I know all the palace guards.")
                stage = 11
            }

            11 -> end()

            20 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, yes I suppose we'd let him up, He doesn't",
                    "generally want to come up here, but if he did want to,",
                    "he could.",
                )
                stage = 21
            }

            21 -> end()
            32 -> end()

            600 -> {
                if (inInventory(player, DemonSlayerUtils.SECOND_KEY.id) ||
                    inBank(
                        player,
                        DemonSlayerUtils.SECOND_KEY.id,
                    )
                ) {
                    npc("Yes, you said before, haven't you killed it yet?")
                    stage = 620
                } else {
                    npc("Is it a powerful demon?")
                    stage = 601
                }
            }

            601 -> {
                player("Yes, very.")
                stage = 602
            }

            602 -> {
                npc(
                    "As good as the palace guards are, I don't know if",
                    "they're up to taking on a very powerful demon.",
                )
                stage = 603
            }

            603 -> {
                player("It's not them who are going to fight the demon, it's me.")
                stage = 604
            }

            604 -> {
                npc("What, all by yourself? How are you going to do that?")
                stage = 605
            }

            605 -> {
                player(
                    "I'm going to use the powerful sword Silverlight, which I",
                    "believe you have one of the keys for?",
                )
                stage = 606
            }

            606 -> {
                npc("Yes, I do. But why should I give it to you?")
                stage = 607
            }

            607 -> {
                player("Sir Prysin said you would give me the key.")
                stage = 608
            }

            608 -> {
                npc("Oh, he did, did he? Well I don't report to Sir Prysin, I", "report directly to the king!")
                stage = 609
            }

            609 -> {
                npc(
                    "I didn't work my way up through the ranks of the",
                    "palace guards so I could take orders from an ill-bred",
                    "moron who only has his job because his great-",
                    "grandfather was a hero with a silly name!",
                )
                stage = 610
            }

            610 -> {
                player("Why did he give you one of the keys then?")
                stage = 611
            }

            611 -> {
                npc(
                    "Only because the king ordered him to! The king",
                    "couldn't get Sir Prysin to part with his precious",
                    "ancestral sword, but he made him lock it up so he",
                    "couldn't lose it.",
                )
                stage = 612
            }

            612 -> {
                npc("I got one key and I think some wizard got another.", "Now what happened to the third one?")
                stage = 613
            }

            613 -> {
                player("Sir Prysin dropped it down a drain!")
                stage = 614
            }

            614 -> {
                npc("Ha ha ha! The idiot!")
                stage = 615
            }

            615 -> {
                npc("Okay, I'll give you the key, just so that it's you that", "kills the demon and not Sir Prysin!")
                stage = 616
            }

            616 -> {
                end()
                if (freeSlots(player) == 0) {
                    npc("Talk to me again when you have free inventory space.")
                    return
                }
                if (player.inventory.add(DemonSlayerUtils.SECOND_KEY)) {
                    interpreter.sendItemMessage(DemonSlayerUtils.SECOND_KEY, "Captain Rovin hands you a key.")
                    return
                }
            }

            620 -> {
                player(
                    "Well I'm going to use the powerful sword Silverlight",
                    "which I believe you have one of the keys for?",
                )
                stage = 621
            }

            621 -> {
                npc("I already gave you my key. Check your pockets.")
                stage = 622
            }

            622 -> end()
        }
    }

    override fun newInstance(player: Player): Dialogue {
        return CaptainRovinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_ROVIN_884)
    }
}
