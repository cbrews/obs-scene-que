package gui.quickAccessButtons

import mocks.MockPlugin
import plugins.PluginLoader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("DEPRECATION")
class QuickAccessButtonPanelTest {

    private val plugin = MockPlugin()

    @Test
    fun testGetStringQueItemFromQueExpandsArrayIfNeeded() {
        val list = ArrayList<String>()
        val panel = QuickAccessButtonPanel()

        val item = panel.getStringQueItemFromQue(list, 0)

        assertEquals(1, list.size)
        assertEquals("", list[0])
        assertNull(item)
    }

    @Test
    fun testGetStringQueItemFromQueDoesNotExpandsArrayIfNotNeeded() {
        val list = ArrayList<String>()
        list.add("""{"pluginName":"MockPlugin","className":"QueItemMock","name":"item1","executeAfterPrevious":false,"data":{}}""")
        val panel = QuickAccessButtonPanel()
        PluginLoader.registerQueItemPlugin(plugin)
        val queItem = plugin.configStringToQueItem("item1")

        val item = panel.getStringQueItemFromQue(list, 0)

        assertEquals(1, list.size)
        assertEquals("""{"pluginName":"MockPlugin","className":"QueItemMock","name":"item1","executeAfterPrevious":false,"data":{}}""", list[0])
        assertEquals(queItem.name, item!!.name)
    }
}