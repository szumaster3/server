package content.region.kandarin.feldip.jiggig.quest.zogre.dialogue

import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.BLUE
import core.tools.DARK_BLUE
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Grish dialogue.
 */
@Initializable
class GrishDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GrishDialogueFile(), npc)
        return false
    }

    override fun newInstance(player: Player?): Dialogue = GrishDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.GRISH_2038)
}

private class GrishDialogueFile : DialogueFile() {

    companion object {
        private const val DEFAULT = 1
    }

    override fun handle(componentID: Int, buttonID: Int) {
        val p = player!!
        npc = NPC(NPCs.GRISH_2038)

        val questComplete = isQuestComplete(p, Quests.ZOGRE_FLESH_EATERS) || getVarbit(p, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13
        val hasTankard = inInventory(p, Items.DRAGON_INN_TANKARD_4811)
        val hasTornPage = inInventory(p, Items.TORN_PAGE_4809)
        val hasBlackPrism = inInventory(p, Items.BLACK_PRISM_4808)
        val hasArtefact = inInventory(p, Items.OGRE_ARTEFACT_4818)
        val talkedWithSithik = getAttribute(p, ZogreUtils.TALK_WITH_SITHIK_OGRE_DONE, false)

        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.HALF_ASKING, "What can I get you?").also { stage = DEFAULT }
            DEFAULT -> return when {
                questComplete -> stage = 10
                hasTankard -> stage = 39
                hasTornPage -> stage = 42
                hasBlackPrism -> stage = 45
                hasArtefact || talkedWithSithik -> stage = 48
                else -> stage = 11
            }
            10 -> npcl(FaceAnim.OLD_DEFAULT, "All da zogries stayin' in da olde Jiggig, we's gonna do da new Jiggig someways else. Yous creature da good-un for geddin' da oldie fings...").also { stage = END_DIALOGUE }
            11 -> npc(FaceAnim.OLD_DEFAULT, "Hey yous creature...wha's you's doing here? Yous be", "cleverer to be running so da sickies from da zogres", "don't dead ya.").also { stage++ }
            12 -> options("I'm just looking around thanks.", "What do you mean sickies?", "What are Zogres?", "Sorry, I have to go.").also { stage++ }
            13 -> when (buttonID) {
                1 -> player("I'm just looking around thanks.").also { stage = 35 }
                2 -> player("What do you mean sickies?").also { stage++ }
                3 -> player("What are Zogres?").also { stage = 17 }
                4 -> player("Sorry, I have to go.").also { stage = 35 }
            }
            14 -> npc(FaceAnim.OLD_DEFAULT, "Da zogries comin wiv da sickies...yous get bashed by da", "zogries and get da sickies...den you gonna be like da", "zogries.").also { stage++ }
            15 -> player("Sorry, I just don't understand...").also { stage++ }
            16 -> npc(FaceAnim.OLD_DEFAULT, "Da sickies is when yous creature goes like orange till", "green and then goes 'Urggghhhh!'", "$BLUE~ Grish imitates falling down with only the white of his", "$BLUE eyes visible. ~").also { stage++ }
            17 -> options("I'm just looking around thanks.", "What do you mean sickies?", "What are Zogres?", "Can I help in any way?", "Sorry, I have to go.").also { stage++ }
            18 -> when (buttonID) {
                1 -> player("I'm just looking around thanks.").also { stage = 35 }
                2 -> player("What do you mean sickies?").also { stage = 14 }
                3 -> player("What are Zogres?").also { stage = 19 }
                4 -> player("Can I help in any way?").also { stage = 21 }
                5 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
            }
            19 -> npc(FaceAnim.OLD_DEFAULT, "Da Zogres are da bigun nasties wiv da sickies, deys old", "pals of Grish but deys jig in Jiggig when dey's full", "home is deep in da dirt, dey's is not da same dead'uns", "like was before.").also { stage++ }
            20 -> npc(FaceAnim.OLD_DEFAULT, "Dem zogries commin from da under dirt and us is lost", "for da Jiggie jig place.").also { stage++ }
            21 -> npc(FaceAnim.OLD_DEFAULT, "Yes creatures...yous does good fings for Grish and", "learn why Zogries at Jiggig and den get da Zogries", "back in da ground.").also { stage++ }
            22 -> player("Oh, so you want me to find out why the Zogres have", "appeared and then find a way of burying them?").also { stage++ }
            23 -> npc(FaceAnim.OLD_DEFAULT, "Is what Grish says! But dis is da biggy danger fing", "yous creatures...yous be geddin' sickies most", "surely...yous needs be ready..wiv da foodies un da", "glug-glugs.").also { stage++ }
            24 -> player("Right, so you think there's a good chance that I can", "get ill from this, so I need to get some food and", "something to drink?").also { stage++ }
            25 -> npc(FaceAnim.OLD_DEFAULT, "Yea creatures, yous just say what Grish says...not know", "own wordies creature?").also { stage++ }
            26 -> options("Hmm, sorry, it sounds a bit too dangerous.", "Ok, I'll check things out then and report back.").also { stage++ }
            27 -> when (buttonID) {
                1 -> player("Hmm, sorry, it sounds a bit too dangerous.").also { stage = 36 }
                2 -> player("Ok, I'll check things out then and report back.").also { stage++ }
            }
            28 -> npc(FaceAnim.OLD_DEFAULT, "Is yous creatures really, really sure yous wanna do dis", "creatures..we's got no glug-glugs for da sickies? We's", "knows nuffin for da going of da sickies?").also { stage++ }
            29 -> options("Yes, I'm really sure!", "Hmm, sorry, it sounds a bit too dangerous.").also { stage++ }
            30 -> when (buttonID) {
                1 -> player("Yes, I'm really sure!").also { stage++ }
                2 -> player("Hmm, sorry, it sounds a bit too dangerous.").also { stage = 36 }
            }
            31 -> npc(FaceAnim.OLD_DEFAULT, "Dats da good fing yous creature...yous does Grish a", "good fing. But yous know dat yous get sickies and", "mebe get dead!").also { stage++ }
            32 -> player("If that's your idea of a pep talk, I have to say that it", "leaves a lot to be desired.").also { stage++ }
            33 -> npc(FaceAnim.OLD_DEFAULT, "Yous creatures is alus says funny stuff...speaks proper", "like Grish!").also {
                sendDoubleItemDialogue(player!!, Items.COOKED_CHOMPY_7228, Items.SUPER_RESTORE3_3026, "Grish hands you some food and two potions.")
                if (getVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 0) {
                    setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 1, true)
                    addItemOrDrop(player!!, Items.SUPER_RESTORE3_3026, 2)
                    addItemOrDrop(player!!, Items.COOKED_CHOMPY_2878, 3)
                }
                stage++
            }
            34 -> npc(FaceAnim.OLD_DEFAULT, "Der's yous go creatures...da best me's do for", "yous...and be back wivout da sickies.").also { stage = END_DIALOGUE }
            35 -> npc(FaceAnim.OLD_DEFAULT, "Yous creature won'ts see muchly in dis place...just", "da zogries coming wiv da sickies.").also { stage = END_DIALOGUE }
            36 -> npc(FaceAnim.OLD_DEFAULT, "Yous creature is not a stoopid one...stays out of dere, like", "clever Grish. Yous can paint circles on chest and", "be da Shaman too!").also { stage++ }
            37 -> player("Hmm, is it too late to reconsider?").also { stage = END_DIALOGUE }
            38 -> npcl(FaceAnim.OLD_DEFAULT, "All da zogries stayin' in da olde Jiggig, we's gonna do da new Jiggig someways else. Yous creature da good-un for geddin' da oldie fings...").also { stage = END_DIALOGUE }

