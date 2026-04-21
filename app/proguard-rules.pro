##############################################
# 🔹 GENERAL (Safe Defaults)
##############################################
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses

##############################################
# 🔹 KOTLIN
##############################################
-keep class kotlin.Metadata { *; }

##############################################
# 🔹 HILT / DAGGER
##############################################
-dontwarn dagger.hilt.internal.**
-dontwarn javax.inject.**

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

##############################################
# 🔹 RETROFIT
##############################################
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep API interfaces
-keep interface * {
    @retrofit2.http.* <methods>;
}

##############################################
# 🔹 GSON
##############################################
# Keep model classes (IMPORTANT)
-keep class com.example.myapplication.**.model.** { *; }

# Keep serialized names
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

##############################################
# 🔹 OKHTTP
##############################################
-dontwarn okhttp3.**
-dontwarn okio.**

##############################################
# 🔹 COMPOSE (usually not needed, but safe)
##############################################
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

##############################################
# 🔹 NAVIGATION
##############################################
-keep class androidx.navigation.** { *; }

##############################################
# 🔹 ENUMS (if you use them in API)
##############################################
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}