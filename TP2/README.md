# Assignment TP2 — Kotlin Exercises & Cool Weather App

Course: Desenvolvimento de Aplicações Móveis (DAM)
Student(s): Luís Simões (47514)
Date: April 12th, 2026
Repository URL: https://github.com/IceBasilisk/DAM.git

---

## 1. Introduction

This assignment builds on the foundations established in TP1, advancing both Kotlin programming skills and Android development. The objectives are to:

- Deepen Kotlin knowledge by implementing advanced language features — sealed classes, generics, higher-order functions, lambdas, and operator overloading.
- Build a functional Android weather application that consumes a real REST API (Open-Meteo) and adapts its interface to any screen size and orientation.
- Practice UI design, theming (day/night), and internationalisation in Android.
- Experiment with AI-assisted code generation for the MIP-2 task.

---

## 2. System Overview

The assignment is divided into two major parts:

**Kotlin Exercises (Section 1):** Four console-based programs built in IntelliJ IDEA using a Maven project, each focused on a specific Kotlin language feature:
- Exercise 1.1 — Event log processing using sealed classes, extension functions, and higher-order functions.
- Exercise 1.2 — A type-safe generic in-memory cache (`Cache<K, V>`) with `getOrPut`, `transform`, and `snapshot` operations.
- Exercise 1.3 — A configurable data processing pipeline built with higher-order functions and lambdas, supporting a `buildPipeline` DSL-style builder.
- Exercise 1.4 — A 2D vector library (`Vec2`) with operator overloading (`+`, `-`, `*`, unary `-`, `[]`, `compareTo`) and utility functions (`magnitude`, `dot`, `normalized`).

**Android Application (Section 2):** The Cool Weather App displays real-time meteorological data for any location specified by latitude and longitude, fetched from the Open-Meteo API. It supports portrait and landscape orientations, day and night themes, and English/Portuguese localisation.

---

## 3. Architecture and Design

### Kotlin Project (IntelliJ / Maven)

All four exercises reside in the same Maven project under a single source tree:

```
src/main/kotlin/dam/exer_1/
  Event.kt       → Exercise 1.1 — Sealed class event log
  Cache.kt       → Exercise 1.2 — Generic in-memory cache
  Pipeline.kt    → Exercise 1.3 — Configurable data pipeline
  Vec2.kt        → Exercise 1.4 — 2D vector library
```

Each file contains its own `main()` function for standalone execution and testing.

### Android Project (`dam_47514/`)

Standard single-activity Android project with the following structure:

```
app/src/main/
  java/dam_47514/coolweatherapp/
    MainActivity.kt     → Activity lifecycle, theme selection, UI wiring, API call dispatch
    WeatherData.kt      → Data classes (WeatherData, CurrentWeather, Hourly),
                          WMO_WeatherCode enum, getWeatherCodeMap()
  res/
    layout/             → Portrait layout (activity_main.xml)
    layout-land/        → Landscape layout (activity_main.xml)
    drawable/           → Vector weather icons (clear_day, rain, snow, tstorm, …)
    values/             → strings.xml, colors.xml, themes.xml (Day / Day.Land)
    values-night/       → themes.xml (Night / Night.Land)
    values-pt-rPT/      → Portuguese string translations
```

The theme is applied programmatically at the top of `onCreate`, before `super.onCreate`, to ensure the correct background image is set for the combination of orientation (portrait/landscape) and time of day (day/night).

---

## 4. Implementation

### Exercise 1.1 — Event Log Processing

A `sealed class Event` with three subclasses (`Login`, `Purchase`, `Logout`) is used to model a strongly-typed event stream. Two extension functions are defined on `List<Event>`:

```kotlin
fun List<Event>.filterByUser(username: String): List<Event>
fun List<Event>.totalSpent(username: String): Double
```

A top-level higher-order function `processEvents(events, handler)` iterates the list and applies the provided lambda to each event. A `when` expression inside the lambda provides exhaustive, readable dispatch over all sealed subclasses.

### Exercise 1.2 — Generic In-Memory Cache

`Cache<K : Any, V : Any>` wraps a `MutableMap<K, V>` and exposes:

```kotlin
fun put(key: K, value: V)
fun get(key: K): V?
fun evict(key: K)
fun size(): Int
fun getOrPut(key: K, default: () -> V): V
fun transform(key: K, action: (V) -> V): Boolean
fun snapshot(): Map<K, V>   // returns an immutable copy
```

`getOrPut` checks for an existing entry before invoking the `default` lambda, mirroring the semantics of `MutableMap.getOrPut` from the standard library. `transform` applies an action in-place and returns `false` if the key is absent, avoiding silent failures. `snapshot` returns `map.toMap()` to guarantee the caller cannot mutate the internal state.

### Exercise 1.3 — Configurable Data Pipeline

`Pipeline` stores stages in a `MutableMap<String, (List<String>) -> List<String>>` to associate each transformation with a human-readable name. `execute` threads the input through all stages sequentially. The `buildPipeline` top-level function accepts a lambda with `Pipeline` as receiver, enabling a DSL-like construction:

