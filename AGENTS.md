# AGENTS

## Big picture
- Single-module Android app in `:app` with package `com.example.verviapp`.
- UI is Jetpack Compose; app entry sets navigation routes in `app/src/main/java/com/example/verviapp/MainActivity.kt`.
- Screens live in `app/src/main/java/com/example/verviapp/ui/screens/`, reusable UI in `ui/components/`, theme in `ui/theme/`.
- State + logic mostly in `app/src/main/java/com/example/verviapp/viewmodel/` using `StateFlow` and `SharedFlow` (see `RequestManagementViewModel.kt`, `ChatViewModel.kt`).
- UI state classes are in `app/src/main/java/com/example/verviapp/viewmodel/state/` (e.g., `RequestItem.kt`); shared/domain UI models also exist in `app/src/main/java/com/example/verviapp/model/` (e.g., `RequestDetail.kt`).
- Persistence currently uses Room DAOs injected directly into ViewModels (e.g., `RequestManagementViewModel` -> `RequestDao`, `LoginViewModel` -> `UserDao`); feature-specific repository code exists in `repository/` (`RatingsRepository`, `RatingsRepositoryImpl`).
- Room entities are in `data/entity/` (e.g., `RequestEntity.kt`), DAOs in `data/dao/` (e.g., `RequestDao.kt`), and database class in `data/database/AppDatabase.kt`.
- Hilt is enabled: `App.kt` is `@HiltAndroidApp`; database/DAO providers are in `di/DatabaseModule.kt`; session providers are in `di/SessionModule.kt`.
- Local auth session is persisted with SharedPreferences via `data/session/SessionManager.kt` (simple `userId` key, no backend/encryption).

## Data flow example (current pattern)
- `RequestManagementViewModel` -> `RequestDao` -> Room database (`AppDatabase`) for request list updates.
- UI observes `uiState` via `collectAsState()` and handles navigation through one-off `SharedFlow` events in `RequestManagementViewModel`.
- Room DAOs return `Flow<List<Entity>>` for reactive updates; ViewModels map entities to UI models (e.g., `RequestEntity` -> `RequestItem`).
- Login/Register flow: `LoginViewModel` -> `UserDao` (credentials) + `SessionManager` (save/clear session `userId`).
- Profile/EditProfile flow: `ProfileViewModel`/`EditProfileViewModel` use `SessionManager` to resolve current editable user and gate edit/logout actions.
- Home flow: `HomeViewModel` consumes `RequestDao.observeHomeRequests(...)` + `CategoryDao.getCategoryNames()` for reactive request list with snapshot categories.

## Database & Persistence
- **Room Setup**: `AppDatabase` is the main database class (version 1) with multiple entities across users, requests, services, chat, and notifications (see `data/database/AppDatabase.kt`).
- **RequestEntity** (`data/entity/RequestEntity.kt`): Persists request card fields plus extended request data (`description`, `location`, `applicationCount`, `isUrgent`, `budgetCop`, timestamps/FKs).
- **RequestDao** (`data/dao/RequestDao.kt`): Provides CRUD operations; reactive list queries use `Flow` (`getAllRequests`, `getRequestsByStatus`, `observeHomeRequests`) and single-item lookup is `suspend` (`getRequestById`).
- **DAO split**: user/category responsibilities are separated (`UserDao` for users and user-category projections; `CategoryDao` for categories + cross refs).
- **Sample Data Initialization**: First-run seed is implemented in `di/DatabaseModule.kt` Room callback using `data/repository/SampleData.kt`.
- **Seed notes**: Home active requests include `budgetCop` + `categoryId` to support real price and category filtering; user seed includes `photoUrl` for profile/prestadores UI.

## Project-specific conventions
- Package folders use lowercase for `viewmodel`, `data`, `ui`; persistence packages are split as `data/dao/`, `data/entity/`, and `data/database/`.
- Navigation uses string routes defined inline in `MainActivity.kt` (e.g., `"requests/management"`, `"request/details"`).
- Database constants (name, table names) stored in entity/database classes (e.g., `AppDatabase.DATABASE_NAME = "vervi_app.db"`).
- UI State classes (e.g., `RequestItem`) live in `viewmodel/state/`; additional shared UI/domain models live in `model/`; Room entities remain separate in `data/entity/`.
- Session-aware navigation is handled from ViewModels/Composables using `SessionManager`; avoid reading/writing session state directly from random UI logic.

## Workflows (from README + Gradle setup)
- Recommended: open the project in Android Studio, sync Gradle, and run on device/emulator.
- Gradle wrapper at repo root (`gradlew`/`gradlew.bat`); KSP runs annotation processing for Hilt + Room.
- Requirements: Android Studio, JDK 17+, Android SDK.

## Integrations and dependencies
- Compose + Material3 + Navigation Compose + Hilt with KSP codegen.
- **Room 2.8.4**: Runtime, KTX extensions, and Compiler are configured via version catalog (`gradle/libs.versions.toml`) and KSP.
- Coil for image loading.
- No backend integration yet; TODOs in place for future API calls.

## Key patterns to follow when extending
- **Entity → DAO → ViewModel (current default)**: Current features inject DAOs directly in ViewModels; add a repository layer only when multiple data sources or shared business orchestration is needed.
- **Flow vs suspend**: Use `Flow` where UI must react to DB changes (e.g., provider list, home list, profile observation) and `suspend` for snapshot reads (e.g., categories list, login checks, one-off lookups).
- **Hilt Injection**: DAOs are provided by `DatabaseModule` and injected via ViewModel constructors.
- **Separation of Concerns**: Keep UI/domain models (`viewmodel/state/`, `model/`) separate from persistence models (`data/entity/`).
- **Session rules**: keep session storage minimal (`userId`), clear on logout, and gate profile edit actions by comparing requested profile id with session user id.
