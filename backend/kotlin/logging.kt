class Logging(val level: Int = 3) {
    companion object {
        const val DEBUG = 1
        const val INFO = 2
        const val WARN = 3
        const val ERROR = 4

        fun getLogger(level: Int = WARN): Logging {
            val logger = Logging(level)
            logger.info("Logger created with level '${levelToString(level)}' (${level}).")
            return logger
        }

        private fun levelToString(level: Int): String {
            return when (level) {
                1 -> "debug"
                2 -> "info"
                3 -> "warn"
                4 -> "error"
                else -> throw Exception("Logging level set to invalid value (${level}).")
            }
        }

        fun log(message: String, level: Int) {
            System.err.println("${levelToString(level).toUpperCase()}: $message")
        }
        fun logThrow(message: String, level: Int): Exception? {
            System.err.println("${levelToString(level).toUpperCase()}: $message")
            return when (level) {
                ERROR -> Exception(message)
                else -> null
            }
        }
    }

    fun log(value: Any?, level: Int) {
        if (level >= this.level)
            Logging.log(value.toString(), level)
    }
    fun debug(value: Any?) = log(value, DEBUG)
    fun info(value: Any?) = log(value, INFO)
    fun warn(value: Any?) = log(value, WARN)
    fun error(value: Any?): Exception {
        log(value, ERROR)
        return Exception(value.toString())
    }

    fun getLogger(level: Int) {
      Logging.getLogger(kotlin.math.max(level, this.level))
    }
}
