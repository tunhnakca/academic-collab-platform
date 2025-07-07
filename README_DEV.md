# academic-collab-platform

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

Update your package.json file with the following scripts for watching, building, cleaning, and running the application:
```json
{
    "scripts": {
    "watch": "npx parcel watch src/main/resources/static/js/app.js src/main/resources/static/scss/main.scss --dist-dir src/main/resources/static/dist --public-url /dist",
    "build-assets": "npx parcel build src/main/resources/static/js/app.js src/main/resources/static/scss/main.scss --dist-dir src/main/resources/static/dist --public-url /dist",
    "build": "npm run build-assets && ./mvnw clean package",
    "clean": "rm -rf .parcel-cache src/main/resources/static/dist",
    "dev": "npm run clean && concurrently -k --names \"watch,backend\" \"npm run watch\" \"./mvnw spring-boot:run\""
  }
}
```

- watch: Watches for changes in JavaScript and SCSS files, then updates the dist directory dynamically.

- build-assets: Builds optimized assets for production.

- build: Combines the build-assets script with Maven's clean and package commands to create a production-ready build.

- clean: Deletes the .parcel-cache and previously built assets in dist directory.

- dev: Cleans old assets and starts both the Parcel watcher and Spring Boot backend concurrently, ensuring a smooth development environment.

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
spring.web.resources.chain.strategy.content.enabled=true
spring.web.resources.chain.strategy.content.paths=/**
```

These settings append a hash to your static asset filenames, ensuring browsers always fetch the latest versions of your files after updates.

## 🔐 Environment Variables Setup

This project uses a `.env` file to load mail credentials.

1. Copy the example:
```bash
cp .env.example .env

then fill in your values:

MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
