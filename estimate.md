#  Project Estimate — Rating System (Java Backend)

## Overview
The project implements a rating system for in-game item sellers.  
Main features: registration, email verification (Redis), comment moderation, rating calculation, and admin control.

---

## Development Phases and Time Estimates

| # | Phase | Description | Estimated Time |
|---|--------|--------------|----------------|
| **1** | **Environment Setup** | Create Spring Boot project, configure PostgreSQL, Redis, and email service | **4h** |
| **2** | **Database Design** | Design ER diagram, create JPA entities, and relationships | **6h** |
| **3** | **Authentication Module** | Implement registration, login, JWT security, and role-based access | **10h** |
| **4** | **Email Confirmation** | Implement Redis-based email verification and activation link | **6h** |
| **5** | **Password Reset** | Implement forgot/reset password logic using Redis codes | **6h** |
| **6** | **Seller Module** | CRUD for Seller profiles and admin approval flow | **6h** |
| **7** | **Game Module** | CRUD for games (add/edit/delete only by seller) | **5h** |
| **8** | **Comment Module** | Add, update, delete, and admin verification of comments | **8h** |
| **9** | **Rating Logic** | Calculate and display top sellers by average rating and game | **6h** |
| **10** | **Filtering and Search** | Filter sellers by game, rating range | **4h** |
| **11** | **Testing** | 2 Unit + 2 Integration tests using JUnit and Spring Boot Test | **6h** |
| **12** | **Documentation** | Swagger / Postman setup, README, estimate.md, database diagram PDF | **4h** |
| **13** | **Version Control and PRs** | Frequent commits, PR reviews, branch management | **2h** |

---

## Total Estimated Time
**≈ 69 hours (8–9 full days)**

---

## Notes
- Use **Spring Security + JWT** for role separation (Admin/Seller/User).
- **Redis** handles email confirmation and password reset code storage.
- Admin manually verifies both new sellers and comments.
- Testing should include basic integration (API) and service-level unit tests.
- Provide clear Git commit history and documentation.

