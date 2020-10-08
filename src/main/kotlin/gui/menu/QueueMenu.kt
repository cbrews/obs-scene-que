package gui.menu

import GUI
import config.Config
import gui.HotKeysMapping
import gui.utils.addHotKeyMapping
import gui.utils.getMainFrameComponent
import objects.que.Que
import themes.Theme
import java.io.File
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JFileChooser
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.filechooser.FileNameExtensionFilter

class QueueMenu : JMenu("Queue") {
    private val logger = Logger.getLogger(QueueMenu::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        popupMenu.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        addHotKeyMapping(HotKeysMapping.QUEUE_MENU)

        val newItem = JMenuItem("New...")
        val saveAsItem = JMenuItem("Save as...")
        val openItem = JMenuItem("Open...")

        // Set alt keys
        newItem.addHotKeyMapping(HotKeysMapping.FILE_NEW_ITEM, ctrl = true, alt = false, shift = false)
        saveAsItem.addHotKeyMapping(HotKeysMapping.FILE_SAVE_AS_ITEM, ctrl = true, alt = false, shift = true)
        openItem.addHotKeyMapping(HotKeysMapping.FILE_OPEN_ITEM, ctrl = true, alt = false, shift = false)

        add(newItem)
        add(saveAsItem)
        add(openItem)

        newItem.addActionListener { newFile() }
        saveAsItem.addActionListener { saveAsFile() }
        openItem.addActionListener { openFile() }
    }

    private fun newFile() {
        logger.info("Creating new Queue file")
        val file = requestFileSaveLocation("New") ?: return

        Config.queFile = file.absolutePath
        Que.clear()
        Que.save()
        GUI.refreshQueItems()
    }

    private fun saveAsFile() {
        logger.info("Creating file chooser for Save As File")
        val file = requestFileSaveLocation("Save As...") ?: return

        Config.queFile = file.absolutePath
        Que.save()
    }

    private fun openFile() {
        logger.info("Creating file chooser for Open File")

        // Save current queue changes
        Que.save()

        val file = requestOpenFileLocation() ?: return

        Que.load(file)
    }

    private fun createFileChooser(fileName: String): JFileChooser {
        val fileChooser = JFileChooser(fileName)
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        fileChooser.isMultiSelectionEnabled = false
        fileChooser.fileFilter = FileNameExtensionFilter("Deprecated Queue files", "osq")
        fileChooser.fileFilter = FileNameExtensionFilter("JSON files", "json")
        return fileChooser
    }

    private fun requestFileSaveLocation(title: String): File? {
        val fileChooser = createFileChooser(Config.queFile)
        fileChooser.selectedFile = File(Config.queFile)
        fileChooser.ensureFileIsVisible(fileChooser.selectedFile)
        fileChooser.dialogTitle = title

        val chosenOption = fileChooser.showSaveDialog(getMainFrameComponent(this))
        if (chosenOption != JFileChooser.APPROVE_OPTION) {
            logger.info("Save As File file chooser canceled")
            return null
        }

        var file = fileChooser.selectedFile
        logger.info("File chosen to save as: ${file.absolutePath}")

        if (file.isDirectory) {
            logger.info("Cannot save file as a directory: ${file.absolutePath}")
            return null
        }

        if (file.extension.isEmpty()) {
            logger.info("File doesn't have an extension. Adding .json extension to file.")
            file = File(file.absolutePath + ".json")
        }

        if (file.extension != "json") {
            logger.warning("Chosen file is not a .json file: ${file.name}")
        }

        if (file.exists()) {
            logger.warning("File already exists: ${file.absolutePath}")
        }

        return file
    }

    private fun requestOpenFileLocation(): File? {
        val fileChooser = createFileChooser(Config.queFile)

        val chosenOption = fileChooser.showOpenDialog(getMainFrameComponent(this))
        if (chosenOption != JFileChooser.APPROVE_OPTION) {
            logger.info("Open File file chooser canceled")
            return null
        }

        val file = fileChooser.selectedFile
        logger.info("File chosen to open: ${file.absolutePath}")

        if (file.isDirectory) {
            logger.info("Cannot open file because it is a directory: ${file.absolutePath}")
            return null
        }

        if (file.extension != "json") {
            logger.warning("Chosen file is not a .json file: ${file.name}")
        }

        return file
    }
}