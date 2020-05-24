package plugins.common

import plugins.PluginLoader
import javax.swing.JComponent

interface DetailPanelBasePlugin : BasePlugin {

    override fun enable() {
        super.enable()
        PluginLoader.registerDetailPanelPlugin(this)
    }

    override fun disable() {
        super.disable()
        PluginLoader.unregisterDetailPanelPlugin(this)
    }

    /**
     * Renders the panel component for the detail panel (right bottom panel of the main split pane)
     */
    fun detailPanel(): JComponent
}