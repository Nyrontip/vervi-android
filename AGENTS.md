# AGENTS

## Big picture
- Single-module Android app in `:app` with package `com.example.verviapp`.
- UI is Jetpack Compose; app entry sets navigation routes in `app/src/main/java/com/example/verviapp/MainActivity.kt`.
- Screens live in `app/src/main/java/com/example/verviapp/ui/screens/`, reusable UI in `ui/components/`, theme in `ui/theme/`.
- State + logic mostly in `app/src/main/java/com/example/verviapp/viewModel/` using `StateFlow` (see `RequestManagementViewModel.kt`).
- Data models and UI state classes are in `app/src/main/java/com/example/verviapp/viewModel/state/` (e.g., `RequestItem.kt`).
- Repository pattern with Room database for persistence: `RequestRepository` + `RequestRepositoryImpl` (see `data/repository/RequestRepositoryImpl.kt`).
- Room entities in `data/database/entity/` (e.g., `RequestEntity.kt`), DAOs in `data/database/dao/` (e.g., `RequestDao.kt`), database class `AppDatabase.kt`.
- Hilt is enabled: `App.kt` is `@HiltAndroidApp`, bindings are in `di/RepositoryModule.kt` and `di/DatabaseModule.kt`.

## Data flow example (current pattern)
- `RequestManagementViewModel` -> `RequestRepository.getRequests(...)` -> `RequestRepositoryImpl` -> `RequestDao` -> Room database (`AppDatabase`).
- UI observes `uiState` via `collectAsState()` and handles navigation through one-off `SharedFlow` events in `RequestManagementViewModel`.
- Room DAOs return `Flow<List<Entity>>` for reactive updates; repository handles mapping between entities and UI models.

## Database & Persistence
- **Room Setup**: `AppDatabase` is the main database class (version 1, single entity: `RequestEntity`).
- **RequestEntity** (`data/database/entity/RequestEntity.kt`): Simplified version of `RequestItem` without Compose-specific types; stores status, title, date, applications, imageUrl, buttonText, isActive flag.
- **RequestDao** (`data/database/dao/RequestDao.kt`): Provides CRUD operations; returns `Flow` for reactive queries (getAllRequests, getRequestsByStatus, getRequestById).
- **Sample Data Initialization**: Repository companion object contains `sampleEntities` for initialization (TODO: implement first-run insert in Repository or ViewModel).

## Project-specific conventions
- Package folders use lowercase for `viewModel`, `data`, `ui`; entity/dao packages nested under `data/database/`.
- Navigation uses string routes defined inline in `MainActivity.kt` (e.g., `"requests/management"`, `"request/details"`).
- Database constants (name, table names) stored in entity/database classes (e.g., `AppDatabase.DATABASE_NAME = "vervi_app.db"`).
- UI State classes and domain models (like `RequestItem`) live in `viewModel/state/`; Room entities separate in `data/database/entity/`.

## Workflows (from README + Gradle setup)
- Recommended: open the project in Android Studio, sync Gradle, and run on device/emulator.
- Gradle wrapper at repo root (`gradlew`/`gradlew.bat`); KSP runs annotation processing for Hilt + Room.
- Requirements: Android Studio, JDK 17+, Android SDK.

## Integrations and dependencies
- Compose + Material3 + Navigation Compose + Hilt with KSP codegen.
- **Room 2.6.1**: RuntimeLib, KTX extensions, and Compiler (KSP-based code generation).
- Coil for image loading.
- No backend integration yet; TODOs in place for future API calls.

## Key patterns to follow when extending
- **Entity → Repository → ViewModel**: New database tables should follow the entity (Room) → DAO → repository pattern.
- **Flow-based DAOs**: Queries in DAO return `Flow` for reactivity; collect in repository or ViewModel as needed.
- **Hilt Injection**: All DAOs injected via constructor; Database provided by `DatabaseModule`.
- **Separation of Concerns**: Keep UI models (`RequestItem`) separate from persistence models (`RequestEntity`) for flexibility.

