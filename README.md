maven-nailgun [![Build Status](https://travis-ci.org/nigelzor/maven-nailgun.svg?branch=master)](https://travis-ci.org/nigelzor/maven-nailgun)
=============

like [mvnsh](https://github.com/jdillon/mvnsh), using [nailgun](http://martiansoftware.com/nailgun/)

without any serious benchmarking, they seem to offer comparable speedup. trivial apps will save a second or two of jvm startup, but larger ones will see that gain erased by memory lost to leaked classloaders.

using it:
---------
* copy nailgun-server-0.9.1.jar and maven-nailgun-1.0-SNAPSHOT.jar into maven's /lib/ext/
* in maven's /bin/, make copies of `mvn` and `m2.conf` (`mvn-ng-server` and `m2-ng.conf`)
* in `mvn-ng-server` change `"-Dclassworlds.conf=${M2_HOME}/bin/m2.conf"` to `"-Dclassworlds.conf=${M2_HOME}/bin/m2-ng.conf"`
* in `m2-ng.conf` change `org.apache.maven.cli.MavenCli` to `com.github.nigelzor.maven.nailgun.Server`
* start the server with `mvn-ng-server localhost:2113`
* whenever you would call `mvn foo`, now use `ng com.github.nigelzor.maven.nailgun.Client foo`

