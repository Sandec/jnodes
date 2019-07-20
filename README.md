# simplefx-util

### publish
local:
```gradle
./gradlew :publishToMavenLocal jnodes-devices:publishToMavenLocal jnodes-config:publishToMavenLocal jnodes-lang:publishToMavenLocal
```

note: you have to bintray-account in ~/.gradle/gradle.properties
```gradle
./gradlew :bintrayUpload jnodes-devices:bintrayUpload jnodes-config:bintrayUpload jnodes-lang:bintrayUpload
```

