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
     * Retrieve a value by key
     */
    fun get(key: K): V? {
        // key exists -> return key
        // key does not exist -> return null
        return map[key]
    }

    /**
     * Remove entry from the cache
     */
    fun evict(key: K) {
        // key exists -> remove key
        // key does not exist -> return null
        map.remove(key)
    }

    /**
     * @return number of entries currently stored
     */
    fun size(): Int {
        return map.size
    }

    /**
     * Return value of key, or sets default value if there is none
     */
    fun getOrPut(key: K, default: () -> V): V {
        // Get key value
        val cacheValue = get(key)

        if (cacheValue != null)
            return cacheValue
        else {
            val defaultValue = default()
            put(key, defaultValue)
            return defaultValue
        }
    }

    /**
     * Use action handler to alter key's value
     * @return True if value was altered, False if value is null
     */
    fun transform(key: K, action: (V) -> V): Boolean {
        val cacheValue = get(key)

        if (cacheValue != null) {
            val newValue = action(cacheValue)
            put(key, newValue)
            return true
        }
        return false
    }

    /**
     * Returns copy of map variable
     */
    fun snapshot(): Map<K, V> {
        return map.toMap()
    }
}

fun main() {
    /**
     * Word frequency counts
     */
    var cache1 = Cache<String, Int>()
    cache1.put("kotlin", 1)
    cache1.put("scala", 1)
    cache1.put("haskell", 1)

    println(
        "--- Word frequency cache ---" +
                "\nSize: ${cache1.size()}" +
                "\nFrequency of \"kotlin\": ${cache1.get("kotlin")}" +
                "\ngetOrPut \"kotlin\":  ${cache1.getOrPut("kotlin", { 0 })}" +
                "\ngetOrPut \"java\":  ${cache1.getOrPut("java", { 0 })}" +
                "\nSize after getOrPut: ${cache1.size()}" +
                "\nTransform \"kotlin\" (+1): ${cache1.transform("kotlin", { it + 1 })}" +
                "\nTransform \"cobol\" (+1): ${cache1.transform("cobol", { it + 1 })}" +
                "\nSnapshot: ${cache1.snapshot()}"
    )

    println()

    /**
     * Small id-to-name registry
     */
    var cache2 = Cache<Int, String>()
    cache2.put(1, "Alice")
    cache2.put(2, "Bob")

    println(
        "--- Id registry cache ---" +
                "\nId 1 -> ${cache2.get(1)}" +
                "\nId 2 -> ${cache2.get(2)}"
    )
    cache2.evict(1)
    println(
        "After evict id 1, size: ${cache2.size()}" +
                "\nId 1 after evict -> ${cache2.get(1)}"
    )

}