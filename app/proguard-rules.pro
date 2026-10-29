# Keep app classes
-keep class com.fastlauncher.** { *; }

# Keep RecyclerView adapters
-keepclassmembers class * extends androidx.recyclerview.widget.RecyclerView$Adapter {
    public *;
}

# Keep AppInfo
-keep class com.fastlauncher.AppInfo { *; }
