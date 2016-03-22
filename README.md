[BITalino](http://www.bitalino.com) Java SDK
=============================================

Here you'll find the BITalino protocol and utilities meant to write to/read from BITalino devices.

Note that the physical link (i.e. Bluetooth connection) management however, is not included, since it's OS-dependent.

This SDK has been test both in [Java (Windows, Mac and Linux)](https://github.com/bitalino/bitalino-java-example) and [Android](https://github.com/bitalino/bitalino-android-example) applications.

## Prerequisites
- JDK 7 or newer
- Maven 3.1.0 or newer

## Build

```
mvn clean install
```

## Usage

### Maven

```
<dependency>
  <groupId>com.bitalino</groupId>
  <artifactId>bitalino-java-sdk</groupId>
  <version>1.1.0</version>
</dependency>
```

### Gradle

```
dependencies {
  compile 'com.bitalino:bitalino-java-sdk:1.1.0'
}
```
