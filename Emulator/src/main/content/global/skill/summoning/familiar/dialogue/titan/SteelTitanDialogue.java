package content.global.skill.summoning.familiar.dialogue.titan;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.GameWorld;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;
import static core.tools.DialogueHelperKt.START_DIALOGUE;

/**
 * The type Steel titan dialogue.
 */
@Initializable
public class SteelTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SteelTitanDialogue(player);
    }

    /**
     * Instantiates a new Steel titan dialogue.
     */
    public SteelTitanDialogue() {}

    /**
     * Instantiates a new Steel titan dialogue.
     *
     * @param player the player
     */
    public SteelTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        npc = new NPC(NPCs.STEEL_TITAN_7343);

        switch ((int)(Math.random() * 5)) {
            case 0:
                switch (stage) {
                    case START_DIALOGUE:
                        npcl(FaceAnim.CHILD_NORMAL, "Forward, master, to a battle that will waken the gods!");
                        stage++;
                        break;
                    case 1:
                        playerl(FaceAnim.FRIENDLY, "I'd rather not, if it's all the same to you.");
                        stage++;
                        break;
                    case 2:
                        npcl(FaceAnim.CHILD_NORMAL, "I shall never meet my end at this rate...");
                        stage = END_DIALOGUE;
                        break;
                }
                break;
            case 1:
                switch (stage) {
                    case START_DIALOGUE:
                        npcl(FaceAnim.CHILD_NORMAL, "How do you wish to meet your end, master?");
                        stage++;
                        break;
                    case 1:
                        playerl(FaceAnim.FRIENDLY, "Hopefully not for a very long time.");
                        stage++;
                        break;
                    case 2:
                        npcl(FaceAnim.CHILD_NORMAL, "You do not wish to be torn asunder by the thousand limbs of a horde of demons?");
                        stage++;
                        break;
                    case 3:
                        playerl(FaceAnim.FRIENDLY, "No! I'm quite happy picking flax and turning unstrung bows into gold...");
                        stage = END_DIALOGUE;
                        break;
                }
                break;
            case 2:
                switch (stage) {
                    case START_DIALOGUE:
                        npcl(FaceAnim.CHILD_NORMAL, "Why must we dawdle when glory awaits?");
                        stage++;
                        break;
                    case 1:
                        playerl(FaceAnim.FRIENDLY, "I'm beginning to think you just want me to die horribly...");
                        stage++;
                        break;
                    case 2:
                        npcl(FaceAnim.CHILD_NORMAL, "We could have deaths that bards sing of for a thousand years.");
                        stage++;
                        break;
                    case 3:
                        playerl(FaceAnim.FRIENDLY, "That's not much compensation.");
                        stage = END_DIALOGUE;
                        break;
                }
                break;
            case 3:
                switch (stage) {
                    case START_DIALOGUE:
                        npcl(FaceAnim.CHILD_NORMAL, "Master, we should be marching into glorious battle!");
                        stage++;
                        break;
                    case 1:
                        playerl(FaceAnim.FRIENDLY, "You know, I think you're onto something.");
                        stage++;
                        break;
                    case 2:
                        npcl(FaceAnim.CHILD_NORMAL, "We could find a death befitting such heroes of " + GameWorld.getSettings().getName() + "!");
                        stage++;
                        break;
                    case 3:
                        playerl(FaceAnim.FRIENDLY, "Ah. You know, I'd prefer not to die...");
                        stage++;
                        break;
                    case 4:
                        npcl(FaceAnim.CHILD_NORMAL, "Beneath the claws of a mighty foe shall I be sent into the embrace of death!");
                        stage = END_DIALOGUE;
                        break;
                }
                break;
            case 4:
                switch (stage) {
                    case START_DIALOGUE:
                        npcl(FaceAnim.CHILD_NORMAL, "Let us go forth to battle, my " + (player.isMale() ? "lord" : "lady") + "!");
                        stage++;
                        break;
                    case 1:
                        playerl(FaceAnim.FRIENDLY, "Why do you like fighting so much? It's not very nice to kill things.");
                        stage++;
                        break;
                    case 2:
                        npcl(FaceAnim.CHILD_NORMAL, "It is the most honourable thing in life.");
                        stage++;
                        break;
                    case 3:
                        playerl(FaceAnim.FRIENDLY, "But I summoned you, I'm not sure I can even say that you're alive...");
                        stage++;
                        break;
                    case 4:
                        npcl(FaceAnim.CHILD_NORMAL, "Alas, you have discovered the woe of all summoned creatures' existence.");
                        stage++;
                        break;
                    case 5:
                        playerl(FaceAnim.ASKING, "Really? I was right?");
                        stage++;
                        break;
                    case 6:
                        npcl(FaceAnim.CHILD_NORMAL, "Oh, woe...woe!");
                        stage = END_DIALOGUE;
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.STEEL_TITAN_7343, NPCs.STEEL_TITAN_7344 };
    }
}
