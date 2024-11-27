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
npm install --save-dev parcel
```

#### 1.3 Update package.json

Update your package.json file with build and start scripts:

```json
{
  "name": "learning-platform",
  "version": "1.0.0",
  "scripts": {
    "start": "parcel src/main/resources/static/js/app.js src/main/resources/static/scss/main.scss --dist-dir src/main/resources/static/dist",
    "build": "parcel build src/main/resources/static/js/app.js src/main/resources/static/scss/main.scss --dist-dir src/main/resources/static/dist"
  },
  "author": "Tunahan Akça",
  "license": "ISC",
  "devDependencies": {
    "parcel": "latest"
  }
}
```

#### 1.4 Run Parcel

To start development mode:

```bash
npm start
```

To build for production:

```bash
npm run build
```
