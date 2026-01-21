# Dependencies

## Overview

This page describes the dependencies, requirements, and integration considerations for the Demo Authentication Extension.

## System Requirements

### Platform Requirements

| Requirement | Minimum Version | Recommended | Notes |
|-------------|----------------|-------------|-------|
| Krista Platform | 3.4.0 | Latest | Core platform |
| Java | 21 | 21+ | Runtime environment |

### Browser Requirements

| Browser | Minimum Version | Notes |
|---------|----------------|-------|
| Chrome | 90+ | Recommended |
| Firefox | 88+ | Supported |
| Safari | 14+ | Supported |
| Edge | 90+ | Supported |

> **📝 Note**: Browsers must support modern JavaScript and CORS.

## Extension Dependencies

### Required Dependencies

The Demo Authentication Extension has **no external extension dependencies**. It is a standalone authentication extension.

### Optional Dependencies

While not required, the following extensions can be used alongside Demo Authentication:

| Extension | Purpose | Compatibility |
|-----------|---------|---------------|
| Krista Chatbot | Chatbot integration | ✅ Compatible |
| Krista Portal | Portal integration | ✅ Compatible |
| Krista Managed Entities | Entity storage | ✅ Compatible |
| Krista Extension | Core functionality | ✅ Compatible |
| Scheduler | Scheduled tasks | ✅ Compatible |

## Workspace Requirements

### Admin Rights

The Demo Authentication Extension requires:

1. **Workspace Admin Rights**:
   - Required to deploy extension
   - Required to configure extension
   - Required for account creation during login

> **⚠️ Important**: The extension checks for workspace admin rights during login. If the authenticated user is not a workspace admin, login will fail.

### Role Requirements

**For Extension Deployment**:
- Workspace Admin role required

**For New Users**:
- Default role (configured or "Krista Client User")
- Role created automatically if doesn't exist

**Example Role Configuration**:
```
Default Role: Demo User
Permissions:
  - Workspace Access: Enabled
  - Catalog Access: Read-only
  - Conversation Access: Limited
```

## Integration Patterns

### Pattern 1: Chatbot Integration

Use Demo Authentication with Krista Chatbot:

```
┌─────────────────────────────────────┐
│ Krista Workspace                    │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Demo Authentication           │ │
│  │ - Email-based login           │ │
│  │ - Auto account creation       │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Krista Chatbot                │ │
│  │ - Uses Demo Auth              │ │
│  │ - Seamless user experience    │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
```

**Use Cases**:
- Demo chatbot to customers
- Internal chatbot testing
- Proof-of-concept projects

**Setup**:
1. Deploy Demo Authentication
2. Deploy Krista Chatbot
3. Configure chatbot to use Demo Auth
4. Test chatbot with email login

### Pattern 2: Portal Integration

Use Demo Authentication with Krista Portal:

```
┌─────────────────────────────────────┐
│ Krista Workspace                    │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Demo Authentication           │ │
│  │ - Email-based login           │ │
│  │ - Auto account creation       │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Krista Portal                 │ │
│  │ - Uses Demo Auth              │ │
│  │ - Quick user onboarding       │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
```

**Use Cases**:
- Demo portal to stakeholders
- Internal portal testing
- Trial user access

**Setup**:
1. Deploy Demo Authentication
2. Deploy Krista Portal
3. Configure portal to use Demo Auth
4. Test portal with email login

### Pattern 3: Development + Production Auth

Use Demo Authentication for development, different auth for production:

```
Development Workspace:
┌─────────────────────────────────────┐
│ Demo Authentication                 │
│ - Email-based login                 │
│ - Auto account creation             │
│ - Quick development access          │
└─────────────────────────────────────┘

Production Workspace:
┌─────────────────────────────────────┐
│ OAuth 2.0 Authentication            │
│ - Google/Microsoft SSO              │
│ - Secure production access          │
│ - Email verification                │
└─────────────────────────────────────┘
```

**Use Cases**:
- Separate dev/prod environments
- Different auth per environment
- Production security requirements

### Pattern 4: Multi-Extension Demo

Use Demo Authentication with multiple extensions:

```
┌─────────────────────────────────────┐
│ Demo Workspace                      │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Demo Authentication           │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Krista Chatbot                │ │
│  │ Krista Portal                 │ │
│  │ Krista Managed Entities       │ │
│  │ Custom Extensions             │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
```

**Use Cases**:
- Comprehensive product demos
- Multi-feature demonstrations
- Integrated solution showcases

## Using Demo Authentication as a Dependency

### In Custom Extensions

If you're building a custom extension that needs authentication, you can use Demo Authentication:

#### Step 1: Deploy Demo Authentication

```
1. Deploy Demo Authentication extension
2. Configure default role
3. Verify authentication works
```

#### Step 2: Reference in Custom Extension

Your custom extension will automatically use the workspace authentication:

```java
@Extension(version = "1.0.0")
public class MyCustomExtension {
    
    @Inject
    private Invoker invoker;
    
    @CatalogRequest(name = "My Request", area = "Custom")
    public void myRequest() {
        // Authentication handled by Demo Auth
        String accountId = invoker.getAccountId();
        // Use authenticated account
    }
}
```

#### Step 3: Access Authenticated User

```java
// Get authenticated account ID
String accountId = invoker.getAccountId();

// Get account details
Account account = workspace.getAccountProvider()
    .getAccount(accountId);

// Use account information
String email = account.getPrimaryEmailAddress();
String name = account.getPerson().getPersonName();
Set<Role> roles = account.getRoles();
```

## Network Configuration

### Firewall Rules

For secure deployment, configure firewall rules:

