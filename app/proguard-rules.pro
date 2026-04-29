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
##############################################
# 🔥 GSON FIX (CRITICAL)
##############################################

# Keep generic signatures (you already have, keep it)
-keepattributes Signature

# Keep TypeToken and its subclasses (VERY IMPORTANT)
-keep class com.google.gson.reflect.TypeToken { *; }

# 🔥 THIS is the missing piece (most important)
-keep class * extends com.google.gson.reflect.TypeToken

# Keep Gson core (safe)
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Keep your model classes
-keep class com.example.myapplication.**.model.** { *; }

# Keep fields inside models
-keepclassmembers class com.example.myapplication.**.model.** {
    <fields>;
}

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

#  LOG REMOVAL ==
# Remove logs in release (performance)
-assumenosideeffects class android.util.Log {
  public static *** d(...);
  public static *** w(...);
  public static *** v(...);
  public static *** i(...);
  public static *** e(...);
}