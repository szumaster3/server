package content.global.skill.summoning.familiar.dialogue;

import content.global.skill.gathering.fishing.Fish;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.anyInEquipment;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Bunyip dialogue.
 */
@Initializable
public class BunyipDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new BunyipDialogue(player);
    }

    /**
     * Instantiates a new Bunyip dialogue.
     */
    public BunyipDialogue() {}

    /**
     * Instantiates a new Bunyip dialogue.
     *
     * @param player the player
     */
    public BunyipDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (anyInEquipment(player, fishes)) {
            npcl(FaceAnim.CHILD_NORMAL, "I see you've got some fish there, mate.");
            stage = 0;
            return true;
        }
        switch (new java.util.Random().nextInt(4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Where are we going and why is it not to the beach?");
                stage = 4;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Hey Bruce, can we go down to the beach t'day?");
                stage = 8;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Pass me another bunch of shrimps, mate!");
                stage = 10;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Sigh...");
                stage = 13;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Yeah, but I might cook them up before I give them to you!");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Humans...always ruining good fishes.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "You know, some people prefer them cooked.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Yeah. We call 'em freaks.");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Well, we have a fair few places to go, but I suppose we could go to the beach if we get time.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Bonza! I'll get my board ready!");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Well, even if we do go to the beach I don't know if we'll have time for that.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Awww, that's a drag...");
                stage = END_DIALOGUE;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Well, I have a lot of things to do today but maybe later.");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Bonza!");
                stage = END_DIALOGUE;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "I don't know if I want any more water runes.");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "Righty, but I do know that I want some shrimps!");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "A fair point.");
                stage = END_DIALOGUE;
                break;
            case 13:
                playerl(FaceAnim.HALF_ASKING, "What's the matter?");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "I'm dryin' out in this sun, mate.");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.ASKING, "Well, what can I do to help?");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "Well, fish oil is bonza for the skin, ya know.");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Oh, right, I think I see where this is going.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.BUNYIP_6813, NPCs.BUNYIP_6814};
    }

    private static final int[] fishes = Fish.getFishMap().values().stream().mapToInt(fish -> fish.getId()).toArray();
}