**Recommended Rules**:
```
Allow:
- HTTPS (443) from trusted IPs
- Internal network access

Deny:
- HTTP (80) from all sources (if possible)
- External network access (for internal demos)
```

### HTTPS Configuration

Always use HTTPS in production:

**Requirements**:
- Valid SSL/TLS certificate
- HTTPS enabled on Krista platform
- Secure connection support

**Configuration**:
```
1. Obtain SSL certificate
2. Configure Krista platform for HTTPS
3. Verify HTTPS works
4. Test authentication over HTTPS
```

### CORS Configuration

The extension enables CORS by default:

**CORS Headers**:
- `Access-Control-Allow-Origin`: * (all origins)
- `Access-Control-Allow-Methods`: POST, GET, OPTIONS
- `Access-Control-Allow-Headers`: Content-Type

**Customization**:
- CORS headers are set in ExtensionResource
- Can be modified for specific origins
- Suitable for cross-origin demos

## Migration Considerations

### Migrating FROM Demo Authentication

When moving to production authentication:

#### Step 1: Choose New Authentication

Select production authentication method:
- OAuth 2.0 (Google, Microsoft, etc.)
- SAML 2.0 (Enterprise SSO)
- Email/Password with MFA
- Custom authentication

#### Step 2: Export User Data

```
1. Navigate to Setup → Users
2. Export user list
3. Document user roles
4. Note user attributes
5. Prepare migration plan
```

#### Step 3: Deploy New Extension

```
1. Deploy new authentication extension
2. Configure authentication provider
3. Test authentication flow
4. Verify user access
```

#### Step 4: Migrate Users

```
1. Create accounts in new system
2. Map existing accounts by email
3. Assign appropriate roles
4. Test user authentication
5. Verify permissions
```

#### Step 5: Remove Demo Authentication

```
1. Verify new authentication works
2. Test all user scenarios
3. Undeploy Demo Authentication
4. Update documentation
5. Notify users of change
```

### Migrating TO Demo Authentication

When setting up demo environment:

#### Step 1: Undeploy Existing Auth

```
1. Identify current authentication extension
2. Export user data if needed
3. Undeploy current extension
4. Verify no authentication conflicts
```

#### Step 2: Deploy Demo Authentication

```
1. Deploy Demo Authentication
2. Configure default role
3. Test authentication
4. Verify account creation works
```

## Compatibility Matrix

### Extension Compatibility

| Extension | Version | Compatible | Notes |
|-----------|---------|------------|-------|
| Krista Chatbot | 3.4.0+ | ✅ Yes | Full compatibility |
| Krista Portal | 3.4.0+ | ✅ Yes | Full compatibility |
| Krista Managed Entities | 3.5.0+ | ✅ Yes | Full compatibility |
| Guest Authentication | 2.0.0+ | ⚠️ Conflict | Cannot use both |
| Static Authentication | 2.0.0+ | ⚠️ Conflict | Cannot use both |
| Email Password Auth | 2.0.0+ | ⚠️ Conflict | Cannot use both |
| OAuth2 Authentication | 2.0.0+ | ⚠️ Conflict | Cannot use both |
| Krista Extension | 3.4.0+ | ✅ Yes | Full compatibility |
| Scheduler | 3.4.0+ | ✅ Yes | Full compatibility |

> **⚠️ Warning**: Only one authentication extension can be active at a time. Deploying multiple authentication extensions will cause conflicts.

### Platform Compatibility

| Platform Version | Compatible | Notes |
|-----------------|------------|-------|
| 3.4.0 | ✅ Yes | Minimum version |
| 3.5.0 | ✅ Yes | Recommended |
| 4.0.0 | ✅ Yes | Future compatibility |

## Troubleshooting Dependencies

### Issue: Extension Won't Deploy

**Cause**: Missing dependencies or incompatible platform

**Solution**:
1. Verify Krista Platform is 3.4.0+
2. Check Java 21+ is installed
3. Verify you have admin rights
4. Review deployment logs

### Issue: Authentication Conflicts

**Cause**: Multiple authentication extensions deployed

**Solution**:
1. Identify all authentication extensions
2. Undeploy conflicting extensions
3. Keep only one authentication extension
4. Redeploy Demo Authentication

### Issue: Chatbot/Portal Integration Fails

**Cause**: Configuration or compatibility issue

**Solution**:
1. Verify Demo Authentication is deployed
2. Check chatbot/portal configuration
3. Test authentication separately
4. Review integration logs
5. Verify CORS headers are set

## Best Practices

### 1. Environment Separation

Use different authentication per environment:

```
✅ Good:
- Development: Demo Authentication
- Testing: Demo Authentication
- Production: OAuth 2.0

❌ Bad:
- All environments: Demo Authentication
```

### 2. Dependency Management

Keep dependencies up to date:

```
✅ Good:
- Regular platform updates
- Monitor compatibility
- Test after updates

❌ Bad:
- Never update platform
- Ignore compatibility issues
```

### 3. Integration Testing

Test integrations thoroughly:

```
✅ Good:
- Test chatbot integration
- Test portal integration
- Verify account creation
- Test all user flows

❌ Bad:
- No integration testing
- Assume it works
```

### 4. Documentation

Document dependencies and configuration:

```
✅ Good:
- Document all dependencies
- Track versions used
- Note compatibility issues
- Update regularly

❌ Bad:
- No documentation
- Unknown dependencies
```

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration
- [Authentication](pages/Authentication.md) - How authentication works
- [Get Script Element](pages/GetScriptElement.md) - Login form integration
- [Troubleshooting](pages/Troubleshooting.md) - Common issues and solutions
- [Release Notes](pages/ReleaseNotes.md) - Version history

