import org.json.simple.JSONObject

class JsonObject(map: Map<Any, Any?>) : JSONObject(map) {
  override fun toString(): String {
    return super.toString().replace("\\", "")
  }
}