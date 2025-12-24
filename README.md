# NavVis Code Challenge

Simple Spring Boot service to localize a 3D point (x, y, z).

## Prerequisites
* Please configure the JDK 21 or above in your environment (Java 21 or above. [Java](https://www.oracle.com/java/technologies/downloads/) download available here.)
* A build tool such as [Maven](https://maven.apache.org/). However, Installing maven locally isn't necessary because Springboot uses maven wrapper.

## Quick start (from project root)

1. Verify Java and the wrapper:

   ```powershell
   java -version
   .\mvnw.cmd -v
   ```

2. Build and run tests:

   ```powershell
   .\mvnw.cmd clean test
   ```

3. Build a runnable jar:

   ```powershell
   .\mvnw.cmd clean package
   ```

That's it, these are the only steps you need to build, test, and run the service locally.
