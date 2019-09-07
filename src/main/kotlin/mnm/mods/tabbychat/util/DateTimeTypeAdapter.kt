package mnm.mods.tabbychat.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import java.io.IOException
import java.time.LocalDateTime

class DateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    @Throws(IOException::class)
    override fun write(jsonOut: JsonWriter, value: LocalDateTime) {
        jsonOut.value(value.toString())
    }

    @Throws(IOException::class)
    override fun read(jsonIn: JsonReader): LocalDateTime {
        return LocalDateTime.parse(jsonIn.nextString())
    }
}
