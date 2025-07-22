package ui

import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JPanel
import javax.swing.SwingConstants

class TileInformationPanel : JPanel() {
    init {
        MapEditor.underlayInfo.foreground = Color.white
        MapEditor.underlayInfo.verticalAlignment = SwingConstants.TOP
        MapEditor.underlayInfo.horizontalAlignment = SwingConstants.CENTER
        layout = BorderLayout()
        add(MapEditor.underlayInfo)
    }
}