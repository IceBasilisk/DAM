package dam.exer_1

/**
 * Maps keys to values of any type
 */
class Cache<K : Any, V : Any> {
    private val map = mutableMapOf<K, V>()

    /**
     * Insert/overwrite entry
     */
    fun put(key: K, value: V) {
        // key exists -> update
        // key does not exist -> add
        map[key] = value
    }

    /**
     * Retrieve a value by key, null if key is not present
     * (return map[key] is enough)
     */
    fun get(key: K): V? {
        if (map[key] != null) {
            return map[key]
        }
        return null
    }

    /**
     * Remove entry from the cache
     */
    fun evict(key: K) {
        if (map[key] != null)
            map.remove(key)
        else
            println("Entry does not exist in cache.")
    }

    /**
     * @return number of entries currently stored
     */
    fun size(): Int {
        return map.size
    }

    fun getOrPut(key: K, default: () -> V): V {
        val cacheValue = get(key)

        if (cacheValue != null)
            return cacheValue
        else {
            val defaultValue = default()
            put(key, defaultValue)
            return defaultValue
        }
    }

    fun transform(key: K, action: (V) -> V): Boolean {
        val cacheValue = get(key)

        if (cacheValue != null){
            val newValue = action(cacheValue)
            put(key, newValue)
            return true
        }
        return false
    }

    fun snapshot(): Map<K, V> {
        return map.toMap()
    }
}

fun main() {
    var cache1 = Cache<Int, String>()
    cache1.put(0, "kotlin")
    cache1.put(1, "kotlin")
    cache1.put(2, "kotlin")
    var cache2 = Cache<String, Int>()
    println(
        "--- Word frequency cache ---" +
                "\nSize: ${cache1.size()}" +
                "\nFrequency of \"kotlin\": " +
                "\ngetOrPut \"kotlin\": " +
                "\ngetOrPut \"java\": " +
                "\nSize after getOrPut: " +
                "\nTransform \"kotlin\" (+1): " +
                "\nTransform \"cobol\" (+1) " +
                "\nSnapshot: {"
    )
    println()
    println(
        "--- Id registry cache ---" +
                "\nId 1 -> " +
                "\nId 2 -> " +
                "\nAfter evict id 1, size: " +
                "\nId 1 after evict ->"
    )

}