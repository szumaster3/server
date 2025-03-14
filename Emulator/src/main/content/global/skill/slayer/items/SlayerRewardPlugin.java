package content.global.skill.slayer.items;

import content.global.skill.slayer.SlayerManager;
import content.global.skill.slayer.SlayerMaster;
import content.global.skill.slayer.Tasks;
import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Components;
import org.rs.consts.Items;
import org.rs.consts.Quests;

import static core.api.quest.QuestAPIKt.hasRequirement;

/**
 * The type Slayer reward plugin.
 */
@Initializable
public class SlayerRewardPlugin extends ComponentPlugin {
    private static final Component ASSIGNMENT = new Component(Components.SMKI_ASSIGNMENT_161);
    private static final Component LEARN = new Component(Components.SMKI_LEARN_163);
    private static final Component BUY = new Component(Components.SMKI_BUY_164);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.forId(Components.SMKI_ASSIGNMENT_161).setPlugin(this);
        ComponentDefinition.forId(Components.SMKI_LEARN_163).setPlugin(this);
        ComponentDefinition.forId(Components.SMKI_BUY_164).setPlugin(this);
        ClassScanner.definePlugins(new SlayerMasterPlugin(), new SlayerHelmCraftPlugin());
        return this;
    }

    @Override
    public void open(Player player, Component open) {
        updateInterface(player, open);
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch (component.getId()) {
            case Components.SMKI_ASSIGNMENT_161:
                switch (button) {
                    case 23:
                    case 26:
                        if (!SlayerManager.getInstance(player).hasTask()) {
                            player.sendMessage("You don't have an active task right now.");
                            break;
                        }
                        if (purchase(player, 30)) {
                            SlayerManager.getInstance(player).clear();
                            player.sendMessage("You have canceled your current task.");
                        }
                        break;
                    case 24:
                    case 27:
                        if (SlayerManager.getInstance(player).getTask() == null) {
                            player.sendMessage("You don't have a slayer task.");
                            break;
                        }
                        if (SlayerManager.getInstance(player).getRemoved().size() >= 4) {
                            player.sendMessage("You can't remove anymore tasks.");
                            break;
                        }
                        if (SlayerManager.getInstance(player).getSlayerPoints() >= 30 && !player.isAdmin()) {
                            int size = SlayerManager.getInstance(player).getRemoved().size();
                            int qp = player.getQuestRepository().getAvailablePoints();
                            if (size == 0 && qp < 50) {
                                player.sendMessage("You need 50 quest points as a requirement in order to block one task.");
                                break;
                            } else if (size == 1 && qp < 100) {
                                player.sendMessage("You need 100 quest points as a requirement in order to block two tasks.");
                                break;
                            } else if (size == 2 && qp < 150) {
                                player.sendMessage("You need 150 quest points as a requirement in order to block three tasks.");
                                break;
                            } else if (size == 3 && qp < 200) {
                                player.sendMessage("You need 200 quest points as a requirement in order to block four tasks.");
                                break;
                            }
                        }
                        if (purchase(player, 100)) {
                            SlayerManager.getInstance(player).getRemoved().add(SlayerManager.getInstance(player).getTask());
                            SlayerManager.getInstance(player).clear();
                            updateInterface(player, player.getInterfaceManager().getOpened());
                        }
                        break;
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                        int index = 3 - (39 - button);
                        if (SlayerManager.getInstance(player).getRemoved().isEmpty() || index > SlayerManager.getInstance(player).getRemoved().size() - 1 || SlayerManager.getInstance(player).getRemoved().get(index) == null) {
                            break;
                        }
                        SlayerManager.getInstance(player).getRemoved().remove(index);
                        updateInterface(player, player.getInterfaceManager().getOpened());
                        break;
                    case 15:
                        openTab(player, BUY);
                        break;
                    case 14:
                        openTab(player, LEARN);
                        break;
                }
                break;
            case Components.SMKI_LEARN_163:
                switch (button) {
                    case 14:
                        openTab(player, ASSIGNMENT);
                        break;
                    case 15:
                        openTab(player, BUY);
                        break;
                    case 22:
                    case 29:
                        if (SlayerManager.getInstance(player).flags.isBroadsUnlocked()) {
                            player.sendMessage("You don't need to learn this ability again.");
                            break;
                        }
                        if (purchase(player, 300)) {
                            SlayerManager.getInstance(player).flags.unlockBroads();
                            updateInterface(player, component);
                        }
                        break;
                    case 23:
                    case 30:
                        if (SlayerManager.getInstance(player).flags.isRingUnlocked()) {
                            player.sendMessage("You don't need to learn this ability again.");
                            break;
                        }
                        if (purchase(player, 300)) {
                            SlayerManager.getInstance(player).flags.unlockRing();
                            updateInterface(player, component);
                        }
                        break;
                    case 24:
                    case 31:
                        if (SlayerManager.getInstance(player).flags.isHelmUnlocked()) {
                            player.sendMessage("You don't need to learn this ability again.");
                            break;
                        }
                        if (purchase(player, 400)) {
                            SlayerManager.getInstance(player).flags.unlockHelm();
                            updateInterface(player, component);
                        }
                        break;
                }
                break;
            case Components.SMKI_BUY_164:
                switch (button) {
                    case 16:
                        openTab(player, LEARN);
                        break;
                    case 17:
                        openTab(player, ASSIGNMENT);
                        break;
                    case 24:
                    case 32:
                        if (purchase(player, 400)) {
                            player.getSkills().addExperience(Skills.SLAYER, 10000, false);
                        }
                        break;
                    case 26:
                    case 33:
                        if (player.getInventory().freeSlots() < 1 && SlayerManager.getInstance(player).getSlayerPoints() >= 75) {
                            player.sendMessage("You don't have enough inventory space.");
                            break;
                        }
                        if (purchase(player, 75)) {
                            player.getInventory().add(new Item(13281), player);
                        }
                        break;
                    case 28:
                    case 36:
                        if (purchase(player, 35)) {
                            player.getInventory().add(new Item(558, 1000), player);
                            player.getInventory().add(new Item(560, 250), player);
                        }
                        break;
                    case 34:
                    case 37:
                        if (purchase(player, 35)) {
                            player.getInventory().add(new Item(13280, 250), player);
                        }
                        break;
                    case 35:
                    case 39:
                        if (purchase(player, 35)) {
                            player.getInventory().add(new Item(4172, 250), player);
                        }
                        break;
                }
                break;
        }
        return true;
    }

    private boolean purchase(Player player, int amount) {
        if (SlayerManager.getInstance(player).getSlayerPoints() < amount) {
            player.sendMessage("You need " + amount + " slayer points in order to purchase this reward.");
            return false;
        }
        SlayerManager.getInstance(player).setSlayerPoints(SlayerManager.getInstance(player).getSlayerPoints() - amount);
        updateInterface(player, player.getInterfaceManager().getOpened());
        return true;
    }

    private void openTab(Player player, Component open) {
        player.getInterfaceManager().open(open);
        updateInterface(player, open);
    }

    private void updateInterface(Player player, Component open) {
        if (open == null) {
            return;
        }
        String space = "";
        String num = String.valueOf(SlayerManager.getInstance(player).getSlayerPoints());
        if (num != "0") {
            for (int i = 0; i < num.length(); i++) {
                space += " ";
            }
        }
        switch (open.getId()) {
            case 161:
                int[] childs = new int[]{35, 30, 31, 32};
                String[] letters = new String[]{"A", "B", "C", "D"};
                Tasks task = null;
                for (int i = 0; i < 4; i++) {
                    task = i > SlayerManager.getInstance(player).getRemoved().size() - 1 ? null : SlayerManager.getInstance(player).getRemoved().get(i);
                    player.getPacketDispatch().sendString(task == null ? letters[i] : task.getName(), open.getId(), childs[i]);
                }
                player.getPacketDispatch().sendString(space + SlayerManager.getInstance(player).getSlayerPoints(), open.getId(), 19);
                break;
            case 163:
                for (int i = 0; i < 3; i++) {
                    switch (i) {
                        case 0:
                            player.getPacketDispatch().sendInterfaceConfig(open.getId(), 25 + i, !SlayerManager.getInstance(player).flags.isBroadsUnlocked());
                            break;
                        case 1:
                            player.getPacketDispatch().sendInterfaceConfig(open.getId(), 25 + i, !SlayerManager.getInstance(player).flags.isRingUnlocked());
                            break;
                        case 2:
                            player.getPacketDispatch().sendInterfaceConfig(open.getId(), 25 + i, !SlayerManager.getInstance(player).flags.isHelmUnlocked());
                            break;
                        default:
                            break;
                    }
                }
                player.getPacketDispatch().sendString(space + SlayerManager.getInstance(player).getSlayerPoints(), open.getId(), 18);
                break;
            case 164:
                player.getPacketDispatch().sendString(space + SlayerManager.getInstance(player).getSlayerPoints(), open.getId(), 20);
                break;
        }
    }

    /**
     * The type Slayer master plugin.
     */
    public class SlayerMasterPlugin extends OptionHandler {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (SlayerMaster m : SlayerMaster.values()) {
                NPCDefinition.forId(m.getNpc()).getHandlers().put("option:rewards", this);
            }
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            if (!hasRequirement(player, Quests.SMOKING_KILLS)) return true;
            openTab(player, BUY);
            return true;
        }

    }

    /**
     * The type Slayer helm craft plugin.
     */
    public static class SlayerHelmCraftPlugin extends UseWithHandler {
        private static final Item SLAYER_HELM = new Item(Items.SLAYER_HELMET_13263);
        private static final Item SPINY_HELMET = new Item(Items.SPINY_HELMET_4551);
        private static final int[] INGREDIENTS = new int[]{Items.NOSE_PEG_4168, Items.EARMUFFS_4166, Items.FACE_MASK_4164, Items.BLACK_MASK_8921};

        /**
         * Instantiates a new Slayer helm craft plugin.
         */
        public SlayerHelmCraftPlugin() {
            super(INGREDIENTS);
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            addHandler(SPINY_HELMET.getId(), ITEM_TYPE, this);
            ClassScanner.definePlugin(new OptionHandler() {

                @Override
                public Plugin<Object> newInstance(Object arg) throws Throwable {
                    ItemDefinition.forId(SLAYER_HELM.getId()).getHandlers().put("option:disassemble", this);
                    return this;
                }

                @Override
                public boolean handle(Player player, Node node, String option) {
                    if (player.getInventory().freeSlots() < 4) {
                        player.sendMessage("You don't have enough inventory space.");
                        return true;
                    }
                    player.lock(1);
                    if (player.getInventory().remove(node.asItem())) {
                        for (int id : INGREDIENTS) {
                            player.getInventory().add(new Item(id));
                        }
                        player.getInventory().add(SPINY_HELMET);
                    }
                    player.sendMessage("You disassemble your Slayer helm.");
                    return true;
                }

            });
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            final Player player = event.getPlayer();
            if (player.getSkills().getStaticLevel(Skills.CRAFTING) < 55) {
                player.sendMessage("You need a Crafting level of at least 55 in order to do this.");
                return true;
            }
            if (!SlayerManager.getInstance(player).flags.isHelmUnlocked()) {
                player.sendMessage("You need to unlock the ability to do that first.");
                return true;
            }
            if (!player.getInventory().containItems(INGREDIENTS)) {
                player.sendMessages("You need a nosepeg, facemask, earmuffs, spiny helmet, and a black mask in", "your inventory in order to construct a Slayer helm.");
                return true;
            }
            player.lock(1);
            if (player.getInventory().remove(SPINY_HELMET)) {
                for (int id : INGREDIENTS) {
                    if (!player.getInventory().remove(new Item(id))) {
                        return true;
                    }
                }
                player.getInventory().add(SLAYER_HELM);
                player.sendMessage("You combine the items into a Slayer helm.");
            }
            return true;
        }

    }
}
