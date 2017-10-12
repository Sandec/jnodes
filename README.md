# simplefx-util

### publish
local:
```gradle
gradle :publishToMavenLocal jnodes-devices:publishToMavenLocal jnodes-config:publishToMavenLocal jnodes-lang:publishToMavenLocal
```

note: you have to bintray-account in ~/.gradle/gradle.properties
```gradle
gradle :bintrayUpload jnodes-devices:bintrayUpload jnodes-config:bintrayUpload jnodes-lang:bintrayUpload
```

