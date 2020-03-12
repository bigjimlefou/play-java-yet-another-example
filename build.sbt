
name := """play-java-yet-another-example"""
organization := "com.yourorganisation"

// Generated dist package informations for RPM build
version := "1.0-SNAPSHOT"
maintainer in Linux := "Cédric Sougné <cedric.sougne@acis-group.org>"
packageSummary in Linux := "play-java-yet-another-example"
packageDescription := "Yet another java play framework example"
rpmRelease := "1"
rpmVendor := "Cédric Sougné"
rpmUrl := Some("http://www.acis-group.org")
rpmLicense := Some("All rights reserved")

// Javadoc Settings
lazy val Javadoc = config("genjavadoc") extend Compile

lazy val javadocSettings = inConfig(Javadoc)(Defaults.configSettings) ++ Seq(
  addCompilerPlugin("com.typesafe.genjavadoc" %% "genjavadoc-plugin" % "0.11" cross CrossVersion.full),
  scalacOptions += s"-P:genjavadoc:out=${target.value}/java",
  packageDoc in Compile := (packageDoc in Javadoc).value,
  sources in Javadoc :=
    (target.value / "java" ** "*.java").get ++
      (sources in Compile).value.filter(_.getName.endsWith(".java")),
  javacOptions in Javadoc := Seq(),
  artifactName in packageDoc in Javadoc := ((sv, mod, art) =>
    "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar")
)

// Swagger
// swaggerDomainNameSpaces := Seq("models.entities","models.dtos")
swaggerPlayJava := true

// Play framework
lazy val root = (project in file(".")).configs(Javadoc).settings(javadocSettings: _*).enablePlugins(PlayJava,PlayEbean,RpmPlugin,SwaggerPlugin)

// Rpm plugin requirements and config
enablePlugins(JavaServerAppPackaging)
enablePlugins(SystemdPlugin)

// Rpm plugin requirements and config - To catch play success exit status
serverLoading := Some(ServerLoader.Systemd)
systemdSuccessExitStatus in Debian += "143"
systemdSuccessExitStatus in Rpm += "143"
linuxPackageMappings += packageTemplateMapping(s"/var/lib/${packageName.value}")() withUser((daemonUser in Linux).value) withGroup((daemonGroup in Linux).value)

// Rpm plugin requirements and config - Execution config
javaOptions in Universal ++= Seq(
  // JVM memory tuning
  "-J-Xmx1024m",
  "-J-Xms512m",

  // Since play uses separate pidfile we have to provide it with a proper path
  // name of the pid file must be play.pid
  s"-Dpidfile.path=/var/run/${packageName.value}/play.pid",

  // alternative, you can remove the PID file
  // s"-Dpidfile.path=/dev/null",

  // Define Akka http server configuration
  s"-Dhttp.port=disabled",
  s"-Dhttps.port=9443",
  s"-Dhttps.keyStore=/usr/share/${packageName.value}/conf/${packageName.value}.keystore",
  s"-Dhttps.keyStorePassword=password",

  // Use separate configuration file for production environment
  s"-Dconfig.file=/usr/share/${packageName.value}/conf/production.conf",

  // Use separate logger configuration file for production environment
  // s"-Dlogger.file=/usr/share/${packageName.value}/conf/production-logger.xml",

  // Use separate logger configuration file for production environment
  // s"-Dlogger.file=/usr/share/${packageName.value}/conf/production-logger.xml",

  // You may also want to include this setting if you use play evolutions
  // "-DapplyEvolutions.default=true"
)

scalaVersion := "2.12.8"

// Project dependencies

val playPac4jVersion = "9.0.0-RC3"
val pac4jVersion = "4.0.0-RC3"
val playVersion = "2.8.0"
val guiceVersion = "4.2.2"

val guiceDeps = Seq(
  "com.google.inject" % "guice" % guiceVersion,
  "com.google.inject.extensions" % "guice-assistedinject" % guiceVersion
)

libraryDependencies ++= Seq(
  // Main play framework dependencies used
  guice,
  jdbc,
  ehcache,
  javaCore,
  javaJdbc,
  javaJpa,

  // EBean and database dependencies
  "io.ebean" % "ebean-elastic" % "11.39.1",
  "org.avaje.ebeanorm" % "avaje-ebeanorm-api" % "3.1.1",
  "org.avaje.ebeanorm" % "avaje-ebeanorm-api" % "3.1.1",
  "org.avaje" % "avaje-agentloader" % "4.3",

  // H2 database driver
  "com.h2database" % "h2" % "1.4.199",

  // play-pac4j
  "org.pac4j" %% "play-pac4j" % playPac4jVersion,
  "org.pac4j" % "pac4j-http" % pac4jVersion,
  "org.pac4j" % "pac4j-cas" % pac4jVersion,
  "org.pac4j" % "pac4j-openid" % pac4jVersion exclude("xml-apis" , "xml-apis"),
  "org.pac4j" % "pac4j-oauth" % pac4jVersion,
  "org.pac4j" % "pac4j-saml" % pac4jVersion exclude("org.springframework", "spring-core"),
  "org.pac4j" % "pac4j-oidc" % pac4jVersion exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-gae" % pac4jVersion,
  "org.pac4j" % "pac4j-jwt" % pac4jVersion exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-ldap" % pac4jVersion,
  "org.pac4j" % "pac4j-sql" % pac4jVersion,
  "org.pac4j" % "pac4j-mongo" % pac4jVersion,
  "org.pac4j" % "pac4j-kerberos" % pac4jVersion exclude("org.springframework", "spring-core"),
  "org.pac4j" % "pac4j-couch" % pac4jVersion,

  "com.typesafe.play" % "play-cache_2.12" % playVersion,
  "commons-io" % "commons-io" % "2.6",
  "be.objectify" %% "deadbolt-java" % "2.6.1",

  // Test
  "org.awaitility" % "awaitility" % "2.0.0" % Test,
  "org.assertj" % "assertj-core" % "3.6.2" % Test,
  "org.mockito" % "mockito-core" % "2.1.0" % Test,

  // For Java > 8
  "javax.xml.bind" % "jaxb-api" % "2.3.0",
  "javax.annotation" % "javax.annotation-api" % "1.3.2",
  "javax.el" % "javax.el-api" % "3.0.0",
  "org.glassfish" % "javax.el" % "3.0.0",

  // To provide an implementation of JAXB-API, which is required by Ebean.
  "javax.activation" % "activation" % "1.1.1",
  "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2",

  // Api
  "io.dropwizard.metrics" % "metrics-core" % "3.2.6",
  "com.palominolabs.http" % "url-builder" % "1.1.0",
  "net.jodah" % "failsafe" % "1.0.5",

  // JGravatar

  "com.timgroup" % "jgravatar" % "1.2",

  // Play-Bootstrap
  "com.adrianhurt" %% "play-bootstrap" % "1.5.1-P27-B4",

  // Swagger ui
  "org.webjars" % "swagger-ui" % "2.2.0"
) ++ guiceDeps //For Play 2.6 & JDK9

testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

// Add some dependecy repositories

resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/", "Shibboleth releases" at "https://build.shibboleth.net/nexus/content/repositories/releases/")

// Dependency injection for play routing

routesGenerator := InjectedRoutesGenerator