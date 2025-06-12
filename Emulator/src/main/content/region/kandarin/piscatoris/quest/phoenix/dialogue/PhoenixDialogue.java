package content.region.kandarin.piscatoris.quest.phoenix.dialogue;

import content.data.GameAttributes;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.dialogue.IfTopic;
import core.game.dialogue.Topic;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;
import org.rs.consts.Quests;
import org.rs.consts.Vars;

import static core.api.ContentAPIKt.*;
import static core.api.quest.QuestAPIKt.isQuestComplete;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * Once killed, you will be rewarded with 5 phoenix quills (upon talking to the phoenix) and 5000 Slayer experience,
 * from every kill after that you will only receive 500 Slayer experience. In addition, in subsequent visits you will
 * also receive 7500 Firemaking experience, 3000 Fletching experience, as well as 1000 Crafting experience as a reward
 * for defeating her.
 * <p>
 * Once you have completed the quest, and have 51 Slayer, you may fight the Phoenix after traveling through her lair (Her guardians will block and multi-attack you, be warned.)
 * <p>
 * The Phoenix herself has a Combat level of 235 and has high defence. She attacks purely with magic and has been said to hit up to 29.
 * <p>
 * Note: Your familiar RARELY follows you through the whole set of caves.
 */
@Initializable
public class PhoenixDialogue extends Dialogue {

    /**
     * Instantiates a new Phoenix dialogue.
     */
    public PhoenixDialogue() {

    }

