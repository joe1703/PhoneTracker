# Phone Usage Tracker

An Android app that tracks phone screen-on time in the background.

## Features

- **Current Session Timer**: Shows how long the phone screen has been on in the current session
- **Daily Total**: Displays total screen-on time for today
- **All-Time Total**: Shows cumulative screen-on time since app installation
- **Background Tracking**: Monitors screen on/off events even when the app is closed
- **Persistent Storage**: Uses Room database to store all tracking data

## How It Works

1. The app registers a `BroadcastReceiver` to listen for `ACTION_SCREEN_ON` and `ACTION_SCREEN_OFF` intents
2. When the screen turns on, a new session is started
3. When the screen turns off, the current session is ended
4. Session durations are aggregated and stored in a local Room database
5. The UI updates every second to show real-time stats

## Installation

1. Open the project in Android Studio
2. Connect an Android device (or use an emulator)
3. Run `./gradlew build` to build the app
4. Run `./gradlew installDebug` to install on your device
5. Or click "Run" in Android Studio

## Permissions

The app requires:
- No special permissions are needed for basic functionality
- Optional: `RECEIVE_BOOT_COMPLETED` for starting tracking on device boot

## Architecture

- **MainActivity**: UI layer displaying stats
- **UsageViewModel**: Manages UI state and stats updates
- **UsageRepository**: Data layer handling database operations
- **ScreenReceiver**: BroadcastReceiver listening for screen state changes
- **Room Database**: Local SQLite storage for sessions and daily totals

## Dependencies

- AndroidX libraries for UI and lifecycle management
- Room for database management
- Kotlin Coroutines for async operations

## Building from Source

```bash
# Clone the repository
cd phone_usage_tracker

# Build APK
./gradlew build

# Run tests (if any)
./gradlew test
```

## Data Storage

The app stores data locally on your device using Room database. No data is sent to external servers.

**Note**: Uninstalling the app will delete all tracking history.

## Future Enhancements

- Export usage statistics as CSV
- Weekly/monthly usage charts
- Notifications for high screen time
- App-specific usage tracking
- Widget for quick stats view
- Cloud sync option

## License

This project is open source and available under the MIT License.
