package content.global.skill.summoning.familiar.dialogue.spirit;

import content.global.skill.prayer.Bones;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.anyInInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit wolf dialogue.
 */
@Initializable
public class SpiritWolfDialogue extends Dialogue {

    private static final List<Integer> bones = new ArrayList<>();

    static {
        for (Bones bone : Bones.values()) {
            bones.add(bone.getItemId());
        }
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritWolfDialogue(player);
    }

    /**
     * Instantiates a new Spirit wolf dialogue.
     */
    public SpiritWolfDialogue() {
    }

    /**
     * Instantiates a new Spirit wolf dialogue.
     *
     * @param player the player
     */
    public SpiritWolfDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (anyInInventory(player, bones.stream().mapToInt(Integer::intValue).toArray())) {
            npc(FaceAnim.CHILD_NORMAL, "Whuff-Whuff! Arf!", "(Throw the bone! I want to chase it!)");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "Whurf?", "(What are you doing?)");
                stage = 1;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Bark Bark!", "(Danger!)");
                stage = 2;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Whuff whuff. Pantpant awff!", "(I smell something good! Hunting time!)");
                stage = 4;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Pant pant whine?", "(When am I going to get to chase something?)");
                stage = 5;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "I can't just throw bones away - I need them to train my Prayer!");
                stage = END_DIALOGUE;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "Oh, just some... biped things. I'm sure it would bore you.");
                stage = END_DIALOGUE;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Where?!");
                stage++;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Whiiiine...", "(False alarm...)");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "We can go hunting in a moment. I just have to take care of something first.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Oh, I'm sure we'll find something for you in a bit.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SPIRIT_WOLF_6829, NPCs.SPIRIT_WOLF_6830};
    }
}
