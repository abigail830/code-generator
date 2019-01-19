#!/bin/bash
./gradlew build fatJar && java -jar ./build/libs/code-generator-all-1.0-SNAPSHOT.jar
