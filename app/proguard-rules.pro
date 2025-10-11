# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.potaninpm.feature_home.data.remote.dto.** { *; }
-keep class com.potaninpm.feature_home.domain.model.** { *; }

# Koin specific rules for Compose and ViewModel
-keep class org.koin.androidx.viewmodel.ext.android.ViewModelOwnerKt
-keep class org.koin.androidx.compose.CompatViewModelComposeKt

# Keep rules for Kotlin reflection
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    private void readObjectNoData();
    private java.lang.Object writeReplace();
    private java.lang.Object readResolve();
}

# Keep AuthViewModel and its dependencies for Koin
-keep class com.potaninpm.feature_auth.presentation.viewModels.** { *; }

# This is generated automatically by the Android Gradle plugin.
-dontwarn kotlinx.parcelize.Parcelize