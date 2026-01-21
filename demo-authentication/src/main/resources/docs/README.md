# Demo Authentication Extension

## Overview

The **Demo Authentication Extension** provides a simple, email-based authentication mechanism designed specifically for demonstrations, proof-of-concepts, and development environments. This extension allows users to authenticate using only their email address, with automatic account creation if the user doesn't exist in the workspace.

This extension is ideal for:
- **Product demonstrations** where simplified authentication is needed
- **Proof-of-concept projects** requiring quick user onboarding
- **Development and testing** environments with dynamic user creation
- **Chatbot and portal integrations** where traditional authentication is not required
- **Internal applications** with trusted users

> **⚠️ Security Warning**: This extension is designed for demonstrations and development purposes. For production environments, use more secure authentication methods like OAuth 2.0, SAML, or multi-factor authentication.

## Key Features

### ✅ Email-Only Authentication
- No password required
- Simple email input
- Instant authentication
- User-friendly login experience

### ✅ Automatic Account Creation
- Creates accounts automatically if user doesn't exist
- Assigns default role to new users
- Configurable default role
- Seamless user onboarding

### ✅ Session Management
- Automatic session creation
- Session-based authentication
- Logout functionality
- Secure session handling

### ✅ REST API Integration
- RESTful login/logout endpoints
- JSON-based request/response
- CORS support for cross-origin requests
- Health check endpoint

### ✅ Workspace Integration
- Seamless integration with Krista workspace
- Role-based access control
- Admin rights enforcement
- Domain management

### ✅ Flexible Configuration
- Configurable default role for new users
- Automatic role assignment
- Domain-based organization
- Custom user attributes

## Quick Start Guide

### 1. Deploy Extension

1. Navigate to **Setup → Extensions**
2. Find **Demo Authentication** in the extension catalog
3. Click **Deploy**
4. Wait for deployment to complete

### 2. Configure Default Role (Optional)

1. Go to **Setup → Extensions → Demo Authentication**
2. Enter the default role name for new users (optional)
3. If not specified, uses "Krista Client User" as default
4. Click **Save**

### 3. Test Authentication

1. Open your Krista application
2. Enter an email address on the login page
3. User will be authenticated (account created if needed)
4. Access application with assigned role

### 4. Verify Access

1. Check user is logged in
2. Verify user has appropriate roles
3. Test application functionality

## Documentation Structure

### Getting Started

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration guide
- [Authentication](pages/Authentication.md) - How authentication works
- [Dependencies](pages/Dependencies.md) - Integration requirements

### Catalog Requests

- [Get Script Element](pages/GetScriptElement.md) - Retrieve login form script

### Additional Resources

- [Release Notes](pages/ReleaseNotes.md) - Version history and changes
- [Troubleshooting](pages/Troubleshooting.md) - Common issues and solutions

## Architecture

### Authentication Flow

```
1. User accesses application
2. Login page displayed with email input
3. User enters email address
4. Extension validates email format
5. Extension checks if account exists:
   - If exists: Authenticate user
   - If not exists: Create account with default role
6. Create session
7. Return authentication response
8. User logged in
```

### Components

#### DemoAuthenticationExtension
- Main extension class
- Manages configuration (default role)
- Provides request authenticator
- Handles custom tabs

#### ExtensionRequestAuthenticator
- Implements RequestAuthenticator interface
- Authenticates requests using session
- Handles login requests
- Returns authenticated account ID

#### ExtensionResource (REST API)
- Provides `/login` endpoint
- Provides `/logout` endpoint
- Provides `/type` health check endpoint
- Manages session creation/deletion
- Handles account creation
- Sets CORS headers

#### IntegrationArea
- Provides "Get Script Element" catalog request
- Returns JavaScript for login form
- Enables UI integration

### Account Creation

When a new user logs in:

1. **Email Validation**: Validates email format
2. **Account Lookup**: Checks if account exists
3. **Domain Management**: Adds email domain to workspace if needed
4. **Role Assignment**: Assigns default role (configured or "Krista Client User")
5. **Account Creation**: Creates account with:
   - Name: Email prefix (before @)
   - Email: User's email address
   - Roles: Default role
   - Attributes: Organization, source, last login, user agent
6. **Session Creation**: Creates session for user
7. **Response**: Returns authentication response

## Use Cases

### 1. Product Demonstration

