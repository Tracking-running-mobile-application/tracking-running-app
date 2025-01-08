import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

object LocaleUtils {

    private const val PREF_NAME = "app_preferences"
    private const val LANGUAGE_KEY = "language"

    // Thiết lập ngôn ngữ
    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Lưu ngôn ngữ vào SharedPreferences
        saveLanguagePreference(context, languageCode)
    }

    // Lưu ngôn ngữ vào SharedPreferences
    private fun saveLanguagePreference(context: Context, languageCode: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LANGUAGE_KEY, languageCode).apply()
    }

    // Lấy ngôn ngữ từ SharedPreferences
    fun getLanguagePreference(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(LANGUAGE_KEY, "en") ?: "en" // "en" là ngôn ngữ mặc định
    }
}
