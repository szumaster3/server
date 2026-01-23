package content.region.misthalin.varrock.quest.surok.dialogue

import content.region.misthalin.varrock.quest.surok.WhatLiesBelow
import content.region.misthalin.varrock.quest.surok.plugin.WhatLiesBelowCutscene
import core.api.*
import core.game.activity.ActivityManager
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Handles the Surok Magis dialogue.
 * @author Vexia, Nuggles
 */
@Initializable
class SurokMagisDialogue : Dialogue {

    private var quest: Quest? = null
    var cutscene: WhatLiesBelowCutscene? = null

    constructor()

    constructor(player: Player?) : super(player)

    override fun newInstance(player: Player?): Dialogue {
        return SurokMagisDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.WHAT_LIES_BELOW) ?: return false

        when (quest?.getStage(player)) {
            20 -> player("Hello.")
            30, 40 -> npc("Ah! You're back. Have you found the things I need yet?")
            50 -> if (!inInventory(player, WhatLiesBelow.SUROKS_LETTER, 1) &&
                !inBank(player, WhatLiesBelow.SUROKS_LETTER, 1)) {
                player("That letter was treasonous so I destroyed it!")
                stage = 10
            } else {
                player("This letter is treasonous! I'm going to report you to the", "king!")
            }
            60 -> npc("Hi, ${player.username}!")
            70 -> {
                if (!inInventory(player, WhatLiesBelow.BEACON_RING, 1) &&
                    !inEquipment(player, WhatLiesBelow.BEACON_RING, 1)) {
                    player("I should probably get my beacon ring first...")
                    stage = END_DIALOGUE
                    return true
                }

                if (args.size >= 2) {
                    cutscene = player.getAttribute("cutscene", null)

                    if (args.size == 3) {
                        player.dialogueInterpreter.sendDialogues(cutscene!!.zaff, null, "Your teleport spell has been corrupted, Surok! I have", "placed a magic block on this room. You will remain here,", "under guard, in the library from now on.")
                        stage = 12
                    } else {
                        interpreter.sendPlainMessage(true, "The room grows dark and you sense objects moving...")
                        stage = 10
                    }
                    return true
                }

                player("Surok!! Your plans have been uncovered! You are hereby", "under arrest on the authority of the Varrock Palace", "Secret Guard!")
            }

            80, 90, 100 -> npc("You have foiled my plans, ${player.username}... I obviously", "underestimated you.")
            else -> npc("Excuse me?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (quest!!.getStage(player)) {
            20 -> when (stage) {
                0 -> npc("Hah! Come for my Aphro-Dizzy-Yak spell! Want", "someone to fall madly in love with you eh? Not", "surprised with a face like that, to be honest!").also { stage++ }
                1 -> player("I didn't come here to be insulted!").also { stage++ }
                2 -> npc("Really? Well, with ears like that, you do surprise me!").also { stage++ }
                3 -> if (!inInventory(player, WhatLiesBelow.RATS_LETTER, 1)) {
                    player("Nevermind!").also { stage = 99 }
                } else {
                    player("No, look. I have a letter for you.").also { stage++ }
                }
                4 -> npc("Really? Well then, let me see it!").also { stage++ }
                5 -> player("Here it is!").also { stage++ }
                6 -> {
                    playGlobalAudio(player.location, 3523)
                    npc.animate(Animation.create(6096))
                    npc("Of all the luck!").also { stage++ }
                }
                7 -> player(FaceAnim.HALF_ASKING,"Why did you destroy the letter?").also { stage++ }
                8 -> npc("None of your business! It's a secret!").also { stage++ }
                9 -> player("Yes, there seems to be a lot of them going around at", "the moment.").also { stage++ }
                10 -> npc("Of course. Hmmm. However, I could let you in on", "another secret, if you like?").also { stage++ }
                11 -> player("Go on, then!").also { stage++ }
                12 -> npc("My secret is this. I have been spending time here in", "the palace library trying to discover some ancient spells", "and magics.").also { stage++ }
                13 -> npc("In my research, I have uncovered the most astounding", "spell that will allow me to transform simple clay into solid", "gold bars!").also { stage++ }
                14 -> npc("Now I am ready to use the spell to create all the gold", "I...uh...the city wants. I would gladly share this gold", "with you; I simply need a few more things.").also { stage++ }
                15 -> player(FaceAnim.HALF_ASKING,"Okay, what do you need?").also { stage++ }
                16 -> npc("I will only need a couple of items. The first is very", "simple: an ordinary bowl to use as a casting vessel.").also { stage++ }
                17 -> npc("You should be able to find one of these at any local", "store here in Varrock. I would go myself but I", "am...uh...busy with my research.").also { stage++ }
                18 -> npc("The other item is much harder. I need a metal wand", "infused with chaos magic.").also { stage++ }
                19 -> player(FaceAnim.HALF_ASKING,"How would I get something like that?").also { stage++ }
                20 -> npc("Take this metal wand. You will also need 15 chaos", "runes. When you get to the Chaos Altar, use the wand", "on the altar itself. This would infuse the runes into the", "wand.").also { stage++ }
                21 -> player("How on earth do you know about Runecrafting? I", "thought only a few people knew of it.").also { stage++ }
                22 -> npc("Hah! Don't presume to think that those wizards in their", "fancy towers are the only people to have heard of", "Runecrafting! Now pay attention!").also { stage++ }
                23 -> npc("You will need to have the 15 chaos runes in your", "inventory. Make sure you also have either a chaos", "talisman or chaos tiara to complete the infusion.").also { stage++ }
                24 -> player(FaceAnim.HALF_ASKING, "Where can I get a talisman or a tiara?").also { stage++ }
                25 -> npc("I'm afraid I don't know. You will need to research for", "one.").also { stage++ }
                26 -> npc("Bring the infused wand and a bowl back to me and I", "will make us both rich!").also { stage++ }
                27 -> npc("One more thing. I have uncovered information here in", "the library which may be of use to you. It tells of a", "safe route to the Chaos Altar that avoids the Wilderness.").also { stage++ }
                28 -> player("Great! What is it?").also { stage++ }
                29 -> npc("It is an old tome...a history book of sorts. It's", "somewhere here in the library. I forget where I left it,", "but it should be easy enough for you to find.").also { stage++ }
                30 -> npc("I have also given you a copy of a diary", "I...uh...acquired. It may also help you to find that which", "you seek.").also { stage++ }
                31 -> {
                    if(removeItem(player, WhatLiesBelow.RATS_LETTER, Container.INVENTORY)) {
                        addItem(player, WhatLiesBelow.WAND, 1, Container.INVENTORY)
                        addItem(player, WhatLiesBelow.SIN_KETH_DIARY, 1, Container.INVENTORY)
                        quest!!.setStage(player, 30)
                    }
                    end()
                }
                99 -> end()
            }

            30, 40 -> when (stage) {
                0 -> when {
                    inInventory(player, WhatLiesBelow.INFUSED_WAND, 1) -> {
                        if (!inInventory(player, WhatLiesBelow.BOWL, 1)) {
                            player("No, I still need to get you a bowl.").also { stage = 1 }
                        } else {
                            player("I have the things you wanted.").also { stage = 6 }
                        }
                    }

                    !inInventory(player, WhatLiesBelow.WAND, 1) &&
                            !inBank(player, WhatLiesBelow.WAND, 1) -> {
                        player(FaceAnim.SAD,"I lost the wand!").also { stage = 2 }
                    }

                    !inInventory(player, WhatLiesBelow.SIN_KETH_DIARY, 1) &&
                            !inBank(player, WhatLiesBelow.SIN_KETH_DIARY, 1) -> {
                        player(FaceAnim.SAD,"I lost the diary!").also { stage = 4 }
                    }

                    else -> player(FaceAnim.NEUTRAL,"No not yet.").also { stage++ }

                }
                1 -> end()
                2 -> npc("Somehow, I knew that would happen so I have made a", "few spares for just such an occasion!").also { stage++ }
                3 -> {
                    npc("Here you are. Try not to lose this one!")
                    addItem(player, WhatLiesBelow.WAND, 1, Container.INVENTORY)
                    stage = 1
                }

                4 -> {
                    npc("Here you are, Try not to lose this one!")
                    addItem(player, WhatLiesBelow.SIN_KETH_DIARY, 1, Container.INVENTORY)
                    stage = 5
                }

                5 -> end()
                6 -> npc("Excellent! Well done! I knew that you would not let", "me down.").also { stage++ }
                7 -> player("So...about this gold that you're going to", "give me?").also { stage++ }
                8 -> npc("All in good time. I must prepare the spell first", ", and that will take a little time. While I am", "doing that, please take this letter to Rat, the trader", "outside the city who sent you here.").also { stage++ }
                9 -> player("Okay, but I'll be back for my gold.").also { stage++ }
                10 -> npc("Yes, yes, yes. Now off you go!").also { stage++ }
                11 -> {
                    quest!!.setStage(player, 50)
                    addItem(player, WhatLiesBelow.SUROKS_LETTER, 1, Container.INVENTORY)
                    end()
                }
            }

            50 -> when (stage) {
                0 -> npc("Hah! Have fun with that!").also { stage++ }
                1 -> end()
                10 -> npc("Really? You destroyed it?").also { stage++ }
                11 -> player("Yes!").also { stage++ }
                12 -> npc("You want another one, don't you?").also { stage++ }
                13 -> player("Ah..uh..yes..yes, I do.").also { stage++ }
                14 -> {
                    npc("Fine. Here you are. And stop all this complaining; it's", "getting me down!")
                    addItem(player, WhatLiesBelow.SUROKS_LETTER, 1, Container.INVENTORY)
                    stage++
                }

                15 -> end()
            }

            60 -> when (stage) {
                0 -> player("You are a bad man!").also { stage++ }
                1 -> end()
            }

            70 -> when (stage) {
                -1 -> end()
                0 -> npc("So! You're with the Secret Guard, eh? I should have", "known! I knew you had ugly ears from the start...", "and your nose is too short!").also { stage++ }
                1 -> player("Give yourself up, Surok!").also { stage++ }
                2 -> npc("Never! I am Surok Magis, descendant of the High Elder", "Sin'keth Magis, rightful heir of the Dagon'hai Order! I will", "have my revenge on those who destroyed my people!") .also { stage++ }
                3 -> player("The place is surrounded. There is nowhere to run!").also { stage++ }
                4 -> npc("Do you really wish to die so readily? Are you prepared", "to face your death?").also { stage++ }
                5 -> player("Bring it on!").also { stage++ }
                6 -> npc("I am Dagon'hai! I run from nothing. My spell has", "been completed and it is time for you to meet your", "end, " + player.username + "! The king is now under my control!").also { stage++ }
                7 -> {
                    end()
                    ActivityManager.start(player, Quests.WHAT_LIES_BELOW, false);
                }

                10 -> {
                    close()
                    player.unlock()
                    cutscene?.let {
                        it.king.unlock()
                        it.reset()
                        it.king.properties.combatPulse.attack(player)
                    }
                    player.interfaceManager.restoreTabs()
                    stage = 11
                }

                11 -> {
                    close()
                    stage = 12
                }
                12 -> {
                    player.lock()
                    npc("No! All is lost! I must escape!").also { stage++ }
                }
                13 -> interpreter.sendDialogues(cutscene!!.zaff, null, "You will not escape justice this time, Surok!").also { stage++ }
                14 -> {
                    npc("No! My plans have been ruined! I was so close to", "success!")
                    stage = 16
                }

                16 -> {
                    interpreter.sendDialogues(cutscene!!.zaff, null, "Thank you for your help, " + player.username + ". I will put the", "room back in order and then I must leave. Surok is", "defeated and will be no more trouble for us. We will", "guard him more closely from now on!")
                    stage++
                }

                17 -> {
                    quest!!.setStage(player, 80)
                    interpreter.sendPlainMessage(true, "The room grows dark and you sense objects moving...")
                    cutscene!!.stop(true)
                }
            }

            80, 90, 100 -> when (stage) {
                0 -> player("Yes. Let this be a lesson to you.").also { stage++ }
                1 -> npc("...") .also { stage++ }
                2 -> end()
            }

            else -> when (stage) {
                0 -> npc("What do you want? ...Oh, wait. I know! You're", "probably just like all the others, aren't you? After some", "fancy spell or potion from me, I bet!").also { stage++ }
                1 -> player("No! at least, I don't think so. What sort of spells", "do you have?").also { stage++ }
                2 -> npc("Hah! I knew it! I expect you want my Aphro-Dizzy-", "Yak spell! Want someone to fall madly in love with you,", "eh?").also { stage++ }
                3 -> player("That spell sounds very interesting, but I didn't mean to", "disturb you!").also { stage++ }
                4 -> npc("Well, I see that you do have some manners. I'm glad", "to see that you use them.").also { stage++ }
                5 -> npc("Now, if it's all the same, I am very busy at the", "moment. Come back another time", "please and thank you.") .also { stage++ }
                6 -> player("Yes, of course!").also { stage++ }
                7 -> end()
            }
        }
        return true
    }

    override fun end() {
        if (cutscene != null) {
            return
        }
        super.end()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SUROK_MAGIS_5834, NPCs.SUROK_MAGIS_5835)
    }
}
