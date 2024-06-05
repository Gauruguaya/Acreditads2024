plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.acreditads2023"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.acreditads2023"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions {
        exclude ("META-INF/DEPENDENCIES")
        exclude ("META-INF/LICENSE")
        exclude ("META-INF/LICENSE.txt")
        exclude ("META-INF/license.txt")
        exclude ("META-INF/NOTICE")
        exclude ("META-INF/NOTICE.txt")
        exclude ("META-INF/notice.txt")
        exclude ("META-INF/ASL2.0")
        exclude ("META-INF/INDEX.LIST")
    }
}


dependencies {


    implementation ("com.android.volley:volley:1.2.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.apis:google-api-services-sheets:v4-rev20240402-2.0.0")
    implementation ("com.google.api-client:google-api-client:2.5.1")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.17.1")

}