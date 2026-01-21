# Release Notes

## Overview

This page documents the version history, changes, and updates for the Demo Authentication Extension.

## Version 2.0.0 (Current)

**Release Date**: Jan 2026
**Platform Compatibility**: Krista Platform 3.4.0+
**Developer**: Vrushali Gaikwad

### New Features

#### ✨ Email-Based Authentication
- Simple email-only authentication
- No password required
- Instant user authentication
- User-friendly login experience

#### ✨ Automatic Account Creation
- Automatically creates accounts for new users
- Configurable default role assignment
- Seamless user onboarding
- No manual account management needed

#### ✨ Session Management
- Automatic session creation on login
- Session-based authentication
- Logout functionality
- Secure session handling via Krista platform

#### ✨ REST API
- RESTful login endpoint (`POST /authn/login`)
- RESTful logout endpoint (`POST /authn/logout`)
- Health check endpoint (`GET /authn/type`)
- JSON-based request/response
- CORS support for cross-origin requests

#### ✨ Configurable Default Role
- Optional default role configuration
- Automatic role creation if doesn't exist
- Falls back to "Krista Client User" if not configured
- Flexible role assignment

#### ✨ Domain Management
- Automatic domain extraction from email
- Adds domain to workspace if not present
- Domain-based user organization
- Supports multiple domains

### Catalog Requests

#### Get Script Element
- **Type**: QUERY_SYSTEM
- **Area**: Integration
- **Description**: Retrieves JavaScript code snippet for login form
- **Output**: JavaScript code with email input field
- **Use Case**: Integrate login form in custom UIs

### Improvements

#### 🚀 Performance
- Fast authentication (100-300ms)
- Efficient account lookup
- Optimized session creation
- Minimal overhead

#### 🔒 Security
- Workspace admin rights enforcement
- Email format validation
- Session-based authentication
- CORS headers for cross-origin support

#### 📝 Documentation
- Comprehensive setup guide
- Authentication flow documentation
- Integration examples
- Troubleshooting guide
- Best practices

#### 🔧 Configuration
- Simple one-parameter configuration
- Optional default role setting
- Easy deployment process
- No complex setup required

### Technical Details

#### Platform Requirements
- **Krista Platform**: 3.4.0+
- **Java**: 21+
- **Browser**: Modern browsers with JavaScript support

#### Dependencies
- No external extension dependencies
- Standalone authentication extension
- Compatible with Krista Chatbot, Portal, and other extensions

#### API Endpoints
- `POST /authn/login` - Authenticate user with email
- `POST /authn/logout` - Logout user and destroy session
- `GET /authn/type` - Health check (returns "Demo Authentication")
- `OPTIONS /authn/login` - CORS preflight for login
- `OPTIONS /authn/logout` - CORS preflight for logout

### Known Limitations

#### Security Limitations
- ⚠️ **No password protection**: Anyone with valid email format can authenticate
- ⚠️ **No email verification**: Email ownership not verified
- ⚠️ **Automatic account creation**: No approval process for new accounts
- ⚠️ **Development only**: Not recommended for production use

#### Functional Limitations
- Single default role for all new users
- Cannot prevent automatic account creation
- No multi-factor authentication support
- No password reset functionality (not applicable)

#### Integration Limitations
- Cannot use with other authentication extensions simultaneously
- Requires workspace admin rights for deployment
- CORS enabled for all origins (security consideration)

### Migration Notes

#### From Version 1.x
If upgrading from version 1.x:

1. **Backup Configuration**: Export existing configuration
2. **Export Users**: Export user list from Setup → Users
3. **Undeploy Old Version**: Undeploy version 1.x
4. **Deploy New Version**: Deploy version 2.0.0
5. **Configure**: Set default role if needed
6. **Test**: Verify authentication works
7. **Verify Users**: Check existing users still have access

#### To Production Authentication
When migrating to production authentication:

1. **Choose Auth Method**: Select OAuth 2.0, SAML, or other
2. **Export Users**: Export user data
3. **Deploy New Extension**: Deploy production auth extension
4. **Migrate Accounts**: Create accounts in new system
5. **Test**: Verify authentication works
6. **Undeploy Demo Auth**: Remove Demo Authentication
7. **Update Docs**: Update user documentation

### Bug Fixes

#### 🐛 Fixed Issues
- Improved error handling for invalid email formats
- Enhanced session management reliability
- Fixed CORS header configuration
- Improved account creation error messages

### Deprecations

None in this version.

### Breaking Changes

None in this version.

## Version 1.0.0

**Release Date**: March 2024

**Platform Compatibility**: Krista Platform 3.0.0+

### Initial Release

