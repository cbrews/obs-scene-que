package plugins.easyworship.queItems

import plugins.common.QueItem

abstract class EasyWorshipQueItem(override val name: String) : QueItem {
    override val pluginName: String = "EasyWorship"

    override fun toString() = name

    override fun toConfigString(): String {
        return name
    }
}