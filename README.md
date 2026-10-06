# BiswaTube

Android YouTube discovery/player app.

Features:
- YouTube Data API v3 search
- Thumbnail, title and channel name cards
- YouTube embedded playback inside the app
- Category shortcuts
- No video downloading or re-hosting

## API key

Create your own YouTube Data API v3 key and put it in:

app/src/main/java/com/biswajit/barman/video/YouTubeApi.kt

Replace:
PUT_YOUR_YOUTUBE_API_KEY_HERE

Do not publish a real API key in a public GitHub repository. For a public release, use a safer build configuration/backend.

## Build

GitHub Actions builds a debug APK automatically after a push. The APK is available in the workflow run's Artifacts section.

