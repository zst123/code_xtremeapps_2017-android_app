package codextreme.jimmyneutron.wordcloud

import android.util.Log
import codextreme.jimmyneutron.Common
import org.jsoup.Jsoup
import java.util.*


/**
 * Created by manzelseet on 11/07/17.
 */
public class WordCloudTextCreator constructor(tableId: String) {
    val cloud: String
    val size: Int

    init {
        var strArr: List<String>
        if (tableId.isNullOrEmpty()) {
            strArr = """Kotlin
            | Programming application development Project management Help desk technical support
            | Security compliance governance Web development Database administration Business
            | intelligence analytics Accounting Banking/Brokerage Cross-Functional Desktop Graphics
            | Desktop Publishing Electronic Mail Help Desk Insurance Integrated PC Apps Legal Manufacturing
            | Online Services Payroll/Personnel PC Communications Presentation Report Writing Scheduling/Planning
            | Screen Grabbers Security Spreadsheets Miscellaneous Apps
            """.trimMargin().replace("\n", "").split(" ")
            Log.d("ZST123", "To jumble?" + (Common.JUMBLE_WORDCLOUD))
            if (Common.JUMBLE_WORDCLOUD) {
                Collections.shuffle(strArr)
            }
        } else {
            strArr = listOf("Error")
            val t = object : Thread() {
                override fun run() {
                    var result: String = "Error"
                    try {
                        result = Jsoup.connect(Common.URL_WORDCLOUD)
                                .data("tableId", tableId)
                                .ignoreHttpErrors(true)
                                .post()
                                .text()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Log.d("ZST123", "wordcloud webservice - result:")
                    Log.d("ZST123", result)
                    var items = result
                            .split(":")
                            .filterNot { it.isNullOrBlank() }
                    //items.forEach { Log.d("ZST123", "> " + it) }
                    items = items.sortedWith(compareBy({ it.split(";")[1].toInt() }, { it.split(";")[0].length }))
                    items = items.map { it.split(";")[0] }
                    Log.d("ZST123", items.toString())
                    strArr = items
                }
            }
            t.start()
            t.join()
        }

        size = strArr.size

        var wordArray = mutableListOf<String>()
        strArr.forEachIndexed { i, s ->
            var percent: Int = (i * 100.0 / size).toInt()
            var priority: Int

            if (percent == 0) priority = 25
            else if (percent <= 2) priority = 15
            else if (percent <= 10) priority = 10
            else if (percent <= 20) priority = 8
            else if (percent <= 25) priority = 7
            else if (percent <= 30) priority = 6
            else if (percent <= 40) priority = 5
            else if (percent <= 60) priority = 3
            else priority = 2

            wordArray.add("""["$s", $priority]""")
        }

        cloud = wordArray.joinToString(separator = ", ", prefix = "[", postfix = "]")
    }

}