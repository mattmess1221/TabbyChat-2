package mnm.mods.tabbychat.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import java.io.IOException

class ToStringAdapter<T>(val fromString: (String) -> T) : TypeAdapter<T>() {
    @Throws(IOException::class)
    override fun write(jsonOut: JsonWriter, value: T) {
        jsonOut.value(value.toString())
    }

    @Throws(IOException::class)
    override fun read(jsonIn: JsonReader): T {
        return fromString(jsonIn.nextString())
    }
}
