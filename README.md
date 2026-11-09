# JR-JSQUAD-M3-FP

Final project of module 3 for the **j-squad** group: a Java web application (Servlets + JSP) deployed on Tomcat.

## Structure

- `Homepage` — root app (`/`), lists all deployed student modules.
- `Example` — reference module (a small quiz) you can copy as a starting point.
- `<Your-Name>` — your own module (add it, see below).

## How to add your module

1. Fork this repository and clone your fork.
2. Copy `Example` into a new folder named after you, e.g. `Ivan-Petrenko`.
3. In your module's `pom.xml`:
   - change `<finalName>` — it becomes your URL path (`http://localhost:8080/<finalName>/`);
   - change `<name>`.
4. Rename the Java package to your own (e.g. `ua.com.javarush.jsquad.m3.ivanpetrenko`).
5. Register your module in the root `pom.xml` inside `<modules>`.
6. Write tests (JUnit 5 + Mockito) — they run on every push via GitHub Actions.
7. Open a pull request.

## How to run

Tomcat **11** is required (Jakarta EE 11: the code uses the `jakarta.servlet` API and JSTL 3.0 with the `jakarta.tags.core` taglib URI; Tomcat 9 and older do not support it).

### Docker (all modules)

```bash
docker build -t jsquad-m3 .
docker run --rm -p 8080:8080 jsquad-m3
```

Open http://localhost:8080/.

### Maven + local Tomcat

Requires JDK 21+, Maven and Tomcat 11.

```bash
mvn clean package
cp */target/*.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/catalina.sh run
```

### IntelliJ IDEA

Add a *Tomcat Server → Local* run configuration (Tomcat 11), and on the *Deployment* tab add the `<module>:war exploded` artifact with the context path you need.
