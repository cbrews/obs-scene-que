package gui

import GUI
import gui.notifications.NotificationListPanel
import mocks.GuiComponentMock
import objects.notifications.Notifications
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NotificationListPanelTest {

    @Test
    fun testMarkNotificationsAsReadOnOpen() {
        Notifications.markAllAsRead()
        Notifications.add("message")
        assertEquals(1, Notifications.unreadNotifications)

        val panelMock = GuiComponentMock()
        GUI.register(panelMock)
        assertFalse(panelMock.refreshNotificationsCalled)

        // When
        NotificationListPanel()

        assertEquals(0, Notifications.unreadNotifications)
        assertTrue(panelMock.refreshNotificationsCalled)
    }
}