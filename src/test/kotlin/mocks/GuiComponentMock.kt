package mocks

import gui.Refreshable

class GuiComponentMock : Refreshable {
    var switchedScenesCalled: Boolean = false
    var refreshScenesCalled: Boolean = false
    var refreshTransitionsCalled: Boolean = false
    var refreshOBSStatusCalled: Boolean = false
    var refreshNotificationsCalled: Boolean = false

    override fun switchedScenes() {
        switchedScenesCalled = true
    }

    override fun refreshScenes() {
        refreshScenesCalled = true
    }

    override fun refreshTransitions() {
        refreshTransitionsCalled = true
    }

    override fun refreshOBSStatus() {
        refreshOBSStatusCalled = true
    }

    override fun refreshNotifications() {
        refreshNotificationsCalled = true
    }

    fun resetCalleds() {
        switchedScenesCalled = false
        refreshScenesCalled = false
        refreshTransitionsCalled = false
        refreshOBSStatusCalled = false
        refreshNotificationsCalled = false
    }
}