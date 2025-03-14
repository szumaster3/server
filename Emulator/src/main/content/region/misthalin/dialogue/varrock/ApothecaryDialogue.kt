package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ApothecaryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.getQuestRepository().getQuest(Quests.ROMEO_JULIET).getStage(player) == 40) {
            player("Apothecary. Father Lawrence sent me.")
            stage = 500
            return true
        }
        if (player.getQuestRepository().getQuest(Quests.ROMEO_JULIET).getStage(player) == 50) {
            return if (!player.inventory.contains(753, 1)) {
                npc("Keep searching for those Cadava berries. They're needed", "for the potion.")
                stage = 507
                true
            } else {
                npc("Well done. You have the berries.")
                stage = 637
                true
            }
        }
        if (player.getQuestRepository().getQuest(Quests.ROMEO_JULIET).getStage(player) == 60) {
            if (!player.inventory.contains(756, 1) && !player.bank.contains(756, 1)) {
                return if (player.inventory.contains(753, 1)) {
                    npc("Well done. You have the berries.")
                    stage = 637
                    true
                } else {
                    npc("Keep searching for those Cadava berries. They're needed", "for the potion.")
                    stage = 507
                    true
                }
            } else {
                npc("I am the Apothecary. I brew potions.", "Do you need anything specific?")
            }
        }
        npc("I am the Apothecary. I brew potions.", "Do you need anything specific?")
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> {
                options(
                    "Can you make a strength potion?",
                    "Do you know a potion to make hair fall out?",
                    "Have you got any good potions to give away?",
                    "Can you make a potion that makes it seem like I'm dead?",
                )
                stage = 2
            }

            2 ->
                when (buttonId) {
                    1 -> {
                        player("Can you make a strength potion?")
                        stage = 10
                    }

                    2 -> {
                        player("Do you know a potion to make hair fall out?")
                        stage = 20
                    }

                    3 -> {
                        player("Have you got any good potions to give away?")
                        stage = 140
                    }

                    4 -> {
                        player("Can you make a potion that makes it seems like I'm dead?")
                        stage = 40
                    }
                }

            10 -> {
                if (player.inventory.containItems(223, 225)) {
                    npc("Certainly, just hand over the ingredients and 5 coins.")
                    stage = 50
                    return true
                }
                npc(
                    "Yes, but the ingredients are a little hard to find. If you",
                    "ever get them I will make it for you, for a fee.",
                )
                stage = 11
            }

            50 -> {
                if (!player.inventory.contains(995, 5)) {
                    end()
                    player.packetDispatch.sendMessage("You need 5 gold coins to do that.")
                    return true
                }
                interpreter.sendDialogue("You hand over the ingredients and money.")
                stage = 51
            }

            51 ->
                if (player.inventory.remove(*POTION_ITEMS)) {
                    player.inventory.add(POTION)
                    end()
                    player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, 0)
                }

            11 -> {
                player("So what are the ingredients?")
                stage = 12
            }

            12 -> {
                npc("You'll need to find the eggs of the deadly red spider and a", "limpwurt root.")
                stage = 13
            }

            13 -> {
                npc("Oh and you'll have to pay me 5 coins.")
                stage = 14
            }

            14 -> {
                player("Ok, I'll look out for them.")
                stage = 15
            }

            15 -> end()
            20 -> {
                npc("I do indeed. I gave it to my mother. That's why I now live", "alone.")
                stage = 21
            }

            21 -> end()
            30 -> {
                npc("Ok then. Try this potion.")
                player.inventory.add(NULL_POTION)
                stage = 31
            }

            31 -> end()
            40 -> {
                npc(
                    "What a strange and morbid request! I can as it happens.",
                    "The berry of the cadava bush, prepared properly, will",
                    "induce a coma so deep that you will seem to be dead. It's",
                    "very dangerous.",
                )
                stage = 41
            }

            41 -> {
                npc("I have the other ingredients, but I'll need you to bring me", "one bunch of cadava berries.")
                stage = 42
            }

            42 -> {
                player("I'll bear that in mind.")
                stage = 43
            }

            43 -> end()
            140 -> {
                npc("Sorry, charity is not my strong point.")
                stage = 141
            }

            141 -> end()
            500 -> {
                player("I need a Cadava potion to help Romeo and Juliet.")
                stage = 501
            }

            501 -> {
                npc("Cadava potion. It's pretty nasty. And hard to make.")
                stage = 502
            }

            502 -> {
                npc("Wing of rat, tail of frog.", "Ear of snake and horn of dog.")
                stage = 503
            }

            503 -> {
                npc("I have all that, but I need some Cadava berries.")
                stage = 504
            }

            504 -> {
                npc("You will have to find them while I get the rest ready.")
                stage = 505
            }

            505 -> {
                npc(
                    "Bring them here when you have them. But be careful.",
                    "They are nasty.",
                )
                stage = 506
            }

            506 -> {
                player.getQuestRepository().getQuest(Quests.ROMEO_JULIET).setStage(player, 50)
                player("Ok, thanks.")
                stage = 507
            }

            507 -> end()
            637 -> {
                interpreter.sendItemMessage(753, "You hand over the berries.")
                stage = 638
            }

            638 -> {
                if (player.inventory.remove(CADAVA_BERRIES)) {
                    npc("Phew! Here is what you need.")
                }
                stage = 639
            }

            639 -> {
                if (!player.inventory.add(CADAVA_POTION)) {
                    GroundItemManager.create(GroundItem(CADAVA_POTION, player.location, player))
                }
                interpreter.sendItemMessage(756, "The Apothecary gives you a Cadava potion.")
                stage = 640
            }

            640 -> {
                player.getQuestRepository().getQuest(Quests.ROMEO_JULIET).setStage(player, 60)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.APOTHECARY_638)
    }

    companion object {
        private val POTION_ITEMS = arrayOf(Item(223), Item(225), Item(995, 5))
        private val POTION = Item(115)
        private val NULL_POTION = Item(195, 1)
        private val CADAVA_BERRIES = Item(753)
        private val CADAVA_POTION = Item(756)
    }
}
