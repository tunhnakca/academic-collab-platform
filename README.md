# LearningPlatformFirstTerm

This project is carried out in cooperation with Tunahan Akça and Mert Munar under the leadership of our teacher Ahmet Özmen for Seng students at Sakarya University

## Spring Boot and Parcel Integration

This guide explains step-by-step how to integrate **Parcel** with your **Spring Boot** project to manage and compile frontend assets.

### 1. Installing Parcel

#### 1.1 Create package.json in src/main/resources/static/

Create a package.json file inside root directory to manage frontend dependencies.

```bash
npm init -y
```

#### 1.2 Install Parcel

Install Parcel as a development dependency.

```bash
npm install --save-dev parcel concurrently
```

#### 1.3 Update package.json

Update your package.json file with the following scripts for watching, building assets, and running the application:

```json
{
  "scripts": {
    "watch": "npx parcel watch src/main/resources/static/js/app.js src/main/resources/static/scss/main.scss --dist-dir src/main/resources/static/dist --public-url /dist",
    "build-assets": "npx parcel build src/main/resources/static/js/app.js src/main/resources/static/scss/main.scss --dist-dir src/main/resources/static/dist --public-url /dist",
    "build": "npm run build-assets && ./mvnw clean package",
    "dev": "concurrently -k \"npm run watch\" \"./mvnw spring-boot:run\""
  }
}
```

- watch: Watches for changes in JavaScript and SCSS files, then updates the dist directory dynamically.

- build-assets: Builds optimized assets for production.

- build: Combines the build-assets script with Maven's clean and package commands to create a production-ready build.

- dev: Runs the Parcel watcher and the Spring Boot application concurrently for a seamless development experience.

#### 1.4 Run Parcel and Spring Boot

To start development mode with Parcel and Spring Boot:

```bash
npm run dev
```

To build assets for production:

```bash
npm run build-assets
```

To create a complete production build (frontend + backend):

```bash
npm run build
```

### 2. Cache Busting with Spring Boot

Add the following lines to your application.properties file to enable cache busting for static resources:

```properties
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**
```

These settings append a hash to your static asset filenames, ensuring browsers always fetch the latest versions of your files after updates.
<<<<<<< HEAD
=======

>>>>>>> main
