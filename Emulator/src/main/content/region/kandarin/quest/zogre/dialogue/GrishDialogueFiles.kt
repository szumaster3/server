package content.region.kandarin.quest.zogre.dialogue

import content.region.kandarin.quest.zogre.handlers.ZUtils
import core.api.*
import core.api.quest.finishQuest
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.BLUE
import core.tools.DARK_BLUE
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Vars

class GrishDialogueFiles : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GRISH_2038)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    Items.DRAGON_INN_TANKARD_4811,
                    "You show the tankard to Grish.",
                ).also { stage++ }
            1 -> player("I found this tankard in the tomb, have you got any", "suggestions?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Das a good drinker for da drinkies dat un is...is a small-un",
                    "for Grish so yous creature keeps it yes. Yous creature keeps da",
                    "fimble drinkers for da smaller drinkies.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class GrishBlackPrismDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GRISH_2038)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    Items.BLACK_PRISM_4808,
                    "You show the black prism to Grish.",
                ).also { stage++ }
            1 -> player("Hey Grish, I found this in the tomb, do you know what", "it is?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Whas you's shuvvin wizzy stuff in Grish face...is a",
                    "pretty one but dat's more stuff for da wizzy's dan Grish.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class GrishTornPageDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GRISH_2038)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    Items.TORN_PAGE_4809,
                    "You show the necromantic page to Grish.",
                ).also { stage++ }
            1 -> player("This torn page was on a lecturn in the tomb, do you know why?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Dat's der wizzy stuff, not Ogery stuffsies like what Grish got. Das",
                    "not even big enough for empty da big blower on! No use",
                    "for Grish dat creatures...you's keeps it.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class GrishFinishDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GRISH_2038)
        when (stage) {
            0 ->
                if (getAttribute(player!!, ZUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                    npcl(FaceAnim.OLD_DEFAULT, "Yous creature got da old fings yet?").also { stage++ }
                } else {
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "Yous creature dun da fing yet? Da zogries going in da ground?",
                    ).also { stage++ }
                }

            1 ->
                if (getAttribute(player!!, ZUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                    sendDialogueOptions(
                        player!!,
                        "Grish asks if you have the items yet.",
                        "Nope, not yet.",
                        "There must be an easier way to kill these zogres!",
                        "There must be a way to cure this disease!",
                        "Sorry, I have to go.",
                    ).also { stage++ }
                } else if (inInventory(player!!, Items.OGRE_ARTEFACT_4818)) {
                    options(
                        "Yeah, I have them here!",
                        "How everything going now?",
                        "I have some other questions for you.",
                        "Sorry, I have to go now.",
                    ).also { stage++ }
                } else {
                    options(
                        "I found who's responsible for the Zogres being here.",
                        "I've got some information on how to kill the zogres from a distance.",
                        "I've found out how to cure the disease.",
                        "I have some other questions for you.",
                        "Sorry, I have to go.",
                    ).also { stage++ }
                }

            2 ->
                if (getAttribute(player!!, ZUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                    when (buttonID) {
                        1 -> player("Nope, not yet.").also { stage++ }
                        2 -> player("There must be an easier way to kill these zogres!").also { stage = 12 }
                        3 -> player("There must be a way to cure this disease!").also { stage = 21 }
                        4 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
                    }
                } else if (inInventory(player!!, Items.OGRE_ARTEFACT_4818)) {
                    when (buttonID) {
                        1 -> playerl("Yeah, I have them here!").also { stage = 22 }
                        2 -> player("How everything going now?").also { stage = 25 }
                        3 -> player("I have some other questions for you.").also { stage = END_DIALOGUE }
                        4 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
                    }
                } else {
                    when (buttonID) {
                        1 -> playerl("I found who's responsible for the Zogres being here.").also { stage++ }
                        2 ->
                            player(
                                "Sithik told me how to make Brutal arrows which means",
                                "I can kill these zogres from a distance!",
                            ).also {
                                stage =
                                    12
                            }
                        3 -> player("There must be a way to cure this disease!").also { stage = 21 }
                        4 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
                    }
                }

            3 ->
                if (getAttribute(player!!, ZUtils.RECEIVED_KEY_FROM_GRISH, false)) {
                    npc(
                        FaceAnim.OLD_DEFAULT,
                        "Yous gets 'em quick tho, cos we'ze wonna do da new Jiggig place...",
                    ).also { stage++ }
                } else {
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "Where is da creature? Me's wants to squeeze him till he's a deadun...",
                    ).also { stage++ }
                }

            4 ->
                playerl(
                    "The person responsible is a wizard named 'Sithik Ints' and he's going to be in serious trouble. He told me that the spell which raised the zogres from the ground will last forever.",
                ).also {
                    stage++
                }
            5 ->
                playerl(
                    "I'm sorry to say, but you'll have to move the site of your ceremonial dancing somewhere else.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Dat is da bad fing creature...we's needs new Jiggig for da fallin' down jig.",
                ).also {
                    stage++
                }
            7 -> playerl("Yes, that's right, you'll need to create a new ceremonial dance area.").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Urghhh...not good fing creature, yous gotta get da ogrish old fings for da making new jiggig special. You's creature needs da key for getting in da low bury place.",
                ).also {
                    stage++
                }
            9 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.OGRE_GATE_KEY_4839,
                    "Grish gives you a crudely crafted key.",
                ).also {
                    sendMessage(player!!, "Grish gives you a crudely crafted key.")
                    setAttribute(player!!, "/save:${ZUtils.RECEIVED_KEY_FROM_GRISH}", true)
                    setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 8)
                    addItem(player!!, Items.OGRE_GATE_KEY_4839)
                    stage++
                }
            10 -> playerl("Oh, so you want me to go back in there and look for something for you?").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Yeah creature, yous gotta get da ogrish old fings for da making new jiggig and proper in da special way.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            12 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Uhggh, whas you's sayin' creature? Yous speakies too stupid for Grish...",
                ).also {
                    stage++
                }
            13 ->
                playerl(
                    "I know how to make large arrows...you know, 'big stabbers', to kill the zogres...they're bigger and apparently do a lot of damage, only thing is, the normal ogre bow I need to fire it is quite slow.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Why you's not say so creature...me's shows you how to",
                    "make da bigger stabber chucker...",
                    "~ ${BLUE}Grish gets a couple of items out of his back pack.</col>~",
                ).also {
                    stage++
                }
            15 ->
                sendDoubleItemDialogue(
                    player!!,
                    Items.ACHEY_TREE_LOGS_2862,
                    Items.WOLF_BONES_2859,
                    "Grish shows you he has Achey tree logs and wolf bones, he starts to whittle away at them both with a knife.",
                ).also {
                    stage++
                }
            16 ->
                sendItemDialogue(
                    player!!,
                    Items.COMP_OGRE_BOW_4827,
                    "Grish shows you his achievement, a rather powerful looking composite bow frame...",
                ).also {
                    stage++
                }
            17 ->
                sendDoubleItemDialogue(
                    player!!,
                    Items.UNSTRUNG_COMP_BOW_4825,
                    Items.BOW_STRING_1777,
                    "He shows you the bow frame and the string and after some time and a great deal of effort, he strings the composite ogre bow.",
                ).also {
                    stage++
                }
            18 ->
                sendItemDialogue(
                    player!!,
                    Items.COMP_OGRE_BOW_4827,
                    "Grish shows you his proud achievement...",
                ).also { stage++ }
            19 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "De're creature...now yous is makin' da bigga stabber",
                    "chucker...",
                ).also { stage++ }
            20 -> player("Thanks! I think....").also { stage = 1 }
            21 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Did yous creature makes da sickies glug glug and putin some wiv Uglug for bright pretties? He's goodun for makin' da glug glugs...yous maken da glug-glug, den sellin' one for Uglug, he's makin' more of da sickies glug glug and sellin' for bright pretties to yous creature...",
                ).also {
                    stage =
                        1
                }
            22 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Dat is da goodly fing yous creature, now's we's can",
                    "make da new Jiggig place away from zogries! Yous",
                    "been da big helpy fing yous creature, Grish wishin' yous",
                    "good stuff for da next fings for creature.",
                ).also {
                    stage++
                }
            23 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "$DARK_BLUE~ Grish seems very pleased about the return of the</col>",
                    "$DARK_BLUE artefacts. ~</col>",
                ).also {
                    removeItem(player!!, Items.OGRE_ARTEFACT_4818)
                    removeItem(player!!, Items.OGRE_GATE_KEY_4839)
                    stage++
                }
            24 ->
                player("Thanks, that's very nice of you!").also {
                    end()
                    finishQuest(player!!, Quests.ZOGRE_FLESH_EATERS)
                }
            25 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "All da zogries stayin' in da olde Jiggig, we's gonna do da new Jiggig someways else. Yous creature da good-un for geddin' da oldie fings...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
