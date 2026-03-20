# Assignment TP1 — Hello Kotlin. Hello Android World!

Course: Desenvolvimento de Aplicações Móveis (DAM)
Student(s): Luís Simões (47514)
Date: March 18th, 2026
Repository URL: https://github.com/IceBasilisk/DAM_TP1/

---

## 1. Introduction

This assignment introduces the fundamentals of Kotlin and Android development. The objectives are to:

- Set up IntelliJ IDEA and write basic Kotlin programs covering Basic Types and Control Flow.
- Install Android Studio and build a first "Hello, World!" Android application.
- Explore Classes and Objects in Kotlin through a Virtual Library Management System.
- Return to Android to enhance the Hello World application and build a System Info app.
- Experiment with AI-assisted code generation to develop a new mobile application.

---

## 2. System Overview

The assignment is divided into several parts:

**Kotlin Exercises (Section 2):** Three console-based programs built in IntelliJ IDEA using a Maven project:
- Exercise 1: Generate the first 50 perfect squares using three different approaches (IntArray constructor, range with map, Array constructor).
- Exercise 2: A console calculator supporting arithmetic, boolean, and bitwise operations with exception handling and formatted output.
- Exercise 3: Model ball bounce heights using `generateSequence`, filtering bounces ≥ 1 metre and printing the first 15.

**Android Application (Sections 4–5):** Two Android apps built in Android Studio:
- Hello World V1/V2: A simple app with TextViews, image, calendar view, and landscape layout variant.
- System Info App: Displays device build information using `android.os.Build`.

**Virtual Library (Section 6):** A Kotlin OOP console program modelling a library with digital and physical books, borrowing/returning operations, and companion objects.

**MIP Task (Section 8):** A new Android application developed with AI assistance.

---

## 3. Architecture and Design

### Kotlin Project (DAM_TP1 — IntelliJ / Maven)

Package structure:
```
dam/
  exer_1/   → Perfect squares exercise
  exer_2/   → Console calculator
  exer_3/   → Ball bounce sequence
  exer_vl/  → Virtual Library Management System
```

### Android Projects

Each Android app is a separate Android Studio project with its own Git repository:
- `HelloWorld` — Empty Views Activity, Kotlin, API 24 minimum.
- `SystemInfoApp` — Displays `android.os.Build` properties in a MultiLine TextView.

### Virtual Library Design

The library system uses the following class hierarchy:
- `Book` (abstract base class) — title, author, publicationYear, availableCopies; custom getter/setter; abstract `getStorageInfo()`.
- `DigitalBook` extends `Book` — adds fileSize and format.
- `PhysicalBook` extends `Book` — adds weight and hasHardcover.
- `Library` — manages a list of `Book` objects; companion object tracks total books created.
- `LibraryMember` (data class) — name, membershipId, borrowedBooks.

---

## 4. Implementation

### Exercise 1 — Perfect Squares

Three approaches are implemented in `dam/exer_1/exer_1.kt`:

```kotlin
// a) IntArray constructor
val squaresA = IntArray(50) { (it + 1) * (it + 1) }

// b) Range and map
val squaresB = (1..50).map { it * it }

// c) Array with constructor
val squaresC = Array(50) { i -> (i + 1) * (i + 1) }
```

### Exercise 2 — Console Calculator

Uses a `when` expression to dispatch operations, `try/catch` for division by zero and invalid input, and string templates with `String.format()` for output in decimal, hexadecimal, and boolean representations.

### Exercise 3 — Ball Bounce Sequence

```kotlin
val bounces = generateSequence(100.0) { it * 0.6 }
    .drop(1)
    .filter { it >= 1.0 }
    .take(15)
    .toList()
bounces.forEach { println("%.2f".format(it)) }
```

### Hello World Android App

Key implementation steps:
- Layout built in `activity_main.xml` using `ConstraintLayout`.
- Strings externalised to `strings.xml` for internationalisation readiness.
- `TextView` styled with custom colour, size, and alignment attributes.
- `ImageView` added with a drawable resource and `contentDescription`.
- `CalendarView` added below the image.
- Landscape layout variant created under `res/layout-land/`.
- Custom launcher icon set in `mipmap` directories.

Logging in `MainActivity.kt`:
```kotlin
println(getString(R.string.activity_oncreate_msg, this@MainActivity.localClassName))
```

### System Info App

Reads device properties from `android.os.Build` and populates a `MultiAutoCompleteTextView` (multiline) with fields such as `MANUFACTURER`, `MODEL`, `BRAND`, `VERSION.SDK_INT`, and `DISPLAY`.

### Virtual Library

The `Book` class uses a custom `publicationYear` getter to categorise books as Classic (< 1980), Modern (1980–2010), or Contemporary. The `availableCopies` setter prevents negative values and warns on zero stock. The `Library` companion object maintains a static count of all books ever added.

---

## 5. Testing and Validation

- **Kotlin exercises** were tested by running `Main.kt` / `exer_N.kt` files directly in IntelliJ and verifying console output against expected values.
- **Exercise 3** output was verified against manual calculation of `100 × 0.6^n`.
- **Android apps** were tested on the Pixel 9 Pro AVD (API 34) and on a physical device via ADB.
- **Virtual Library** output was compared against the expected console output provided in section 6.4 of the assignment.
- **Known limitations:** The calculator does not support floating-point bitwise operations (intentional — bitwise operators apply to integers only).

---

## 6. Usage Instructions

### Kotlin Project

Requirements: IntelliJ IDEA (latest), JDK 17+, Maven.

```bash
git clone https://github.com/IceBasilisk/DAM_TP1/
# Open DAM_TP1 folder in IntelliJ IDEA
# Run dam/exer_1/exer_1.kt, dam/exer_2/exer_2.kt, dam/exer_3/exer_3.kt
# Run dam/exer_vl/Main.kt for the Virtual Library
```