            39 -> sendItemDialogue(player!!, Items.DRAGON_INN_TANKARD_4811, "You show the tankard to Grish.").also { stage++ }
            40 -> player("I found this tankard in the tomb, have you got any", "suggestions?").also { stage++ }
            41 -> npc(FaceAnim.OLD_DEFAULT, "Das a good drinker for da drinkies dat un is...is a small-un", "for Grish so yous creature keeps it yes. Yous creature keeps da", "fimble drinkers for da smaller drinkies.").also { stage = END_DIALOGUE }

            42 -> sendItemDialogue(player!!, Items.TORN_PAGE_4809, "You show the necromantic page to Grish.").also { stage++ }
            43 -> player("This torn page was on a lecturn in the tomb, do you know why?").also { stage++ }
            44 -> npc(FaceAnim.OLD_DEFAULT, "Dat's der wizzy stuff, not Ogery stuffsies like what Grish got. Das", "not even big enough for empty da big blower on! No use", "for Grish dat creatures...you's keeps it.").also { stage = END_DIALOGUE }

            45 -> sendItemDialogue(player!!, Items.BLACK_PRISM_4808, "You show the black prism to Grish.").also { stage++ }
            46 -> player("Hey Grish, I found this in the tomb, do you know what", "it is?").also { stage++ }
            47 -> npc(FaceAnim.OLD_DEFAULT, "Whas you's shuvvin wizzy stuff in Grish face...is a", "pretty one but dat's more stuff for da wizzy's dan Grish.").also { stage = END_DIALOGUE }

