package content.global.skill.summoning.familiar.dialogue.titan;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.GameWorld;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.amountInInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Geyser titan dialogue.
 */
@Initializable
public class GeyserTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new GeyserTitanDialogue(player);
    }

    /**
     * Instantiates a new Geyser titan dialogue.
     */
    public GeyserTitanDialogue() {}

    /**
     * Instantiates a new Geyser titan dialogue.
     *
     * @param player the player
     */
    public GeyserTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (amountInInventory(player, Items.SHARK_385) < 5) {
            npcl(FaceAnim.CHILD_NORMAL, "Hey mate, how are you?");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 7)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Over the course of their lifetime a shark may grow and use 20,000 teeth.");
                stage = 3;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Did you know a snail can sleep up to three years?");
                stage = 4;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Unlike most animals, both the shark's upper and lower jaws move when they bite.");
                stage = 5;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Did you know that in one feeding a mosquito can absorb one-and-a-half times its own body weight in blood?");
                stage = 7;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Did you know that sharks have the most powerful jaws of any animal on the planet?");
                stage = 8;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Did you know that " + GameWorld.getSettings().getName() + " gets 100 tons heavier every day, due to dust falling from space?");
                stage = 10;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Did you know that sharks normally eat alone?");
                stage = 11;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Not so bad.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Did you know that during the average human life-span the heart will beat approximately 2.5 billion times?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Wow, that is a lot of non-stop work!");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.HALF_ASKING, "Wow! That is a whole load of teeth. I wonder what the Tooth Fairy would give for those?");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "I wish I could do that. Ah...sleep.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.HALF_ASKING, "Really?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Yup. Chomp chomp.");
                stage = END_DIALOGUE;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Eugh.");
                stage = END_DIALOGUE;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "No, I didn't.");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Full of facts, me.");
                stage = END_DIALOGUE;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "What a fascinating fact.");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "I see.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "Sometimes one feeding shark attracts others and they all try and get a piece of the prey.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "They take a bite at anything in their way and may even bite each other!");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "Ouch!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.GEYSER_TITAN_7339, NPCs.GEYSER_TITAN_7340 };
    }
}
