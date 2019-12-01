### Publish gradle and kotlin compiler plugins to maven local repo
./gradlew --console=plain :compiler-plugin:publishToMavenLocal :gradle-plugin:publishToMavenLocal

### For debug
./gradlew --rerun-tasks --console=plain --info :compiler-plugin:publishToMavenLocal :example:assemble | grep "TAG"
