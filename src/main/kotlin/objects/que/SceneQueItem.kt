package objects.que

import objects.OBSClient
import objects.TScene

interface SceneQueItem: QueItem {
    val scene: TScene
}