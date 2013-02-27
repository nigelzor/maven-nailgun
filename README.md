maven-nailgun
=============

using it:
---------
* copy maven-nailgun-1.0-SNAPSHOT.jar into maven's /lib/ext/
* build a server-startup script, starting from `mvn`
  - add nailgun-server-0.9.1.jar and maven-nailgun-1.0-SNAPSHOT.jar to the -classpath
  - replace `${CLASSWORLDS_LAUNCHER} "$@"` with `com.martiansoftware.nailgun.NGServer`
* whenever you would call `mvn foo`, now use `ng com.github.nigelzor.maven.nailgun.Client foo`

