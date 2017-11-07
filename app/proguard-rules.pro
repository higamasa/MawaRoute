# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\owner-PC\AppData\Local\Android\sdk1/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ----------------------------------------
# Support Library
# ----------------------------------------
-dontwarn android.databinding.**

# ----------------------------------------
# RxJava
# ----------------------------------------
-dontwarn io.reactivex.internal.util.unsafe.**
-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.Schedulers {
    public static ** test();
}

# ----------------------------------------
# Retrofit and OkHttp
# ----------------------------------------
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn retrofit2.**

# ----------------------------------------
# Picasso
# ----------------------------------------
-dontwarn com.squareup.okhttp.**


# ----------------------------------------
# Simple XML
# ----------------------------------------
-dontwarn com.bea.xml.stream.**
-dontwarn org.simpleframework.xml.stream.**
-keep class org.simpleframework.xml.**{ *; }
-keepclassmembers,allowobfuscation class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <init>(...);
}
