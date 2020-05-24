package mocks

import objects.que.JsonQue
import plugins.common.QueItem
import plugins.common.QueItemBasePlugin
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class MockPlugin : QueItemBasePlugin {
    override val name: String = "MockPlugin"
    override val description: String = "description"
    override val version: String = "0.0.0"
    override val icon: Icon? = null
    override val tabName: String = "MockPluginTabName"

    override fun sourcePanel(): JComponent {
        return JPanel()
    }

    override fun configStringToQueItem(value: String): QueItem {
        return QueItemMock(this, value)
    }

    override fun jsonToQueItem(jsonQueItem: JsonQue.QueItem): QueItem {
        return QueItemMock(this, jsonQueItem.name)
    }
}