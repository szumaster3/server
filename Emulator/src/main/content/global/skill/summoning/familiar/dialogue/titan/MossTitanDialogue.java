package content.global.skill.summoning.familiar.dialogue.titan;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Moss titan dialogue.
 */
@Initializable
public class MossTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new MossTitanDialogue(player);
    }

    /**
     * Instantiates a new Moss titan dialogue.
     */
    public MossTitanDialogue() {}

    /**
     * Instantiates a new Moss titan dialogue.
     *
     * @param player the player
     */
    public MossTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, look! A bug.");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "When you stamp on 'em, humies go squish.");
                stage = 9;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big feet.");
                stage = 23;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "What are we doing today?");
                stage = 31;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "It's quite a large bug.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "He's so cute! I wanna keep him.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Well, be careful.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I'm gonna call him Buggie and I'm gonna keep him in a box.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Don't get overexcited.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "I'm gonna feed him and we're gonna be so happy together!");
                stage++;
                break;
            case 6:
                sendDialogue( "The Moss titan begins to bounce up and down.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Aww...Buggie went squish.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Sigh.");
                stage = END_DIALOGUE;
                break;

            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "When you punch 'em, humies go squish.");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "When you push 'em, humies go squish.");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "Squish squish squish.");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "When you touch 'em, humies go squish.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "When you talk to 'em, humies go squish.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.CHILD_NORMAL, "When you poke 'em, humies go squish.");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "Squish squish squish.");
                stage++;
                break;
            case 22:
                playerl(FaceAnim.FRIENDLY, "You have problems, you know that. Come on, we have got stuff to do.");
                stage = END_DIALOGUE;
                break;

            case 23:
                playerl(FaceAnim.FRIENDLY, "Are you quite finished?");
                stage++;
                break;
            case 24:
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big hands.");
                stage++;
                break;

            case 25:
                playerl(FaceAnim.FRIENDLY, "Done yet?");
                stage++;
                break;
            case 26:
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big chest.");
                stage++;
                break;

            case 27:
                playerl(FaceAnim.FRIENDLY, "Done yet?");
                stage++;
                break;
            case 28:
                npcl(FaceAnim.CHILD_NORMAL, "Stampy stampy stampy stampy stampy stampy, I've got big hair.");
                stage++;
                break;

            case 29:
                playerl(FaceAnim.FRIENDLY, "Oh, be quiet and come on.");
                stage++;
                break;
            case 30:
                npcl(FaceAnim.CHILD_NORMAL, "...");
                stage = END_DIALOGUE;
                break;

            case 31:
                playerl(FaceAnim.FRIENDLY, "Let's just wait and see.");
                stage++;
                break;
            case 32:
                npcl(FaceAnim.CHILD_NORMAL, "I want to do some squishing of tiny things!");
                stage++;
                break;
            case 33:
                playerl(FaceAnim.FRIENDLY, "Preferably not me.");
                stage++;
                break;
            case 34:
                npcl(FaceAnim.CHILD_NORMAL, "Even if only a little bit, like your foot or something?");
                stage++;
                break;

            case 35:
                playerl(FaceAnim.FRIENDLY, "Um, no. I really don't fancy being squished today, thanks.");
                stage++;
                break;

            case 36:
                npcl(FaceAnim.CHILD_NORMAL, "Awww...");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.MOSS_TITAN_7357, NPCs.MOSS_TITAN_7358 };
    }
}
