package content.region.kandarin.dialogue.jiggig

import content.region.kandarin.quest.zogre.dialogue.GrishFinishDialogue
import content.region.kandarin.quest.zogre.handlers.ZUtils
import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class GrishDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getAttribute(player, ZUtils.TALK_WITH_SITHIK_OGRE_DONE, false) ||
            inInventory(player, Items.OGRE_ARTEFACT_4818)
        ) {
            openDialogue(player, GrishFinishDialogue())
            return true
        }
        if (isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS)) {
            player("How's everything going now?")
            stage = 27
            return true
        }
        player("Hello there, what's going on here?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Hey yous creature...wha's you's doing here? Yous be",
                    "cleverer to be running so da sickies from da zogres",
                    "don't dead ya.",
                ).also {
                    stage++
                }
            1 ->
                options(
                    "I'm just looking around thanks.",
                    "What do you mean sickies?",
                    "What are Zogres?",
                    "Sorry, I have to go.",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 -> player("I'm just looking around thanks.").also { stage = 24 }
                    2 -> player("What do you mean sickies?").also { stage++ }
                    3 -> player("What are Zogres?").also { stage = 6 }
                    4 -> player("Sorry, I have to go.").also { stage = 24 }
                }
            3 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Da zogries comin wiv da sickies...yous get bashed by da",
                    "zogries and get da sickies...den you gonna be like da",
                    "zogries.",
                ).also {
                    stage++
                }
            4 -> player("Sorry, I just don't understand...").also { stage++ }
            5 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Da sickies is when yous creature goes like orange till",
                    "green and then goes 'Urggghhhh!'",
                    "$BLUE~ Grish imitates falling down with only the white of his",
                    "$BLUE eyes visible. ~",
                ).also {
                    stage++
                }
            6 ->
                options(
                    "I'm just looking around thanks.",
                    "What do you mean sickies?",
                    "What are Zogres?",
                    "Can I help in any way?",
                    "Sorry, I have to go.",
                ).also {
                    stage++
                }
            7 ->
                when (buttonId) {
                    1 -> player("I'm just looking around thanks.").also { stage = 24 }
                    2 -> player("What do you mean sickies?").also { stage = 3 }
                    3 -> player("What are Zogres?").also { stage = 8 }
                    4 -> player("Can I help in any way?").also { stage = 10 }
                    5 -> player("Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            8 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Da Zogres are da bigun nasties wiv da sickies, deys old",
                    "pals of Grish but deys jig in Jiggig when dey's full",
                    "home is deep in da dirt, dey's is not da same dead'uns",
                    "like was before.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Dem zogries commin from da under dirt and us is lost",
                    "for da Jiggie jig place.",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Yes creatures...yous does good fings for Grish and",
                    "learn why Zogries at Jiggig and den get da Zogries",
                    "back in da ground.",
                ).also {
                    stage++
                }
            11 ->
                player(
                    "Oh, so you want me to find out why the Zogres have",
                    "appeared and then find a way of burying them?",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Is what Grish says! But dis is da biggy danger fing",
                    "yous creatures...yous be geddin' sickies most",
                    "surely...yous needs be ready..wiv da foodies un da",
                    "glug-glugs.",
                ).also {
                    stage++
                }
            13 ->
                player(
                    "Right, so you think there's a good chance that I can",
                    "get ill from this, so I need to get some food and",
                    "something to drink?",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Yea creatures, yous just say what Grish says...not know",
                    "own wordies creature?",
                ).also {
                    stage++
                }
            15 ->
                options(
                    "Hmm, sorry, it sounds a bit too dangerous.",
                    "Ok, I'll check things out then and report back.",
                ).also {
                    stage++
                }
            16 ->
                when (buttonId) {
                    1 -> player("Hmm, sorry, it sounds a bit too dangerous.").also { stage = 25 }
                    2 -> player("Ok, I'll check things out then and report back.").also { stage++ }
                }
            17 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Is yous creatures really, really sure yous wanna do dis",
                    "creatures..we's got no glug-glugs for da sickies? We's",
                    "knows nuffin for da going of da sickies?",
                ).also {
                    stage++
                }
            18 -> options("Yes, I'm really sure!", "Hmm, sorry, it sounds a bit too dangerous.").also { stage++ }
            19 ->
                when (buttonId) {
                    1 -> player("Yes, I'm really sure!").also { stage++ }
                    2 -> player("Hmm, sorry, it sounds a bit too dangerous.").also { stage = 25 }
                }
            20 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Dats da good fing yous creature...yous does Grish a",
                    "good fing. But yous know dat yous get sickies and",
                    "mebe get dead!",
                ).also {
                    stage++
                }
            21 ->
                player(
                    "If that's your idea of a pep talk, I have to say that it",
                    "leaves a lot to be desired.",
                ).also { stage++ }
            22 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Yous creatures is alus says funny stuff...speaks proper",
                    "like Grish!",
                ).also {
                    sendDoubleItemDialogue(
                        player,
                        Items.COOKED_CHOMPY_7228,
                        Items.SUPER_RESTORE3_3026,
                        "Grish hands you some food and two potions.",
                    )
                    if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 0) {
                        setVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 1, true)
                        addItemOrDrop(player, Items.SUPER_RESTORE3_3026, 2)
                        addItemOrDrop(player, Items.COOKED_CHOMPY_2878, 3)
                    }
                    stage++
                }
            23 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Der's yous go creatures...da best me's do for",
                    "yous...and be back wivout da sickies.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            24 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Yous creature won'ts see muchly in dis place...just",
                    "da zogries coming wiv da sickies.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            25 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Yous creature is not a stoopid one...stays out of dere, like",
                    "clever Grish. Yous can paint circles on chest and",
                    "be da Shaman too!",
                ).also {
                    stage++
                }
            26 -> player("Hmm, is it too late to reconsider?").also { stage = END_DIALOGUE }
            27 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "All da zogries stayin' in da olde Jiggig, we's gonna do da new Jiggig someways else. Yous creature da good-un for geddin' da oldie fings...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GrishDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRISH_2038)
    }
}
