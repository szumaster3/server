package core.game.node.entity.combat.equipment;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.InterfaceType;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager;
import core.game.node.item.Item;
import core.game.system.config.ItemConfigParser;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.InterfaceConfigContext;
import core.net.packet.context.InterfaceContext;
import core.net.packet.context.StringContext;
import core.net.packet.out.Interface;
import core.net.packet.out.InterfaceConfig;
import core.net.packet.out.StringPacket;
import org.rs.consts.Components;

import static core.api.ContentAPIKt.setVarp;

/**
 * The type Weapon interface.
 */
public final class WeaponInterface extends Component {

    private static final int[] MODERN_SPELL_IDS = { 1, 4, 6, 8, 10, 14, 17, 20, 24, 27, 33, 38, 45, 48, 52, 55 };

    private static final int[] ANCIENT_SPELL_IDS = { 8, 12, 4, 0, 10, 14, 6, 2, 9, 13, 5, 1, 11, 15, 7, 3, 16, 17, 18, 19 };

    private static final int[] SLAYER_STAFF_SPELL_IDS = { 22, 31, 45, 48, 52, 55 };

    private static final int[] VOID_STAFF_SPELL_IDS = { 22, 42, 45, 48, 52, 55 };

    /**
     * The constant DEFAULT_ANIMS.
     */
    public static final Animation[] DEFAULT_ANIMS = { new Animation(422, Priority.HIGH), new Animation(423, Priority.HIGH), new Animation(422, Priority.HIGH), new Animation(422, Priority.HIGH) };

    /**
     * The constant BONUS_STAB.
     */
    public static final int BONUS_STAB = 0;

    /**
     * The constant BONUS_SLASH.
     */
    public static final int BONUS_SLASH = 1;

    /**
     * The constant BONUS_CRUSH.
     */
    public static final int BONUS_CRUSH = 2;

    /**
     * The constant BONUS_MAGIC.
     */
    public static final int BONUS_MAGIC = 3;

    /**
     * The constant BONUS_RANGE.
     */
    public static final int BONUS_RANGE = 4;

    /**
     * The constant STYLE_ACCURATE.
     */
    public static final int STYLE_ACCURATE = 0;

    /**
     * The constant STYLE_AGGRESSIVE.
     */
    public static final int STYLE_AGGRESSIVE = 1;

    /**
     * The constant STYLE_CONTROLLED.
     */
    public static final int STYLE_CONTROLLED = 2;

    /**
     * The constant STYLE_DEFENSIVE.
     */
    public static final int STYLE_DEFENSIVE = 3;

    /**
     * The constant STYLE_RANGE_ACCURATE.
     */
    public static final int STYLE_RANGE_ACCURATE = 4;

    /**
     * The constant STYLE_RAPID.
     */
    public static final int STYLE_RAPID = 5;

    /**
     * The constant STYLE_LONG_RANGE.
     */
    public static final int STYLE_LONG_RANGE = 6;

    /**
     * The constant STYLE_DEFENSIVE_CAST.
     */
    public static final int STYLE_DEFENSIVE_CAST = 7;

    /**
     * The constant STYLE_CAST.
     */
    public static final int STYLE_CAST = 8;

    private final Player player;

    private WeaponInterfaces current;

    private boolean specialBar;

    private Animation[] attackAnimations;

    /**
     * Instantiates a new Weapon interface.
     *
     * @param player the player
     */
    public WeaponInterface(Player player) {
        super(92);
        this.player = player;
        player.addExtension(WeaponInterface.class, this);
    }

    @Override
    public void open(Player player) {
        current = null;
        updateInterface();
    }

    private void open() {
        ComponentDefinition definition = ComponentDefinition.forId(Components.WEAPON_FISTS_SEL_92);
        boolean resizable = player.getInterfaceManager().isResizable();
        PacketRepository.send(Interface.class, new InterfaceContext(player, definition.getWindowPaneId(resizable), definition.getChildId(resizable), id, definition.isWalkable()));
        int slot = ensureStyleIndex(player, player.getSettings().getAttackStyleIndex());
        if (slot != player.getSettings().getAttackStyleIndex()) {
            player.getSettings().toggleAttackStyleIndex(slot);
        }
        player.getProperties().setAttackStyle(current.getAttackStyles()[slot]);
        checkStaffConfigs(slot);
    }

