package plugins.obs

import GUI
import gui.Refreshable
import gui.utils.createImageIcon
import handles.QueItemTransferHandler
import objects.OBSState
import objects.TScene
import plugins.common.BasePlugin
import plugins.common.QueItem
import java.awt.*
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

@Suppress("unused")
class ObsPlugin : BasePlugin, Refreshable {
    override val name = "ObsPlugin"
    override val description = "Que items for integration with OBS"

    override val icon: Icon? = createImageIcon("/plugins/obs/icon-14.png")

    override val tabName = "OBS"

    private val list: JList<QueItem> = JList()

    override fun enable() {
        GUI.register(this)
    }

    override fun disable() {
        GUI.unregister(this)
    }

    override fun sourcePanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Available scenes")
        panel.add(titleLabel, BorderLayout.PAGE_START)

        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.dragEnabled = true
        list.transferHandler = QueItemTransferHandler()
        list.background = null
        list.font = Font("Dialog", Font.PLAIN, 14)
        list.cursor = Cursor(Cursor.HAND_CURSOR)
        list.border = CompoundBorder(
            BorderFactory.createLineBorder(Color(180, 180, 180)),
            EmptyBorder(10, 10, 0, 10)
        )

        val scrollPanel = JScrollPane(list)
        scrollPanel.preferredSize = Dimension(300, 500)
        scrollPanel.border = null
        panel.add(scrollPanel, BorderLayout.CENTER)

        return panel
    }

    override fun configStringToQueItem(value: String): QueItem {
        return ObsSceneQueItem(this, TScene(value))
    }

    override fun refreshScenes() {
        list.setListData(OBSState.scenes.map { ObsSceneQueItem(this, it) }.toTypedArray())
        list.repaint()
    }
}