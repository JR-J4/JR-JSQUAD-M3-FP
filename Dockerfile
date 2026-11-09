# ─── 1) Build stage: compile each module and collect WARs ───
FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /workspace

# 1.1 Copy your full repo
COPY . .

# 1.2 (Optional) Pre-fetch dependencies for each module to speed up repeat builds
#    This loops through every first-level folder containing a pom.xml
RUN for module in $(find . -maxdepth 1 -type d ! -name .); do \
      if [ -f "$module/pom.xml" ]; then \
        echo "⏳ Going offline deps for $module"; \
        mvn -f "$module/pom.xml" dependency:go-offline -B; \
      fi; \
    done

# 1.3 Build each module’s WAR (skips your root POM altogether)
RUN for module in $(find . -maxdepth 1 -type d ! -name .); do \
      if [ -f "$module/pom.xml" ]; then \
        echo "🔨 Building $module"; \
        mvn -f "$module/pom.xml" package -DskipTests -B; \
      fi; \
    done

# 1.4 Gather every generated WAR into /artifacts
RUN mkdir -p /artifacts \
 && find . -type f -path "*/target/*.war" -exec cp {} /artifacts/ \;

# ─── 2) Runtime stage: drop all WARs into Tomcat ───
FROM tomcat:9.0-jdk17

# Clean out any default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy in all your student WARs
COPY --from=builder /artifacts/*.war /usr/local/tomcat/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
