package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Karam overlord dialogue.
 */
@Initializable
public class KaramOverlordDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new KaramOverlordDialogue(player);
    }

    /**
     * Instantiates a new Karam overlord dialogue.
     */
    public KaramOverlordDialogue() {}

    /**
     * Instantiates a new Karam overlord dialogue.
     *
     * @param player the player
     */
    public KaramOverlordDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Do you want-");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "Kneel before my awesome might!");
                stage = 10;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "...");
                stage = 19;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "Errr...Have you FRIENDLYed down yet?");
                stage = 31;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.OLD_NORMAL, "Silence!");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "But I only...");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "Silence!");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "Now, listen here...");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.OLD_NORMAL, "SIIIIIILLLLLEEEEENCE!");
                stage++;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Fine!");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.OLD_NORMAL, "Good!");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "But I only...");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Maybe I'll be so silent you'll think I never existed");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.OLD_NORMAL, "Oh, how I long for that day...");
                stage = END_DIALOGUE;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "I would, but I have a bad knee you see...");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.OLD_NORMAL, "Your feeble prattlings matter not, air-breather! Kneel or face my wrath!");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "I'm not afraid of you. You're only a squid in a bowl!");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.OLD_NORMAL, "Only? I, radiant in my awesomeness, am 'only' a squid in a bowl? Clearly you need to be shown in your place, lung-user!");
                stage++;
                break;
            case 14:
                sendDialogue("The Karamthulhu overlord narrows its eye and you find yourself unable to breathe!");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "Gaak! Wheeeze!");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.OLD_NORMAL, "Who rules?");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "You rule!");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.OLD_NORMAL, "And don't forget it!");
                stage = END_DIALOGUE;
                break;
            case 19:
                npcl(FaceAnim.OLD_NORMAL, "The answer 'be silent'!");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you.");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.OLD_NORMAL, "Yes I do; I know all!");
                stage++;
                break;
            case 22:
                playerl(FaceAnim.FRIENDLY, "You have no idea what I was going to ask you.");
                stage++;
                break;
            case 23:
                npcl(FaceAnim.OLD_NORMAL, "Yes I do; I know all!");
                stage++;
                break;
            case 24:
                playerl(FaceAnim.FRIENDLY, "Then you will not be surprised to know I was going to ask you what you wanted to do today.");
                stage++;
                break;
            case 25:
                npcl(FaceAnim.OLD_NORMAL, "You dare doubt me!");
                stage++;
                break;
            case 26:
                npcl(FaceAnim.OLD_NORMAL, "The answer 'be silent' because your puny compressed brain could not even begin to comprehend my needs!");
                stage++;
                break;
            case 27:
                playerl(FaceAnim.FRIENDLY, "Well, how about I dismiss you so you can go and do what you like?");
                stage++;
                break;
            case 28:
                npcl(FaceAnim.OLD_NORMAL, "Well, how about I topple your nations into the ocean and dance my tentacle-waving victory dance upon your watery graves?");
                stage++;
                break;
            case 29:
                playerl(FaceAnim.HALF_ASKING, "Yeah...well...");
                stage++;
                break;
            case 30:
                npcl(FaceAnim.OLD_NORMAL, "Silence! Your burbling vexes me greatly!");
                stage = END_DIALOGUE;
                break;
            case 31:
                npcl(FaceAnim.OLD_NORMAL, "FRIENDLYed down? Why would I need to FRIENDLY down?");
                stage++;
                break;
            case 32:
                playerl(FaceAnim.FRIENDLY, "Well there is that whole 'god complex' thing...");
                stage++;
                break;
            case 33:
                npcl(FaceAnim.OLD_NORMAL, "Complex? What 'complex' are you drooling about this time, minion?");
                stage++;
                break;
            case 34:
                playerl(FaceAnim.FRIENDLY, "I don't really think sheep really make mewling noises...");
                stage++;
                break;
            case 35:
                npcl(FaceAnim.OLD_NORMAL, "Silence!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.KARAMTHULHU_OVERLORD_6809, NPCs.KARAMTHULHU_OVERLORD_6810};
    }
}
