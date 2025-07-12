# Activity Dashboard (Kotlin Multiplatform Web)

A cross-platform web application for visualizing and exporting Fitbit activity data — built with [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) and [App Platform](https://github.com/amzn/app-platform).

---

## 🚀 Features

- 📊 View recent Fitbit activity by month
- 📅 Month-by-month navigation
- 📄 Export activity table as PDF
- 🌐 Runs in the browser using Kotlin/WASM
- 🔧 Built with [App Platform](https://github.com/amzn/app-platform) for structured dependency injection and composable rendering

---

## 🛠 Getting Started

To run the application locally:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

To build a production version:

```bash
./gradlew :composeApp:wasmJsBrowserDistribution
```

---

## 🧹 Code Style

We use [Ktlint](https://github.com/pinterest/ktlint) for formatting and linting.

Check formatting:

```bash
./gradlew ktlintCheck
```

Apply formatting:

```bash
./gradlew ktlintFormat
```

---

## ⚙️ Local Properties

You can override local build settings by creating a `local.properties` file in the root directory:

```properties
isDebug=true
```

> ℹ️ `local.properties` is ignored via `.gitignore` and should not be committed.

---

## 🧑‍💻 Development & Structure

This project uses [App Platform](https://github.com/amzn/app-platform) to manage:

- Dependency injection
- Composable view rendering
- Scoped presenters and renderers per screen

All navigation, DI, and rendering are defined declaratively and follow App Platform conventions.