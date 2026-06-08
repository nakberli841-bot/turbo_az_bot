# 🚗 Turbo.az Automated Car Scraper & Telegram Notification Bot

An automated, intelligent web scraping system built with **Java 17** and **Spring Boot** that tracks newly posted car advertisements on Turbo.az. The system filters these advertisements against customized user-defined search criteria and delivers real-time notifications to users via the **Telegram Bot API**.

---

## 🏗️ System Architecture & Workflow

The system is designed following professional backend and data pipeline patterns:

[ Turbo.az Website ] 
        │
        ▼ (Scheduled Web Scraping via Jsoup / Elements)
[ ScraperScheduler / TurboAzScraper ]
        │
        ▼ (Maps raw data to CarAd Entities)
[ MatchingService ] <───► [ PostgreSQL / MySQL Database ]
        │                      (Fetches User Search Criteria & Sessions)
        ▼ (Finds matching criteria)
[ NotificationService ]
        │
        ▼ (Executes non-blocking Telegram alerts)
[ TurboAzBot (Telegram API) ] ───► [ 📱 Telegram Users ]

1. Scraping Engine: TurboAzScraper periodically parses the target automobile marketplace using a multi-threaded execution structure handled by ScraperScheduler.
2. Persistence Layer: Raw data is processed, filtered, and mapped to optimized DB models using standard JPA Repositories and tailored query optimizations via CarAddCustomRepo.
3. State & Session Management: UserSession tracks interactive onboarding and stateful multi-step filter generation within the Telegram Interface.
4. Asynchronous Matching Engine: MatchingService continuously triggers high-performance cross-checks between active newly discovered items (CarAd) and user configurations (SearchCriteria), preventing redundant database lookups.

---

## 🛠️ Technology Stack

- Core Framework: Java 17, Spring Boot 3.x
- Data Access: Spring Data JPA, Hibernate, Custom Repository Implementations (CarAddCustomRepo)
- Automated Execution: Spring Scheduling (@EnableScheduling)
- API Integration: Telegram Bots Spring Boot Starter
- Object Mapping: Component-driven DTO Mapping pattern (CarAdDtoMapper)
- Build Automation: Gradle

---

## 📂 Project Structure

com.nurlan.turboazbot.turbo_az_bot
│
├── controller/          # REST Endpoints for system checks and management
│   ├── ResultController.java
│   └── UserController.java
│
├── entity/              # Database models defining the core business relationships
│   ├── CarAd.java           # Tracks individual car listing properties
│   ├── SearchCriteria.java  # Represents user-defined search filters (Price, Model, Year, etc.)
│   ├── User.java            # Represents active Telegram clients
│   ├── UserSession.java     # Tracks stateful interactive steps of users in the Bot
│   └── NotificationLog.java # System logs recording dispatched alert histories
│
├── repo/                # Native and Dynamic Custom Query Execution Layers
│   ├── CarAddRepo.java / CarAddCustomRepo.java
│   ├── CriteriaDataRepo.java
│   ├── UserDataRepo.java
│   └── UsersessionRepo.java
│
├── scrapper/            # Periodic Web HTML Parser and Target Extraction Job Scheduler
│   ├── TurboAzScraper.java
│   └── ScraperScheduler.java
│
├── service/             # Encapsulated Business Logic Layer
│   ├── MatchingService.java     # Algorithmic check between Ads and Target Criteria
│   ├── NotificationService.java # Handles messaging pipeline and logs dispatches
│   └── UserService.java         # Context manager for bot user operations
│
└── telegram/            # Interactive Interface Handlers
    └── TurboAzBot.java  # Custom Telegram Long-Polling Core Event Listener

---

## ⚙️ Core System Capabilities

- Stateful Interactive Telegram UI: Uses multi-step conversational input screens managed dynamically in the database via UserSession to construct precise search filters.
- Dynamic Database Queries: Features automated complex SQL logic via CarAddCustomRepo ensuring performant matching workflows without overhead.
- Fail-Safe Dispatching: Utilizing NotificationLog, the system ensures a strict at-most-once or exactly-once dispatch matrix so users are never spammed with repetitive duplicate alerts.

---

## 🚀 Getting Started (Local Deployment)

### Prerequisites
- JDK 17 or higher installed
- Database Engine (PostgreSQL / MySQL) active
- Telegram Bot Token (Acquired via @BotFather on Telegram)

### Configuration
Update the environment parameters inside your src/main/resources/application.properties file:

# Database Connectivity Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/turboaz_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# Hibernate Configurations
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Telegram Bot Credentials
bot.name=YourTurboAzScraperBot
bot.token=YOUR_TELEGRAM_BOT_BOT_TOKEN_HERE

# Scraping Interval Properties
scraper.fixed-rate.ms=300000

### Building and Running

1. Clone or extract the repository.
2. Build the executable jar without test performance overhead via Gradle:
./gradlew clean build -x test

3. Boot the application:
java -jar build/libs/turbo-az-bot-0.0.1-SNAPSHOT.jar

---

## 📜 Database Entity Mapping Guide

- User to SearchCriteria (1:N): A single user can register multiple distinct car alarms (e.g., "BMW under $15,000" and "Toyota Prius under $9,000").
- User to UserSession (1:1): Safely persists conversational input state if the bot setup flow gets interrupted mid-way.
- CarAd to NotificationLog (1:N): Records logs regarding exactly which client was notified about a newly discovered vehicle ad.
