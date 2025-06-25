package content.region.kandarin.feldip.gutanoth.dialogue

import content.region.kandarin.feldip.gutanoth.plugin.BogrogPlugin
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Bogrog dialogue.
 */
@Initializable
class BogrogDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.CHILD_NORMAL, "Hello da yooman, you tell bogrog what trade to", "shardies.").also { stage++ }
            1 -> options("So, what's Summoning all about, then?", "Can I buy some summoning supplies?", "Are you interested in buying pouch pouches or scrolls?").also { stage = 10 }

            // Summoning.
            10 -> when (buttonId) {
                1 -> npc(FaceAnim.CHILD_NORMAL, "Whatchoo talkin' about, yooman?").also { stage = 11 }
                2 -> player(FaceAnim.HALF_ASKING,"Can I buy some summoning supplies?").also { stage = 59 }
                3 -> player(FaceAnim.HALF_ASKING,"Are you interested in buying pouch pouches or scrolls?").also { stage = 61 }
            }
            11 -> player(FaceAnim.THINKING, "Yes, I think so.").also { stage++ }
            12 -> npcl(FaceAnim.CHILD_NORMAL , "I gots da pouches and spirit shards, but no charms. Yooman gotta get dem himself. Yooman just gets a club and yooman goes whackin' things till monsters drop charms.").also { stage++ }
            13 -> npcl(FaceAnim.CHILD_NORMAL, "Der'es loads of things that gots charms. Dere is loads of charms all over da place, gold, blue, green, crimson...even some real rare ones like obsidy-un charms as well.").also { stage++ }
            14 -> playerl(FaceAnim.NEUTRAL, "I'll be sure to keep my eyes open for them.").also { stage++ }
            15 -> npcl(FaceAnim.CHILD_NORMAL, "If yooman wants to learn Summonin' good, den yooman needs to make da Summonin' pouches. Yooman also gets learnin' from da Summonin' and da scrolls and da splittin' Summonin' pouches.").also { stage++ }
            16 -> npcl(FaceAnim.CHILD_NORMAL, "Da Summonin' pouches is da bestest way of learnin' of them all. Yooman got it, or yooman stoopid?").also { stage++ }
            17 -> player(FaceAnim.HAPPY, "Sure!").also { stage++ }
            18 -> npcl(FaceAnim.CHILD_NORMAL, "Hah! Yooman is smart for a little ting! Da last ting is da pets. Da more summony you is, da more you understand der birds and der bees and der rocks and stuff.").also { stage++ }
            19 -> npcl(FaceAnim.CHILD_NORMAL, "Der bigger yooman's summonyness is, da stronger der pets yooman will be able to keep. Real big summoners gets to raise animals like lizard tings, including dragons!").also { stage++ }

            20 -> npcl(FaceAnim.CHILD_NORMAL, "Now if yooman's brains isn't gonna pop, does it wanna know about somefin' else?").also { stage++ }
            21 -> options("Tell me about Summoning familiars.", "Tell me about special moves.", "Tell me about pets.").also { stage++ }

            // Familiars.
            22 -> when (buttonId) {
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Summoned familiars are da main ting in Summonin', as if yooman didn't be guessing. There's all kinds of tings yooman can summon. Der bigger der yooman's summonyness, der bigger der tings yooman can summon.").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "If you cuts up one of dem Summonin' pouches, over at der obelisk, den da energy gets mushed up real good, transformin' into a big stack of scrolls.").also { stage = 35 }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Der petties? Well dey's not real summony stuffs, but if yooman trains hard, then yooman gets to be friends wiv dem. Der summonier der yooman gets, der more like nature der yooman gets, so da little petties like yooman better.").also { stage = 47 }
            }
            23 -> npcl(FaceAnim.CHILD_NORMAL, "Ting is, dem familiars not really, REALLY animals, if yer get me. Dey are really REALLY spirits dat are stronger than der real animal, wiv all kinds of powaz dat der rest don't.").also { stage++ }
            24 -> npcl(FaceAnim.CHILD_NORMAL, "Dat's why dey are not runnin' around all da time - it's cos der summoner puts dere Summonin' skill points inta summonin' em, and keeping em here.").also { stage++ }
            25 -> playerl(FaceAnim.HALF_ASKING, "So Summoning skill points are like food to dem - sorry, them?").also { stage++ }
            26 -> npcl(FaceAnim.CHILD_NORMAL, "Yah! Dat's right! Der bigger der ting, der more ya gotta feed it, and der more often it gets hungry.").also { stage++ }
            27 -> npcl(FaceAnim.CHILD_NORMAL, "Dat's why only der big summony types, like me, have enough Summonin' skill points for der biggest familiars. And der bigger der yooman's Summonin' level, der easier it is to be feedin' der littler tings for longer.").also { stage++ }
            28 -> playerl(FaceAnim.HALF_GUILTY, "I'm starting to get a little hungry now.").also { stage++ }
            29 -> npcl(FaceAnim.CHILD_NORMAL, "It's like if yooman be holdin' bags of wheat. Der bigger der bags yooman be holdin', der bigger der yooman's arms get, so de bigger bags dey can hold next time. If da youman feeds a little ting den dey will have lots of wheat left over too!").also { stage++ }
            30 -> playerl(FaceAnim.HALF_THINKING, "Great! So, what can these familiars do?").also { stage++ }
            31 -> npcl(FaceAnim.CHILD_NORMAL, "What can dey not do, more like? Dere is a load of dem - tons, lots and lots - and each of dem is not like der uvvers.").also { stage++ }
            32 -> playerl(FaceAnim.HALF_ASKING, "Well, can you give me some hints, then?").also { stage++ }
            33 -> npcl(FaceAnim.CHILD_NORMAL, "Well, Bogrog only calls on der ones dat fight! Dey hang around until Bogrog be fightin' and den dey leap in! I suppose yooman like you will be wantin' the ones that just make yooman more skilful, or dem ones dat lug around tings dat yooman's arms too puny to lift.").also { stage++ }
            34 -> playerl(FaceAnim.AMAZED, "Amazing!").also { stage = 21 }

            // Special Moves.
            35 -> npcl(FaceAnim.CHILD_NORMAL, "Dese scrolls can den be used to make da familiars do a speshial move! Hur, hur, hur!").also { stage++ }
            36 -> npcl(FaceAnim.CHILD_NORMAL, "Der spirit wolfies, fer example, dey can make little tings run away real good if dey perform dere Howl speshial move. Or longer, in the case of that giant wolpertinger!").also { stage++ }
            37 -> npcl(FaceAnim.CHILD_NORMAL, "Hur hur hur, me hearing about dat from da spirities! Yooman can't mix up der scrolls, though - spirit wolfies get real growly if ya try to make 'em perform dreadfowl speshial move!").also { stage++ }
            38 -> playerl(FaceAnim.HALF_ASKING,"So, what sort of special moves are there?").also { stage++ }
            39 -> npcl(FaceAnim.CHILD_NORMAL, "Dere is lots of dem - like da familiars!").also { stage++ }
            40 -> npcl(FaceAnim.CHILD_NORMAL, "If yooman's confoosed, den just think of dis; the attackin' familiars got a lot more of der attackin' speshial moves, and der soppy, non-attackin' ones gots da non-attackin' speshial moves.").also { stage++ }
            41 -> npcl(FaceAnim.CHILD_NORMAL, "Yooman will want da non-attackin' ones to heal dem, or get dem stuff, or make dem better at uvver tings.").also { stage++ }
            42 -> playerl(FaceAnim.HALF_ASKING,"Are the special moves and the familiar's normal abilities similar?").also { stage++ }
            43 -> npcl(FaceAnim.CHILD_NORMAL, "Naah, dem's mostly diff'rent. Da spirit wolfies' are sim'lar though. Dey's got da Howl speshial move, but dey's also got a howly normal ability.").also { stage++ }
            44 -> npcl(FaceAnim.CHILD_NORMAL, "You can get der spirit wolfy to use Howl on anyt'ing you see, but it can only uses its howly normal ability on tings you is fightin'.").also { stage++ }
            45 -> npcl(FaceAnim.CHILD_NORMAL, "If der familiar with the normal ability is fighting with a yooman, den it makes der normal ability when it sees an openin'. Der speshial moves though - dey never use dem unless yooman tells 'em to wiv a scroll.").also { stage++ }
            46 -> player(FaceAnim.HAPPY, "I see. Thanks for the information!").also { stage = 21 }

            // Pets.
            47 -> npcl(FaceAnim.CHILD_NORMAL, "So, der petties dat would be runnin' away don't, and even der nasty petties get real quiet too. An' when dey not runnin' or bitin', den da yooman gets ta feed 'em and pet 'em and keep 'em.").also { stage++ }
            48 -> player(FaceAnim.HALF_ASKING, "So what will I need to do to raise the animals?").also { stage++ }
            49 -> npcl(FaceAnim.CHILD_NORMAL, "Dey just walk behind da yooman, so yooman just gives dem food, more food and good foods. Da bestest summoners wants to pet themse'ves a dragon!").also { stage++ }
            50 -> player(FaceAnim.HAPPY, "Wow! Imagine riding around on a dragon, and fighting with it!").also { stage++ }
            51 -> npcl(FaceAnim.CHILD_NORMAL, "Yooman is too stoopid.").also { stage++ }
            52 -> player(FaceAnim.ASKING,"What?").also { stage++ }
            53 -> npcl(FaceAnim.CHILD_NORMAL, "One, you too puny to make a dragon do as you sez. Two, when yooman gets a pet, yooman remembers it's still alive, an' yooman's friend.").also { stage++ }
            54 -> npcl(FaceAnim.CHILD_NORMAL, "Pets are not like der spirity familiars - dey die if you kills dem! What friend pokes his friend in der head and says 'We go fight now. I gets on your back and you fights for me'?").also { stage++ }
            55 -> player("I didn't realise.").also { stage++ }
            56 -> npcl(FaceAnim.CHILD_NORMAL, "Dat's 'cos yooman's stoopid. Petties not toys or clubs, or ridy-tings. Dey is livin' tings dat yooman be raisin' from birthyness. If you a bad, petty yooman, I club you!").also { stage++ }
            57 -> player("I'll take good care of them.").also { stage++ }
            58 -> npcl(FaceAnim.CHILD_NORMAL, "Now, if yooman's brains isn't gonna pop, does it wanna know about somefin' else?").also { stage = 21 }

            // Supplies shop.
            59 -> npcl(FaceAnim.CHILD_NORMAL, "Hur, hur, hur! Yooman's gotta buy lotsa stuff if yooman wants ta train good!").also { stage++ }
            60 -> end().also { openNpcShop(player!!, NPCs.BOGROG_4472) }

            // Swap.
            61 -> npcl(FaceAnim.CHILD_NORMAL, "Des other ogres's stealin' Bogrog's stock. Gimmie pouches and scrolls and yooman gets da shardies.").also { stage++ }
            62 -> end().also { BogrogPlugin.openSwap(player!!) }
        }
        return true
    }
    override fun newInstance(player: Player?): Dialogue = BogrogDialogue(player)


    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOGROG_4472)
    }
}