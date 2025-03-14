package content.minigame.castlewars.handlers

import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

/**
 * Castle Wars
 *
 * Author: dginovker, Woahscam
 * See: [dginovker's GitLab](https://gitlab.com/dginovker), [Woahscam GitLab](https://gitlab.com/Woahscam)
 * Since: April 10, 2023
 *
 * For developers interested in contributing:
 * - In-Game interface IDs: 57 (Lobby), 58 (Saradomin team), 59 (Zamorak team).
 *
 * Related CS2 Scripts:
 * - `SCRIPT 1366` (Iface: 57): Lobby countdown until game starts
 * - `SCRIPT 1367` (Iface: 58): Saradomin team interface
 * - `SCRIPT 1368` (Iface: 59): Zamorak team interface
 *
 * ```
 * VARP/BIT    VALUES        USE
 * (S) 136     0 / 1 - 100   % Health of the door
 * (S) 137     0 / 1         Castle side door Locked / Unlocked
 * (S) 138     0 / 1         Tunnel 1 Collapsed / Cleared
 * (S) 139     0 / 1         Tunnel 2 Collapsed / Cleared
 * (S) 140     0 / 1         Catapult Operational / Destroyed
 * (B) 143     0 / 1 / 2     Saradomin flag state Safe / Taken / Dropped
 * (B) 145     x             Saradomin flag score amount
 * (Z) 146     0 / 1 - 100   % Health of the door
 * (Z) 147     0 / 1         Castle side door Locked / Unlocked
 * (Z) 148     0 / 1         Tunnel 1 Collapsed / Cleared
 * (Z) 149     0 / 1         Tunnel 2 Collapsed / Cleared
 * (Z) 150     0 / 1         Catapult Operational / Destroyed
 * (B) 153     0 / 1 / 2     Zamorak flag state Safe / Taken / Dropped
 * (B) 155     x             Zamorak flag score amount
 * (B) VARP 380x             Match time left
 * ```
 */
object CastleWars {
    const val joinSaradominTeamPortal: Int = Scenery.SARADOMIN_PORTAL_4387
    const val joinZamorakTeamPortal: Int = Scenery.ZAMORAK_PORTAL_4388
    const val saradominLeaveLobbyPortal: Int = Scenery.PORTAL_4389
    const val zamorakLeaveLobbyPortal: Int = Scenery.PORTAL_4390
    const val joinGuthixTeamPortal: Int = Scenery.GUTHIX_PORTAL_4408
    const val cwCastleClimbingRope: Int = Scenery.CLIMBING_ROPE_36312
    const val castleWaterTap: Int = Scenery.TAP_4482
    const val cwSteppingStones: Int = Scenery.STEPPING_STONE_4411

    val cwTableItemRewardMap =
        mapOf(
            Scenery.TABLE_36573 to Items.TOOLKIT_4051,
            Scenery.TABLE_36580 to Items.TOOLKIT_4051,
            Scenery.TABLE_36574 to Items.ROCK_4043,
            Scenery.TABLE_36581 to Items.ROCK_4043,
            Scenery.TABLE_36575 to Items.BARRICADE_4053,
            Scenery.TABLE_36582 to Items.BARRICADE_4053,
            Scenery.TABLE_36576 to Items.CLIMBING_ROPE_4047,
            Scenery.TABLE_36583 to Items.CLIMBING_ROPE_4047,
            Scenery.TABLE_36577 to Items.EXPLOSIVE_POTION_4045,
            Scenery.TABLE_36584 to Items.EXPLOSIVE_POTION_4045,
            Scenery.TABLE_36578 to Items.BRONZE_PICKAXE_1265,
            Scenery.TABLE_36585 to Items.BRONZE_PICKAXE_1265,
            Scenery.TABLE_36579 to Items.BANDAGES_4049,
            Scenery.TABLE_36586 to Items.BANDAGES_4049,
        )

    val cwCastleBattlementsMap =
        mapOf(
            Scenery.BATTLEMENTS_4446 to Scenery.BATTLEMENTS_36313,
            Scenery.BATTLEMENTS_4447 to Scenery.BATTLEMENTS_36314,
        )

    const val saradominTeamHoodedCloak: Int = Items.HOODED_CLOAK_4041
    const val zamorakTeamHoodedCloak: Int = Items.HOODED_CLOAK_4042
    const val saradominFlag: Int = Items.SARADOMIN_BANNER_4037
    const val zamorakFlag: Int = Items.ZAMORAK_BANNER_4039
    const val cwPickaxe: Int = Items.BRONZE_PICKAXE_1265
    const val cwRock: Int = Items.ROCK_4043
    const val cwExplosivePotion: Int = Items.EXPLOSIVE_POTION_4045
    const val cwClimbingRope: Int = Items.CLIMBING_ROPE_4047
    const val cwBandages: Int = Items.BANDAGES_4049
    const val cwToolkit: Int = Items.TOOLKIT_4051
    const val cwBarricade: Int = Items.BARRICADE_4053
    const val cwManualBook: Int = Items.CASTLEWARS_MANUAL_4055
    const val cwTicketRewardCurrency: Int = Items.CASTLE_WARS_TICKET_4067

    const val sheep = NPCs.SHEEP_1529
    const val imp = NPCs.IMP_1531
    const val rabbit = NPCs.RABBIT_1530
    const val unknownCwarsBarricade1 = NPCs.BARRICADE_1532
    const val unknownCwarsBarricadeOnFire = NPCs.BARRICADE_1533
    const val unknownCwarsBarricade2 = NPCs.BARRICADE_1534
    const val unknownCwarsBarricade3 = NPCs.BARRICADE_1535

    val lobbyBankArea: ZoneBorders = ZoneBorders(2440, 3092, 2444, 3086, 0)

    const val saradominName = "Saradomin"
    const val zamorakName = "Zamorak"
    const val guthixName = "Guthix"
    const val portalAttribute = "castlewars_portal"
    const val invFullMessage = "Your inventory is too full to hold any more "

    const val gameTimeMinutes = 20
    const val gameCooldownMinutes = 5
    const val ropeAliveTicks = -1
}
