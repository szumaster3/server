package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Granite lobster dialogue.
 */
@Initializable
public class GraniteLobsterDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new GraniteLobsterDialogue(player);
    }

    /**
     * Instantiates a new Granite lobster dialogue.
     */
    public GraniteLobsterDialogue() {}

    /**
     * Instantiates a new Granite lobster dialogue.
     *
     * @param player the player
     */
    public GraniteLobsterDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 5)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "We shall heap the helmets of the fallen into a mountain!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "We march to war, Fremennik Player Name. Glory and plunder for all!");
                stage = 2;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Fremennik Player Name, what is best in life?");
                stage = 3;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Ho, my Fremennik brother, shall we go raiding?");
                stage = 5;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Clonkclonk clonk grind clonk. (Keep walking, outerlander. We have nothing to discuss.)");
                stage = 7;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "The outerlanders have insulted our heritage for the last time!");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "The longhall will resound with our celebration!");
                stage = END_DIALOGUE;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Yes! We shall pile gold before the longhall of our tribesmen!");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "Crush your enemies, see them driven before you, and hear the lamentation of their women!");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "I would have settled for raw sharks, but that's good too!");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Well, I suppose we could when I'm done with this.");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Yes! To the looting and the plunder!");
                stage = END_DIALOGUE;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Fair enough.");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Clonkclonkclonk grind clonk grind? (It's nothing personal, you're just an outerlander, you know?)");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.GRANITE_LOBSTER_6849, NPCs.GRANITE_LOBSTER_6850};
    }
}
