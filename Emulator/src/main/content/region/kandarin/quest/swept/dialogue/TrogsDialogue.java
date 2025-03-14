package content.region.kandarin.quest.swept.dialogue;

import content.data.GameAttributes;
import content.global.handlers.iface.DiangoReclaimInterface;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.worldevents.WorldEvents;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import java.util.Optional;

import static core.api.ContentAPIKt.getAttribute;
import static core.api.ContentAPIKt.inEquipment;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Trogs dialogue.
 */
@Initializable
public class TrogsDialogue extends Dialogue {

    /**
     * Instantiates a new Trogs dialogue.
     */
    public TrogsDialogue() {
    }

    /**
     * Instantiates a new Trogs dialogue.
     *
     * @param player the player
     */
    public TrogsDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (!inEquipment(player, Items.CATSPEAK_AMULET_4677, 1)) {
            npc(FaceAnim.CHILD_NEUTRAL, "Meow?");
            stage = END_DIALOGUE;
            return true;
        }

        if (isHalloween()) {
            Item[] eligibleItems = DiangoReclaimInterface.Companion.getEligibleItems(player);

            if (hasCostumeItems(player, eligibleItems)) {
                npc(FaceAnim.CHILD_NEUTRAL, "Rather nice costume, that. Are you trick-or-treating this year, then?");
            } else if (hasAnyCostumeItem(player, eligibleItems)) {
                npc(FaceAnim.CHILD_NEUTRAL, "Oh, I see you're wearing one costume item! Aren't you planning on trick-or-treating this year?");
            } else {
                npc(FaceAnim.CHILD_NEUTRAL, "No costume, eh? Aren't you planning on trick-or-treating this year?");
            }

            stage = END_DIALOGUE;
            return true;
        }

        if (getAttribute(player, GameAttributes.MINI_PURPLE_CAT_COMPLETE, false)) {
            player("So, how do you like being purple?");
            stage = 10;
            return true;
        }

        player("You're purple!");
        stage = 0;
        return true;
    }

    private boolean isHalloween() {
        return Optional.ofNullable(WorldEvents.INSTANCE.get("halloween"))
                .map(event -> "halloween".equals(event.getName()))
                .orElse(false);
    }

    private boolean hasCostumeItems(Player player, Item[] eligibleItems) {
        for (Item item : eligibleItems) {
            if (player.getInventory().contains(item.getId(), item.getAmount())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnyCostumeItem(Player player, Item[] eligibleItems) {
        for (Item item : eligibleItems) {
            if (player.getInventory().containsAtLeastOneItem(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npc(FaceAnim.CHILD_NEUTRAL, "Yes. Well spotted.");
                stage++;
                break;
            case 1:
                player(FaceAnim.HALF_ASKING, "Are you naturally, you know, that colour? It's a rather vibrant hue.");
                stage++;
                break;
            case 2:
                npc(FaceAnim.CHILD_NEUTRAL, "Oh, no, I used to be more of a tortoiseshell until Wendy there started playing around.");
                stage++;
                break;
            case 3:
                player("Playing around?");
                stage++;
                break;
            case 4:
                npc(FaceAnim.CHILD_NEUTRAL, "Yes. I'm sure she'll tell you about it if you ask.");
                stage = END_DIALOGUE;
                break;
            case 10:
                npc(FaceAnim.CHILD_NEUTRAL, "Well, I felt rather conspicuous at first, but now I've grown to rather like the colour.");
                stage++;
                break;
            case 11:
                player("That's lucky.");
                stage++;
                break;
            case 12:
                npc(FaceAnim.CHILD_NEUTRAL, "Quite. As far as I know, Wendy doesn't know how to reverse the process.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new TrogsDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.TROGS_8202};
    }

}
