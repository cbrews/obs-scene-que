package api

import config.Config
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URL
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ConfigApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/config"
        private var apiUrl: String = "" + apiRootEndpoint

        @BeforeClass
        @JvmStatic
        fun setup() {
            // Get some random free port
            Config.httpApiServerPort = ServerSocket(0).use { it.localPort }

            ApiServer.start()
            apiUrl = ApiServer.uri().toString() + apiRootEndpoint
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            ApiServer.stop()
        }
    }

    @BeforeTest
    fun before() {
        Config.obsReconnectionTimeout = 3001
    }

    @Test
    fun testGetConfigKeyValue() {
        val connection = URL("${apiUrl}/obsReconnectionTimeout").openConnection() as HttpURLConnection
        connection.connect()

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""
            {
              "key": "obsReconnectionTimeout",
              "value": 3001
            }
        """.trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetInvalidConfigKeyValue() {
        val connection = URL("${apiUrl}/xx").openConnection() as HttpURLConnection
        connection.connect()

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""
            {
              "key": "xx"
            }
        """.trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetList() {
        val connection = URL("${apiUrl}/list").openConnection() as HttpURLConnection
        connection.connect()

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        val body = connection.body().trim()
        assertTrue(body.startsWith("["))
        assertTrue(body.endsWith("]"))
        assertTrue(body.contains("""{
    "key": "obsReconnectionTimeout",
    "value": 3001
  },""".trimIndent()))
    }

    @Test
    fun testGetInvalidPostEndpoint() {
        val connection = URL("${apiUrl}/x").openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connect()

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody().trim())
    }

//    @Test
//    fun testPostConfigKeyValue() {
//        val connection = URL("${apiUrl}/obsReconnectionTimeout").openConnection() as HttpURLConnection
//        connection.requestMethod = "POST"
//        connection.setRequestProperty("Content-Type", "application/json; utf-8")
//        connection.setRequestProperty("Accept", "application/json")
//        connection.doOutput = true
//
//        OutputStreamWriter(connection.outputStream).run {
//            write("""
//            {
//              "key": "obsReconnectionTimeout",
//              "value": 4001
//            }
//        """.trimIndent())
//            flush()
//        }
//        connection.connect()
//
//        assertEquals(HttpStatus.OK_200, connection.responseCode)
//        assertEquals("""
//            {
//              "key": "obsReconnectionTimeout",
//              "value": 4001
//            }
//        """.trimIndent(), connection.body().trim())
//        assertEquals(4001, Config.obsReconnectionTimeout)
//    }
}