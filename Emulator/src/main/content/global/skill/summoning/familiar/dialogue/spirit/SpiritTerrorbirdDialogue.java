package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit terrorbird dialogue.
 */
@Initializable
public class SpiritTerrorbirdDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritTerrorbirdDialogue(player);
    }

    /**
     * Instantiates a new Spirit terrorbird dialogue.
     */
    public SpiritTerrorbirdDialogue() {}

    /**
     * Instantiates a new Spirit terrorbird dialogue.
     *
     * @param player the player
     */
    public SpiritTerrorbirdDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 5)) {
            case 0:
                npcl(FaceAnim.OLD_NORMAL, "This is a fun little walk.");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "I can keep this up for hours.");
                stage = 1;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "Are we going to visit a bank soon?");
                stage = 2;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "Can we go to a bank now?");
                stage = 4;
                break;
            case 4:
                npcl(FaceAnim.OLD_NORMAL, "So...heavy...");
                stage = 9;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Why do I get the feeling you'll change your tune when I start loading you up with items?");
                stage = END_DIALOGUE;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "I'm glad, as we still have plenty of time to go.");
                stage = END_DIALOGUE;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I'm not sure, you still have plenty of room for more stuff.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "Just don't leave it too long, okay?");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Just give me a little longer, okay?");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.OLD_NORMAL, "That's what you said last time!");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Did I?");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.OLD_NORMAL, "Yes!");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Well, I mean it this time, promise.");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "I knew you'd change your tune once you started carrying things.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.OLD_NORMAL, "Can we go bank this stuff now?");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Sure. You do look like you're about to collapse.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {NPCs.SPIRIT_TERRORBIRD_6794, NPCs.SPIRIT_TERRORBIRD_6795};
    }
}
