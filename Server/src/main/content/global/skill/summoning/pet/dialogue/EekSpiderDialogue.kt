package content.global.skill.summoning.pet.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
/*
@Initializable
class EekSpiderDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "I bet you're a mighty hero!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HAPPY, "Player! Hero-of-the-humans! You are a mighty hero, aren't you? I bet you've slain dragons and all sorts of stuff.").also { stage = 1 }
            1 -> options("That's right. I'm awesome!", "I'm not a great hero. I'm just an ordinary person.").also { stage = 2 }
            2 -> when (buttonId) {
                1 -> { player(FaceAnim.HAPPY, "That's right. I'm awesome!"); stage = 3 }
                2 -> { player(FaceAnim.CALM, "I'm not a great hero. I'm just an ordinary person."); stage = 6 }
            }
            3 -> npc(FaceAnim.HAPPY, "I could be your sidekick! Player and Eek! We could have all sorts of adventures together!").also { stage = 4 }
            4 -> options("Yes!", "No. It's too dangerous.").also { stage = 5 }
            5 -> when (buttonId) {
                1 -> { player(FaceAnim.HAPPY, "Yes!"); npc(FaceAnim.HAPPY, "Yay! Player and Eek! We'll travel all over the world fighting for justice! And freedom! And things like that! Come on, Player! Let's have an adventure!"); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.SAD, "No. It's too dangerous."); npc(FaceAnim.SAD, "Aww. You know best, though. You're so BRAVE putting yourself in danger all by yourself!"); stage = END_DIALOGUE }
            }
            6 -> npc(FaceAnim.SAD, "Aww. I bet you'll be a mighty hero someday, though.").also { stage = 7 }
            7 -> options("Yes, I'm trying to advance myself.", "I don't know. I'm not very ambitious.").also { stage = 8 }
            8 -> when (buttonId) {
                1 -> { player(FaceAnim.HAPPY, "Yes, I'm trying to advance myself."); npc(FaceAnim.HAPPY, "Brilliant! You can do it! Come on, let's have an adventure!"); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.SAD, "I don't know. I'm not very ambitious."); npc(FaceAnim.SAD, "Aww...well...never mind."); stage = END_DIALOGUE }
            }

            10 -> npc(FaceAnim.CHILD_NORMAL, "Hey, you've only got four legs. How do you manage? Don't you fall over?").also { stage = 11 }
            11 -> options("Actually, I've only got two legs.", "Oh, it's not so bad.").also { stage = 12 }
            12 -> when (buttonId) {
                1 -> { player(FaceAnim.CALM, "Actually, I've only got two legs."); npc(FaceAnim.CHILD_NORMAL, "Someone has stolen your legs! This is a DISASTER! We've got to catch the leg thief!"); stage = 13 }
                2 -> { player(FaceAnim.CALM, "Oh, it's not so bad."); npc(FaceAnim.HAPPY, "Then again, your legs are much longer than mine. How's you get such long legs? Do you have some kind of leg-extending machine? Where is the leg-extending machine? I want to use it!"); stage = 20 }
            }
            13 -> options("No one stole my legs.", "They're probably long gone by now.").also { stage = 14 }
            14 -> when (buttonId) {
                1 -> { player(FaceAnim.CALM, "No one stole my legs."); npc(FaceAnim.HAPPY, "You gave your legs away? That is so heroic...giving your legs away to someone without legs. You're my hero!"); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.CALM, "They're probably long gone by now."); npc(FaceAnim.HAPPY, "You're right. They probably used the machine to give themselves really long running-away type legs. We'll never catch them now."); stage = END_DIALOGUE }
            }
            20 -> options("There is no leg-extending machine.", "You can't use it. It's broken.").also { stage = 21 }
            21 -> when (buttonId) {
                1 -> { player(FaceAnim.CALM, "There is no leg-extending machine."); npc(FaceAnim.HAPPY, "You would say that! You want to keep it for yourself! Well that's okay. Maybe I don't even need your leg-extending machine! I can do special leg-stretching exercises! Make my legs longer naturally! No artificial legs for me! You just wait! Soon I'll have the best legs ever!"); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.CALM, "You can't use it. It's broken."); npc(FaceAnim.SAD, "Oh! I wouldn't want to use it if it's broken. It might malfunction and give me tiny, tiny legs! Like a slug, only with tiny legs! Or it might make some legs longer than others so I wouldn't be able to walk and I'd fall over! That would be TERRIBLE! I'd better keep away from that machine!"); stage = END_DIALOGUE }
            }

            // Conversation 3
            30 -> npc(FaceAnim.CALM, "Hey, are you scared of spiders?").also { stage = 31 }
            31 -> options("Yes, I'm scared of spiders.", "No, I'm not scared of spiders.").also { stage = 32 }
            32 -> when (buttonId) {
                1 -> { player(FaceAnim.SAD, "Yes, I'm scared of spiders."); npc(FaceAnim.SAD, "Oh no! I'm sorry! I've got to try not to be scary. You should just pretend I'm not a spider! Like, maybe I'm an eight-legged mouse!"); stage = 33 }
                2 -> { player(FaceAnim.HAPPY, "No, I'm not scared of spiders."); npc(FaceAnim.HAPPY, "Not even a bit? I bet I can make you scared of spiders! Boo! Raaar! I'm a scary spider! Next time you go to sleep I'm going to crawl on your face and EAT YOUR EYEBALLS!"); stage = 36 }
            }
            33 -> npc(FaceAnim.HAPPY, "Hello, I'm an eight-legged mouse! Squeak squeak squeak, I like cheese, cats are bad, I'm definitely not a spider. Is that better?").also { stage = 34 }
            34 -> options("You still look like a spider.", "That's much better.").also { stage = 35 }
            35 -> when (buttonId) {
                1 -> { player(FaceAnim.SAD, "You still look like a spider."); npc(FaceAnim.SAD, "A spider? Oh no! I'm scared of spiders!"); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.HAPPY, "That's much better."); npc(FaceAnim.HAPPY, "Yay! I'll be an eight-legged mouse forever until I forget... You know what? It's great being a spider."); stage = END_DIALOGUE }
            }
            36 -> options("I'm still not scared.", "That was a bit scary.").also { stage = 37 }
            37 -> when (buttonId) {
                1 -> { player(FaceAnim.HAPPY, "I'm still not scared."); npc(FaceAnim.CHILD_NORMAL, "Oh, wow! Even I was scared of me when I said that! You're so brave! I bet you're not scared of anything!"); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.CALM, "That was a bit scary."); npc(FaceAnim.HAPPY, "Ha ha ha! I made you scared of me! It's okay, I'm not really going to eat your eyeballs. Well...I don't think they do."); stage = END_DIALOGUE }
            }

            // Conversation 4
            40 -> npc(FaceAnim.HAPPY, "I'm going to learn how to fly! I'm going to be a flying spider!").also { stage = 41 }
            41 -> options("How are you going to fly?", "Spiders can't fly!").also { stage = 42 }
            42 -> when (buttonId) {
                1 -> { player(FaceAnim.ASKING, "How are you going to fly?"); npc(FaceAnim.HAPPY, "I'm going to spin webs between my legs to make wings! Then I can fly!"); stage = 43 }
                2 -> { player(FaceAnim.CALM, "Spiders can't fly!"); npc(FaceAnim.HAPPY, "Some spiders can fly! They use web strands to float in the air! I think I know more about spiders than you, seeing how I am one!"); stage = END_DIALOGUE }
            }
            43 -> options("Let's try it now!", "Sounds dangerous.").also { stage = 44 }
            44 -> when (buttonId) {
                1 -> { player(FaceAnim.HAPPY, "Let's try it now!"); npc(FaceAnim.SAD, "Um...okay! I'll spin the webs...and then you throw me in the air. Okay. Three... Two... One... Wait! I'm not ready! I don't think I've got the webs right. We'll do it another time."); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.CALM, "Sounds dangerous."); npc(FaceAnim.SAD, "It's not dangerous! What's the worst that could happen? The worst that can happen is, like, the wings fail and I fall to my death. Or I get eaten by a bird. I'm going to rethink this."); stage = END_DIALOGUE }
            }

            // Conversation 5
            50 -> npc(FaceAnim.CALM, "Hey...there are so many humans in your world. Back in the Spider Realm there are only spiders. I guess that's why they call it the Spider Realm. Suppose I was bitten by a magically-irradiated man? I might become the Man-Spider! I'd gain all the powers of a man! Like um... Um...what can humans do that's special?").also { stage = 51 }
            51 -> options("Walk on two legs.", "Use tools.", "Project heat-rays from our eyes.", "Nothing, really. Humans are pretty ordinary.").also { stage = 52 }
            52 -> when (buttonId) {
                1 -> { player(FaceAnim.CALM, "Walk on two legs."); npc(FaceAnim.HAPPY, "Walk on two legs! Yes! Then I could use my other six legs to FIGHT CRIME!"); stage = END_DIALOGUE }
                2 -> { player(FaceAnim.CALM, "Use tools."); npc(FaceAnim.HAPPY, "Yes! I could have all sorts of gadgets! Like a gadget that spins webs! Wait...I can already spin webs. Never mind!"); stage = END_DIALOGUE }
                3 -> { player(FaceAnim.FRIENDLY, "Project heat-rays from our eyes."); npc(FaceAnim.SAD, "Oh! I didn't know humans could do that! Please don't fry me with your heat rays! I'm too young to fry!"); stage = END_DIALOGUE }
                4 -> { player(FaceAnim.CALM, "Nothing, really. Humans are pretty ordinary."); npc(FaceAnim.SAD, "Aww, don't say that. Everyone is special!"); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = EekSpiderDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIDER_8212)
}
*/