    private int ensureStyleIndex(Player player, int slot) {
        AttackStyle style = player.getProperties().getAttackStyle();
        if (slot >= current.getAttackStyles().length) {
            slot = current.getAttackStyles().length - 1;
            if (style != null) {
                for (int i = slot; i >= 0; i--) {
                    if (current.getAttackStyles()[i].style == style.style) {
                        return i;
                    }
                }
            }
            return slot;
        }
        if (style != null && current.getAttackStyles()[slot].style != style.style) {
            for (int i = current.getAttackStyles().length - 1; i >= 0; i--) {
                if (current.getAttackStyles()[i].style == style.style) {
                    return i;
                }
            }
        }
        return slot;
    }

    private int getConfig(int buttons, int interfaceId){
        if(interfaceId == Components.WEAPON_STAFF_SEL_90){
            return 87;//Return Config
        }
        if(interfaceId != Components.WEAPON_WHIP_SEL_93 &&
            interfaceId != Components.WEAPON_WARHAMMER_SEL_76 &&
            interfaceId != Components.WEAPON_XBOW_SEL_79 &&
            interfaceId != Components.WEAPON_HALBERD_SEL_84 &&
            interfaceId != Components.WEAPON_THROWN_SEL_91)
        {
            switch(buttons){
                case 3:
                    return 13;
                case 4:
                    return 12;
            }
        } else {
            return 10;
        }
        return 10;
    }

