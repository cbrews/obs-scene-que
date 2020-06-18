package gui.plugins.config

import objects.notifications.Notifications
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.border.EmptyBorder

class ConfigActionPanel(
    override var frame: ConfigWindow?,
    private val onSaveSuccessCallback: (() -> Unit?)? = null
) : PluginConfigActionPanel() {
    private val logger = Logger.getLogger(ConfigActionPanel::class.java.name)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        val saveButton = JButton("Save")
        saveButton.addActionListener { saveConfigAndClose() }
        val cancelButton = JButton("Cancel")
        cancelButton.addActionListener { cancelWindow() }

        add(Box.createHorizontalGlue())
        add(saveButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(cancelButton)
    }

    private fun cancelWindow() {
        logger.fine("Exiting configuration window")
        frame?.dispose()
    }

    private fun saveConfigAndClose() {
        logger.info("Saving configuration changes")
        if (frame == null || !frame!!.saveAll()) {
            return
        }

        if (onSaveSuccessCallback != null) {
            try {
                onSaveSuccessCallback.invoke()
            } catch (e: Exception) {
                logger.warning("Failed to invoke onSaveSuccessCallback")
                e.printStackTrace()
                Notifications.add(
                    "Something went wrong during saving the settings: ${e.localizedMessage}",
                    "Plugin Settings"
                )
            }
        }

        frame?.dispose()
    }
}