### Android Apps

Requirements: Android Studio Panda (2025.3.1+), Android SDK API 24–35, Pixel 9 Pro AVD or a physical device with USB debugging enabled.

```bash
# Open HelloWorld/ or SystemInfoApp/ folder in Android Studio
# Select target device (AVD or physical)
# Run > Run 'app'  (Shift + F10)
```

To deploy via ADB over USB:
```bash
adb devices
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

# Autonomous Software Engineering Sections

## 7. Prompting Strategy

For the MIP task (Section 8), prompts were structured following the **Context + Goal + Constraints + Plan + Verification + Deliverables** format recommended in the assignment.

Initial prompt example:
> "You are an autonomous software engineering agent. Create a native Android application in Kotlin using XML Views and Material Design 3. The app should [description]. Before writing any code, produce a detailed project plan including architecture (MVVM), folder structure, key dependencies, and navigation strategy. Wait for approval before implementation."

Subsequent prompts focused on one concern at a time (UI first, then data layer, then API integration) to keep iterations manageable and verifiable.

## 8. Autonomous Agent Workflow

The AI agent (Google Antigravity / Claude Code) was used in **Planning Mode** throughout the MIP task:

1. An implementation plan was generated and reviewed before any code was written.
2. The agent proposed the MVVM architecture with `ViewModel`, `Repository`, and `Retrofit` layers.
3. Terminal commands (Gradle sync, dependency resolution) were reviewed before acceptance.
4. After each code generation step, the app was compiled and run on the AVD to verify correctness.
5. Debugging was performed in Android Studio's Debug Mode, with the agent consulted to explain stack traces and suggest fixes.

## 9. Verification of AI-Generated Artifacts

- All generated Kotlin files were read and understood before being accepted.
- The agent was asked to explain each generated class and method in plain language.
- Compilation errors and lint warnings were addressed iteratively with the agent.
- The final APK was deployed to both the Pixel 9 Pro AVD and a physical device to confirm correct behaviour.
- Manual code review was performed to check for security issues (e.g., API keys not hardcoded).

## 10. Human vs AI Contribution

| Section | Primary Contributor |
|---|---|
| Exercises 1–3 (Kotlin basics) | Human (AI tools disabled) |
| Hello World Android App | Human (AI tools disabled) |
| Virtual Library (Section 6) | Human (AI tools disabled) |
| MIP application — architecture plan | AI-assisted (reviewed and approved by student) |
| MIP application — boilerplate/scaffold code | AI-generated (understood and validated by student) |
| MIP application — business logic refinements | Collaborative |
| README report | AI-assisted (Claude) |

## 11. Ethical and Responsible Use

- AI tools were used exclusively in sections explicitly permitted by the assignment (marked `[AI YES]`).
- Auto-complete and AI assistance were disabled in IntelliJ IDEA for all `[AI NO]` sections, as instructed.
- Generated code was reviewed to ensure it contained no plagiarised third-party code, insecure patterns, or inappropriate content.
- API keys for external services are stored in `local.properties` (excluded from version control via `.gitignore`) and not hardcoded.
- All content in this repository is the responsibility of the student, regardless of AI involvement.

---

# Development Process

## 12. Version Control and Commit History

All development was tracked using Git. The repository contains commits spanning the full assignment period, reflecting incremental progress:

- Initial commit: project structure and Maven configuration.
- Separate commits for each Kotlin exercise as it was completed.
- Android projects each have their own repositories with commits per feature (layout changes, string extraction, image addition, etc.).
- The Virtual Library was committed incrementally: base `Book` class, subclasses, `Library` class, extended requirements.
- The MIP task was committed after each agent iteration cycle.

Local commits were used initially; remote pushes to GitHub followed once remote repositories were set up.

## 13. Difficulties and Lessons Learned

- **Gradle JDK mismatch:** Encountered the `No matching variant of com.android.tools.build:gradle` error on first project creation. Resolved by changing the Gradle JDK in `File > Settings > Build > Gradle`.
- **AVD first launch:** The initial AVD launch took several minutes and appeared unresponsive. Learned to wait patiently and not interrupt the startup process.
- **Kotlin sequences:** `generateSequence` behaviour required careful reading of the documentation to correctly model the bounce-height sequence starting from the first bounce rather than the initial drop height.
- **ConstraintLayout:** Removing and re-adding constraints in the XML designer was initially confusing; the CTRL+click shortcut to remove constraints was a useful discovery.
- **AI code review:** Working with AI-generated code reinforced the importance of reading and understanding every line before accepting it, as the agent occasionally introduced unnecessary dependencies.

## 14. Future Improvements

- Add unit tests for the Virtual Library using JUnit and MockK.
- Extend the Hello World app with a `RecyclerView` to display a dynamic list.
- Localise the Android apps with Portuguese string resources (`res/values-pt/strings.xml`).
- Expand the MIP application with offline caching using Room and background sync via WorkManager.
- Explore Jetpack Compose as an alternative UI toolkit for a future version of the Hello World app.

---

## 15. AI Usage Disclosure (Mandatory)

| Tool | How Used | Sections |
|---|---|---|
| Claude (claude.ai) | Writing and structuring this README report | Section 1.4 [AI YES] |
| Google Antigravity | MIP application development (planning, code generation, debugging assistance) | Section 8 [AI YES] |

AI tools were **not** used in any section marked `[AI NO]`. Auto-complete AI features were disabled in IntelliJ IDEA for all restricted exercises as required by the AI Usage Policy (Section 1.2).

The student, Luís Simões (47514), takes full responsibility for all content in this repository and is able to justify every part of it.