    /**
     * Update interface.
     */
    public void updateInterface() {
        player.getInterfaceManager().getTabs()[0] = this;
        Item weapon = player.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
        WeaponInterfaces inter = getWeaponInterface(weapon);
        String name;
        if (weapon != null) {
            name = weapon.getDefinition().getName();
            specialBar = weapon.getDefinition().getConfiguration(ItemConfigParser.HAS_SPECIAL, false);
            attackAnimations = weapon.getDefinition().getConfiguration(ItemConfigParser.ATTACK_ANIMS, DEFAULT_ANIMS);
        } else {
            name = "Unarmed";
            specialBar = false;
            attackAnimations = DEFAULT_ANIMS;
        }
        if (inter != current) {
            id = inter.interfaceId;
            current = inter;
            open();
            player.getProperties().getCombatPulse().updateStyle();
        }
        if (player.getSettings().getAttackStyleIndex() < attackAnimations.length && !player.getAppearance().isNpc()) {
            player.getProperties().setAttackAnimation(attackAnimations[player.getSettings().getAttackStyleIndex()]);
        }
        if (current != WeaponInterfaces.STAFF) {
            selectAutoSpell(-1, false);
            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, id, getConfig(current.getAttackStyles().length, current.getInterfaceId()), !specialBar));
        } else { //if staff
            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, id, 87, !specialBar));
        }
        if (!canAutocast(false)) {
            if (current == WeaponInterfaces.STAFF && player.getAttribute("autocast_select", false)) {
                open();
            }
            selectAutoSpell(-1, true);
        }
        PacketRepository.send(StringPacket.class, new StringContext(player, name, id, 0));
        if (player.getSettings().isSpecialToggled()) {
            player.getSettings().toggleSpecialBar();
        }
    }

    /**
     * Sets attack style.
     *
     * @param button the button
     * @return the attack style
     */
    public boolean setAttackStyle(int button) {
        int slot = button - 1;
        if (current != WeaponInterfaces.STAFF) {
            slot--;
        }
        if (current == WeaponInterfaces.WARHAMMER_MAUL || (current.attackStyles.length > 2 && current.attackStyles[2].bonusType == BONUS_RANGE && current.getInterfaceId() != Components.WEAPON_THROWN_SEL_91)) {
            slot = button == 4 ? 1 : button == 3 ? 2 : 0;
        } else if (current == WeaponInterfaces.CLAWS) {
            slot = button == 5 ? 1 : button == 3 ? 3 : slot;
        }
        if (current == WeaponInterfaces.AXE) {
            if (button == 5) {
                slot = 1;
            } else if (button == 3) {
                slot = 3;
            }
        }
        if (slot < 0 || slot >= current.getAttackStyles().length) {
            return false;
        }
        AttackStyle style = current.getAttackStyles()[slot];
        player.getProperties().setAttackStyle(style);
        player.getSettings().toggleAttackStyleIndex(slot);
        if (slot < attackAnimations.length && !player.getAppearance().isNpc()) {
            player.getProperties().setAttackAnimation(attackAnimations[slot]);
        }
        checkStaffConfigs(button - 1);
        return true;
    }

    private void checkStaffConfigs(int slot) {
        if (current != WeaponInterfaces.STAFF) {
            selectAutoSpell(-1, false);
            return;
        }
        boolean defensive = slot == 3;
        setVarp(player, 439, defensive ? -5 : 0);
        if (slot > 2) {
            setVarp(player, 43, defensive ? -1 : 3);
        }
    };

    /**
     * Gets autospell id.
     *
     * @param spellId the spell id
     * @return the autospell id
     */
    public int getAutospellId(int spellId) {
        boolean modern = player.getSpellBookManager().getSpellBook() == Components.MAGIC_192;
        int[] data = modern ? MODERN_SPELL_IDS : ANCIENT_SPELL_IDS;
        if (modern && player.getEquipment().getNew(3).getName().equalsIgnoreCase("Slayer's staff")) {
            data = SLAYER_STAFF_SPELL_IDS;
        }
        if (modern && player.getEquipment().getNew(3).getName().equalsIgnoreCase("Void knight mace")) {
            data = VOID_STAFF_SPELL_IDS;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == spellId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Select auto spell.
     *
     * @param buttonId          the button id
     * @param adjustAttackStyle the adjust attack style
     */
    public void selectAutoSpell(int buttonId, boolean adjustAttackStyle) {
        boolean modern = player.getSpellBookManager().getSpellBook() == Components.MAGIC_192;
        int[] data = modern ? MODERN_SPELL_IDS : ANCIENT_SPELL_IDS;
        if (modern && player.getEquipment().getNew(3).getName().equalsIgnoreCase("Slayer's staff")) {
            data = SLAYER_STAFF_SPELL_IDS;
        }
        if (modern && player.getEquipment().getNew(3).getName().equalsIgnoreCase("Void knight mace")) {
            data = VOID_STAFF_SPELL_IDS;
        }
        CombatSpell current = player.getProperties().getAutocastSpell();
        if (buttonId >= data.length) {
            return;
        }
        int configStart = modern ? 45 : 13;
        if (current != null) {
            for (int index = 0; index < data.length; index++) {
                if (data[index] == current.spellId) {
                    player.getPacketDispatch().sendInterfaceConfig(Components.WEAPON_STAFF_SEL_90, configStart + (2 * index), true);
                    player.getPacketDispatch().sendInterfaceConfig(Components.WEAPON_STAFF_SEL_90, 100 + configStart + (2 * index), true);
                }
            }
        }
        if (buttonId < 0) {
            player.getProperties().setAutocastSpell(null);
            if (adjustAttackStyle && current != null) {
                setAttackStyle(3);
                player.getProperties().getCombatPulse().updateStyle();
            }
            return;
        }
        boolean defensive = player.getSettings().getAttackStyleIndex() == 3;
        player.getPacketDispatch().sendInterfaceConfig(Components.WEAPON_STAFF_SEL_90, 183, defensive);
        player.getPacketDispatch().sendInterfaceConfig(Components.WEAPON_STAFF_SEL_90, 83, !defensive);
        current = (CombatSpell) (modern ? SpellBookManager.SpellBook.MODERN.getSpell(data[buttonId]) : SpellBookManager.SpellBook.ANCIENT.getSpell(data[buttonId]));
        player.getProperties().setAutocastSpell(current);
        int configId = configStart + (2 * buttonId);
        if (modern && player.getEquipment().getNew(3).getName().equalsIgnoreCase("Slayer's staff") || modern && player.getEquipment().getNew(3).getName().equalsIgnoreCase("Void knight mace")) {
            boolean slayer = player.getEquipment().getNew(3).getName().equalsIgnoreCase("Slayer's staff");
            switch (buttonId) {
                case 0:
                    configId = 77;
                    break;
                case 1:
                    if (slayer) {
                        configId = 79;
                    } else {
                        configId = 81;
                    }
                    break;
                case 2:
                    configId = 69;
                    break;
                case 3:
                    configId = 71;
                    break;
                case 4:
                    configId = 73;
                    break;
                case 5:
                    configId = 75;
                    break;

            }
            if (configId >= 85 || configId <= 65) {
                return;
            } else {
                player.getPacketDispatch().sendInterfaceConfig(Components.WEAPON_STAFF_SEL_90, (defensive ? 100 : 0) + configId, false);
            }
        } else {
            configId += defensive ? 100 : 0;
            switch (buttonId) {
                case 16:
                    configId = defensive ? 265 : 253;
                    break;
                case 17:
                    configId = defensive ? 268 : 256;
                    break;
                case 18:
                    configId = defensive ? 271 : 259;
                    break;
                case 19:
                    configId = defensive ? 274 : 262;
                    break;
            }
            player.getPacketDispatch().sendInterfaceConfig(Components.WEAPON_STAFF_SEL_90, configId, false);
        }

    }

    /**
     * Open autocast select.
     */
    public void openAutocastSelect() {
        if (current != WeaponInterfaces.STAFF) {
            return;
        }
        if (!canAutocast(true)) {
            setAttackStyle(3);
            return;
        }
        player.setAttribute("autocast_select", true);
        int id = player.getSpellBookManager().getSpellBook() == 193 ? 797 : 319;
        boolean slayer = player.getEquipment().getNew(3).getName().equalsIgnoreCase("Slayer's staff");
        boolean mace = player.getEquipment().getNew(3).getName().equalsIgnoreCase("Void knight mace");
        if (slayer) {
            id = 310;
        }
        if (mace) {
            id = 406;
        }
        Component component = new Component(id);
        component.getDefinition().setTabIndex(0);
        component.getDefinition().setType(InterfaceType.TAB);
        player.setAttribute("autocast_component",component);
        player.getInterfaceManager().openTab(component);
    }

    /**
     * Can autocast boolean.
     *
     * @param message the message
     * @return the boolean
     */
    public boolean canAutocast(boolean message) {
        if (current != WeaponInterfaces.STAFF) {
            return false;
        }
        if (player.getSpellBookManager().getSpellBook() == SpellBookManager.SpellBook.LUNAR.getInterfaceId()) {
            if (message) {
                player.getPacketDispatch().sendMessage("You can't autocast Lunar magic.");
            }
            return false;
        }
        boolean ancientStaff = player.getEquipment().getNew(3).getName().contains("ncient staff") || player.getEquipment().getNew(3).getName().contains("uriel's staff");;
        if ((player.getSpellBookManager().getSpellBook() == Components.MAGIC_192 && ancientStaff) || (player.getSpellBookManager().getSpellBook() == Components.MAGIC_ZAROS_193 && !ancientStaff)) {
            if (message) {
                player.getPacketDispatch().sendMessage("You can only autocast ancient magicks with an Ancient or Zuriel's staff.");
            }
            return false;
        }
        return true;
    }

    /**
     * Gets weapon interface.
     *
     * @param weapon the weapon
     * @return the weapon interface
     */
    public static WeaponInterfaces getWeaponInterface(Item weapon) {
        if (weapon == null) {
            return WeaponInterfaces.values()[0];
        }
        int slot = weapon.getDefinition().getConfiguration(ItemConfigParser.WEAPON_INTERFACE, 0);
        return WeaponInterfaces.values()[slot];
    }

    /**
     * The type Attack style.
     */
    public static class AttackStyle {

        private final int style;

        private final int bonusType;

        /**
         * Instantiates a new Attack style.
         *
         * @param style     the style
         * @param bonusType the bonus type
         */
        public AttackStyle(int style, int bonusType) {
            this.style = style;
            this.bonusType = bonusType;
        }

        /**
         * Gets style.
         *
         * @return the style
         */
        public int getStyle() {
            return style;
        }

        /**
         * Gets bonus type.
         *
         * @return the bonus type
         */
        public int getBonusType() {
            return bonusType;
        }
    }

    /**
     * The enum Weapon interfaces.
     */
    public static enum WeaponInterfaces {

        /**
         * The Unarmed.
         */
        UNARMED(Components.WEAPON_FISTS_SEL_92, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The Staff.
         */
        STAFF(Components.WEAPON_STAFF_SEL_90, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE_CAST, BONUS_MAGIC), new AttackStyle(STYLE_CAST, BONUS_MAGIC)),

        /**
         * The Axe.
         */
        AXE(Components.WEAPON_BAXE_SEL_75, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The Scepter.
         */
        SCEPTER(Components.WEAPON_SCEPTER_SEL_85, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The Pickaxe.
         */
        PICKAXE(Components.WEAPON_PICKAXE_SEL_83, new AttackStyle(STYLE_ACCURATE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The Sword dagger.
         */
        SWORD_DAGGER(Components.WEAPON_DAGGER_SEL_89, new AttackStyle(STYLE_ACCURATE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The Scimitar.
         */
        SCIMITAR(Components.WEAPON_SCIMITAR_SEL_81, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The Two h sword.
         */
        TWO_H_SWORD(Components.WEAPON_2H_SWORD_SEL_82, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The Mace.
         */
        MACE(Components.WEAPON_MACE_SEL_88, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The Claws.
         */
        CLAWS(Components.WEAPON_CLAWS_SEL_78, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The Warhammer maul.
         */
        WARHAMMER_MAUL(Components.WEAPON_WARHAMMER_SEL_76, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The Whip.
         */
        WHIP(Components.WEAPON_WHIP_SEL_93, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_SLASH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The Flowers.
         */
        FLOWERS(Components.WEAPON_WARHAMMER_SEL_76, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The Mud pie.
         */
        MUD_PIE(Components.WEAPON_THROWN_SEL_91, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The Spear.
         */
        SPEAR(Components.WEAPON_SPEAR_SEL_87, new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_CONTROLLED, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The Halberd.
         */
        HALBERD(Components.WEAPON_HALBERD_SEL_84, new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The Bow.
         */
        BOW(Components.WEAPON_BOW_SEL_77, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The Crossbow.
         */
        CROSSBOW(Components.WEAPON_XBOW_SEL_79, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The Thrown weapons.
         */
        THROWN_WEAPONS(Components.WEAPON_THROWN_SEL_91, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The Chinchompa.
         */
        CHINCHOMPA(Components.WEAPON_CHINCHOMPA_SEL_473, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The Fixed device.
         */
        FIXED_DEVICE(Components.WEAPON_FIXED_DEVICE_SEL_80, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH)),

        /**
         * The Salamander.
         */
        SALAMANDER(Components.WEAPON_SALAMANDER_SEL_474, new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_DEFENSIVE_CAST, BONUS_MAGIC)),

        /**
         * The Scythe.
         */
        SCYTHE(Components.WEAPON_SCYTHE_SEL_86, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The Ivandis flail.
         */
        IVANDIS_FLAIL(Components.WEAPON_SCEPTER_SEL_85, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH));

        private final int interfaceId;

        private final AttackStyle[] attackStyles;

        private WeaponInterfaces(int interfaceId, AttackStyle... attackStyles) {
            this.interfaceId = interfaceId;
            this.attackStyles = attackStyles;
        }

        /**
         * Gets interface id.
         *
         * @return the interface id
         */
        public int getInterfaceId() {
            return interfaceId;
        }

        /**
         * Get attack styles attack style [ ].
         *
         * @return the attack style [ ]
         */
        public AttackStyle[] getAttackStyles() {
            return attackStyles;
        }
    }

    /**
     * Gets weapon interface.
     *
     * @return the weapon interface
     */
    public WeaponInterfaces getWeaponInterface() {
        return current;
    }

    /**
     * Is special bar boolean.
     *
     * @return the boolean
     */
    public boolean isSpecialBar() {
        return specialBar;
    }

}
