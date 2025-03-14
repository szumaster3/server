package content.region.misthalin.quest.romeo.dialogue

import content.region.misthalin.quest.romeo.cutscene.JulietCutscene
import core.api.*
import core.api.quest.setQuestStage
import core.api.quest.updateQuestTab
import core.game.activity.ActivityManager
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.path.Pathfinder
import core.game.world.repository.Repository.findNPC
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class JulietDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null
    private var cutscene: JulietCutscene? = null

    override fun open(vararg args: Any): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.ROMEO_JULIET)
        npc = args[0] as NPC
        if (args.size > 1) {
            cutscene = args[1] as JulietCutscene
            npc("Oh, here's Phillipa, my cousin...she's in on the plot too!")
            stage = 2000
            return true
        }
        stage =
            when (quest!!.getStage(player)) {
                0 -> {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "Romeo, Romeo, wherefore art thou Romeo? Bold",
                        "adventurer, have you seen Romeo on your travels?",
                        "Skinny guy, a bit wishy washy, head full of poetry.",
                    )
                    0
                }

                10 -> {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "Juliet, I come from Romeo.",
                        "He begs me to tell you that he cares still.",
                    )
                    700
                }

                20 -> {
                    player(FaceAnim.HALF_GUILTY, "Hello Juliet!")
                    100
                }

                30 -> {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "Hi Juliet, I have passed your message on to",
                        "Romeo...he's scared half out of his wits at the news that",
                        "your father wants to kill him.",
                    )
                    900
                }

                40 -> {
                    player(
                        "Hi Juliet, I've found Father Lawrence and he has a",
                        "cunning plan. But for it to work, I need to seek the",
                        "Apothecary!",
                    )
                    236
                }

                50 -> {
                    player("Hi Juliet!")
                    656
                }

                60 ->
                    if (!player.inventory.contains(756, 1)) {
                        player(
                            "Hi Juliet! I have an interesting proposition for",
                            "you...suggested by Father Lawrence. It may be the",
                            "only way you'll be able to escape from this house and",
                            "be with Romeo.",
                        )
                        950
                    } else {
                        player(
                            "Hi Juliet! I have an interesting proposition for",
                            "you...suggested by Father Lawrence. It may be the",
                            "only way you'll be able to escape from this house and",
                            "be with Romeo.",
                        )
                        950
                    }

                70 -> {
                    npc("Quickly! Go and tell Romeo the plan!")
                    1002
                }

                else -> {
                    npc(FaceAnim.ANGRY, "Oh, Romeo, that no good scoundrel!")
                    22
                }
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ROMEO_JULIET)
        val phil = if (cutscene != null) cutscene!!.phillipia else findNPC(3325)!!
        val dad = if (cutscene != null) cutscene!!.npcs[2] else findNPC(3324)!!
        when (stage) {
            2000 -> npc("She's going to make it seem even more convincing!").also { stage++ }
            2001 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.HALF_GUILTY,
                        "Yes, I'm quite the actress! Good luck dear cousin!",
                    ).also {
                        stage++
                    }
            2002 -> npc("Right...bottoms up!").also { stage++ }
            2003 -> {
                close()
                Pulser.submit(
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> npc.animate(DRINK_ANIM)
                                2 -> {
                                    npc(FaceAnim.THINKING, "Urk!")
                                    stage = 2004
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }

            2004 -> {
                close()
                npc.animate(Animation(836))
                Pulser.submit(
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                2 -> {
                                    interpreter.sendDialogues(phil, FaceAnim.THINKING, "Oh no...Juliet has...died!")
                                    stage = 2005
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }

            2005 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "You might be more believable if you're not smiling when",
                    "you say it...",
                ).also {
                    stage++
                }
            2006 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.HALF_GUILTY,
                        "Oh yeah...you might be right...ok, let's try again.",
                    ).also {
                        stage++
                    }
            2007 -> interpreter.sendDialogues(phil, FaceAnim.HALF_GUILTY, "Oh no...Juliet has...died?").also { stage++ }
            2008 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Perhaps a bit louder, like you're upset...that your cousin",
                    "has died!",
                ).also {
                    stage++
                }
            2009 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.HALF_GUILTY,
                        "Right...yes...Ok, ok, I think I'm getting my motivation",
                        "now. Ok, let's try this again!",
                    ).also {
                        stage++
                    }
            2010 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.FURIOUS,
                        "OH NO...JULIET HAS...DIED?....",
                        "Oooooohhhhhh....(sob), (sob).Juliet...my poor dead cousin!",
                    ).also {
                        stage++
                    }
            2011 -> interpreter.sendDialogues(dad, FaceAnim.HALF_GUILTY, "What's all that screaming?").also { stage++ }
            2012 -> {
                val path = Pathfinder.find(dad, player.location.transform(0, 1, 0))
                path.walk(dad)
                interpreter.sendDialogues(
                    dad,
                    FaceAnim.HALF_GUILTY,
                    "Oh no! My poor daughter...what has become of you?",
                )
                stage++
            }

            2013 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.FURIOUS,
                        "Poor Juliet...make preparations for her body to be",
                        "placed in the Crypt...",
                    ).also {
                        stage++
                    }
            2014 ->
                if (player.inventory.remove(POTION)) {
                    quest.setStage(player, 70)
                    cutscene!!.stop(true)
                    end()
                }

            0 ->
                options(
                    "Yes I've met him.",
                    "No, I think I'd have remembered if I'd met him.",
                    "I guess I could look for him for you.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes I've met him.").also { stage = 10 }
                    2 ->
                        player(FaceAnim.HALF_GUILTY, "No, I think I'd have remembered if I'd met him.").also {
                            stage =
                                20
                        }
                    3 -> player(FaceAnim.HALF_GUILTY, "I guess I could look for him for you.").also { stage = 30 }
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'd be most grateful if you could please deliver a",
                    "message to him?",
                ).also {
                    stage =
                        31
                }
            20 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh, well that's a shame, I was hopping that you might",
                    "deliver a message to him for me.",
                ).also {
                    stage++
                }
            21 ->
                player(FaceAnim.HALF_GUILTY, "Sorry, but I don't even know what he looks like.").also {
                    stage =
                        END_DIALOGUE
                }
            30 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh, that would be so wonderful of you!",
                    "I'd be most grateful if you could please deliver a",
                    "message to him?",
                ).also {
                    stage++
                }
            31 -> player(FaceAnim.HALF_GUILTY, "Certainly, I'll do it straight away.").also { stage++ }
            32 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Many thanks! Oh, i'm so very grateful. You may be",
                    "our only hope.",
                ).also { stage++ }
            33 -> {
                end()
                if (!player.inventory.add(Item(755))) {
                    GroundItemManager.create(GroundItem(Item(755), npc.location, player))
                }
                setQuestStage(player, Quests.ROMEO_JULIET, 20)
                updateQuestTab(player)
                sendItemDialogue(player, Items.MESSAGE_755, "Juliet gives you a message.")
            }
            100 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Hello there...have you delivered the message to Romeo",
                    "yet? What news do you have from my loved one?",
                ).also {
                    stage++
                }
            101 ->
                if (!inInventory(player, Items.MESSAGE_755, 1) && !inBank(player, Items.MESSAGE_755, 1)) {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "Hmmm, that's the thing about messages....they're so easy",
                        "to misplace...",
                    ).also {
                        stage =
                            105
                    }
                } else {
                    player(FaceAnim.HALF_GUILTY, "Oh, sorry, I've not had a chance to deliver it yet!").also { stage++ }
                }
            102 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh, that's a shame. I've been waiting so patiently to",
                    "hear some word from him.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            105 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "How could you lose that message?",
                    "It was incredibly important...and it took me an age to",
                    "write! I used joined up writing and everything!",
                ).also {
                    stage++
                }
            106 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Please, take this new message to him,",
                    "and please don't loose it.",
                ).also { stage++ }
            107 -> {
                end()
                if (!player.inventory.add(Item(755))) {
                    GroundItemManager.create(GroundItem(Item(755), npc.location, player))
                }
                setQuestStage(player, Quests.ROMEO_JULIET, 20)
                updateQuestTab(player)
                sendItemDialogue(player, Items.MESSAGE_755, "Juliet gives you a message.")
            }
            900 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "yes, unfortunately my father is quite the hunter, you",
                    "may have seen some the animal head trophies on the",
                    "wall. And it would be so awful to see Romeo's head up",
                    "there with them!",
                ).also {
                    stage++
                }
            901 -> player(FaceAnim.HALF_GUILTY, "I know what you mean...").also { stage++ }
            902 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "...this hair colour will clash terribly with the rest of the",
                    "decoration.",
                ).also {
                    stage++
                }
            903 -> npc(FaceAnim.HALF_GUILTY, "That's not what I was suggesting at all...").also { stage++ }
            904 -> player(FaceAnim.HALF_GUILTY, "I know, I know...I was just kidding.").also { stage++ }
            905 ->
                player(
                    "Anyway, don't worry because I'm on the case. I'm",
                    "going to get some help from Father Lawrence.",
                ).also {
                    stage++
                }
            906 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh yes, I'm sure that Father Lawrence will come up",
                    "with a solution. I hope you find him soon.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            700 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh how my heart soars to hear this news! Please take",
                    "this message to him with great haste.",
                ).also {
                    stage++
                }
            701 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Well, I hope it's good news...he was quite upset when I",
                    "left him.",
                ).also {
                    stage++
                }
            702 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "He's quite often upset...the poor sensitive soul. But I",
                    "don't think he's going to take this news very well,",
                    "however, all is not lost.",
                ).also {
                    stage++
                }
            703 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Everything is explained in the letter, would you be so",
                    "kind and deliver it to him please?",
                ).also {
                    stage++
                }
            704 -> player(FaceAnim.HALF_GUILTY, "Certainly, I'll do so straight away.").also { stage++ }
            705 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Many thanks! Oh, I'm so very grateful. You may be",
                    "our only hope.",
                ).also { stage++ }
            706 -> {
                end()
                if (!player.inventory.add(Item(755))) {
                    GroundItemManager.create(GroundItem(Item(755), npc.location, player))
                }
                setQuestStage(player, Quests.ROMEO_JULIET, 20)
                updateQuestTab(player)
                sendItemDialogue(player, Items.MESSAGE_755, "Juliet gives you a message.")
            }
            236 ->
                npc(
                    "Oh good! I knew Father Lawrence would come up with",
                    "something. However, I don't know where the apothecary",
                    "is...I hope you find him soon. My father's temper gets",
                    "no better.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            656 ->
                npc(
                    "Hi " + player.username + ", how close am I to being with my true",
                    "love Romeo?",
                ).also { stage++ }
            657 -> player("Sorry, I still have to get a speical potion for you.").also { stage++ }
            658 ->
                npc(
                    "Oh, I hope it isn't a love potion because you would be",
                    "wasting your time. My love for Romeo grows stronger",
                    "every minute...",
                ).also {
                    stage++
                }
            659 -> player("That must be because you're not with him...").also { stage++ }
            660 -> npc("Oh no! I long to be close to my true love Romeo!").also { stage++ }
            661 -> player("Well, ok then...I'll set about getting this potion as quickly", "as I can!").also { stage++ }
            662 -> npc("Fair luck to you, the end is close.").also { stage = END_DIALOGUE }
            950 -> npc("Go on....").also { stage++ }
            951 ->
                if (!player.inventory.containsItem(POTION)) {
                    player("Let me go get the potion..").also { stage = END_DIALOGUE }
                } else {
                    player(
                        "I have a Cadava potion here, suggested by Father",
                        "Lawrence. If you drink it, it will make you appear dead!",
                    ).also {
                        stage++
                    }
                }

            952 -> npc("Yes...").also { stage++ }
            953 ->
                player(
                    "And when you appear dead...your still and lifeless",
                    "corpse will be removed to the crypt!",
                ).also {
                    stage++
                }
            954 -> npc("Oooooh, a cold dark creepy crypt...").also { stage++ }
            955 -> npc("...sounds just peachy!").also { stage++ }
            956 ->
                player(
                    "Then...Romeo can steal into the crypt and rescure you",
                    "just as you wake up!",
                ).also { stage++ }
            957 -> npc("...and this is the great idea for getting me out of here?").also { stage++ }
            958 ->
                player(
                    "To be fair, I can't take all the credit...in fact...it was all",
                    "Father Lawrence's suggestion...",
                ).also {
                    stage++
                }
            959 -> npc("Ok...if this is the best we can do...hand over the potion!").also { stage++ }
            960 ->
                sendItemDialogue(
                    player,
                    Items.CADAVA_POTION_756,
                    "You pass the suspicious potion to Juliet.",
                ).also { stage++ }
            961 -> npc("Wonderful! I just hope Romeo can remember to get", "me from the crypt.").also { stage++ }
            962 ->
                npc(
                    "Please go to Romeo and make sure he understands.",
                    "Although I love his gormless, lovelorn soppy ways, he",
                    "can be a bit dense sometimes and I don't want to wake",
                    "up in that crypt on my own.",
                ).also {
                    stage++
                }
            963 ->
                npc(
                    "Before I swig this potion down, let me stand on the",
                    "balcony so that I might see the sun one last time before",
                    "I am commited to the crypt.",
                ).also {
                    stage++
                }
            964 -> {
                end()
                ActivityManager.start(player, "Juliet Cutscene", false)
            }
            965 -> npc("She's going to make it seem even more convincing!").also { stage++ }
            966 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.HALF_GUILTY,
                        "Yes, I'm quite the actress! Good luck dear cousin!",
                    ).also {
                        stage++
                    }
            967 -> npc("Right...buttoms up!").also { stage++ }
            968 -> {
                npc.animate(npc.properties.deathAnimation)
                npc("Urk!")
                stage++
            }
            969 -> interpreter.sendDialogues(phil, FaceAnim.HALF_GUILTY, "Oh no...Juliet has...died!").also { stage++ }
            970 -> player("You might be more believable if you're not smiling when", "you say it...").also { stage++ }
            971 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.HALF_GUILTY,
                        "Oh yeah...you might be right...ok, let's try again.",
                    ).also {
                        stage++
                    }
            972 -> interpreter.sendDialogues(phil, FaceAnim.HALF_GUILTY, "Oh no...Juliet has...died?").also { stage++ }
            973 -> player("Perhaps a bit louder, like you're upset...that your cousin", "has died!").also { stage++ }
            974 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.HALF_GUILTY,
                        "Right...yes...Ok, ok, I think I'm getting my motivation",
                        "now. Ok, let's try this again!",
                    ).also {
                        stage++
                    }
            975 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.SAD,
                        "OH NO...JULIET HAS...DIED?...",
                        "Ooooooohhhhhh...(sob), (sob).Juliet...my poor dead cousin!",
                    ).also {
                        stage++
                    }
            976 -> interpreter.sendDialogues(dad, FaceAnim.HALF_GUILTY, "What's all that screaming?").also { stage++ }
            977 ->
                interpreter
                    .sendDialogues(
                        dad,
                        FaceAnim.SAD,
                        "Oh no! My poor daughter...what has become of you?",
                    ).also {
                        stage++
                    }
            978 ->
                interpreter
                    .sendDialogues(
                        phil,
                        FaceAnim.HALF_GUILTY,
                        "Poor Juliet...make preparations for her body to be",
                        "placed in the Crypt...",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            1002 -> player("I'm on my way!").also { stage = END_DIALOGUE }
            22 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return JulietDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JULIET_637)
    }

    companion object {
        private val POTION = Item(Items.CADAVA_POTION_756)
        private val DRINK_ANIM = Animation(Animations.DRINK_BEER_1327)
    }
}
