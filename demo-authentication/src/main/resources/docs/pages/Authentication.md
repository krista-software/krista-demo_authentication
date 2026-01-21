# Authentication

## Overview

The Demo Authentication Extension provides a simplified authentication mechanism that authenticates users based on their email address, with automatic account creation for new users. This page explains how the authentication process works, account creation, session management, and security considerations.

> **⚠️ Security Warning**: This extension is designed for demonstrations and development purposes only. It does not provide password protection, email verification, or multi-factor authentication.

## Authentication Mechanism

### How It Works

The Demo Authentication Extension uses an **email-based authentication with automatic account creation** approach:

1. **Login Page**: User accesses application and sees login page
2. **Email Input**: User enters email address
3. **Email Validation**: Extension validates email format
4. **Account Lookup**: Extension checks if account exists in workspace
5. **Account Creation** (if needed): Creates account with default role
6. **Session Creation**: Creates session for user
7. **Authentication**: User is authenticated and logged in

### No Password Required

Unlike traditional authentication:

- ❌ **No password prompt**: Users are not asked for a password
- ❌ **No email verification**: Email ownership not verified
- ❌ **No approval process**: Accounts created automatically
- ✅ **Automatic**: Authentication happens automatically
- ✅ **Simple**: Only email address needed
- ✅ **Fast**: Instant account creation and login

## Authentication Flow

### Detailed Flow

```
┌─────────────────────────────────────────────────────────────┐
│ 1. User Accesses Application                                │
│    - Opens Krista application URL                           │
│    - Login page displayed                                   │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. User Enters Email                                         │
│    - User types email address                               │
│    - Clicks login/submit button                             │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. Email Validation                                          │
│    - Extension validates email format                       │
│    - Checks for @ symbol and domain                         │
│    - If invalid: Return error                               │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. Domain Management                                         │
│    - Extract domain from email (e.g., company.com)          │
│    - Check if domain exists in workspace                    │
│    - If not: Add domain to workspace                        │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. Account Lookup                                            │
│    - Extension looks up email in workspace                  │
│    - AccountManager.lookupAccount(email)                    │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 6. Account Creation (if needed)                              │
│    - If account not found:                                  │
│      * Create account with email                            │
│      * Assign default role                                  │
│      * Set user attributes (org, source, login time)        │
│    - If account exists: Use existing account                │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 7. Session Creation                                          │
│    - SessionManager.createSession(accountId)                │
│    - Generate unique session ID                             │
│    - Store session in session manager                       │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 8. Authentication Response                                   │
│    - Create AuthenticationResponse object                   │
│    - Include: sessionId, accountId, personName, roles       │
│    - Return JSON response to client                         │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 9. User Authenticated                                        │
│    - User logged in with created/existing account           │
│    - Session stored in client                               │
│    - User can access application                            │
└─────────────────────────────────────────────────────────────┘
```

### Code Flow

#### 1. Login Request

