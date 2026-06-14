package a47514.masterplanner.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PremiumManager {
    private const val PREF_NAME = "masterplanner_prefs"
    private const val KEY_IS_PREMIUM = "is_premium"

    const val FREE_ROADMAP_LIMIT = 3
    const val FREE_TASK_LIMIT = 6
    const val PREMIUM_ROADMAP_LIMIT = Int.MAX_VALUE
    const val PREMIUM_TASK_LIMIT = Int.MAX_VALUE

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    val roadmapLimit get() = if (_isPremium.value) PREMIUM_ROADMAP_LIMIT else FREE_ROADMAP_LIMIT
    val taskLimit get() = if (_isPremium.value) PREMIUM_TASK_LIMIT else FREE_TASK_LIMIT

    fun init(context: Context) {
        val prefs = prefs(context)
        _isPremium.value = prefs.getBoolean(KEY_IS_PREMIUM, false)
    }

    fun setPremium(context: Context, value: Boolean) {
        _isPremium.value = value
        prefs(context).edit().putBoolean(KEY_IS_PREMIUM, value).apply()
    }

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}