# Demo Authentication Extension - Architecture Document

## Document Information

**Version:** 1.0  
**Last Updated:** 2026-01-15  
**Extension Version:** 3.5.8  
**Author:** Technical Architecture Team  
**Status:** ✅ Complete

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [System Architecture Overview](#system-architecture-overview)
3. [Layered Architecture](#layered-architecture)
4. [Component Architecture](#component-architecture)
5. [Data Flow Architecture](#data-flow-architecture)
6. [Deployment Architecture](#deployment-architecture)
7. [Performance Architecture](#performance-architecture)
8. [Error Handling Architecture](#error-handling-architecture)
9. [Security Architecture](#security-architecture)
10. [Integration Architecture](#integration-architecture)
11. [Scalability & Limitations](#scalability--limitations)

---

## Executive Summary

### Purpose

The Demo Authentication Extension is a lightweight, email-based authentication system designed for demonstrations, proof-of-concepts, and development environments within the Krista platform ecosystem.

### Key Architectural Characteristics

| Characteristic | Description |
|----------------|-------------|
| **Architecture Style** | Layered, RESTful, Microservice |
| **Runtime** | Java 21, JAX-RS |
| **Integration** | Krista Platform SDK (KSDK) |
| **Authentication** | Email-based, Session-managed |
| **Deployment** | Krista Extension Framework |
| **Scalability** | Small to medium workloads (< 1000 users) |
| **Performance** | Low latency (< 300ms login) |

### Design Principles

1. **Simplicity First**: Minimal authentication for rapid prototyping
2. **Zero Configuration**: Works out-of-the-box with sensible defaults
3. **Platform Integration**: Deep integration with Krista workspace
4. **Stateless REST**: RESTful API design with session management
5. **Fail-Safe**: Graceful error handling and recovery

---

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

## Layered Architecture

### Layer 1: Presentation Layer (Client-Side)

**Purpose**: User interaction and authentication UI

**Components**:
- **authenticator.js**: JavaScript library for authentication
- **Login Forms**: HTML forms for email input
- **Session Storage**: Client-side session management

**Responsibilities**:
```
┌─────────────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │           authenticator.js Library                  │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • init(data, url)                                  │   │
│  │  • login(payload)                                   │   │
│  │  • logout()                                         │   │
│  │  • decorateRequest()                                │   │
│  │  • validatePayload(payload)                         │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Login Form UI                          │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • Email input field                                │   │
│  │  • Submit button                                    │   │
│  │  • Error display                                    │   │
│  │  • Loading indicator                                │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │          Session Storage                            │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • Store session ID                                 │   │
│  │  • Store account info                               │   │
│  │  • Manage session lifecycle                         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Key Files**:
- `authenticator.js` (102 lines)
- Location: `src/main/resources/authenticator.js`

**API Interactions**:
```javascript
// Login flow
fetch('/authn/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email: 'user@example.com' })
})

// Authenticated request
fetch('/api/endpoint', {
  headers: {
    'X-Krista-Context': encodeURIComponent(JSON.stringify({
      clientSessionId: sessionId
    }))
  }
})
```

---

### Layer 2: REST API Layer (JAX-RS)

**Purpose**: HTTP endpoint exposure and request handling

**Components**:
- **ExtensionResource**: JAX-RS resource class
- **CORS Handler**: Cross-origin request support
- **Request Validators**: Input validation

**Architecture**:
```
┌─────────────────────────────────────────────────────────────┐
│                    REST API LAYER                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         ExtensionResource.java                      │   │
│  │         (JAX-RS Resource)                           │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │                                                     │   │
│  │  @POST /login                                       │   │
│  │  ┌───────────────────────────────────────────────┐ │   │
│  │  │ 1. Validate email format                      │ │   │
│  │  │ 2. Extract user agent                         │ │   │
│  │  │ 3. Add domain to workspace                    │ │   │
│  │  │ 4. Get/create account                         │ │   │
│  │  │ 5. Create session                             │ │   │
│  │  │ 6. Build authentication response              │ │   │
│  │  │ 7. Set CORS headers                           │ │   │
│  │  └───────────────────────────────────────────────┘ │   │
│  │                                                     │   │
│  │  @POST /logout                                      │   │
│  │  ┌───────────────────────────────────────────────┐ │   │
│  │  │ 1. Extract session ID                         │ │   │
│  │  │ 2. Delete session                             │ │   │
│  │  │ 3. Return success response                    │ │   │
│  │  │ 4. Set CORS headers                           │ │   │
│  │  └───────────────────────────────────────────────┘ │   │
│  │                                                     │   │
│  │  @GET /type                                         │   │
│  │  ┌───────────────────────────────────────────────┐ │   │
│  │  │ 1. Return authentication type                 │ │   │
│  │  │ 2. Health check endpoint                      │ │   │
│  │  └───────────────────────────────────────────────┘ │   │
│  │                                                     │   │
│  │  @GET /authenticator.js                             │   │
│  │  ┌───────────────────────────────────────────────┐ │   │
│  │  │ 1. Serve JavaScript library                   │ │   │
│  │  │ 2. Static resource delivery                   │ │   │
│  │  └───────────────────────────────────────────────┘ │   │
│  │                                                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              CORS Handler                           │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  Access-Control-Allow-Origin: *                     │   │
│  │  Access-Control-Allow-Methods: POST, GET, OPTIONS   │   │
│  │  Access-Control-Allow-Headers: Content-Type         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Key Class**: `ExtensionResource.java` (282 lines)

**Endpoint Specifications**:

| Endpoint | Method | Input | Output | Response Time |
|----------|--------|-------|--------|---------------|
| `/login` | POST | `{email: string}` | AuthenticationResponse | 100-300ms |
| `/logout` | POST | `{clientSessionId: string}` | `{message: string}` | 50-100ms |
| `/type` | GET | None | `"Demo Authentication"` | < 10ms |
| `/authenticator.js` | GET | None | JavaScript file | < 10ms |

---

### Layer 3: Business Logic Layer

**Purpose**: Core authentication logic and business rules

**Components**:
- **Email Validator**: Email format validation
- **Account Manager**: Account creation and lookup
- **Role Manager**: Role assignment and creation
- **Domain Manager**: Domain extraction and registration

**Architecture**:
```
┌─────────────────────────────────────────────────────────────┐
│                 BUSINESS LOGIC LAYER                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         Email Validation Logic                      │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  Pattern: ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$        │   │
│  │  • Check @ symbol exists                            │   │
│  │  • Validate domain format                           │   │
│  │  • Ensure non-empty                                 │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         Account Management Logic                    │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  getAccount(name, email, userAgent)                 │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │ 1. Lookup account by email                    │  │   │
│  │  │ 2. If exists:                                 │  │   │
│  │  │    • Update last login timestamp              │  │   │
│  │  │    • Update user agent                        │  │   │
│  │  │    • Return existing account                  │  │   │
│  │  │ 3. If not exists:                             │  │   │
│  │  │    • Create default role if needed            │  │   │
│  │  │    • Create new account                       │  │   │
│  │  │    • Assign default role                      │  │   │
│  │  │    • Set account attributes                   │  │   │
│  │  │    • Return new account                       │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         Role Management Logic                       │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  createRoleIfNotPresent()                           │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │ 1. Get default role from config               │  │   │
│  │  │ 2. If empty: use "Krista Client User"         │  │   │
│  │  │ 3. Check if role exists                       │  │   │
│  │  │ 4. If not: create role with basic permissions │  │   │
│  │  │ 5. Return role name                           │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         Domain Management Logic                     │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  addDomainToWorkspaceIfNotPresent(email)            │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │ 1. Extract domain from email (after @)        │  │   │
│  │  │ 2. Check if domain exists in workspace        │  │   │
│  │  │ 3. If not: add domain to workspace            │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Account Attributes**:
```java
Map.of(
    "ORG", "KristaSoft",
    "KRISTA_SOURCE", "EXTENSION_DEMO_AUTHENTICATION",
    "KRISTA_LAST_LOGIN", getCurrentDateTime(),
    "KRISTA_USER_AGENT_INFO", userAgent
)
```

---

### Layer 4: SDK Integration Layer

**Purpose**: Integration with Krista platform services

**Components**:
- **SessionManager**: Session lifecycle management
- **AccountManager**: Account CRUD operations
- **RoleManager**: Role management
- **AuthorizationContext**: Authorization checks
- **RuntimeContext**: Platform context access

**Architecture**:
```
┌─────────────────────────────────────────────────────────────┐
│              KRISTA SDK INTEGRATION LAYER                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         SessionManager (KSDK)                       │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • createSession(accountId): String                 │   │
│  │  • deleteSession(sessionId): void                   │   │
│  │  • isValidSession(sessionId): boolean               │   │
│  │  • getAccountId(sessionId): String                  │   │
│  │  • Session timeout: 24 hours (platform-managed)     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         AccountManager (KSDK)                       │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • lookupAccount(email): ModifiableAccount          │   │
│  │  • createAccount(name, email, roles, attrs): Acc    │   │
│  │  • updateAccount(account): void                     │   │
│  │  • getAccount(accountId): Account                   │   │
│  │  • Account caching enabled                          │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         RoleManager (KSDK)                          │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • roleExists(roleName): boolean                    │   │
│  │  • createRole(name, attributes): ModifiableRole     │   │
│  │  • getRole(roleName): Role                          │   │
│  │  • updateRole(role): void                           │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         AuthorizationContext (KSDK)                 │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • isWorkspaceAdmin(): boolean                      │   │
│  │  • getAccountId(): String                           │   │
│  │  • hasPermission(permission): boolean               │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         RuntimeContext (KSDK)                       │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  • getWorkspaceId(): String                         │   │
│  │  • getConfiguration(key): String                    │   │
│  │  • getDomainManager(): DomainManager                │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Dependency Injection**:
```java
@Inject
public ExtensionResource(
    SessionManager sessionManager,
    AccountManager accountManager,
    RuntimeContext runtimeContext,
    RoleManager roleManager,
    AuthenticationSettings authenticationSettings,
    AuthorizationContext authorizationContext,
    @Named("self") Invoker invoker
) {
    // Dependencies injected by Krista platform
}
```

---

### Layer 5: Request Authentication Layer

**Purpose**: Authenticate incoming requests using session

**Components**:
- **ExtensionRequestAuthenticator**: Request authentication implementation
- **Session Validator**: Session validation logic
- **Account Resolver**: Account ID resolution

**Architecture**:
```
┌─────────────────────────────────────────────────────────────┐
│           REQUEST AUTHENTICATION LAYER                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │   ExtensionRequestAuthenticator.java                │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │                                                     │   │
│  │  authenticate(RoutingInfo routingInfo): String      │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │ 1. Extract X-Krista-Context header           │  │   │
│  │  │ 2. Parse JSON to get clientSessionId         │  │   │
│  │  │ 3. Validate session exists                   │  │   │
│  │  │ 4. Check session not expired                 │  │   │
│  │  │ 5. Get account ID from session               │  │   │
│  │  │ 6. Return account ID (authenticated)         │  │   │
│  │  │ 7. Return null (not authenticated)           │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  │                                                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         Session Extraction Logic                    │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │  extractSessionId(RoutingInfo): String              │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │ 1. Get headers from routing info             │  │   │
│  │  │ 2. Find X-Krista-Context header              │  │   │
│  │  │ 3. URL decode header value                   │  │   │
│  │  │ 4. Parse JSON                                │  │   │
│  │  │ 5. Extract clientSessionId field             │  │   │
│  │  │ 6. Return session ID or null                 │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Request Flow**:
```
Client Request
     │
     ↓
[X-Krista-Context: {"clientSessionId": "abc123"}]
     │
     ↓
ExtensionRequestAuthenticator.authenticate()
     │
     ├─→ Extract session ID
     ├─→ Validate session
     ├─→ Get account ID
     │
     ↓
Return account ID (authenticated) or null (not authenticated)
```

---

## Component Architecture

### Core Components

```
┌─────────────────────────────────────────────────────────────────────┐
│                    COMPONENT ARCHITECTURE                           │
└─────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│  DemoAuthenticationExtension                                     │
│  (Main Extension Class)                                          │
├──────────────────────────────────────────────────────────────────┤
│  Responsibilities:                                               │
│  • Extension lifecycle management                                │
│  • Configuration management (default role)                       │
│  • Provide RequestAuthenticator                                  │
│  • Provide custom tabs (Documentation)                           │
│                                                                  │
│  Dependencies:                                                   │
│  • AuthorizationContext                                          │
│  • SessionManager                                                │
│                                                                  │
│  Annotations:                                                    │
│  • @Extension(version="3.5.8", requireWorkspaceAdminRights=true) │
│  • @Field(name="default_role", type="Text", required=false)      │
│  • @Java(version=JAVA_21)                                        │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ provides
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│  ExtensionRequestAuthenticator                                   │
│  (Request Authentication)                                        │
├──────────────────────────────────────────────────────────────────┤
│  Responsibilities:                                               │
│  • Authenticate incoming requests                                │
│  • Extract session from X-Krista-Context header                  │
│  • Validate session                                              │
│  • Return authenticated account ID                               │
│                                                                  │
│  Dependencies:                                                   │
│  • AuthorizationContext                                          │
│  • SessionManager                                                │
│                                                                  │
│  Interface:                                                      │
│  • RequestAuthenticator (Krista SDK)                             │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│  ExtensionResource                                               │
│  (REST API Endpoints)                                            │
├──────────────────────────────────────────────────────────────────┤
│  Responsibilities:                                               │
│  • Expose REST API endpoints                                     │
│  • Handle login/logout requests                                  │
│  • Manage account creation                                       │
│  • Manage session lifecycle                                      │
│  • Set CORS headers                                              │
│  • Serve static resources (authenticator.js)                     │
│                                                                  │
│  Dependencies:                                                   │
│  • SessionManager                                                │
│  • AccountManager                                                │
│  • RoleManager                                                   │
│  • RuntimeContext                                                │
│  • AuthenticationSettings                                        │
│  • AuthorizationContext                                          │
│  • Invoker                                                       │
│                                                                  │
│  Endpoints:                                                      │
│  • POST /login                                                   │
│  • POST /logout                                                  │
│  • GET /type                                                     │
│  • GET /authenticator.js                                         │
│  • OPTIONS /login (CORS preflight)                               │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│  IntegrationArea                                                 │
│  (Catalog Integration)                                           │
├──────────────────────────────────────────────────────────────────┤
│  Responsibilities:                                               │
│  • Provide catalog requests                                      │
│  • Serve authenticator.js via catalog                            │
│  • Enable UI integration                                         │
│                                                                  │
│  Catalog Requests:                                               │
│  • Get Script Element                                            │
│                                                                  │
│  Annotations:                                                    │
│  • @CatalogArea(name="Integration")                              │
│  • @CatalogRequest(name="Get Script Element")                    │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│  KsdkApplication                                                 │
│  (JAX-RS Application)                                            │
├──────────────────────────────────────────────────────────────────┤
│  Responsibilities:                                               │
│  • Configure JAX-RS application                                  │
│  • Register resources                                            │
│  • Register exception mappers                                    │
│                                                                  │
│  Registered Components:                                          │
│  • ExtensionResource                                             │
│  • KristaExceptionMapper                                         │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│  KristaExceptionMapper                                           │
│  (Exception Handling)                                            │
├──────────────────────────────────────────────────────────────────┤
│  Responsibilities:                                               │
│  • Map exceptions to HTTP responses                              │
│  • Format error messages                                         │
│  • Set appropriate HTTP status codes                             │
│                                                                  │
│  Handles:                                                        │
│  • IllegalArgumentException → 500                                │
│  • AuthorizationException → 500                                  │
│  • Generic exceptions → 500                                      │
└──────────────────────────────────────────────────────────────────┘
```

### Component Interaction Diagram

```
┌──────────────┐
│   Client     │
└──────┬───────┘
       │ HTTP POST /login
       ↓
┌──────────────────────────────────────────────────────────────┐
│  ExtensionResource                                           │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ login(headers, loginInput)                             │ │
│  │   │                                                    │ │
│  │   ├─→ Validate email                                  │ │
│  │   │                                                    │ │
│  │   ├─→ addDomainToWorkspaceIfNotPresent(email)         │ │
│  │   │      │                                             │ │
│  │   │      └─→ RuntimeContext.getDomainManager()        │ │
│  │   │                                                    │ │
│  │   ├─→ getAccount(name, email, userAgent)              │ │
│  │   │      │                                             │ │
│  │   │      ├─→ AccountManager.lookupAccount(email)      │ │
│  │   │      │                                             │ │
│  │   │      ├─→ createRoleIfNotPresent()                 │ │
│  │   │      │      │                                      │ │
│  │   │      │      └─→ RoleManager.roleExists()          │ │
│  │   │      │      └─→ RoleManager.createRole()          │ │
│  │   │      │                                             │ │
│  │   │      └─→ AccountManager.createAccount()           │ │
│  │   │                                                    │ │
│  │   ├─→ SessionManager.createSession(accountId)         │ │
│  │   │                                                    │ │
│  │   └─→ Build AuthenticationResponse                    │ │
│  │                                                        │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
       │
       ↓
┌──────────────┐
│   Response   │
│  (Session ID)│
└──────────────┘
```

---

## Data Flow Architecture

### Login Flow (Detailed)

```
┌─────────────────────────────────────────────────────────────────────┐
│                         LOGIN DATA FLOW                             │
└─────────────────────────────────────────────────────────────────────┘

Step 1: Client Initiates Login
┌──────────┐
│  Client  │
└────┬─────┘
     │
     │ POST /authn/login
     │ Content-Type: application/json
     │ Body: {"email": "user@company.com"}
     │
     ↓
┌─────────────────────────────────────────────────────────────────┐
│  ExtensionResource.login()                                      │
└─────────────────────────────────────────────────────────────────┘

Step 2: Authorization Check
     │
     ├─→ AuthorizationContext.isWorkspaceAdmin()
     │   • Check deploying user has admin rights
     │   • If not: throw IllegalArgumentException
     │
     ↓

Step 3: Email Validation
     │
     ├─→ isEmailAddressValid(email)
     │   • Pattern: ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$
     │   • Check @ symbol exists
     │   • Validate domain format
     │   • If invalid: return error response
     │
     ↓

Step 4: Domain Management
     │
     ├─→ addDomainToWorkspaceIfNotPresent(email)
     │   │
     │   ├─→ Extract domain: email.substring(email.indexOf("@") + 1)
     │   │   Example: "user@company.com" → "company.com"
     │   │
     │   ├─→ RuntimeContext.getDomainManager()
     │   │
     │   └─→ DomainManager.addDomain(domain)
     │       • Add domain if not exists
     │
     ↓

Step 5: Account Lookup/Creation
     │
     ├─→ getAccount(name, email, userAgent)
     │   │
     │   ├─→ AccountManager.lookupAccount(email)
     │   │   • Query workspace for account by email
     │   │   • Uses account cache if available
     │   │
     │   ├─→ If account exists:
     │   │   │
     │   │   ├─→ Update KRISTA_LAST_LOGIN attribute
     │   │   ├─→ Update KRISTA_USER_AGENT_INFO attribute
     │   │   └─→ AccountManager.updateAccount(account)
     │   │
     │   └─→ If account does NOT exist:
     │       │
     │       ├─→ createRoleIfNotPresent()
     │       │   │
     │       │   ├─→ Get default role from configuration
     │       │   │   • RuntimeContext.getConfiguration("default_role")
     │       │   │   • If empty: use "Krista Client User"
     │       │   │
     │       │   ├─→ RoleManager.roleExists(roleName)
     │       │   │
     │       │   └─→ If not exists:
     │       │       └─→ RoleManager.createRole(roleName, attributes)
     │       │
     │       └─→ AccountManager.createAccount(
     │               name,    // Email prefix (before @)
     │               email,   // Full email address
     │               roles,   // Set containing default role
     │               attributes: {
     │                   "ORG": "KristaSoft",
     │                   "KRISTA_SOURCE": "EXTENSION_DEMO_AUTHENTICATION",
     │                   "KRISTA_LAST_LOGIN": getCurrentDateTime(),
     │                   "KRISTA_USER_AGENT_INFO": userAgent
     │               }
     │           )
     │
     ↓

Step 6: Session Creation
     │
     ├─→ SessionManager.createSession(account.getAccountId())
     │   • Generate unique session ID (UUID)
     │   • Store session in platform session store
     │   • Set expiration (24 hours, platform-managed)
     │   • Return session ID
     │
     ↓

Step 7: Build Response
     │
     ├─→ AuthenticationResponse.builder()
     │   .clientSessionId(sessionId)
     │   .name(account.getName())
     │   .avatarUrl(account.getAvatarUrl())
     │   .accountId(account.getAccountId())
     │   .kristaAccountId(account.getKristaAccountId())
     │   .personId(account.getPersonId())
     │   .roles(account.getRoles())
     │   .inboxId(account.getInboxId())
     │   .isWorkspaceAdmin(account.isWorkspaceAdmin())
     │   .isApplianceManager(account.isApplianceManager())
     │   .identificationToken(Map.of("email", email))
     │   .extras(Map.of("creationTime", getCurrentDateTime()))
     │   .build()
     │
     ↓

Step 8: Set CORS Headers & Return
     │
     ├─→ getResponseBuilderWithCORSHeaders()
     │   • Access-Control-Allow-Origin: *
     │   • Access-Control-Allow-Methods: POST, GET, OPTIONS
     │   • Access-Control-Allow-Headers: Content-Type
     │
     └─→ Response.ok(authenticationResponse).build()
         │
         ↓
    ┌──────────┐
    │  Client  │
    │ (Session │
    │  Stored) │
    └──────────┘
```

### Logout Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                        LOGOUT DATA FLOW                             │
└─────────────────────────────────────────────────────────────────────┘

Step 1: Client Initiates Logout
┌──────────┐
│  Client  │
└────┬─────┘
     │
     │ POST /authn/logout
     │ Content-Type: application/json
     │ Body: {"clientSessionId": "abc123..."}
     │
     ↓
┌─────────────────────────────────────────────────────────────────┐
│  ExtensionResource.logout()                                     │
└─────────────────────────────────────────────────────────────────┘

Step 2: Extract Session ID
     │
     ├─→ logoutInput.get("clientSessionId")
     │   • Get session ID from request body
     │   • If null: return error response
     │
     ↓

Step 3: Delete Session
     │
     ├─→ SessionManager.deleteSession(sessionId)
     │   • Remove session from platform session store
     │   • Invalidate session
     │   • Free resources
     │
     ↓

Step 4: Return Success
     │
     └─→ Response.ok(Map.of("message", "Logout successful")).build()
         │
         ↓
    ┌──────────┐
    │  Client  │
    │ (Session │
    │ Cleared) │
    └──────────┘
```

### Authenticated Request Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                  AUTHENTICATED REQUEST FLOW                         │
└─────────────────────────────────────────────────────────────────────┘

Step 1: Client Makes Authenticated Request
┌──────────┐
│  Client  │
└────┬─────┘
     │
     │ GET /api/some-endpoint
     │ X-Krista-Context: {"clientSessionId": "abc123..."}
     │
     ↓
┌─────────────────────────────────────────────────────────────────┐
│  Krista Platform Request Interceptor                           │
└─────────────────────────────────────────────────────────────────┘

Step 2: Platform Calls Authenticator
     │
     ├─→ ExtensionRequestAuthenticator.authenticate(routingInfo)
     │
     ↓

Step 3: Extract Session from Header
     │
     ├─→ extractSessionId(routingInfo)
     │   │
     │   ├─→ Get X-Krista-Context header
     │   ├─→ URL decode header value
     │   ├─→ Parse JSON
     │   └─→ Extract clientSessionId field
     │
     ↓

Step 4: Validate Session
     │
     ├─→ SessionManager.isValidSession(sessionId)
     │   • Check session exists
     │   • Check session not expired
     │   • Return true/false
     │
     ↓

Step 5: Get Account ID
     │
     ├─→ If session valid:
     │   └─→ SessionManager.getAccountId(sessionId)
     │       • Return associated account ID
     │
     └─→ If session invalid:
         └─→ Return null (not authenticated)
         │
         ↓
┌─────────────────────────────────────────────────────────────────┐
│  Krista Platform                                                │
│  • If account ID returned: Request authenticated                │
│  • If null returned: Request rejected (401 Unauthorized)        │
└─────────────────────────────────────────────────────────────────┘
```

---

## Deployment Architecture

### Extension Deployment Model

```
┌─────────────────────────────────────────────────────────────────────┐
│                    DEPLOYMENT ARCHITECTURE                          │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Krista Workspace                                               │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  Extension Container                                      │ │
│  │  ┌─────────────────────────────────────────────────────┐ │ │
│  │  │  Demo Authentication Extension                      │ │ │
│  │  │  ┌───────────────────────────────────────────────┐  │ │ │
│  │  │  │  JAX-RS Application (Jersey)                 │  │ │ │
│  │  │  │  • Base Path: /authn                         │  │ │ │
│  │  │  │  • Port: Platform-managed                    │  │ │ │
│  │  │  └───────────────────────────────────────────────┘  │ │ │
│  │  │  ┌───────────────────────────────────────────────┐  │ │ │
│  │  │  │  Extension Resources                         │  │ │ │
│  │  │  │  • ExtensionResource (REST endpoints)        │  │ │ │
│  │  │  │  • IntegrationArea (Catalog requests)        │  │ │ │
│  │  │  │  • Static resources (docs, JS)               │  │ │ │
│  │  │  └───────────────────────────────────────────────┘  │ │ │
│  │  │  ┌───────────────────────────────────────────────┐  │ │ │
│  │  │  │  Configuration                               │  │ │ │
│  │  │  │  • default_role: "Demo User" (optional)      │  │ │ │
│  │  │  └───────────────────────────────────────────────┘  │ │ │
│  │  └─────────────────────────────────────────────────────┘ │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  Krista Platform Services                                 │ │
│  │  • SessionManager                                         │ │
│  │  • AccountManager                                         │ │
│  │  • RoleManager                                            │ │
│  │  • RuntimeContext                                         │ │
│  │  • AuthorizationContext                                   │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  Data Storage                                             │ │
│  │  • Workspace Database (accounts, roles, domains)          │ │
│  │  • Session Store (in-memory or distributed)               │ │
│  │  • Configuration Store                                    │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Deployment Requirements

| Requirement | Specification |
|-------------|---------------|
| **Java Version** | Java 21+ |
| **Platform Version** | Krista 3.4.0+ |
| **Admin Rights** | Workspace Admin required for deployment |
| **Memory** | ~100 MB baseline + 10 MB per 1000 users |
| **CPU** | 1-2 cores recommended |
| **Network** | HTTPS recommended, CORS enabled |
| **Dependencies** | None (standalone extension) |

### Deployment Process

```
┌─────────────────────────────────────────────────────────────────┐
│  Deployment Steps                                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Admin Access                                                │
│     • Log in as workspace admin                                 │
│     • Navigate to Setup → Extensions                            │
│                                                                 │
│  2. Extension Selection                                         │
│     • Find "Demo Authentication" in catalog                     │
│     • Review extension details                                  │
│                                                                 │
│  3. Deployment                                                  │
│     • Click "Deploy" button                                     │
│     • Platform validates admin rights                           │
│     • Platform loads extension JAR                              │
│     • Platform initializes extension                            │
│     • Platform registers REST endpoints                         │
│     • Platform registers authenticator                          │
│                                                                 │
│  4. Configuration (Optional)                                    │
│     • Set default role (or leave empty)                         │
│     • Save configuration                                        │
│                                                                 │
│  5. Verification                                                │
│     • Extension status: "Deployed"                              │
│     • Test health check: GET /authn/type                        │
│     • Test login with sample email                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Performance Architecture

### Performance Characteristics

```
┌─────────────────────────────────────────────────────────────────────┐
│                   PERFORMANCE ARCHITECTURE                          │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Response Time Targets                                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Operation              │ Target  │ P95    │ P99    │ Max      │
│  ──────────────────────────────────────────────────────────────│
│  Health Check           │ < 10ms  │ 10ms   │ 15ms   │ 20ms     │
│  Email Validation       │ < 5ms   │ 5ms    │ 8ms    │ 10ms     │
│  Existing User Login    │ 120ms   │ 180ms  │ 250ms  │ 300ms    │
│  New User Login         │ 250ms   │ 350ms  │ 500ms  │ 600ms    │
│  Logout                 │ 75ms    │ 120ms  │ 180ms  │ 200ms    │
│  Session Validation     │ 10ms    │ 20ms   │ 30ms   │ 50ms     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Throughput Capacity                                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Metric                      │ Capacity    │ Notes             │
│  ──────────────────────────────────────────────────────────────│
│  Concurrent Sessions         │ 10,000+     │ Platform-managed  │
│  Login Requests/sec          │ 100+        │ DB-dependent      │
│  Session Validations/sec     │ 1,000+      │ In-memory cache   │
│  Account Lookups/sec         │ 500+        │ With caching      │
│  Concurrent Login Requests   │ 50-100      │ Recommended max   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Resource Utilization                                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Memory Usage:                                                  │
│  • Baseline: 100 MB                                             │
│  • Per 1000 sessions: +10 MB                                    │
│  • Per 1000 accounts (cached): +5 MB                            │
│  • Total (10K users): ~200 MB                                   │
│                                                                 │
│  CPU Usage:                                                     │
│  • Idle: < 1%                                                   │
│  • Login (new user): 5-10% (200-300ms burst)                    │
│  • Login (existing): 2-5% (100-150ms burst)                     │
│  • Session validation: < 1% (10-20ms)                           │
│                                                                 │
│  Network:                                                       │
│  • Login request: ~500 bytes                                    │
│  • Login response: ~1-2 KB                                      │
│  • Logout request: ~200 bytes                                   │
│  • Logout response: ~100 bytes                                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Performance Bottlenecks

```
┌─────────────────────────────────────────────────────────────────┐
│  Potential Bottlenecks                                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Database Operations                                         │
│     ┌─────────────────────────────────────────────────────┐    │
│     │ • Account lookup: 50-100ms                          │    │
│     │ • Account creation: 100-150ms                       │    │
│     │ • Role creation: 50-100ms                           │    │
│     │ • Domain addition: 20-50ms                          │    │
│     │                                                     │    │
│     │ Mitigation:                                         │    │
│     │ • Account caching (workspace-level)                 │    │
│     │ • Connection pooling                                │    │
│     │ • Batch operations where possible                   │    │
│     └─────────────────────────────────────────────────────┘    │
│                                                                 │
│  2. Session Storage                                             │
│     ┌─────────────────────────────────────────────────────┐    │
│     │ • Session creation: 20-30ms                         │    │
│     │ • Session lookup: 5-10ms                            │    │
│     │ • Session deletion: 10-20ms                         │    │
│     │                                                     │    │
│     │ Mitigation:                                         │    │
│     │ • In-memory session store                           │    │
│     │ • Platform-managed optimization                     │    │
│     │ • Automatic cleanup of expired sessions             │    │
│     └─────────────────────────────────────────────────────┘    │
│                                                                 │
│  3. Concurrent Account Creation                                 │
│     ┌─────────────────────────────────────────────────────┐    │
│     │ • Multiple users creating accounts simultaneously   │    │
│     │ • Database lock contention                          │    │
│     │ • Role creation race conditions                     │    │
│     │                                                     │    │
│     │ Mitigation:                                         │    │
│     │ • Database transaction isolation                    │    │
│     │ • Role existence check before creation              │    │
│     │ • Retry logic for transient failures                │    │
│     └─────────────────────────────────────────────────────┘    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Performance Optimization Strategies

```
┌─────────────────────────────────────────────────────────────────┐
│  Optimization Strategies                                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Account Caching                                             │
│     • Workspace-level account cache                             │
│     • Reduces database queries                                  │
│     • Faster account lookups                                    │
│     • Cache invalidation on updates                             │
│                                                                 │
│  2. Session Management                                          │
│     • Platform-managed session store                            │
│     • In-memory storage for fast access                         │
│     • Automatic expiration (24 hours)                           │
│     • Periodic cleanup of expired sessions                      │
│                                                                 │
│  3. Connection Pooling                                          │
│     • Database connection pool                                  │
│     • Reuse connections                                         │
│     • Configurable pool size                                    │
│     • Connection timeout management                             │
│                                                                 │
│  4. Lazy Loading                                                │
│     • Load account details only when needed                     │
│     • Defer role loading until required                         │
│     • Minimize data transfer                                    │
│                                                                 │
│  5. Asynchronous Operations                                     │
│     • Non-blocking I/O where possible                           │
│     • Async database queries (future enhancement)               │
│     • Background cleanup tasks                                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Error Handling Architecture

### Error Handling Strategy

```
┌─────────────────────────────────────────────────────────────────────┐
│                   ERROR HANDLING ARCHITECTURE                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Error Categories                                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Validation Errors                                           │
│     • Invalid email format                                      │
│     • Missing required fields                                   │
│     • Malformed request body                                    │
│                                                                 │
│  2. Authorization Errors                                        │
│     • Missing workspace admin rights                            │
│     • Insufficient permissions                                  │
│     • Invalid session                                           │
│                                                                 │
│  3. Business Logic Errors                                       │
│     • Account creation failure                                  │
│     • Role creation failure                                     │
│     • Session creation failure                                  │
│                                                                 │
│  4. System Errors                                               │
│     • Database connection failure                               │
│     • Platform service unavailable                              │
│     • Unexpected exceptions                                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Error Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│  Error Handling Flow                                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Request                                                        │
│     │                                                           │
│     ↓                                                           │
│  ┌──────────────────────────────────────────────────────┐      │
│  │  Input Validation                                    │      │
│  └──────────────────────────────────────────────────────┘      │
│     │                                                           │
│     ├─→ Valid ──→ Continue processing                          │
│     │                                                           │
│     └─→ Invalid                                                 │
│         │                                                       │
│         ↓                                                       │
│  ┌──────────────────────────────────────────────────────┐      │
│  │  Build Error Response                                │      │
│  │  {                                                   │      │
│  │    "message": "Invalid email input.",               │      │
│  │    "code": "Demo Auth - 500"                        │      │
│  │  }                                                   │      │
│  └──────────────────────────────────────────────────────┘      │
│         │                                                       │
│         ↓                                                       │
│  ┌──────────────────────────────────────────────────────┐      │
│  │  Set HTTP Status: 500                                │      │
│  └──────────────────────────────────────────────────────┘      │
│         │                                                       │
│         ↓                                                       │
│  ┌──────────────────────────────────────────────────────┐      │
│  │  Set CORS Headers                                    │      │
│  └──────────────────────────────────────────────────────┘      │
│         │                                                       │
│         ↓                                                       │
│  ┌──────────────────────────────────────────────────────┐      │
│  │  Return Response to Client                           │      │
│  └──────────────────────────────────────────────────────┘      │
│         │                                                       │
│         ↓                                                       │
│  Client receives error                                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Error Scenarios & Handling

```
┌─────────────────────────────────────────────────────────────────┐
│  Error Scenario Matrix                                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Scenario 1: Invalid Email Format                               │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Trigger: Email missing @ or domain                        │ │
│  │ Detection: isEmailAddressValid() returns false            │ │
│  │ Response: HTTP 500                                        │ │
│  │ Message: "Invalid email input."                           │ │
│  │ Code: "Demo Auth - 500"                                   │ │
│  │ Recovery: Client re-submits with valid email             │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario 2: Missing Workspace Admin Rights                     │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Trigger: Extension deployed without admin rights          │ │
│  │ Detection: authorizationContext.isWorkspaceAdmin() false  │ │
│  │ Response: HTTP 500                                        │ │
│  │ Message: "Please provide workspace admin access"          │ │
│  │ Code: "Demo Auth - 500"                                   │ │
│  │ Recovery: Re-deploy with admin rights                     │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario 3: Missing Session ID on Logout                       │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Trigger: Logout request without clientSessionId          │ │
│  │ Detection: logoutInput.get("clientSessionId") == null    │ │
│  │ Response: HTTP 500                                        │ │
│  │ Message: "Missing client session id."                     │ │
│  │ Code: "Demo Auth - 500"                                   │ │
│  │ Recovery: Client includes session ID in request           │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario 4: Account Creation Failure                           │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Trigger: Database error during account creation          │ │
│  │ Detection: AccountManager.createAccount() throws         │ │
│  │ Response: HTTP 500                                        │ │
│  │ Message: Exception message                               │ │
│  │ Code: "Demo Auth - 500"                                   │ │
│  │ Recovery: Retry, check database connectivity             │ │
│  │ Logging: Full stack trace logged                         │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario 5: Session Creation Failure                           │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Trigger: Session store unavailable                       │ │
│  │ Detection: SessionManager.createSession() throws         │ │
│  │ Response: HTTP 500                                        │ │
│  │ Message: Exception message                               │ │
│  │ Code: "Demo Auth - 500"                                   │ │
│  │ Recovery: Retry, check platform services                 │ │
│  │ Logging: Full stack trace logged                         │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario 6: Invalid Session on Authentication                  │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Trigger: Expired or invalid session ID                   │ │
│  │ Detection: SessionManager.isValidSession() returns false │ │
│  │ Response: Return null (not authenticated)                │ │
│  │ Platform Action: HTTP 401 Unauthorized                   │ │
│  │ Recovery: Client redirects to login                      │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Exception Mapping

```
┌─────────────────────────────────────────────────────────────────┐
│  KristaExceptionMapper                                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Exception Type              │ HTTP Status │ Response Format    │
│  ──────────────────────────────────────────────────────────────│
│  IllegalArgumentException    │ 500         │ {message, code}    │
│  AuthorizationException      │ 500         │ {message, code}    │
│  RuntimeException            │ 500         │ {message, code}    │
│  Generic Exception           │ 500         │ {message, code}    │
│                                                                 │
│  All responses include:                                         │
│  • "message": Error description                                 │
│  • "code": "Demo Auth - 500"                                    │
│  • CORS headers                                                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Security Architecture

### Security Model

```
┌─────────────────────────────────────────────────────────────────────┐
│                      SECURITY ARCHITECTURE                          │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Security Layers                                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Layer 1: Transport Security                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ • HTTPS recommended for production                        │ │
│  │ • TLS 1.2+ encryption                                     │ │
│  │ • Certificate validation                                  │ │
│  │ • Secure headers (CORS, Content-Type)                     │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Layer 2: Authentication                                        │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ • Email-based identification                              │ │
│  │ • Session-based authentication                            │ │
│  │ • Session ID in X-Krista-Context header                   │ │
│  │ • Session expiration (24 hours)                           │ │
│  │ • No password required (demo purposes)                    │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Layer 3: Authorization                                         │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ • Workspace admin required for deployment                 │ │
│  │ • Role-based access control (RBAC)                        │ │
│  │ • Account-level permissions                               │ │
│  │ • Platform-managed authorization                          │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Layer 4: Input Validation                                      │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ • Email format validation                                 │ │
│  │ • Domain validation                                       │ │
│  │ • JSON parsing with error handling                        │ │
│  │ • Header validation                                       │ │
│  │ • URL decoding with error handling                        │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Layer 5: Session Management                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ • Unique session IDs (UUID)                               │ │
│  │ • Session validation on every request                     │ │
│  │ • Automatic session expiration                            │ │
│  │ • Secure session storage                                  │ │
│  │ • Session invalidation on logout                          │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Security Considerations

```
┌─────────────────────────────────────────────────────────────────┐
│  Security Strengths                                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ✓ Session-based authentication                                 │
│  ✓ Automatic session expiration                                 │
│  ✓ Email validation                                             │
│  ✓ CORS headers configured                                      │
│  ✓ Workspace admin requirement for deployment                   │
│  ✓ Platform-managed session storage                             │
│  ✓ Role-based access control                                    │
│  ✓ Input validation on all endpoints                            │
│  ✓ Error handling without information leakage                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Security Limitations (Demo Extension)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ⚠ No password authentication                                   │
│    • Anyone with valid email can log in                         │
│    • Suitable for demos, not production                         │
│                                                                 │
│  ⚠ No multi-factor authentication (MFA)                         │
│    • Single-factor authentication only                          │
│                                                                 │
│  ⚠ No rate limiting                                             │
│    • Potential for brute force attacks                          │
│    • Recommend adding rate limiting for production              │
│                                                                 │
│  ⚠ No email verification                                        │
│    • Email ownership not verified                               │
│    • Users can use any email address                            │
│                                                                 │
│  ⚠ CORS allows all origins (*)                                  │
│    • Permissive CORS policy                                     │
│    • Recommend restricting origins in production                │
│                                                                 │
│  ⚠ No account lockout mechanism                                 │
│    • No protection against repeated login attempts              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Security Recommendations for Production                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Add Password Authentication                                 │
│     • Implement password hashing (bcrypt, Argon2)               │
│     • Password complexity requirements                          │
│     • Password reset functionality                              │
│                                                                 │
│  2. Implement Email Verification                                │
│     • Send verification email on registration                   │
│     • Verify email ownership before account activation          │
│     • Email change verification                                 │
│                                                                 │
│  3. Add Multi-Factor Authentication (MFA)                       │
│     • TOTP (Time-based One-Time Password)                       │
│     • SMS verification                                          │
│     • Backup codes                                              │
│                                                                 │
│  4. Implement Rate Limiting                                     │
│     • Limit login attempts per IP                               │
│     • Limit login attempts per email                            │
│     • Exponential backoff on failures                           │
│                                                                 │
│  5. Restrict CORS Origins                                       │
│     • Whitelist specific domains                                │
│     • Remove wildcard (*) origin                                │
│     • Validate Origin header                                    │
│                                                                 │
│  6. Add Account Lockout                                         │
│     • Lock account after N failed attempts                      │
│     • Temporary lockout (e.g., 15 minutes)                      │
│     • Admin unlock capability                                   │
│                                                                 │
│  7. Implement Audit Logging                                     │
│     • Log all authentication events                             │
│     • Log failed login attempts                                 │
│     • Log account changes                                       │
│     • Retain logs for compliance                                │
│                                                                 │
│  8. Add Session Security                                        │
│     • Shorter session expiration (e.g., 1 hour)                 │
│     • Session refresh tokens                                    │
│     • Detect concurrent sessions                                │
│     • IP address validation                                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Threat Model

```
┌─────────────────────────────────────────────────────────────────┐
│  Threat Analysis                                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Threat 1: Unauthorized Access                                  │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Description: Attacker logs in with any email              │ │
│  │ Likelihood: HIGH (no password required)                   │ │
│  │ Impact: HIGH (full account access)                        │ │
│  │ Mitigation: Add password authentication                   │ │
│  │ Status: ACCEPTED (demo extension)                         │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Threat 2: Session Hijacking                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Description: Attacker steals session ID                   │ │
│  │ Likelihood: MEDIUM (requires network access)              │ │
│  │ Impact: HIGH (impersonate user)                           │ │
│  │ Mitigation: Use HTTPS, secure session storage             │ │
│  │ Status: PARTIALLY MITIGATED                               │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Threat 3: Brute Force Attack                                   │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Description: Attacker tries many emails                   │ │
│  │ Likelihood: MEDIUM (no rate limiting)                     │ │
│  │ Impact: LOW (can create many accounts)                    │ │
│  │ Mitigation: Add rate limiting, account lockout            │ │
│  │ Status: NOT MITIGATED                                     │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Threat 4: Email Spoofing                                       │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Description: Attacker uses someone else's email           │ │
│  │ Likelihood: HIGH (no email verification)                  │ │
│  │ Impact: MEDIUM (impersonate email owner)                  │ │
│  │ Mitigation: Add email verification                        │ │
│  │ Status: ACCEPTED (demo extension)                         │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Threat 5: Cross-Site Request Forgery (CSRF)                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Description: Attacker tricks user into making request     │ │
│  │ Likelihood: LOW (CORS configured)                         │ │
│  │ Impact: MEDIUM (unauthorized actions)                     │ │
│  │ Mitigation: CORS headers, CSRF tokens                     │ │
│  │ Status: PARTIALLY MITIGATED                               │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Testing Architecture

### Test Strategy

```
┌─────────────────────────────────────────────────────────────────────┐
│                      TESTING ARCHITECTURE                           │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Test Pyramid                                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                    ┌─────────────────┐                          │
│                    │   E2E Tests     │                          │
│                    │   (Manual)      │                          │
│                    └─────────────────┘                          │
│                  ┌─────────────────────┐                        │
│                  │  Integration Tests  │                        │
│                  │  (Platform APIs)    │                        │
│                  └─────────────────────┘                        │
│              ┌───────────────────────────────┐                  │
│              │      Unit Tests               │                  │
│              │  (Business Logic, Validation) │                  │
│              └───────────────────────────────┘                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Test Coverage

```
┌─────────────────────────────────────────────────────────────────┐
│  Unit Tests                                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Component: Email Validation                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Test Cases:                                               │ │
│  │ • Valid email formats                                     │ │
│  │ • Invalid email formats (missing @, domain)               │ │
│  │ • Edge cases (special characters, long emails)            │ │
│  │ • Null/empty email                                        │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Component: Session Extraction                                  │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Test Cases:                                               │ │
│  │ • Valid X-Krista-Context header                           │ │
│  │ • Missing header                                          │ │
│  │ • Malformed JSON                                          │ │
│  │ • Missing clientSessionId field                           │ │
│  │ • URL-encoded header                                      │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Component: Error Response Building                             │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Test Cases:                                               │ │
│  │ • Error message formatting                                │ │
│  │ • Error code inclusion                                    │ │
│  │ • CORS headers present                                    │ │
│  │ • HTTP status code                                        │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Integration Tests                                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Scenario: New User Login                                       │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Steps:                                                    │ │
│  │ 1. POST /authn/login with new email                       │ │
│  │ 2. Verify account created                                 │ │
│  │ 3. Verify role assigned                                   │ │
│  │ 4. Verify session created                                 │ │
│  │ 5. Verify response contains session ID                    │ │
│  │ 6. Verify domain added to workspace                       │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario: Existing User Login                                  │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Steps:                                                    │ │
│  │ 1. Create account manually                                │ │
│  │ 2. POST /authn/login with existing email                  │ │
│  │ 3. Verify account not duplicated                          │ │
│  │ 4. Verify session created                                 │ │
│  │ 5. Verify last login updated                              │ │
│  │ 6. Verify user agent updated                              │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario: Logout                                               │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Steps:                                                    │ │
│  │ 1. Login to get session ID                                │ │
│  │ 2. POST /authn/logout with session ID                     │ │
│  │ 3. Verify session deleted                                 │ │
│  │ 4. Verify subsequent requests fail authentication         │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Scenario: Authenticated Request                                │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Steps:                                                    │ │
│  │ 1. Login to get session ID                                │ │
│  │ 2. Make request with X-Krista-Context header              │ │
│  │ 3. Verify request authenticated                           │ │
│  │ 4. Verify correct account ID returned                     │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  End-to-End Tests (Manual)                                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Test 1: Complete Login Flow                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ 1. Deploy extension to workspace                          │ │
│  │ 2. Open login page                                        │ │
│  │ 3. Enter email address                                    │ │
│  │ 4. Click login button                                     │ │
│  │ 5. Verify redirect to application                         │ │
│  │ 6. Verify user authenticated                              │ │
│  │ 7. Verify user can access protected resources             │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Test 2: Session Persistence                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ 1. Login successfully                                     │ │
│  │ 2. Close browser                                          │ │
│  │ 3. Reopen browser                                         │ │
│  │ 4. Navigate to application                                │ │
│  │ 5. Verify still authenticated (within 24 hours)           │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Test 3: Logout Flow                                            │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ 1. Login successfully                                     │ │
│  │ 2. Click logout button                                    │ │
│  │ 3. Verify redirect to login page                          │ │
│  │ 4. Verify cannot access protected resources               │ │
│  │ 5. Verify session invalidated                             │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Integration Architecture

### Platform Integration Points

```
┌─────────────────────────────────────────────────────────────────────┐
│                   INTEGRATION ARCHITECTURE                          │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Krista Platform Integration                                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Integration Point 1: Request Authenticator                     │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Interface: RequestAuthenticator                           │ │
│  │ Method: authenticate(RoutingInfo routingInfo)             │ │
│  │ Purpose: Authenticate incoming HTTP requests              │ │
│  │ Called By: Krista platform request interceptor            │ │
│  │ Returns: Account ID (authenticated) or null (not auth)    │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Integration Point 2: Session Manager                           │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Service: SessionManager                                   │ │
│  │ Methods:                                                  │ │
│  │ • createSession(accountId): Create new session            │ │
│  │ • isValidSession(sessionId): Validate session             │ │
│  │ • getAccountId(sessionId): Get account from session       │ │
│  │ • deleteSession(sessionId): Invalidate session            │ │
│  │ Purpose: Manage user sessions                             │ │
│  │ Storage: Platform-managed session store                   │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Integration Point 3: Account Manager                           │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Service: AccountManager                                   │ │
│  │ Methods:                                                  │ │
│  │ • lookupAccount(email): Find account by email             │ │
│  │ • createAccount(...): Create new account                  │ │
│  │ • updateAccount(account): Update account attributes       │ │
│  │ Purpose: Manage user accounts                             │ │
│  │ Storage: Workspace database                               │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Integration Point 4: Role Manager                              │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Service: RoleManager                                      │ │
│  │ Methods:                                                  │ │
│  │ • roleExists(roleName): Check if role exists              │ │
│  │ • createRole(roleName, attributes): Create new role       │ │
│  │ Purpose: Manage user roles                                │ │
│  │ Storage: Workspace database                               │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Integration Point 5: Runtime Context                           │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Service: RuntimeContext                                   │ │
│  │ Methods:                                                  │ │
│  │ • getConfiguration(key): Get extension configuration      │ │
│  │ • getDomainManager(): Get domain manager service          │ │
│  │ Purpose: Access platform services and configuration       │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Integration Point 6: Authorization Context                     │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Service: AuthorizationContext                             │ │
│  │ Methods:                                                  │ │
│  │ • isWorkspaceAdmin(): Check admin rights                  │ │
│  │ Purpose: Verify deploying user permissions                │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Integration Point 7: Catalog Integration                       │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ Area: Integration                                         │ │
│  │ Request: Get Script Element                               │ │
│  │ Purpose: Serve authenticator.js via catalog               │ │
│  │ Usage: UI integration, script injection                   │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Client Integration

```
┌─────────────────────────────────────────────────────────────────┐
│  Client-Side Integration                                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Step 1: Include authenticator.js                               │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ <script src="/authn/authenticator.js"></script>           │ │
│  │                                                           │ │
│  │ Or via catalog:                                           │ │
│  │ GET /catalog/Integration/Get%20Script%20Element           │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Step 2: Initialize Authenticator                               │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ const authenticator = new KristaAuthenticator({           │ │
│  │   loginUrl: '/authn/login',                               │ │
│  │   logoutUrl: '/authn/logout'                              │ │
│  │ });                                                       │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Step 3: Login                                                  │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ authenticator.login(email)                                │ │
│  │   .then(response => {                                     │ │
│  │     // Store session ID                                   │ │
│  │     sessionStorage.setItem('sessionId',                   │ │
│  │       response.clientSessionId);                          │ │
│  │     // Redirect to app                                    │ │
│  │     window.location.href = '/app';                        │ │
│  │   })                                                      │ │
│  │   .catch(error => {                                       │ │
│  │     // Handle error                                       │ │
│  │     console.error('Login failed:', error);                │ │
│  │   });                                                     │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Step 4: Make Authenticated Requests                            │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ const sessionId = sessionStorage.getItem('sessionId');    │ │
│  │                                                           │ │
│  │ fetch('/api/some-endpoint', {                             │ │
│  │   headers: {                                              │ │
│  │     'X-Krista-Context': JSON.stringify({                  │ │
│  │       clientSessionId: sessionId                          │ │
│  │     })                                                    │ │
│  │   }                                                       │ │
│  │ });                                                       │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Step 5: Logout                                                 │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ authenticator.logout(sessionId)                           │ │
│  │   .then(() => {                                           │ │
│  │     // Clear session                                      │ │
│  │     sessionStorage.removeItem('sessionId');               │ │
│  │     // Redirect to login                                  │ │
│  │     window.location.href = '/login';                      │ │
│  │   });                                                     │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Appendix

### Glossary

| Term | Definition |
|------|------------|
| **Account** | A user account in the Krista workspace, identified by email |
| **Authentication** | Process of verifying user identity |
| **Authorization** | Process of verifying user permissions |
| **CORS** | Cross-Origin Resource Sharing, HTTP header-based mechanism |
| **Domain** | Email domain (e.g., "company.com") |
| **Extension** | Krista platform extension, adds functionality to workspace |
| **JAX-RS** | Java API for RESTful Web Services |
| **Role** | Set of permissions assigned to accounts |
| **Session** | Temporary authentication state, expires after 24 hours |
| **Session ID** | Unique identifier for a session (UUID) |
| **Workspace** | Krista platform workspace, contains accounts, roles, data |
| **Workspace Admin** | User with administrative rights in workspace |

### Acronyms

| Acronym | Full Form |
|---------|-----------|
| **API** | Application Programming Interface |
| **CORS** | Cross-Origin Resource Sharing |
| **CSRF** | Cross-Site Request Forgery |
| **HTTP** | Hypertext Transfer Protocol |
| **HTTPS** | HTTP Secure |
| **JAX-RS** | Java API for RESTful Web Services |
| **JSON** | JavaScript Object Notation |
| **JWT** | JSON Web Token (not used in this extension) |
| **MFA** | Multi-Factor Authentication |
| **RBAC** | Role-Based Access Control |
| **REST** | Representational State Transfer |
| **SDK** | Software Development Kit |
| **TLS** | Transport Layer Security |
| **TOTP** | Time-based One-Time Password |
| **URL** | Uniform Resource Locator |
| **UUID** | Universally Unique Identifier |

### References

| Resource | Description |
|----------|-------------|
| **Krista SDK Documentation** | Platform SDK reference and guides |
| **JAX-RS Specification** | Java API for RESTful Web Services spec |
| **Jersey Documentation** | JAX-RS reference implementation |
| **Java 21 Documentation** | Java language and API documentation |
| **RFC 6749** | OAuth 2.0 Authorization Framework |
| **RFC 7519** | JSON Web Token (JWT) |
| **OWASP Top 10** | Web application security risks |

### Version History

| Version | Date | Changes |
|---------|------|---------|
| **1.0** | 2024-01-15 | Initial architecture document |

---

**Document Status:** Complete
**Last Updated:** 2024-01-15
**Author:** Architecture Team
**Reviewers:** Development Team, Security Team

