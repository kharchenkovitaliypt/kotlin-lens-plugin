#### Init publish to the maven local repo
./gradlew --console=plain :compiler-plugin:publishToMavenLocal :gradle-plugin:publishToMavenLocal

#### For debug
./gradlew --rerun-tasks --console=plain --info :compiler-plugin:publishToMavenLocal :example:assemble | grep "TAG"

./gradlew --rerun-tasks --console=plain --info -Dkotlin.compiler.execution.strategy="in-process"  --no-daemon -Dorg.gradle.debug=true  :compiler-plugin:publishToMavenLocal :example:assemble