#### Features
- Basic email-based authentication
- Manual account creation
- Simple login/logout endpoints
- Basic session management

#### Limitations
- Manual account creation required
- No automatic role assignment
- Limited error handling
- Basic documentation

## Upgrade Guide

### Upgrading to 2.0.0

#### Prerequisites
1. Krista Platform 3.4.0 or higher
2. Java 21 or higher
3. Workspace admin rights

#### Upgrade Steps

**Step 1: Backup Current Configuration**
```
1. Navigate to Setup → Extensions → Demo Authentication
2. Document current configuration
3. Export user list from Setup → Users
4. Save configuration details
```

**Step 2: Undeploy Current Version**
```
1. Navigate to Setup → Extensions
2. Find Demo Authentication
3. Click Undeploy
4. Wait for undeployment to complete
```

**Step 3: Deploy New Version**
```
1. Navigate to Setup → Extensions
2. Find Demo Authentication (version 2.0.0)
3. Click Deploy
4. Wait for deployment to complete
```

**Step 4: Configure Extension**
```
1. Click on deployed Demo Authentication
2. Configure default role (optional)
3. Save configuration
```

**Step 5: Verify Upgrade**
```
1. Test login with existing user
2. Test login with new user
3. Verify account creation works
4. Check user roles are correct
5. Test logout functionality
```

**Step 6: Update Documentation**
```
1. Update user documentation
2. Notify users of any changes
3. Update integration documentation
```

## Compatibility Matrix

### Platform Compatibility

| Demo Auth Version | Krista Platform | Java | Status |
|------------------|----------------|------|--------|
| 2.0.0 | 3.4.0+ | 21+ | ✅ Current |
| 1.0.0 | 3.0.0 - 3.3.x | 17+ | ⚠️ Legacy |

### Extension Compatibility

| Extension | Compatible | Notes |
|-----------|------------|-------|
| Krista Chatbot 3.4.0+ | ✅ Yes | Full compatibility |
| Krista Portal 3.4.0+ | ✅ Yes | Full compatibility |
| Krista Managed Entities 3.5.0+ | ✅ Yes | Full compatibility |
| Guest Authentication | ❌ No | Conflicting auth extension |
| Static Authentication | ❌ No | Conflicting auth extension |
| Email Password Auth | ❌ No | Conflicting auth extension |
| OAuth2 Authentication | ❌ No | Conflicting auth extension |

## Roadmap

### Planned Features

#### Version 2.1.0 (Planned)
- Enhanced error messages
- Improved logging
- Additional configuration options
- Performance optimizations

#### Version 2.2.0 (Planned)
- Custom email validation rules
- Multiple default roles support
- Enhanced domain management
- Improved documentation

#### Version 3.0.0 (Future)
- Optional password support
- Email verification option
- Account approval workflow
- Enhanced security features

> **📝 Note**: Roadmap items are subject to change based on user feedback and requirements.

## Support

### Getting Help

**Documentation**:
- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Authentication](pages/Authentication.md)
- [Dependencies](pages/Dependencies.md)
- [Troubleshooting](pages/Troubleshooting.md)

**Support Channels**:
- **Email**: support@kristasoft.com
- **Community**: https://community.kristasoft.com
- **Documentation**: [Demo Authentication Docs](/)

### Reporting Issues

To report issues:

1. **Check Documentation**: Review troubleshooting guide
2. **Check Known Issues**: Review known limitations
3. **Gather Information**:
   - Extension version
   - Platform version
   - Error messages
   - Steps to reproduce
4. **Contact Support**: Email support with details

### Feature Requests

To request features:

1. **Check Roadmap**: Review planned features
2. **Describe Use Case**: Explain why feature is needed
3. **Provide Details**: Describe desired functionality
4. **Submit Request**: Email support or post in community

## Changelog

### 2.0.0 (October 2024)
- ✨ Added automatic account creation
- ✨ Added configurable default role
- ✨ Added domain management
- ✨ Added comprehensive REST API
- ✨ Added Get Script Element catalog request
- 🚀 Improved performance
- 🔒 Enhanced security
- 📝 Comprehensive documentation
- 🐛 Bug fixes and improvements

### 1.0.0 (March 2024)
- 🎉 Initial release
- ✨ Basic email authentication
- ✨ Login/logout endpoints
- ✨ Session management

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration
- [Authentication](pages/Authentication.md) - How authentication works
- [Dependencies](pages/Dependencies.md) - Integration requirements
- [Get Script Element](pages/GetScriptElement.md) - Login form integration
- [Troubleshooting](pages/Troubleshooting.md) - Common issues and solutions

---

**Copyright © 2024 Krista Software. All rights reserved.**

