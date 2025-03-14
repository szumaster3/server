package content.region.asgarnia.dialogue.craftguild;

import org.rs.consts.Items;
import org.rs.consts.NPCs;
import core.api.Container;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.plugin.Initializable;

import static core.api.ContentAPIKt.*;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Master crafter dialogue.
 */
@Initializable
public class MasterCrafterDialogue extends Dialogue {

    /**
     * Instantiates a new Master crafter dialogue.
     */
    public MasterCrafterDialogue() {

    }

    private static final int CRAFTING_SKILL_CAPE = Items.CRAFTING_CAPE_9780;
    private static final int CRAFTING_SKILL_CAPE_TRIMMED = Items.CRAFTING_CAPET_9781;
    private static final int SKILL_CAPE_HOOD = Items.CRAFTING_HOOD_9782;
    private static final int COINS = Items.COINS_995;

    /**
     * Instantiates a new Master crafter dialogue.
     *
     * @param player the player
     */
    public MasterCrafterDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npcl(FaceAnim.FRIENDLY, "Hello, and welcome to the Crafting Guild. We're running a master crafting event currently, we're inviting crafters from all over the land to come here and use our top notch workshops!");
        stage = npc.getId() != NPCs.MASTER_CRAFTER_805 ? END_DIALOGUE : 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                options("Skillcape of Crafting", "Nevermind.");
                stage++;
                break;
            case 1:
                if (buttonId == 1) {
                    if (hasLevelStat(player, Skills.CRAFTING, 99)) {
                        player(FaceAnim.ASKING, "Hey, could I buy a Skillcape of Crafting?");
                        stage = 10;
                    } else {
                        player(FaceAnim.ASKING, "Hey, what is that cape you're wearing? I don't recognize it.");
                        stage++;
                    }
                } else {
                    player("Nevermind.");
                    stage = END_DIALOGUE;
                }
                break;
            case 2:
                npcl(FaceAnim.FRIENDLY, "This? This is a Skillcape of Crafting. It is a symbol of my ability and standing here in the Crafting Guild.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.FRIENDLY, "If you should ever achieve level 99 Crafting come and talk to me and we'll see if we can sort you out with one.");
                stage = END_DIALOGUE;
                break;
            case 10:
                npcl(FaceAnim.HAPPY, "Certainly! Right after you pay me 99000 coins.");
                stage++;
                break;
            case 11:
                options("Okay, here you go.", "No thanks.");
                stage++;
                break;
            case 12:
                if (buttonId == 1) {
                    player(FaceAnim.FRIENDLY, "Okay, here you go.");
                    stage++;
                } else {
                    player(FaceAnim.HALF_THINKING, "No, thanks.");
                    stage = END_DIALOGUE;
                }
                break;
            case 13:
                if (!removeItem(player, new Item(COINS, 99000), Container.INVENTORY)) {
                    npcl(FaceAnim.NEUTRAL, "You don't have enough coins for a cape.");
                    stage = END_DIALOGUE;
                } else {
                    addItemOrDrop(player, player.getSkills().getMasteredSkills() >= 1 ? CRAFTING_SKILL_CAPE_TRIMMED : CRAFTING_SKILL_CAPE, 1);
                    addItemOrDrop(player, SKILL_CAPE_HOOD, 1);
                    npcl(FaceAnim.HAPPY, "There you go! Enjoy.");
                    stage = END_DIALOGUE;
                }
                break;
            case 20:
                npcl(FaceAnim.NEUTRAL, "Where's your brown apron? You can't come in here unless you're wearing one.");
                stage++;
                break;
            case 21:
                player(FaceAnim.HALF_GUILTY, "Err... I haven't got one.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {
            NPCs.MASTER_CRAFTER_805,
            NPCs.MASTER_CRAFTER_2732,
            NPCs.MASTER_CRAFTER_2733
        };
    }
}
