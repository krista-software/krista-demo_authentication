# Demo Authentication Extension for Krista

**Version:** 1.0  
## System Architecture Overview

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐             │
│  │   Browser    │  │   Chatbot    │  │    Portal    │             │
│  │  (Web App)   │  │     UI       │  │      UI      │             │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘             │
│         │                  │                  │                      │
│         └──────────────────┴──────────────────┘                      │
│                            │                                         │
│                    authenticator.js                                  │
└────────────────────────────┼────────────────────────────────────────┘
                             │ HTTPS/REST
                             ↓
┌─────────────────────────────────────────────────────────────────────┐
│                    DEMO AUTHENTICATION EXTENSION                    │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │                    REST API LAYER (JAX-RS)                    │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │ │
│  │  │POST /login  │  │POST /logout │  │ GET /type   │          │ │
│  │  └─────────────┘  └─────────────┘  └─────────────┘          │ │
│  └───────────────────────────────────────────────────────────────┘ │
│                             │                                        │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │              BUSINESS LOGIC LAYER                             │ │
│  │  ┌──────────────────┐  ┌──────────────────┐                  │ │
│  │  │ Email Validation │  │ Account Creation │                  │ │
│  │  └──────────────────┘  └──────────────────┘                  │ │
│  │  ┌──────────────────┐  ┌──────────────────┐                  │ │
│  │  │Session Management│  │  Role Assignment │                  │ │
│  │  └──────────────────┘  └──────────────────┘                  │ │
│  └───────────────────────────────────────────────────────────────┘ │
│                             │                                        │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │                 KRISTA SDK INTEGRATION LAYER                  │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │ │
│  │  │SessionManager│  │AccountManager│  │ RoleManager  │       │ │
│  │  └──────────────┘  └──────────────┘  └──────────────┘       │ │
│  └───────────────────────────────────────────────────────────────┘ │
└─────────────────────────────┼───────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────────┐
│                      KRISTA PLATFORM LAYER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐             │
│  │  Workspace   │  │   Database   │  │    Session   │             │
│  │   Manager    │  │    Storage   │  │    Store     │             │
│  └──────────────┘  └──────────────┘  └──────────────┘             │
└─────────────────────────────────────────────────────────────────────┘
```

### Architecture Layers

| Layer | Responsibility | Technology |
|-------|----------------|------------|
| **Client Layer** | User interface, HTTP requests | JavaScript, HTML, CSS |
| **REST API Layer** | HTTP endpoints, request/response | JAX-RS, Jersey |
| **Business Logic Layer** | Authentication logic, validation | Java 21 |
| **SDK Integration Layer** | Platform integration | Krista SDK (KSDK) |
| **Platform Layer** | Data persistence, session storage | Krista Platform |

---



## License

This project is licensed under the **GNU General Public License v3.0**.

```
Demo Authentication Extension for Krista
Copyright (C) 2025 Krista Software

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```

For the full license text, see the [LICENSE](LICENSE) file or visit https://www.gnu.org/licenses/gpl-3.0.html.
