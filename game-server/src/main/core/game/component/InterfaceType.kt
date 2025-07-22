package core.game.component

import org.rs.consts.Components

/**
 * Represents interface types for different components.
 */
enum class InterfaceType(val fixedPaneId: Int, val resizablePaneId: Int, val fixedChildId: Int, val resizableChildId: Int) {
    DEFAULT(Components.TOPLEVEL_548, Components.TOPLEVEL_FULLSCREEN_746, 11, 6),
    OVERLAY(Components.TOPLEVEL_548, Components.TOPLEVEL_FULLSCREEN_746, 4, 5),
    TAB(Components.TOPLEVEL_548, Components.TOPLEVEL_FULLSCREEN_746, 83, 93),
    SINGLE_TAB(Components.TOPLEVEL_548, Components.TOPLEVEL_FULLSCREEN_746, 80, 76),
    DIALOGUE(Components.CHATTOP_752, Components.CHATTOP_752, 12, 12),
    WINDOW_PANE(Components.TOPLEVEL_548, Components.TOPLEVEL_FULLSCREEN_746, 0, 0),
    CS_CHATBOX(Components.CHATTOP_752, Components.CHATTOP_752, 6, 6),
    CHATBOX(Components.CHATTOP_752, Components.CHATTOP_752, 8, 8),
    OVERLAY_B(Components.TOPLEVEL_548, Components.TOPLEVEL_FULLSCREEN_746, 11, 3),
}
