# LeverX Final Project

This is a Spring Boot application, final project for leverX course, that provides a RESTful API for managing users, games, game objects, and comments. It includes features for user authentication, authorization, and email verification.

## Table of Contents

- [Technologies Used](#technologies-used)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Users](#users)
  - [Games](#games)
  - [Game Objects](#game-objects)
- [Database Schema](#database-schema)

## Technologies Used

- **Java 21**
- **Spring Boot 3**
- **Spring Security** for authentication and authorization.
- **Spring Data JPA** for database access.
- **PostgreSQL** as the relational database.
- **Redis** for caching.
- **JWT (JSON Web Tokens)** for securing API endpoints.
- **Lombok** to reduce boilerplate code.
- **MapStruct** for object mapping.
- **Liquibase** for database schema management.
- **JUnit 5** and **Mockito** for testing.

## Configuration

Before running the application, you need to set up the following environment variables:

- `LEVERX_FINAL_PROJECT_DB_URL`: The JDBC URL for your PostgreSQL database.
- `LEVERX_FINAL_PROJECT_USER`: The username for your PostgreSQL database.
- `LEVERX_FINAL_PROJECT_PASSWORD`: The password for your PostgreSQL database.
- `JAVA_COURSE_JWT_KEY`: A secret key for signing JWTs.
- `DESIGN_SERVICE_EMAIL`: The email address for sending verification emails.
- `DESIGN_SERVICE_APP_PASSWORD`: The password for the email account.

These can be configured in your IDE's run configuration or as system environment variables.

The application configuration is located in `src/main/resources/application.yaml`.

## Running the Application

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    ```
2.  **Set up the environment variables** as described in the [Configuration](#configuration) section.
3.  **Build and run the application** using Maven:
    ```bash
    ./mvnw spring-boot:run
    ```
The application will start on `http://localhost:8080`.

## API Endpoints

### Authentication

- `POST /register`: Register a new user.
- `GET /confirm_email`: Confirm a user's email address with a verification code.
- `POST /reset_password`: Initiate the password reset process for a user.
- `POST /auth/login`: Authenticate a user and receive a JWT.

### Users

- `GET /users/profile`: Get a paginated list of all users.
- `GET /users/profile/{id}`: Get a user by their ID.
- `GET /users/profile/top_sellers`: Get a paginated list of top sellers.
- `GET /users/profile/get_seller_by_game_and_rating`: Get sellers based on a game and rating range.
- `GET /users/unverified`: (Admin) Get a paginated list of unverified users.
- `GET /users/unverified/{id}`: (Admin) Get an unverified user by their ID.
- `POST /users/verify?id={id}`: (Admin) Verify a user.
- `POST /users/{id}/comments`: Add a comment to a user's profile.
- `GET /users/{id}/comments`: Get a paginated list of a user's comments.
- `GET /users/{userId}/comments/{commentId}`: Get a specific comment.
- `DELETE /users/{userId}/comments/{commentId}`: Delete a comment.
- `PUT /users/{userId}/comments/{commentId}`: Edit a comment.
- `GET /users/unapproved_comments`: (Admin) Get a paginated list of unapproved comments.
- `POST /users/unapproved_comments/approve/{userId}/comment/{commentId}`: (Admin) Approve a comment.
- `DELETE /users/unapproved_comments/reject/{userId}/comment/{commentId}`: (Admin) Reject a comment.
- `GET /users/unapproved_comments/{commentId}`: (Admin) Get an unapproved comment by its ID.
- `POST /users/profile/create_seller`: Create a seller account.

### Games

- `GET /game`: (Seller/Admin) Get a paginated list of all games.
- `POST /game`: (Seller/Admin) Create a new game.

### Game Objects

- `GET /game_object`: (Seller/Admin) Get a paginated list of all game objects.
- `GET /game_object/{id}`: (Seller/Admin) Get a game object by its ID.
- `POST /game_object`: (Seller/Admin) Create a new game object.
- `PUT /game_object/{id}`: (Seller/Admin) Update a game object.
- `DELETE /game_object/{id}`: (Seller/Admin) Delete a game object.

## Database Schema

The application uses the following database tables, which are created and managed by Liquibase based on the JPA entities:

-   **app_user**: Stores user information, including their username, email, password, and roles.
-   **role**: Stores user roles (e.g., `USER`, `ADMIN`).
-   **game**: Stores information about games.
-   **game_object**: Stores information about objects within a game, linked to the `game` table.
-   **comment**: Stores comments made by users about other users, linked to the `app_user` table.

The relationships between these tables are defined in the JPA entities located in the `src/main/java/alik/leverxfinalproject/entity` directory.
