package codextreme.jimmyneutron.wordcloud

/**
 * Created by manzelseet on 11/07/17.
 */
public class WordCloudTextCreator constructor(user1: String, user2: String) {
    val cloud: String
    val size: Int

    init {
        val strArr = """Kotlin
        | Programming application development Project management Help desk technical support
        | Security compliance governance Web development Database administration Business
        | intelligence analytics Accounting Banking/Brokerage Cross-Functional Desktop Graphics
        | Desktop Publishing Electronic Mail Help Desk Insurance Integrated PC Apps Legal Manufacturing
        | Online Services Payroll/Personnel PC Communications Presentation Report Writing Scheduling/Planning
        | Screen Grabbers Security Spreadsheets Miscellaneous Apps
        """.trimMargin().replace("\n", "").split(" ")

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