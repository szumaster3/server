package content.global.skill.summoning

import core.api.closeInterface
import core.api.sendInputDialogue
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class SummoningCreationPlugin : ComponentPlugin() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(669, this)
        ComponentDefinition.put(673, this)
        ClassScanner.definePlugin(ObeliskHandler())
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        when (button) {
            17, 18 -> {
                closeInterface(player)
                SummoningCreator.configure(player, button == 17)
                return true
            }
        }

        when (opcode) {
            155, 196, 124, 199 -> {
                val pouch = getPouch(component, slot)
                SummoningCreator.create(player, getItemAmount(opcode), pouch)
                return true
            }

            234 -> {
                sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                    val pouch = getPouch(component, slot)
                    if (value is Int && value > 0) {
                        SummoningCreator.create(player, value, pouch)
                    } else {
                        sendMessage(player, "Please enter a valid integer amount greater than zero.")
                    }
                }
                return true
            }

            166 ->
                SummoningPouch
                    .forSlot(if (slot > 50) slot - 1 else slot)
                    ?.let { SummoningCreator.list(player, it) }

            168 ->
                sendMessage(
                    player,
                    ItemDefinition.forId(SummoningScroll.forId(if (slot > 50) slot - 1 else slot)!!.itemId).examine,
                )
        }
        return true
    }

    private fun getPouch(
        component: Component,
        slot: Int,
    ) = if (component.id ==
        669
    ) {
        SummoningPouch.forSlot(getAdjustedSlot(slot))
    } else {
        SummoningScroll.forId(getAdjustedSlot(slot))
    }

    private fun getAdjustedSlot(slot: Int) = if (slot > 50) slot - 1 else slot

    private fun getItemAmount(opcode: Int): Int {
        return when (opcode) {
            155 -> 1
            196 -> 5
            124 -> 10
            199 -> 28
            else -> -1
        }
    }

    class ObeliskHandler :
        UseWithHandler(
            Items.SPIRIT_TERRORBIRD_POUCH_12007,
            Items.GRANITE_CRAB_POUCH_12009,
            Items.PRAYING_MANTIS_POUCH_12011,
            Items.GIANT_ENT_POUCH_12013,
            Items.SPIRIT_COBRA_POUCH_12015,
            Items.SPIRIT_DAGANNOTH_POUCH_12017,
            Items.THORNY_SNAIL_POUCH_12019,
            Items.BEAVER_POUCH_12021,
            Items.KARAM_OVERLORD_POUCH_12023,
            Items.HYDRA_POUCH_12025,
            Items.SPIRIT_JELLY_POUCH_12027,
            Items.BUNYIP_POUCH_12029,
            Items.WAR_TORTOISE_POUCH_12031,
            Items.FRUIT_BAT_POUCH_12033,
            Items.ABYSSAL_PARASITE_POUCH_12035,
            Items.ABYSSAL_LURKER_POUCH_12037,
            Items.UNICORN_STALLION_POUCH_12039,
            Items.MAGPIE_POUCH_12041,
            Items.DREADFOWL_POUCH_12043,
            Items.STRANGER_PLANT_POUCH_12045,
            Items.SPIRIT_WOLF_POUCH_12047,
            Items.DESERT_WYRM_POUCH_12049,
            Items.EVIL_TURNIP_POUCH_12051,
            Items.VAMPIRE_BAT_POUCH_12053,
            Items.SPIRIT_SCORPION_POUCH_12055,
            Items.ARCTIC_BEAR_POUCH_12057,
            Items.SPIRIT_SPIDER_POUCH_12059,
            Items.BLOATED_LEECH_POUCH_12061,
            Items.SPIRIT_KALPHITE_POUCH_12064,
            Items.HONEY_BADGER_POUCH_12065,
            Items.GRANITE_LOBSTER_POUCH_12069,
            Items.MACAW_POUCH_12071,
            Items.BRONZE_MINOTAUR_POUCH_12073,
            Items.IRON_MINOTAUR_POUCH_12075,
            Items.STEEL_MINOTAUR_POUCH_12077,
            Items.MITHRIL_MINOTAUR_POUCH_12079,
            Items.ADAMANT_MINOTAUR_POUCH_12081,
            Items.RUNE_MINOTAUR_POUCH_12083,
            Items.SMOKE_DEVIL_POUCH_12085,
            Items.BULL_ANT_POUCH_12087,
            Items.WOLPERTINGER_POUCH_12089,
            Items.COMPOST_MOUND_POUCH_12091,
            Items.PACK_YAK_POUCH_12093,
            Items.SP_COCKATRICE_POUCH_12095,
            Items.SP_GUTHATRICE_POUCH_12097,
            Items.SP_SARATRICE_POUCH_12099,
            Items.SP_ZAMATRICE_POUCH_12101,
            Items.SP_PENGATRICE_POUCH_12103,
            Items.SP_CORAXATRICE_POUCH_12105,
            Items.SP_VULATRICE_POUCH_12107,
            Items.BARKER_TOAD_POUCH_12123,
            Items.IBIS_POUCH_12531,
            Items.LABRADOR_PUPPY_12710,
            Items.SWAMP_TITAN_POUCH_12776,
            Items.SPIRIT_MOSQUITO_POUCH_12778,
            Items.VOID_SPINNER_POUCH_12781,
            Items.FORGE_REGENT_POUCH_12782,
            Items.SPIRIT_LARUPIA_POUCH_12784,
            Items.GEYSER_TITAN_POUCH_12786,
            Items.LAVA_TITAN_POUCH_12788,
            Items.STEEL_TITAN_POUCH_12790,
            Items.OBSIDIAN_GOLEM_POUCH_12792,
            Items.TALON_BEAST_POUCH_12794,
            Items.ABYSSAL_TITAN_POUCH_12796,
            Items.VOID_TORCHER_POUCH_12798,
            Items.GIANT_CHINCHOMPA_POUCH_12800,
            Items.FIRE_TITAN_POUCH_12802,
            Items.MOSS_TITAN_POUCH_12804,
            Items.ICE_TITAN_POUCH_12806,
            Items.SPIRIT_TZ_KIH_POUCH_12808,
            Items.SPIRIT_KYATT_POUCH_12812,
            Items.VOID_SHIFTER_POUCH_12814,
            Items.PYRELORD_POUCH_12816,
            Items.VOID_RAVAGER_POUCH_12818,
            Items.RAVENOUS_LOCUST_POUCH_12820,
            Items.IRON_TITAN_POUCH_12822,
        ) {
        private val sceneryIDs = intArrayOf(28716, 28719, 28722, 28725, 28278, 28731, 28734)

        @Throws(Throwable::class)
        override fun newInstance(arg: Any?): Plugin<Any> {
            for (id in sceneryIDs) {
                addHandler(id, OBJECT_TYPE, this)
            }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            event.player.let { SummoningCreator.open(it, false) }
            return true
        }
    }
}