            48 -> if (getAttribute(player!!, ZogreUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                npcl(FaceAnim.OLD_DEFAULT, "Yous creature got da old fings yet?").also { stage++ }
            } else {
                npcl(FaceAnim.OLD_DEFAULT, "Yous creature dun da fing yet? Da zogries going in da ground?").also { stage++ }
            }
            49 -> if (getAttribute(player!!, ZogreUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                sendOptions(player!!, "Grish asks if you have the items yet.", "Nope, not yet.", "There must be an easier way to kill these zogres!", "There must be a way to cure this disease!", "Sorry, I have to go.").also { stage++ }
            } else if (inInventory(player!!, Items.OGRE_ARTEFACT_4818)) {
                options("Yeah, I have them here!", "How everything going now?", "I have some other questions for you.", "Sorry, I have to go now.").also { stage++ }
            } else {
                options("I found who's responsible for the Zogres being here.", "I've got some information on how to kill the zogres from a distance.", "I've found out how to cure the disease.", "I have some other questions for you.", "Sorry, I have to go.").also { stage++ }
            }

            50 -> if (getAttribute(player!!, ZogreUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                when (buttonID) {
                    1 -> player("Nope, not yet.").also { stage++ }
                    2 -> player("There must be an easier way to kill these zogres!").also { stage = 60 }
                    3 -> player("There must be a way to cure this disease!").also { stage = 69 }
                    4 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            } else if (inInventory(player!!, Items.OGRE_ARTEFACT_4818)) {
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY,"Yeah, I have them here!").also { stage = 70 }
                    2 -> player("How everything going now?").also { stage = 73 }
                    3 -> player("I have some other questions for you.").also { stage = END_DIALOGUE }
                    4 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            } else {
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY,"I found who's responsible for the Zogres being here.").also { stage++ }
                    2 -> player("Sithik told me how to make Brutal arrows which means", "I can kill these zogres from a distance!").also { stage = 60 }
                    3 -> player("There must be a way to cure this disease!").also { stage = 69 }
                    4 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            }
            51 -> if (getAttribute(player!!, ZogreUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                npc(FaceAnim.OLD_DEFAULT, "Yous gets 'em quick tho, cos we'ze wonna do da new Jiggig place...").also { stage++ }
            } else {
                npcl(FaceAnim.OLD_DEFAULT, "Where is da creature? Me's wants to squeeze him till he's a deadun...").also { stage++ }
            }
            52 -> playerl(FaceAnim.FRIENDLY,"The person responsible is a wizard named 'Sithik Ints' and he's going to be in serious trouble. He told me that the spell which raised the zogres from the ground will last forever.").also { stage++ }
            53 -> playerl(FaceAnim.FRIENDLY,"I'm sorry to say, but you'll have to move the site of your ceremonial dancing somewhere else.").also { stage++ }
            54 -> npcl(FaceAnim.OLD_DEFAULT, "Dat is da bad fing creature...we's needs new Jiggig for da fallin' down jig.").also { stage++ }
            55 -> playerl(FaceAnim.FRIENDLY,"Yes, that's right, you'll need to create a new ceremonial dance area.").also { stage++ }
            56 -> npcl(FaceAnim.OLD_DEFAULT, "Urghhh...not good fing creature, yous gotta get da ogrish old fings for da making new jiggig special. You's creature needs da key for getting in da low bury place.").also { stage++ }
            57 -> sendDoubleItemDialogue(player!!, -1, Items.OGRE_GATE_KEY_4839, "Grish gives you a crudely crafted key.").also {
                sendMessage(player!!, "Grish gives you a crudely crafted key.")
                setAttribute(player!!, ZogreUtils.RECEIVED_KEY_FROM_GRISH, true)
                setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 8, true)
                addItem(player!!, Items.OGRE_GATE_KEY_4839)
                stage++
            }
            58 -> playerl(FaceAnim.FRIENDLY, "Oh, so you want me to go back in there and look for something for you?").also { stage++ }
            59 -> npcl(FaceAnim.OLD_DEFAULT, "Yeah creature, yous gotta get da ogrish old fings for da making new jiggig and proper in da special way.").also { stage = END_DIALOGUE }
            60 -> npcl(FaceAnim.OLD_DEFAULT, "Uhggh, whas you's sayin' creature? Yous speakies too stupid for Grish...").also { stage++ }
            61 -> playerl(FaceAnim.FRIENDLY,"I know how to make large arrows...you know, 'big stabbers', to kill the zogres...they're bigger and apparently do a lot of damage, only thing is, the normal ogre bow I need to fire it is quite slow.").also { stage++ }
            62 -> npc(FaceAnim.OLD_DEFAULT, "Why you's not say so creature...me's shows you how to", "make da bigger stabber chucker...", "~ ${core.tools.BLUE}Grish gets a couple of items out of his back pack.</col>~").also { stage++ }
            63 -> sendDoubleItemDialogue(player!!, Items.ACHEY_TREE_LOGS_2862, Items.WOLF_BONES_2859, "Grish shows you he has Achey tree logs and wolf bones, he starts to whittle away at them both with a knife.").also { stage++ }
            64 -> sendItemDialogue(player!!, Items.COMP_OGRE_BOW_4827, "Grish shows you his achievement, a rather powerful looking composite bow frame...").also { stage++ }
            65 -> sendDoubleItemDialogue(player!!, Items.UNSTRUNG_COMP_BOW_4825, Items.BOW_STRING_1777, "He shows you the bow frame and the string and after some time and a great deal of effort, he strings the composite ogre bow.").also { stage++ }
            66 -> sendItemDialogue(player!!, Items.COMP_OGRE_BOW_4827, "Grish shows you his proud achievement...").also { stage++ }
            67 -> npc(FaceAnim.OLD_DEFAULT, "De're creature...now yous is makin' da bigga stabber", "chucker...").also { stage++ }
            68 -> player("Thanks! I think....").also { stage = 49 }
            69 -> npcl(FaceAnim.OLD_DEFAULT, "Did yous creature makes da sickies glug glug and putin some wiv Uglug for bright pretties? He's goodun for makin' da glug glugs...yous maken da glug-glug, den sellin' one for Uglug, he's makin' more of da sickies glug glug and sellin' for bright pretties to yous creature...").also { stage = 49 }
            70 -> npc(FaceAnim.OLD_DEFAULT, "Dat is da goodly fing yous creature, now's we's can", "make da new Jiggig place away from zogries! Yous", "been da big helpy fing yous creature, Grish wishin' yous", "good stuff for da next fings for creature.").also { stage++ }
            71 -> npc(FaceAnim.OLD_DEFAULT, "$DARK_BLUE~ Grish seems very pleased about the return of the</col>", "$DARK_BLUE artefacts. ~</col>").also {
                removeItem(player!!, Items.OGRE_ARTEFACT_4818)
                removeItem(player!!, Items.OGRE_GATE_KEY_4839)
                stage++
            }
            72 -> player("Thanks, that's very nice of you!").also {
                end()
                finishQuest(player!!, Quests.ZOGRE_FLESH_EATERS)
            }
            73 -> npcl(FaceAnim.OLD_DEFAULT, "All da zogries stayin' in da olde Jiggig, we's gonna do da new Jiggig someways else. Yous creature da good-un for geddin' da oldie fings...").also { stage = END_DIALOGUE }

        }
    }
}