# 1. Java 23 JDK 이미지 사용
FROM eclipse-temurin:23-jdk AS builder

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 프로젝트 파일 복사
COPY . .

# 4. Gradle 빌드 실행
RUN ./gradlew build -x test

# 5. 실행용 JDK 이미지
FROM eclipse-temurin:23-jre

# 6. 실행 디렉토리 설정
WORKDIR /app

# 7. 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 8. 컨테이너에서 실행할 명령
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]

