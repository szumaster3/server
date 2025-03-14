package content.global.skill.summoning.familiar.dialogue.pc;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Void torcher dialogue.
 */
@Initializable
public class VoidTorcherDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new VoidTorcherDialogue(player);
    }

    /**
     * Instantiates a new Void torcher dialogue.
     */
    public VoidTorcherDialogue() {}

    /**
     * Instantiates a new Void torcher dialogue.
     *
     * @param player the player
     */
    public VoidTorcherDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomIndex = (int) (Math.random() * 4);
        switch (randomIndex) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "You okay there, spinner?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "'T' is for torcher, that's good enough for me... 'T' is for torcher, I'm happy you can see.");
                stage = 7;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Burn, baby, burn! Torcher inferno!");
                stage = 8;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "So hungry... must devour...");
                stage = 9;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "I not spinner!");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.HALF_ASKING, "Sorry, splatter?");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I not splatter either!");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "No, wait, I meant defiler.");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "I torcher!");
                stage++;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Hehe, I know. I was just messing with you.");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Grr. Don't be such a pest.");
                stage = END_DIALOGUE;
                break;
            case 7:
                playerl(FaceAnim.HALF_ASKING, "You're just a bit weird, aren't you?");
                stage = END_DIALOGUE;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "*Wibble*");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "*Gulp* Er, yeah, I'll find you something to eat in a minute.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "Is flesh-bag scared of torcher?");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "No, no. I, er, always look like this... honest.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.VOID_TORCHER_7351, NPCs.VOID_TORCHER_7352};
    }
}
