package content.region.kandarin.seers.quest.elemental_quest_2.plugin

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

object EW2Utils {
    const val foundBook = "/save:ew2:book-found"
    const val questProgress = "/save:ew2:progress"

    const val PullSupport = "/save:ew2:pulled"
    const val TurnSupport = "/save:ew2:turn"
    const val FillingWaterTank = "/save:ew2:fill"

    const val PrimedBar = Items.PRIMED_BAR_9727
    const val ElementalBar = Items.ELEMENTAL_METAL_2893
    const val ElementalMindBar = Items.ELEMENTAL_MIND_BAR_9728

    const val elementalClaw = Items.CRANE_CLAW_9720

    const val EW2_QUEST_VARBIT = 2639
    const val EW2_KEY_VARBIT = 2640
    const val EW2_HATCH_VARBIT = 2641
    const val EW2_PIPE_VARBIT = 2650
    const val EW2_COGS_VARBIT = 2657

    const val Pin = Scenery.PIN_18666

    const val SmallCog = Items.SMALL_COG_9726
    const val MediumCog = Items.MEDIUM_COG_9725
    const val LargeCog = Items.LARGE_COG_9724

    const val SmallCogCrate = Scenery.CRATE_18612
    const val MediumCogCrate = Scenery.CRATE_18613
    const val LargeCogCrate = Scenery.CRATE_18616

    const val TakeSmallCog = Scenery.COG_18670
    const val TakeMediumCog = Scenery.COG_18671
    const val TakeLargeCog = Scenery.COG_18669

    const val EW2_SMALL_COG_VARBIT = 2655
    const val EW2_MEDIUM_COG_VARBIT = 2657
    const val EW2_LARGE_COG_VARBIT = 2656

    val allCogs = intArrayOf(SmallCog, MediumCog, LargeCog)

    const val CraneSchematic = Items.CRANE_SCHEMATIC_9718
    const val LeverSchematic = Items.LEVER_SCHEMATIC_9719
    const val SchematicCrate = Scenery.SCHEMATIC_CRATE_18711

    const val Pipe = Items.PIPE_9723
    const val PipeCrate = Scenery.CRATE_18507
    const val BrokenPipe = Scenery.PIPING_18650

    const val OldCrane = Scenery.OLD_CRANE_18623
    const val OldCrane2 = Scenery.OLD_CRANE_18638

    const val BrokenCraneUp = 18758
    const val BrokenCraneDown = 18757

    const val BrokenCraneUpOnCorrectSide = 18760
    const val BrokenCraneDownOnCorrectSide = 18759

    const val CraneJigUp = 18762
    const val CraneJigDown = 18761

    const val CraneDownOnCorrectSide = 18763
    const val CraneUpOnCorrectSide = 18764

    const val CraneElementalBarUp = 18749
    const val CraneElementalBarDown = 18749

    const val CraneElementalBarJigCartUp = 18751
    const val CraneElementalBarJigCartDown = 18750

    const val PulledLever = 18691

    const val CartLever = Scenery.LEVER_18620

    const val LeftLever = Scenery.AN_OLD_LEVER_18621

    const val RightLever = Scenery.AN_OLD_LEVER_18622

    const val CenterLever = Scenery.AN_OLD_LEVER_18640

    const val NorthLever = Scenery.AN_OLD_LEVER_18648

    const val EastLever = Scenery.AN_OLD_LEVER_18663

    const val CorkscrewLever = Scenery.CORKSCREW_LEVER_18649

    const val Piping = Scenery.PIPING_18650
    const val PipingDoorClose = Scenery.DOOR_18651
    const val PipingDoorOpen = Scenery.DOOR_18652
    const val PipingRails = Scenery.DOOR_18653
    const val PipingJigElementalMindBar = Scenery.DOOR_18654
    const val PipingElementalMindBarOpen = Scenery.DOOR_18655
    const val PipingElementalMindBarInside = Scenery.DOOR_18656
    const val PipingJigElementalBar = Scenery.DOOR_18657
    const val PipingJigElementalBarOpen = Scenery.DOOR_18658
    const val PipingJigElementalBarInside = Scenery.DOOR_18659

    const val WaterTankEmpty = Scenery.WATER_TANK_18660
    const val WaterTankFull = Scenery.WATER_TANK_18661

    const val WaterValveLeft = Scenery.WATER_VALVE_18646
    const val WaterValveRight = Scenery.WATER_VALVE_18647

    const val turnValveAnimation = 4861

    const val UsingExtractorHatChairAnimation = 4884
    const val ImproperlyUsingExtractorHatElectricChair = 4885

    const val UsingExtractorHatChairGraphics = 807
    const val ImproperlyUsingExtractorHatChairGraphics = 808

    const val PullCorkscrewLever = 4905
    const val PullLever = 4909
    const val SearchCrate = 6840

    val JigCartNPC = NPC(NPCs.JIG_CART_4913, Location.create(1954, 5147, 2))

    const val EmptyJigCart = NPCs.JIG_CART_4913
    const val JigCartWithElementalBar = NPCs.JIG_CART_4914
    const val JigCartWithElementalMindBar = NPCs.JIG_CART_4915
    const val JigCartWithPrimedElementalMindBar = NPCs.JIG_CART_4916
    const val JigCartWithPrimedElementalBar = NPCs.JIG_CART_4917
    const val JigCartWithPrimedBar = NPCs.JIG_CART_4918

    const val JigCartUnderPressMachine = 18784

    const val EmptyJigCartFrontOfDoors = 18832

    const val JunctionBox = Scenery.JUNCTION_BOX_18641
    const val BlankScroll = Components.BLANK_SCROLL_222
    const val SwitchDiagram = Components.EW2_SWITCH_DIAGRAM_466

    const val Doors = Scenery.DOOR_18702
}