**Scenario**: Demo Krista application to potential customers

**Solution**:
```
1. Deploy Demo Authentication
2. Configure default role for demo users
3. Customers enter email to access demo
4. Accounts created automatically
5. Focus on product features, not authentication
```

### 2. Chatbot Integration

**Scenario**: Integrate authentication with Krista Chatbot

**Solution**:
```
1. Deploy Demo Authentication
2. Configure chatbot to use Demo Auth
3. Users enter email to start chat
4. Automatic authentication and account creation
5. Seamless chatbot experience
```

### 3. Portal Integration

**Scenario**: Integrate authentication with Krista Portal

**Solution**:
```
1. Deploy Demo Authentication
2. Configure portal to use Demo Auth
3. Users enter email to access portal
4. Automatic authentication
5. Portal access granted
```

### 4. Development Environment

**Scenario**: Quick user creation for development

**Solution**:
```
1. Deploy Demo Authentication
2. Developers enter email to access
3. Accounts created automatically
4. No manual account management needed
```

## Security Considerations

### ⚠️ Important Security Notes

1. **Not for Production**: This extension is designed for demonstrations/development only
2. **No Password Protection**: Anyone with an email can authenticate
3. **Automatic Account Creation**: New accounts created without approval
4. **Admin Rights Required**: Extension requires workspace admin rights to deploy
5. **Email Validation Only**: Only validates email format, not ownership

### Security Best Practices

1. **Use Only in Trusted Environments**:
   - Demo workspaces
   - Development environments
   - Internal testing
   - Proof-of-concept projects

2. **Limit Access**:
   - Restrict network access to workspace
   - Use firewall rules
   - Implement IP whitelisting if possible

3. **Monitor Usage**:
   - Review access logs regularly
   - Monitor account creation
   - Track session activity

4. **Migrate for Production**:
   - Use OAuth 2.0 for production
   - Implement multi-factor authentication
   - Use enterprise SSO solutions

## Performance

### Authentication Performance

- **First Login (New User)**: ~200-300ms (account creation + session)
- **Existing User Login**: ~100-150ms (session creation)
- **Logout**: ~50-100ms (session deletion)

### Scalability

The extension is designed for:
- ✅ Small to medium user bases (< 1000 users)
- ✅ Demo and development environments
- ✅ Moderate concurrent users (< 100)

## Limitations

1. **No Password**: No password validation or protection
2. **Email Only**: Only validates email format, not ownership
3. **Automatic Creation**: Cannot prevent account creation
4. **Single Default Role**: All new users get same default role
5. **No MFA**: No multi-factor authentication support
6. **Development Only**: Not recommended for production use

## Support & Resources

### Documentation
- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Authentication](pages/Authentication.md)
- [Get Script Element](pages/GetScriptElement.md)
- [Troubleshooting](pages/Troubleshooting.md)

### Version Information
- **Current Version**: 2.0.0
- **Krista Platform**: 3.4.0+
- **Java Version**: 21+

### Getting Help
- **Support Email**: support@kristasoft.com
- **Community**: https://community.kristasoft.com
- **Documentation**: [Demo Authentication Docs](/)

## Migration Path

### Moving to Production Authentication

When ready for production, migrate to a more secure authentication method:

1. **Choose Authentication Method**:
   - OAuth 2.0 (Google, Microsoft, etc.)
   - SAML 2.0 (Enterprise SSO)
   - Email/Password with MFA
   - Custom authentication

2. **Export User Data**:
   - Export user accounts
   - Document user roles
   - Prepare migration plan

3. **Deploy New Extension**:
   - Deploy chosen authentication extension
   - Configure authentication provider
   - Test authentication flow

4. **Migrate Users**:
   - Create accounts in new system
   - Map existing accounts
   - Test user access

5. **Remove Demo Authentication**:
   - Undeploy Demo Authentication extension
   - Verify new authentication works
   - Update documentation

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup guide
- [Authentication](pages/Authentication.md) - Authentication details
- [Dependencies](pages/Dependencies.md) - Integration requirements
- [Get Script Element](pages/GetScriptElement.md) - Login form integration
- [Release Notes](pages/ReleaseNotes.md) - Version history
- [Troubleshooting](pages/Troubleshooting.md) - Common issues

---

**Copyright © 2024 Krista Software. All rights reserved.**

