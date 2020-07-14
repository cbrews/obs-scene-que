package api


import com.google.gson.GsonBuilder
import config.Config
import objects.que.Que
import objects.que.QueLoader
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class QueueApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(QueueApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val indexMatcher = """^/(\d+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            "/current" -> getCurrent(response)
            "/previous" -> getPrevious(response)
            "/next" -> getNext(response)
            in Regex(indexMatcher.pattern) -> getIndex(response, request.pathInfo.getParams(indexMatcher))
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/current" -> postCurrent(response)
            "/previous" -> postPrevious(response)
            "/next" -> postNext(response)
            in Regex(indexMatcher.pattern) -> postIndex(request, response, request.pathInfo.getParams(indexMatcher))
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        val json = QueLoader.queToJson()

        respondWithJson(response, json)
    }

    private fun getCurrent(response: HttpServletResponse) {
        val currentQueueItemJson = Que.current()?.toJson()
        val json = GsonBuilder().setPrettyPrinting().create().toJson(currentQueueItemJson)

        respondWithJson(response, json)
    }

    private fun getPrevious(response: HttpServletResponse) {
        val currentQueueItemJson = Que.previewPrevious()?.toJson()
        val json = GsonBuilder().setPrettyPrinting().create().toJson(currentQueueItemJson)

        respondWithJson(response, json)
    }

    private fun getNext(response: HttpServletResponse) {
        val currentQueueItemJson = Que.previewNext()?.toJson()
        val json = GsonBuilder().setPrettyPrinting().create().toJson(currentQueueItemJson)

        respondWithJson(response, json)
    }

    private fun getIndex(response: HttpServletResponse, params: List<String>) {
        val index = params[0].toInt()
        logger.info("Getting Queue index: $index")

        val currentQueueItemJson = Que.getAt(index)?.toJson()
        val json = GsonBuilder().setPrettyPrinting().create().toJson(currentQueueItemJson)

        respondWithJson(response, json)
    }

    private fun postCurrent(response: HttpServletResponse) {
        Que.activateCurrent(executeExecuteAfterPrevious = false)
        getCurrent(response)
    }

    private fun postPrevious(response: HttpServletResponse) {
        Que.previous()
        getCurrent(response)
    }

    private fun postNext(response: HttpServletResponse) {
        Que.next()
        getCurrent(response)
    }

    private fun postIndex(request: HttpServletRequest, response: HttpServletResponse, params: List<String>) {
        val index = params[0].toInt()
        logger.info("Activating Queue index: $index")

        val queueItem = Que.getAt(index)
        if (queueItem == null) {
            respondWithNotFound(response)
            return
        }

        Que.setCurrentQueItemByIndex(index)

        val activateNextSubQueueItems =
            when {
                request.getParameter("activateNextSubQueueItems") != null -> request.getParameter("activateNextSubQueueItems") == "true"
                Que.current() != null && Que.current()!!.executeAfterPrevious -> Config.activateNextSubQueueItemsOnMouseActivationSubQueueItem
                else -> Config.activateNextSubQueueItemsOnMouseActivationQueueItem
            }

        Que.activateCurrent(activateNextSubQueueItems)

        getCurrent(response)
    }
}