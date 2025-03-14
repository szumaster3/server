package content.global.skill.summoning.familiar.dialogue.pc;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Void ravager dialogue.
 */
@Initializable
public class VoidRavagerDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new VoidRavagerDialogue(player);
    }

    /**
     * Instantiates a new Void ravager dialogue.
     */
    public VoidRavagerDialogue() {}

    /**
     * Instantiates a new Void ravager dialogue.
     *
     * @param player the player
     */
    public VoidRavagerDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomIndex = (int) (Math.random() * 4);
        switch (randomIndex) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "You look delicious!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Take me to the rift!");
                stage = 1;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Pardon me. Could I trouble you for a moment?");
                stage = 4;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "How do you bear life without ravaging?");
                stage = 12;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Don't make me dismiss you!");
                stage = END_DIALOGUE;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "I'm not taking you there! Goodness knows what you'd get up to.");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I promise not to destroy your world...");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "If only I could believe you...");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Yeah, sure.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, it's just a trifling thing. Mmm, trifle...you look like trifle...So, will you help?");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Fire away!");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, just be honest. I just want a second opinion...Is this me? Mmm trifle...");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Huh?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Oh! The claws! The whiskers! The single, yellow eye! Oh! Is it me? Is it truly me?");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Erm...why yes...of course. It definitely reflects the inner you.");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, I knew it! You've been an absolute delight. An angel delight! And everyone said it was just a phase!");
                stage = END_DIALOGUE;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "It's not always easy.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "I could show you how to ravage, if you like...");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.VOID_RAVAGER_7370, NPCs.VOID_RAVAGER_7371};
    }
}