```kotlin
val pipeline = buildPipeline {
    addStage("Trim")          { it.map { it.trim() } }
    addStage("Filter errors") { it.filter { "ERROR" in it } }
    addStage("Uppercase")     { it.map { it.uppercase() } }
    addStage("Add index")     { var i = 0; it.map { "${++i}. $it" } }
}
```

### Exercise 1.4 — 2D Vector Library

`Vec2` is a `data class` that implements `Comparable<Vec2>` for magnitude-based ordering. Operator overloading covers pairwise `+`, `-`, scalar `*`, unary `-`, index access via `get(index)`, and `compareTo`. `normalized()` guards against the zero vector by throwing `IllegalStateException`. Implementing `Comparable<Vec2>` makes `Vec2` compatible with `List.max()` and `List.min()` without any additional code.

### Cool Weather App — MainActivity

`onCreate` reads the system night-mode flag to set the `day` boolean, then applies one of four themes (`Theme.Day`, `Theme.Day.Land`, `Theme.Night`, `Theme.Night.Land`) before calling `super.onCreate`. This ordering is required by Android to change the window background before the layout is inflated.

The UPDATE button reads latitude and longitude from `EditText` fields, then starts a background `Thread` that calls `WeatherAPI_Call`:

```kotlin
private fun WeatherAPI_Call(lat: Float, long: Float): WeatherData {
    val url = URL(buildString { /* Open-Meteo URL */ })
    url.openStream().use {
        return Gson().fromJson(InputStreamReader(it, "UTF-8"), WeatherData::class.java)
    }
}
```

`updateUI` is always executed on the main thread via `runOnUiThread`, updating pressure, wind direction, wind speed, temperature, time, and the weather icon. The icon name is resolved dynamically using `Resources.getIdentifier`, mapping WMO weather codes to drawable resource names with day/night suffix where applicable.

### Cool Weather App — WeatherData.kt

Contains the data model (`WeatherData`, `CurrentWeather`, `Hourly`) and the `WMO_WeatherCode` enum mapping integer codes to drawable name fragments. `getWeatherCodeMap()` constructs a `HashMap<Int, WMO_WeatherCode>` for O(1) lookup by code.

---

## 5. Testing and Validation

- **Kotlin exercises** were tested by running each file's `main()` directly in IntelliJ IDEA and comparing console output against the expected results in the assignment specification.
- **Exercise 1.2** edge cases tested include: `get` on a missing key (expected `null`), `transform` on a missing key (expected `false`), and verifying that modifying the map after calling `snapshot()` does not affect the returned copy.
- **Exercise 1.4** edge cases include: `normalized()` called on a zero vector (expected `IllegalStateException`) and `get` with index 2 (expected `IndexOutOfBoundsException`).
- **The Android app** was tested on the Pixel 3 AVD (API 34) in both portrait and landscape orientations with day and night mode toggled via device settings.
- **Localisation** was verified by changing the device language to Portuguese (Portugal) and confirming all strings switched correctly, including the button label ("Atualizar") and field labels ("Direção", "Velocidade", "Temperatura").
- **Known limitations:** `pressure_msl` and `temperature_2m` are read at hardcoded index 12 (noon), rather than the index matching the current hour. The night theme backgrounds (`background_dark_vertical`, `background_dark_horizontal`) are not included in the submitted zip, so the night path was tested only indirectly. The MIP-2 section (Section 3 of the tutorial) was not completed in this submission.

---

## 6. Usage Instructions

### Kotlin Project

Requirements: IntelliJ IDEA (latest), JDK 17+, Maven.

```bash
git clone <repository-url>
# Open the root folder in IntelliJ IDEA as a Maven project
# Run each file's main() individually:
#   src/main/kotlin/dam/exer_1/Event.kt
#   src/main/kotlin/dam/exer_1/Cache.kt
#   src/main/kotlin/dam/exer_1/Pipeline.kt
#   src/main/kotlin/dam/exer_1/Vec2.kt
```

### Android App

Requirements: Android Studio Meerkat (2025.1.1+), Android SDK API 24–36, Pixel 3 AVD or any physical device with USB debugging enabled.

```bash
# Open dam_47514/ in Android Studio
# Select target device (AVD or physical)
# Run > Run 'app'  (Shift + F10)
```

To deploy via ADB:

```bash
adb devices
adb install app/build/outputs/apk/debug/app-debug.apk
```

To test day/night themes on the AVD, go to **Settings → Display → Dark Theme** and toggle as needed, then relaunch the app.

---

# Autonomous Software Engineering Sections

## 7. Prompting Strategy

AI assistance was used only for the Android section (Section 2 of the tutorial), as permitted by the assignment. Prompts were structured following a **Context + Goal + Constraints** format. Examples:

> "I am building an Android weather app in Kotlin using XML Views and Material Design 3. The app must apply different themes (day/night, portrait/landscape) before super.onCreate is called. Explain the correct order of operations in onCreate and provide a minimal example."

> "In my updateUI function I need to resolve a drawable resource name dynamically at runtime from a string. Show me how to use Resources.getIdentifier in Kotlin."