    /**
     * Instantiates a new Phoenix dialogue.
     *
     * @param player the player
     */
    public PhoenixDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (isQuestComplete(player, Quests.IN_PYRE_NEED)) {
            npcl(FaceAnim.OLD_NORMAL, "(Welcome back, " + player.getUsername() + ". It is good to see you are enthusiastic, as ever. Heh heh heh!)");
            stage = 40;
        }
        player(FaceAnim.SCARED, "H...hello?");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int componentID, int buttonID) {
        switch (stage) {
            case 0:
                npc(FaceAnim.OLD_NORMAL, "(Hello, human. So, it is you whom I owe a great deal of", "thanks.)");
                stage++;
                break;
            case 1:
                npc(FaceAnim.OLD_NORMAL, "Oh, it was nothing, really.");
                stage++;
                break;
            case 2:
                npc(FaceAnim.HAPPY, "(Ah, such modesty. In that case, I shall withhold the", "reward I planned for you, and send you away with my", "thanks.)");
                stage++;
                break;
            case 3:
                player(FaceAnim.FRIENDLY, "Well, actually, it was an arduous trek through a", "confusing and inhospitably hot cave, coupled with a", "rather difficult Crafting task. And I burned my fingers", "lighting the pyre.");
                stage++;
                break;
            case 4:
                npc(FaceAnim.OLD_NORMAL, "(Heh heh heh! Well, say what you mean in the future,", "not what makes you sound best, lest you really miss", "out on what you're due!)");
                stage++;
                break;
            case 5:
                npc(FaceAnim.OLD_NORMAL, "(In all seriousness, I would not have you walk out of", "ere with merely my thanks.)");
                stage++;
                break;
            case 6:
                sendItemDialogue(player, Items.PHOENIX_QUILL_14616, "The phoenix plucks five large quills from its wings and gives them to you.");
                addItemOrDrop(player, Items.PHOENIX_QUILL_14616, 5);
                stage++;
                break;
            case 7:
                npc(FaceAnim.OLD_NORMAL, "(Those feathers are, as you plainly saw, a part of me.)");
                stage++;
                break;
            case 8:
                npc(FaceAnim.OLD_NORMAL, "(When combined with other ingredients and energies,", "they can be used to summon me to your location", "temporarily.)");
                stage++;
                break;
            case 9:
                player(FaceAnim.FRIENDLY, "I can use them to make a Summoning pouch, in other", "words.");
                stage++;
                break;
            case 10:
                npc(FaceAnim.OLD_NORMAL, "(Correct. So, Pikkupstix taught you the ways of the", "summoner already. How fortunate!)");
                stage++;
                break;
            case 11:
                player(FaceAnim.HALF_ASKING, "How do you know Pikkupstix?");
                stage = 14;
                break;
            case 14:
                npc(FaceAnim.OLD_NORMAL, "Well, in the long time I have lived on this world...let us", "just say that I been around.");
                stage++;
                break;
            case 15:
                npc(FaceAnim.OLD_NORMAL, "(Now, on the second part of your reward. I grant", "you the right to challenge me.)");
                stage++;
                break;
            case 16:
                player(FaceAnim.SCARED, "Huh?");
                stage++;
                break;
            case 17:
                npc(FaceAnim.OLD_NORMAL, "(Once a day, you may enter my lair, brave your way", "through it and challenge me in combat, starting from", "now.)");
                stage++;
                break;
            case 18:
                npc(FaceAnim.OLD_NORMAL, "(If you defeat me, I will give you five more quills,", "plucked from me, by me.)");
                stage++;
                break;
            case 19:
                player(FaceAnim.FRIENDLY, "Forgive me, but I fail to see the advantage this presents", "you. Why would you offer me this as a reward?");
                stage++;
                break;
            case 20:
                npc(FaceAnim.OLD_SAD, "(You, my friend, will be completing my rebirth ritual.", "Each time you defeat me, my life will extend further.)");
                stage++;
                break;
            case 21:
                npc(FaceAnim.OLD_NORMAL, "(My recent near-death has somewhat shaken me. I wish", "to stay in my lair for a while.)");
                stage++;
                break;
            case 22:
                npc(FaceAnim.OLD_SAD, "(I need to keep at least one friend close at all times to", "ensure my continued existence.)");
                stage++;
                break;
            case 23:
                npc(FaceAnim.OLD_NORMAL, "(" + RED + "You must be an accomplished Slayer before you can</col>", RED + "challenge me, though</col>. Our arrangement is no good to", "either of us if you lose.)");
                stage++;
                break;
            case 24:
                player(FaceAnim.FRIENDLY, "I see; and in return for my friendship, I gain yours.");
                stage++;
                break;
            case 25:
                npc(FaceAnim.OLD_NORMAL, "(An agreeable situation, is it not?)");
                stage++;
                break;
            case 26:
                npc(FaceAnim.OLD_NORMAL, "(Anyway, my friend, I fear I must ask you to leave me", "for a while. I am still quite fatigued from my ordeal,", "and would like some peace and quiet to rest.)");
                stage++;
                break;
            case 27:
                player(FaceAnim.FRIENDLY, "I understand. The priest will want to know you're", "alright, anyway.");
                stage++;
                break;
            case 28:
                npc(FaceAnim.OLD_NORMAL, "(Priest? What priest?)");
                stage++;
                break;
            case 29:
                player(FaceAnim.FRIENDLY, "There is a priest who has been studying you unseen", "for quite some time. If it was not for him, I would not", "have known the ritual and would have been powerless to", "help you.");
                stage++;
                break;
            case 30:
                npc(FaceAnim.OLD_NORMAL, "(Oh, THAT priest. While I am aware he has been", "studying me, he has not done so unseen.)");
                stage++;
                break;
            case 31:
                npc(FaceAnim.OLD_NORMAL, "(Thank him for the shrine for me. Also, ask him when I", "can have my trinkets back. Heh heh heh!)");
                stage++;
                break;
            case 32:
                player(FaceAnim.FRIENDLY, "I'll do that for you. Rest well; I'm sure you'll be seeing", "me soon.");
                stage++;
                break;
            case 33:
                npc(FaceAnim.OLD_NORMAL, "(Farewell, " + player.getUsername() + ".)");
                stage++;
                break;
            case 34:
                player(FaceAnim.FRIENDLY, "Farewell!");
                setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 10, true);
                stage = END_DIALOGUE;
                break;

            case 40:
                player("Hello to you, too. Thanks for the workout!");
                stage++;
                break;
            case 41:
                npc(FaceAnim.OLD_NORMAL,"(Was there something you wanted to ask about?)");
                stage++;
                break;
            case 42:
                showTopics(
                        new Topic("Tell me about summoning you again.", 105, true),
                        new Topic("If you knew about the priest...", 98, true),
                        new IfTopic("", 72, true, inInventory(player, Items.PHOENIX_EGGLING_14626, 1)),
                        new IfTopic("Who was Si'morgh?", 48, true, getAttribute(player, GameAttributes.PHOENIX_LAIR_VISITED, false)),
                        new Topic("Not really, I just came to say hello.", 43, true)
                );
                break;

            case 43:
                player("Not really. I just came to say hello.");
                stage++;
                break;
            case 44:
                npc(FaceAnim.OLD_NORMAL,"(Hello!)");
                stage++;
                break;
            case 45:
                player("Hello!");
                stage++;
                break;
            case 46:
                npc(FaceAnim.OLD_NORMAL,"(And goodbye!)");
                stage++;
                break;
            case 47:
                player("Goodbye!");
                stage = END_DIALOGUE;
                break;

            case 48:
                player(FaceAnim.HALF_ASKING, "Who was Si'morgh? You don't have to tell me if you don't want to.");
                stage++;
                break;
            case 49:
                npcl(FaceAnim.OLD_SAD, "(It happened a millennia ago now. You see, I hatched from a lucky egg. My parent was a solo phoenix that stayed alive for a mere two cycles. One egg - mine - was already fertile, and when my parent died, the other egg, Si'morgh, became fertile.)");
                stage++;
                break;
            case 50:
                npcl(FaceAnim.OLD_SAD, "(We hatched together, brother and sister for all intents and purposes, and used to live in this very lair together.)");
                stage++;
                break;
            case 51:
                playerl(FaceAnim.FRIENDLY, "Wow, the chances of that are very slim indeed! It must have been nice to have grown up with someone.");
                stage++;
                break;
            case 52:
                npcl(FaceAnim.OLD_HAPPY, "(Yes, it was. Si'morgh learned a lot faster than I did. He was always looking out for me, finding me food and protecting me from danger as I grew up. We were about the age at which you could call a phoenix a teenager when it happened.)");
                stage++;
                break;
            case 53:
                npcl(FaceAnim.OLD_HAPPY, "(There was a strange new creature outside of our lair. Curiosity led me to leave the safety of the cave and investigate.)");
                stage++;
                break;
            case 54:
                npcl(FaceAnim.OLD_HAPPY, "(I had never seen a dragon before.)");
                stage++;
                break;
            case 55:
                player("It was a dragon?");
                stage++;
                break;
            case 56:
                npcl(FaceAnim.OLD_HAPPY, "(Si'morgh was out looking for something to eat at the time. The dragon attacked me. I tried to defend myself, but I was overpowered. At the last moment, as the dragon was about to land the killing blow, Si'morgh returned.)");
                stage++;
                break;
            case 57:
                npcl(FaceAnim.OLD_HAPPY, "(He threw himself at the dragon to protect me, and screamed at me to run. I got away as fast as I could, hiding in our lair, where the huge dragon could not follow. Hours passed, and Si'morgh did not return.)");
                stage++;
                break;
            case 58:
                npcl(FaceAnim.OLD_HAPPY, "(That night, I took a cautious peek out of the cave. I couldn't see or hear the dragon anywhere, so I went out looking for Si'morgh.)");
                stage++;
                break;
            case 59:
                npcl(FaceAnim.OLD_HAPPY, "(I found him. Bits of him, at least.)");
                stage++;
                break;
            case 60:
                player("That's terrible.");
                stage++;
                break;
            case 61:
                npcl(FaceAnim.OLD_HAPPY, "(He gave his life in exchange for mine, but he left me alone.)");
                stage++;
                break;
            case 62:
                player("So, that's why your lair is so well-defended.");
                stage++;
                break;
            case 63:
                npcl(FaceAnim.OLD_HAPPY, "(Yes. I had to grow up quickly, as there was no Si'morgh to look after me. Essentially, I had to fend for myself, so the first thing I did was to fortify my home.)");
                stage++;
                break;
            case 64:
                npcl(FaceAnim.OLD_HAPPY, "(I started becoming more adventurous when the loneliness got to me. After all, I was used to company, and living with none was quite difficult.)");
                stage++;
                break;
            case 65:
                npcl(FaceAnim.OLD_HAPPY, "(I started collecting trinkets to keep myself occupied, leaving my lair for centuries at a time and returning only to reborn. I became a people-watcher - watching the races as they developed with time.)");
                stage++;
                break;
            case 66:
                npcl(FaceAnim.OLD_HAPPY, "(You know the rest of my story, " + player.getUsername() + ", because you have helped to continue writing it.)");
                stage++;
                break;
            case 67:
                playerl(FaceAnim.FRIENDLY, "Thank you for telling me about this. I know it must have been difficult for you.");
                stage++;
                break;
            case 68:
                npcl(FaceAnim.OLD_HAPPY, "(It is no bother. In truth, it is nice to have someone to talk to - someone to call a friend. Thank you for listening, " + player.getUsername() + ".)");
                stage++;
                break;
            case 69:
                player("It was my pleasure.");
                stage++;
                break;
            case 70:
                npcl(FaceAnim.OLD_HAPPY, "(I do not wish to be rude, friend, but may I have some time alone with my thoughts?)");
                stage++;
                break;
            case 71:
                player("Of course. I'll see you again soon.");
                stage = END_DIALOGUE;
                break;

            case 72:
                player(FaceAnim.NEUTRAL, "I got lost on the way out, and-");
                stage++;
                break;
            case 73:
                npc(FaceAnim.OLD_HAPPY, "(Found my egg chamber?)");
                stage++;
                break;
            case 74:
                player(FaceAnim.NEUTRAL, "Yes. One of the eggs hatched.");
                stage++;
                break;
            case 75:
                npc(FaceAnim.OLD_HAPPY, "(Yes, I know. This is extremely rare. I imagine you have many questions. Ask away.)");
                stage++;
                break;
            case 76:
                player(FaceAnim.NEUTRAL, "Okay. Firstly, are you not upset that I took the eggling with me?");
                stage++;
                break;
            case 77:
                npc(FaceAnim.OLD_HAPPY, "(Not at all. You helped me in my time of need. I know you'll take good care of my eggling until such a time that it can take care of itself.)");
                stage++;
                break;
            case 78:
                player(FaceAnim.FRIENDLY, "What a relief!");
                stage++;
                break;
            case 79:
                player(FaceAnim.NEUTRAL, "I have found myself wondering how the egg became fertile. You are the only phoenix alive at the moment, right?");
                stage++;
                break;
            case 80:
                npc(FaceAnim.OLD_HAPPY, "(Barring the eggling you've adopted for me and any others that may hatch, that is true, yes. The matter of reproduction in my species...differs from the norm, somewhat.)");
                stage++;
                break;
            case 81:
                player(FaceAnim.THINKING, "Care to elaborate?");
                stage++;
                break;
            case 82:
                npc(FaceAnim.OLD_HAPPY, "(Certainly! I had no idea you were such an avid appreciator of nature.)");
                stage++;
                break;
            case 83:
                npc(FaceAnim.OLD_HAPPY, "(After a phoenix performs its rebirth ritual, its ashes are stored within a magical egg. I perform this part of my ritual after you have left; I need some rest before I can utilize the necessary magicks to perform the act.)");
                stage++;
                break;
            case 84:
                npc(FaceAnim.OLD_HAPPY, "(I keep all of these eggs in my egg chamber. They are mostly dormant at the moment, though.)");
                stage++;
                break;
            case 85:
                npc(FaceAnim.OLD_HAPPY, "(The eggs are a sort of contingency plan. You see, the phoenixes are linked. It is a link similar to that which I share with my guardians; a sort of shared consciousness.)");
                stage++;
                break;
            case 86:
                npcl(FaceAnim.OLD_HAPPY, "(If all the phoenixes were to die out at once, one of the eggs would become magically fertile, ensuring that, as long as we complete our rebirth ritual at least once before meeting our end, there will always be an eggling phoenix to take over when we...pass on.)");
                stage++;
                break;
            case 87:
                player(FaceAnim.AMAZED, "Wow! That's amazing!");
                stage++;
                break;
            case 88:
                player(FaceAnim.SCARED, "Hold on: you're not dead! Why did the egg hatch?");
                stage++;
                break;
            case 89:
                npcl(FaceAnim.OLD_HAPPY, "(There is a very small chance that an egg will become magically fertile without the death of all phoenixes. Seeing as we phoenixes perform our rituals once every five hundred years - under normal circumstances - this means the population is usually one.)");
                stage++;
                break;
            case 90:
                npcl(FaceAnim.OLD_HAPPY, "(The most phoenixes alive at once, since my birth, has been two.)");
                stage++;
                break;
            case 91:
                npcl(FaceAnim.OLD_SAD, "(Ah, Si'morgh. I miss you so.)");
                stage++;
                break;
            case 92:
                npcl(FaceAnim.OLD_HAPPY, "(Anyway, to make a long story short, you were simply very lucky to find a hatching egg.)");
                stage++;
                break;
            case 93:
                npcl(FaceAnim.OLD_HAPPY, "(${player.username}, promise me something.)");
                stage++;
                break;
            case 94:
                player(FaceAnim.FRIENDLY, "What would you ask of me?");
                stage++;
                break;
            case 95:
                npcl(FaceAnim.OLD_HAPPY, "(Let my child see the world, but teach it to fear danger. I don't want it to end up dead like Si'morgh, or a frightened hermit like me.)");
                stage++;
                break;
            case 96:
                player(FaceAnim.NEUTRAL, "I will try my best.");
                stage++;
                break;
            case 97:
                npcl(FaceAnim.OLD_HAPPY, "(Thank you, " + player.getUsername() + ".)");
                stage++;
                break;

            case 98:
                npcl(FaceAnim.OLD_HAPPY, "(Why was he not slain like the other intruders in my lair?)");
                stage++;
                break;
            case 99:
                player(FaceAnim.FRIENDLY, "Erm...well, yes.");
                stage++;
                break;
            case 100:
                npcl(FaceAnim.OLD_HAPPY, "(I must admit, I did regard him with cautions for a while; but in all his trips into my lair, he never did anything worth stopping.)");
                stage++;
                break;
            case 101:
                npcl(FaceAnim.OLD_HAPPY, "(I found him most curious, carefully avoiding my guardians with all his stealth and guile. I knew he was there, but I was holding them back.)");
                stage++;
                break;
            case 102:
                npcl(FaceAnim.OLD_HAPPY, "(In all his visits, all he ever did was look around and take notes; watch me as I went about my business; and study my guardians going about theirs.)");
                stage++;
                break;
            case 103:
                npcl(FaceAnim.OLD_HAPPY, "(I did take exception to him taking away my antiques collection, I suppose, but he provided me with a lovely shrine and tapestry in exchange. So, I'm content. Heh heh.)");
                stage++;
                break;
            case 104:
                npcl(FaceAnim.OLD_HAPPY, "(Was there something you wanted to ask about?)");
                stage = 42;
                break;

            case 105:
                npcl(FaceAnim.OLD_HAPPY, "(The feathers I gave you are a part of me. You can use them to infuse a summoning pouch with some of my essence.)");
                stage++;
                break;
            case 106:
                player(FaceAnim.FRIENDLY, "Some of your essence?");
                stage++;
                break;
            case 107:
                npcl(FaceAnim.OLD_HAPPY, "(Indeed. The energy from the pouch creates a spectral version of my, but the essence contained within it links it to my mind.)");
                stage++;
                break;
            case 108:
                npcl(FaceAnim.OLD_HAPPY, "(You see, there is only one of me, but using the summoning pouches and spectral essences means I can be in many places at once.)");
                stage++;
                break;
            case 109:
                player(FaceAnim.FRIENDLY, "I think I understand. What powers do you have in this form?");
                stage++;
                break;
            case 110:
                npcl(FaceAnim.OLD_HAPPY, "(I will still be a powerful magical fighter, and will also have the power to conjure up ashes and hurl them at your foe, blinding them temporarily.)");
                stage++;
                break;
            case 111:
                npcl(FaceAnim.OLD_HAPPY, "(When these ashes fall to the floor, use my summoning scroll on them. This will be particularly useful if I am running low on life points. Heh heh heh!)");
                stage++;
                break;
            case 112:
                player(FaceAnim.FRIENDLY, "Okay, thanks for the information.");
                stage = 104;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.PHOENIX_8548};
    }
}
