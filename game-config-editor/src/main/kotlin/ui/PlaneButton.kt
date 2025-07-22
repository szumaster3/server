package ui

import javax.swing.JButton

class PlaneButton(val plane: Int) : JButton(plane.toString()){
    init {
        addActionListener {
            if(MapEditor.plane != this.plane){
                MapEditor.plane = this.plane
                MapEditor.npcPanel.redrawRows()
                MapEditor.itemPanel.redrawRows()
                MapEditor.mapPanel.repaintMap()
            }
        }
    }
}