# fj-quarkus-tool

Fugerit quarkus tool

## Quickstart

Start in dev mode

```shell script
mvn compile quarkus:dev -Duser.timezone=UTC -Dfile.encoding=UTF-8
```

## Project creation script

```shell script
mvn io.quarkus:quarkus-maven-plugin:3.11.0:create \
-DprojectGroupId=org.fugerit.java.tool \
-DprojectArtifactId=fj-quarkus-tool \
-Dextensions='resteasy-reactive-jackson,quarkus-arc,quarkus-config-yaml,junit5'
```