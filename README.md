# Medication App

A modern Android application for managing medical problems and associated medications. Built with Jetpack Compose and following clean architecture principles.

## Architecture Overview

![Architecture Diagram]

The application follows MVVM (Model-View-ViewModel) architecture pattern with Clean Architecture principles:

- **View (UI/XML)**: The UI layer built with Jetpack Compose that handles user interactions
- **ViewModel**: Acts as an intermediate layer containing UI logic and state management using RxJava/LiveData
- **Repository**: Central point for data management that coordinates between local and remote data sources
- **Data Sources**:
  - Local DataSource: Handles local database operations using Room
  - Remote DataSource: Manages API calls and network operations

The architecture ensures:
- Clear separation of concerns
- Unidirectional data flow
- Better testability
- Offline-first capability

## Features

- View and manage medical problems
- Track medications and drug details
- Offline support with local database caching
- Clean and modern Material Design UI
- Network connectivity monitoring

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM + Clean Architecture
- **Dependency Injection:** Hilt
- **Database:** Room
- **Networking:** Retrofit
- **Concurrency:** Kotlin Coroutines + Flow
- **Build System:** Gradle

## Database Schema

### Problems Table

CREATE TABLE problems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    problemData TEXT NOT NULL
);

CREATE TABLE drugs (
    name TEXT PRIMARY KEY,
    dose TEXT NOT NULL,
    strength TEXT NOT NULL
);

CREATE TABLE problem_drug_cross_ref (
    problemId INTEGER NOT NULL,
    drugName TEXT NOT NULL,
    className TEXT NOT NULL,
    classType TEXT NOT NULL,
    associatedDrugType TEXT NOT NULL,
    PRIMARY KEY (problemId, drugName),
    FOREIGN KEY (problemId) REFERENCES problems(id),
    FOREIGN KEY (drugName) REFERENCES drugs(name)
);


Run the tests using either:
----------------------------------
Android Studio's test runner (right-click on test class → Run)
Terminal command: ./gradlew connectedAndroidTest