<augment_code_snippet path="extensions/demo-authentication/src/main/java/app/krista/extensions/krista/authentication/demo_authentication/ExtensionResource.java" mode="EXCERPT">
````java
@POST
@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response login(@Context HttpHeaders headers, Map<String, String> loginInput) {
    if (loginInput != null && loginInput.containsKey("email") && 
        isEmailAddressValid(loginInput.get("email"))) {
        String email = loginInput.get("email");
        addDomainToWorkspaceIfNotPresent(email);
        Account account = getAccount(email.substring(0, email.indexOf("@")), 
                                     email, getUserAgent(headers));
        String sessionId = sessionManager.createSession(account.getAccountId());
        ...
````
</augment_code_snippet>

#### 2. Account Creation

<augment_code_snippet path="extensions/demo-authentication/src/main/java/app/krista/extensions/krista/authentication/demo_authentication/ExtensionResource.java" mode="EXCERPT">
````java
private Account getAccount(String name, String email, String userAgent) {
    String defaultRole = createRoleIfNotPresent();
    ModifiableAccount account = accountManager.lookupAccount(email);
    if (account == null) {
        account = accountManager.createAccount(name, email,
            new HashSet<>(roleNames), // Default role
            Map.of("ORG", "KristaSoft", 
                   "KRISTA_SOURCE", "EXTENSION_DEMO_AUTHENTICATION",
                   "KRISTA_LAST_LOGIN", getCurrentDateTime(), 
                   "KRISTA_USER_AGENT_INFO", userAgent));
    }
    return account;
}
````
</augment_code_snippet>

## Account Creation

### Automatic Account Creation

When a new user logs in with an email that doesn't exist:

1. **Extract Name**: Uses email prefix as name (before @)
2. **Create Account**: Creates account with:
   - **Name**: Email prefix (e.g., "john.doe" from "john.doe@company.com")
   - **Email**: User's email address
   - **Roles**: Default role (configured or "Krista Client User")
   - **Attributes**:
     - `ORG`: "KristaSoft"
     - `KRISTA_SOURCE`: "EXTENSION_DEMO_AUTHENTICATION"
     - `KRISTA_LAST_LOGIN`: Current timestamp
     - `KRISTA_USER_AGENT_INFO`: Browser user agent
3. **Assign Role**: Assigns default role to account
4. **Return Account**: Returns created account

### Account Attributes

Created accounts have the following attributes:

| Attribute | Value | Purpose |
|-----------|-------|---------|
| ORG | "KristaSoft" | Organization identifier |
| KRISTA_SOURCE | "EXTENSION_DEMO_AUTHENTICATION" | Account creation source |
| KRISTA_LAST_LOGIN | Current timestamp | Last login time |
| KRISTA_USER_AGENT_INFO | Browser user agent | Client information |

### Role Assignment

**Default Role Logic**:
1. Check if default role configured in extension
2. If configured: Use configured role
3. If not configured: Use "Krista Client User"
4. If role doesn't exist: Create role automatically
5. Assign role to account

**Role Creation**:
- Extension creates role if it doesn't exist
- Role created with basic permissions
- Can be customized after creation in Setup → Roles

## Session Management

### Session Creation

When a user authenticates:

1. **Session ID Generated**: Unique session identifier created
2. **Session Stored**: Session saved in session manager
3. **Session Returned**: Session ID returned to client
4. **Client Storage**: Client stores session for subsequent requests

### Session Validation

On subsequent requests:

1. **Extract Session**: Get session ID from request
2. **Validate Session**: Check session exists and is valid
3. **Get Account**: Retrieve account ID from session
4. **Authenticate**: User authenticated if session valid

### Session Expiration

Sessions managed by Krista platform:
- **Expiration**: Platform-managed (typically 24 hours)
- **Automatic Cleanup**: Expired sessions removed automatically
- **Re-authentication**: User must re-authenticate after expiration

## REST API

### Login Endpoint

**Endpoint**: `POST /authn/login`

**Request**:
```json
{
  "email": "user@company.com"
}
```

**Response** (Success):
```json
{
  "clientSessionId": "session_abc123...",
  "name": "John Doe",
  "avatarUrl": "https://...",
  "accountId": "account_xyz789...",
  "kristaAccountId": "krista_account_123...",
  "personId": "person_456...",
  "roles": ["Demo User"],
  "inboxId": "inbox_789...",
  "isWorkspaceAdmin": false,
  "isApplianceManager": false,
  "identificationToken": {
    "email": "user@company.com"
  },
  "extras": {
    "creationTime": "2024-10-10T10:30:00Z"
  }
}
```

**Response** (Error):
```json
{
  "message": "Invalid email input.",
  "code": "Demo Auth - 500"
}
```

### Logout Endpoint

**Endpoint**: `POST /authn/logout`

**Request**:
```json
{
  "clientSessionId": "session_abc123..."
}
```

**Response** (Success):
```
"Successfully logged out."
```

**Response** (Error):
```json
{
  "message": "Missing client session id.",
  "code": "Demo Auth - 500"
}
```

### Health Check Endpoint

**Endpoint**: `GET /authn/type`

**Response**:
```
"Demo Authentication"
```

**Purpose**: Verify extension is running and accessible

## Security Considerations

### Authentication Security

#### ⚠️ No Password Protection

The extension does **not** validate passwords:

- Anyone with a valid email format can authenticate
- No password prompt or validation
- No email ownership verification
- Relies on network security and access controls

**Mitigation**:
- Use only in trusted environments
- Implement network-level access controls
- Use firewall rules to restrict access
- Monitor account creation

#### ⚠️ Automatic Account Creation

Accounts created automatically without approval:

- No admin approval required
- No email verification
- Immediate access granted
- Default role assigned automatically

**Mitigation**:
- Monitor account creation regularly
- Review user list in Setup → Users
- Remove unauthorized accounts
- Implement network restrictions

#### ⚠️ Email Validation Only

Only validates email format, not ownership:

- No email verification sent
- No confirmation required
- Anyone can use any email
- No proof of email ownership

**Mitigation**:
- Use only in controlled environments
- Monitor for suspicious emails
- Implement domain restrictions if possible

### Network Security

#### Recommended Protections

1. **HTTPS Only**: Always use HTTPS in production
2. **Firewall Rules**: Restrict access to trusted IPs
3. **VPN**: Require VPN for access
4. **Network Isolation**: Isolate demo/test environments
5. **IP Whitelisting**: Allow only specific IP ranges

### CORS Configuration

The extension enables CORS for cross-origin requests:

**CORS Headers**:
- `Access-Control-Allow-Origin`: * (all origins)
- `Access-Control-Allow-Methods`: POST, GET, OPTIONS
- `Access-Control-Allow-Headers`: Content-Type

**Security Implications**:
- Allows requests from any origin
- Suitable for demos and development
- Not recommended for production

## Performance

### Authentication Performance

| Operation | Time | Description |
|-----------|------|-------------|
| New User Login | 200-300ms | Account creation + session creation |
| Existing User Login | 100-150ms | Session creation only |
| Logout | 50-100ms | Session deletion |
| Health Check | < 10ms | Simple string response |

### Optimization Strategies

#### 1. Account Caching

The extension uses workspace account caching:

```
✅ Benefits:
- Faster account lookups
- Reduced database queries
- Better performance

⚠️ Considerations:
- Account changes may not reflect immediately
- Cache invalidation needed for updates
```

#### 2. Session Management

Uses Krista platform session management:

```
✅ Benefits:
- Reliable session storage
- Automatic expiration
- Platform-managed lifecycle

⚠️ Considerations:
- Session lookup overhead
- Platform dependency
```

## Troubleshooting Authentication

### Issue: "Invalid email input" Error

**Symptoms**:
- Login fails with error message
- Cannot authenticate

**Cause**: Invalid email format

**Resolution**:
1. Verify email has @ symbol
2. Check email has domain (e.g., .com)
3. Ensure no spaces in email
4. Use valid email format

### Issue: "Please provide workspace admin access" Error

**Symptoms**:
- Login fails with admin access error
- Cannot create account

**Cause**: Extension requires workspace admin rights

**Resolution**:
1. Verify extension deployed by workspace admin
2. Check extension configuration
3. Ensure admin rights are enabled
4. Contact workspace admin

### Issue: Account Not Created

**Symptoms**:
- Login succeeds but account not in user list
- Cannot find user in Setup → Users

**Cause**: Account creation failure or lookup issue

**Resolution**:
1. Check system logs for errors
2. Verify default role exists
3. Check workspace has space for new users
4. Retry login
5. Contact support if issue persists

### Issue: Wrong Role Assigned

**Symptoms**:
- User has wrong role
- Expected different permissions

**Cause**: Default role configuration issue

**Resolution**:
1. Check default role configuration in extension
2. Verify role name is correct
3. Check role exists in Setup → Roles
4. Update user role manually in Setup → Users

## Best Practices

### 1. Use for Appropriate Scenarios

```
✅ Good:
- Product demonstrations
- Proof-of-concept projects
- Development environments
- Internal testing

❌ Bad:
- Production applications
- Customer-facing systems
- Sensitive data access
- Financial applications
```

### 2. Monitor Account Creation

```
✅ Good:
- Review new accounts daily
- Monitor for suspicious emails
- Remove unauthorized accounts
- Track account creation patterns

❌ Bad:
- Never review accounts
- Ignore account creation
- No monitoring
```

### 3. Configure Appropriate Roles

```
✅ Good:
- Limited permissions for demo users
- Read-only access where possible
- No admin rights for new users

❌ Bad:
- Full admin access
- Unrestricted permissions
- Production data access
```

### 4. Implement Network Security

```
✅ Good:
- HTTPS only
- Firewall rules
- IP whitelisting
- VPN required

❌ Bad:
- HTTP allowed
- Public access
- No restrictions
```

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration
- [Dependencies](pages/Dependencies.md) - Integration requirements
- [Get Script Element](pages/GetScriptElement.md) - Login form integration
- [Troubleshooting](pages/Troubleshooting.md) - Common issues and solutions
- [Release Notes](pages/ReleaseNotes.md) - Version history

