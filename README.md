# fj-quarkus-tool

Fugerit quarkus tool

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](https://github.com/fugerit-org/fj-quarkus-tool/blob/master/CHANGELOG.md)
[![license](https://img.shields.io/badge/License-Apache%20License%202.0-teal.svg)](https://opensource.org/licenses/Apache-2.0)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-quarkus-tool&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-quarkus-tool)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-quarkus-tool&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-quarkus-tool)
[![Docker images](https://img.shields.io/badge/dockerhub-images-important.svg?logo=Docker)](https://hub.docker.com/repository/docker/fugeritorg/fj-quarkus-tool/general)

[![Java runtime version](https://img.shields.io/badge/run%20on-java%2021+-%23113366.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java21.html)
[![Java build version](https://img.shields.io/badge/build%20on-GraalVM%2021+-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/gvm21.html)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9.0+-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://universe.fugerit.org/src/docs/versions/maven3_9.html)
[![Node JS](https://img.shields.io/badge/Node%20JS-20+-1AC736?style=for-the-badge&logo=nodedotjs&logoColor=white)](https://universe.fugerit.org/src/docs/versions/maven3_9.html)
## Quickstart

Start in dev mode back end

```shell script
mvn compile quarkus:dev -Duser.timezone=UTC -Dfile.encoding=UTF-8
```

Start in dev mode front end

```shell script
cd src/main/react
npm run start
```

## Java package version

build :

```shell script
mvn package -P buildreact
```

and run :

```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

## Java package uber jar version

build :

```shell script
mvn package -Dquarkus.package.type=uber-jar -P buildreact
```

and run :

```shell script
java -jar target/fj-quarkus-tool-*-runner.jar
```

## Docker container (jvm version)

Build :

```shell script
docker build --file src/main/docker/Dockerfile-jvm -t fj-quarkus-tool:local-jvm .
```

```shell script
docker run -p 8080:8080 --name fj-quarkus-tool-local fj-quarkus-tool:local-jvm
```

## Native package version

Build :

First you need to setup [GraalVM](https://www.graalvm.org/) :
[BUILDING A NATIVE EXECUTABLE](https://quarkus.io/guides/building-native-image)

```shell script
mvn install -Dnative -Pbuildreact
```

Run :

```shell script
./target/fj-quarkus-tool-*-runner -Duser.timezone=UTC -Dfile.encoding=UTF-8
```

## Project creation script

```shell script
mvn io.quarkus:quarkus-maven-plugin:3.11.0:create \
-DprojectGroupId=org.fugerit.java.tool \
-DprojectArtifactId=fj-quarkus-tool \
-Dextensions='resteasy-reactive-jackson,quarkus-arc,quarkus-config-yaml,junit5'
```