bitalino-java-sdk
======================================

Bitalino Java SDK derived from code available from the official website, BITalino.com.

## Prerequisites ##
- JDK 6 or 7
- Maven 3.1.0 or newer
- On Ubuntu, install _libbluetooth-dev_

## Test with device ##

The next step builds the library and installs it into the local Maven repository. It's only needed 
when you're bootstraping the example. Run it, though! 
```
mvn clean install
```

Now, execute the example.
```
mvn --projects example exec:java
```

## Tested on ##
- Ubuntu 12.04+ with OpenJDK 7 64-bit
- MacOS X 10.7.5 with Oracle JDK 1.7.0_40 64-bit
- Windows 7 with Oracle JDK 1.7.0_02 64-bit 
- Also, the library has been tested on Android 4.3 (Galaxy Nexus)

## Trouble-shooting ##
- https://groups.google.com/forum/#!forum/bluecove-users
