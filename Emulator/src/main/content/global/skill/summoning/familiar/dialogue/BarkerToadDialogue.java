package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import java.util.Random;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Barker toad dialogue.
 */
@Initializable
public class BarkerToadDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new BarkerToadDialogue(player);
    }

    /**
     * Instantiates a new Barker toad dialogue.
     */
    public BarkerToadDialogue() {}

    /**
     * Instantiates a new Barker toad dialogue.
     *
     * @param player the player
     */
    public BarkerToadDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        Random rand = new Random();
        switch (rand.nextInt(6)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Ladies and gentlemen, for my next trick, I shall swallow this fly!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Roll up, roll up, roll up! See the greatest show on Gielinor!");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "We need to set up the big top somewhere near here. The locals look friendly enough.");
                stage = 11;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Braaaaaaaaaaaaaaaaaaaaaaap!", "(*Burp!*)");
                stage = 13;
                break;
            case 4:
                npc(FaceAnim.CHILD_NORMAL, "Mumblemumblegrumblemumble...", "(*Inaudible mumbles*)");
                stage = 18;
                break;
            case 5:
                npc(FaceAnim.CHILD_NORMAL, "Bwaaarp graaaawk?", "(What's that croaking in your inventory?)");
                stage = 19;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Seen it.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Ah, but last time was the frog...on fire?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "No! That would be a good trick.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Well, it won't be this time either.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Awwwww...");
                stage = END_DIALOGUE;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Roll up, roll up, roll up! See the greatest show on Gielinor!");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.HALF_ASKING, "Where?");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Well, it's kind of...you.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "Me?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Roll up, roll up, roll up! See the greatest freakshow on Gielinor!");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Don't make me smack you, slimy.");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.HALF_ASKING, "Are you kidding?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "Your problem is that you never see opportunities.");
                stage = END_DIALOGUE;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "That's disgusting behaviour!");
                stage++;
                break;
            case 14:
                npc(FaceAnim.CHILD_NORMAL, "Braap craaaaawk craaaawk.", "(That, my dear boy, was my world-renowned belching.)");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.HALF_ASKING, "I got that part. Why are you so happy about it?");
                stage++;
                break;
            case 16:
                npc(FaceAnim.CHILD_NORMAL, "Braaaaaaap craaaaaawk craaaaaaaawk.", "(My displays have bedazzled the crowned heads of Gielinor.)");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "I'd give you a standing ovation, but I have my hands full.");
                stage = END_DIALOGUE;
                break;
            case 18:
                playerl(FaceAnim.LAUGH, "Well, that cannonball seems to have shut him up!");
                stage = END_DIALOGUE;
                break;
            case 19:
                playerl(FaceAnim.HALF_ASKING, "Ah, you mean that toad?");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.FRIENDLY, "Oh, I'm guessing you're not going to like me carrying a toad about.");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "Craaawk, croak. (I might not be all that happy, no.)");
                stage++;
                break;
            case 22:
                playerl(FaceAnim.FRIENDLY, "I'm not going to eat it.");
                stage++;
                break;
            case 23:
                npc(FaceAnim.CHILD_NORMAL, "Craaaaawk braaap croak.", "(Weeeeell, I'd hope not! Reminds me of my mama toad.",
                        "She was inflated and fed to a jubbly, you know.", "A sad, demeaning way to die.)");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.BARKER_TOAD_6889, NPCs.BARKER_TOAD_6890};
    }
}
