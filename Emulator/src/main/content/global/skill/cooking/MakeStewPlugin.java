package content.global.skill.cooking;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;
import org.rs.consts.Scenery;

/**
 * The type Make stew plugin.
 */
@Initializable
public class MakeStewPlugin extends UseWithHandler {

    /**
     * Instantiates a new Make stew plugin.
     */
    public MakeStewPlugin() {
        super(Items.UNCOOKED_STEW_2001);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(Scenery.COOKING_RANGE_114, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_2728, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_2729, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_2730, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_2731, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_2859, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_3039, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_4172, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_5275, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_8750, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_9682, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_12102, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_14919, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_16893, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_21792, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_22154, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_22713, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_22714, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_24283, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_24284, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_25730, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_33500, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_34410, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_34495, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_34546, OBJECT_TYPE, this);
        addHandler(Scenery.COOKING_RANGE_34565, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_36973, OBJECT_TYPE, this);
        addHandler(Scenery.RANGE_37629, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        event.getPlayer().getDialogueInterpreter().open(43989, event.getUsedItem().getId(), "stew");
        return true;
    }

}