Prompts were kept focused on one concern at a time (theming, background threads, drawable resolution) to keep generated suggestions small and verifiable.

---

## 8. Autonomous Agent Workflow

AI assistance (Claude) was used in an auxiliary capacity for the Android section only — specifically for clarifying Android API usage when the official documentation was unclear, and for suggesting code patterns for theme switching and `runOnUiThread`. No code was accepted without being read and understood first. The AI was not used to generate entire classes or files; it was consulted for targeted explanations and short snippets.

---

## 9. Verification of AI-Generated Artifacts

All AI-suggested code was reviewed line by line before integration. The suggestions for theme switching (`setTheme` before `super.onCreate`) and dynamic drawable resolution (`getIdentifier`) were verified against the official Android Developers documentation. The final app was compiled and run on the AVD to confirm correct behaviour before submission.

---

## 10. Human vs AI Contribution

| Section | Primary Contributor |
|---|---|
| Exercise 1.1 — Event Log (sealed classes, extension functions) | Human (AI disabled) |
| Exercise 1.2 — Generic Cache | Human (AI disabled) |
| Exercise 1.3 — Data Pipeline | Human (AI disabled) |
| Exercise 1.4 — Vec2 operator overloading | Human (AI disabled) |
| Android app — layout design (portrait & landscape) | Human |
| Android app — theme setup and day/night switching | Human (AI consulted for clarification) |
| Android app — API call and JSON parsing | Human (AI consulted for clarification) |
| Android app — UI update and drawable resolution | Human (AI consulted for clarification) |
| Android app — Portuguese localisation | Human |
| MIP-2 section | Not completed in this submission |
| README report | AI-assisted (Claude) |

---

## 11. Ethical and Responsible Use

- AI tools were consulted only for the Android section, which is explicitly marked `[AI YES]` in the assignment.
- All four Kotlin exercises were completed with AI assistance disabled in IntelliJ IDEA, as required by the `[AI NO]` marking.
- AI-generated or AI-suggested code was always reviewed and understood before being used; no snippet was blindly copied.
- The Open-Meteo API is free for non-commercial use; no API key is required or stored.
- No sensitive data (credentials, personal information) appears in the codebase.

---

# Development Process

## 12. Version Control and Commit History

Development was tracked with Git throughout the assignment. Commits were made incrementally to reflect the natural progression of the work:

- Initial project setup for both the Maven Kotlin project and the Android Studio project.
- One commit per completed Kotlin exercise.
- Android project commits per logical step: layout creation, theme configuration, string localisation, API integration, UI wiring.
- A final commit with cleanup and this README.

---

## 13. Difficulties and Lessons Learned

- **Theme application order:** The most significant Android pitfall was discovering that `setTheme` must be called *before* `super.onCreate`; calling it afterwards has no visible effect on the window background. The official documentation mentions this but it is easy to overlook.
- **Background threads and UI updates:** Understanding why direct UI manipulation from a background thread crashes the app reinforced the importance of always routing UI changes through `runOnUiThread`.
- **Dynamic resource resolution:** Using `Resources.getIdentifier` to resolve drawable names at runtime felt fragile compared to compile-time references. A resource XML approach (as suggested in the Challenges section) would be more robust.
- **Sealed class exhaustiveness:** Working with `sealed class` and `when` expressions in Exercise 1.1 demonstrated how the compiler enforces exhaustive handling, eliminating a whole class of runtime errors.
- **`MutableMap` vs insertion order:** `Pipeline` uses `MutableMap` to pair stage names with their transforms; however, `HashMap` does not guarantee iteration order. In practice the insertion order was preserved with `LinkedHashMap` semantics, but this should be made explicit with `mutableMapOf` (which returns a `LinkedHashMap` in Kotlin).

---

## 14. Future Improvements

- Replace the hardcoded hourly index (12) in `updateUI` with a dynamic lookup that matches the current local hour using the `timezone` field returned by the API.
- Implement the GPS coordinates challenge (Section 2.2.1) to auto-populate latitude/longitude from the device's location on startup.
- Refactor `MainActivity` to use the MVVM pattern (Section 2.2.2), extracting the API call and data logic into a `ViewModel` with `LiveData`.
- Replace the hardcoded `WMO_WeatherCode` enum with an XML array resource for easier maintenance and to satisfy the XML resources challenge.
- Add error handling for network failures (no internet connection, API timeout) and invalid coordinate input.
- Complete the MIP-2 section (Section 3) — an image-browsing Android app with `RecyclerView`, built using a planning-first AI-assisted workflow.
- Add Portuguese locale background images if night backgrounds are ever included.

---

## 15. AI Usage Disclosure (Mandatory)

| Tool | How Used | Sections |
|---|---|---|
| Claude (claude.ai) | Consulted for Android API clarification (theme switching, `runOnUiThread`, `getIdentifier`); writing this README | Section 2 [AI YES], README [AI YES] |

AI tools were **not** used in any section marked `[AI NO]`. Auto-complete AI features were disabled in IntelliJ IDEA for all four Kotlin exercises as required by the AI Usage Policy.

The student, Luís Simões (47514), takes full responsibility for all content in this repository and is able to justify every part of it.
