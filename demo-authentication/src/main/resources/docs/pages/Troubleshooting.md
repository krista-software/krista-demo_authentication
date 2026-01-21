# Troubleshooting

## Overview

This page provides solutions to common issues encountered when using the Demo Authentication Extension.

## Common Issues

### Authentication Issues

#### Issue: "Invalid email input" Error

**Symptoms**:
- Login fails with error message
- Cannot authenticate
- Error displayed on login page

**Cause**: Invalid email format

**Error Message**: `"Invalid email input."`

**Resolution**:
1. Verify email contains @ symbol
2. Check email has domain (e.g., .com, .org)
3. Ensure no spaces in email
4. Use valid email format: `user@domain.com`
5. Try different email address

**Valid Email Examples**:
```
✅ user@company.com
✅ test.user@example.org
✅ demo+tag@domain.co.uk
```

**Invalid Email Examples**:
```
❌ user (no domain)
❌ user@ (incomplete domain)
❌ @domain.com (no username)
❌ user domain.com (no @)
❌ user @domain.com (space before @)
```

#### Issue: "Please provide workspace admin access" Error

**Symptoms**:
- Login fails with admin access error
- Cannot create account
- Error message about admin rights

**Cause**: Extension requires workspace admin rights

**Error Message**: `"Please provide workspace admin access"`

**Resolution**:
1. Verify extension was deployed by workspace admin
2. Check extension configuration
3. Ensure admin rights are enabled for extension
4. Contact workspace admin to verify deployment
5. Redeploy extension with admin rights

**Admin Rights Check**:
```
1. Navigate to Setup → Extensions
2. Find Demo Authentication
3. Verify "Require Workspace Admin Rights" is enabled
4. If not enabled, undeploy and redeploy
```

#### Issue: Account Not Created

**Symptoms**:
- Login succeeds but account not in user list
- Cannot find user in Setup → Users
- User not showing in workspace

**Cause**: Account creation failure or lookup issue

**Resolution**:
1. Check system logs for errors
2. Verify default role exists or can be created
3. Check workspace has space for new users
4. Verify email format is valid
5. Try logging in again
6. Check if account exists with different email
7. Contact support if issue persists

**Verification Steps**:
```
1. Navigate to Setup → Users
2. Search for user email
3. Check if account exists
4. Verify account has correct role
5. Check account creation timestamp
```

#### Issue: Wrong Role Assigned

**Symptoms**:
- User has wrong role
- Expected different permissions
- User cannot access expected features

**Cause**: Default role configuration issue

**Resolution**:
1. Check default role configuration in extension
2. Verify role name is correct (case-sensitive)
3. Check role exists in Setup → Roles
4. Update user role manually in Setup → Users
5. Reconfigure default role in extension
6. Test with new user account

**Manual Role Update**:
```
1. Navigate to Setup → Users
2. Find user account
3. Click to edit
4. Update roles
5. Save changes
```

### Configuration Issues

#### Issue: Extension Won't Deploy

**Symptoms**:
- Deployment fails
- Error message during deployment
- Extension not showing as deployed

**Cause**: Insufficient permissions, platform issues, or dependency problems

**Resolution**:
1. Verify you have workspace admin rights
2. Check Krista Platform version is 3.4.0+
3. Verify Java 21+ is installed
4. Review deployment logs for specific errors
5. Check system resources (memory, disk space)
6. Try undeploying and redeploying
7. Contact support if issue persists

**Deployment Logs**:
```
1. Navigate to Setup → Extensions
2. Find Demo Authentication
3. Click on extension
4. View deployment logs
5. Look for error messages
```

#### Issue: Configuration Not Saving

**Symptoms**:
- Changes not persisted
- Configuration reverts to previous value
- Save button doesn't work

**Cause**: Validation failure, system error, or browser issue

**Resolution**:
1. Check role name is valid (no special characters)
2. Verify role name is not empty if specified
3. Try refreshing page and retry
4. Check browser console for JavaScript errors
5. Try different browser
6. Clear browser cache
7. Review system logs for errors

**Browser Console Check**:
```
1. Open browser developer tools (F12)
2. Go to Console tab
3. Look for error messages
4. Try saving configuration again
5. Check for errors
```

#### Issue: Default Role Not Applied

**Symptoms**:
- New users not getting configured role
- Users getting "Krista Client User" instead
- Role assignment not working

**Cause**: Configuration issue or role creation failure

**Resolution**:
1. Verify default role is configured correctly
2. Check role exists in Setup → Roles
3. Verify role name spelling (case-sensitive)
4. Test with new user account
5. Review system logs for role creation errors
6. Try reconfiguring default role
7. Manually create role in Setup → Roles first

**Role Verification**:
```
1. Navigate to Setup → Roles
2. Search for default role name
3. Verify role exists
4. Check role permissions
5. Test with new user
```

### Session Issues

#### Issue: Session Expired

**Symptoms**:
- User logged out unexpectedly
- "Session expired" message
- Need to re-authenticate

**Cause**: Session timeout or session invalidation

**Resolution**:
1. Log in again with email
2. Session will be recreated
3. Check session timeout settings
4. Verify session not manually deleted

**Session Timeout**:
- Default: Platform-managed (typically 24 hours)
- Cannot be configured in Demo Authentication
- Managed by Krista platform

#### Issue: Cannot Logout

**Symptoms**:
- Logout fails
- Error message on logout
- Session not destroyed

**Cause**: Missing session ID or system error

**Error Message**: `"Missing client session id."`

**Resolution**:
1. Verify session ID is provided in logout request
2. Check session ID is valid
3. Try logging out again
4. Clear browser cookies
5. Close browser and reopen
6. Contact support if issue persists

**Logout Request Format**:
```json
{
  "clientSessionId": "session_abc123..."
}
```

### Integration Issues

#### Issue: Login Form Not Displayed

**Symptoms**:
- Login page blank
- No email input field
- Form not rendering

**Cause**: Missing HTML elements, jQuery not loaded, or script error

**Resolution**:
1. Verify jQuery is loaded before script
2. Check `template-form` element exists in HTML
3. Check `__hosted__container__` element exists in HTML
4. Review browser console for JavaScript errors
5. Verify script from "Get Script Element" is injected
6. Check HTML structure matches requirements

**Required HTML Elements**:
```html
<template id="template-form">
  <form id="form">
    <!-- Email input will be added here -->
  </form>
</template>

<div id="__hosted__container__"></div>
```

#### Issue: Email Input Not Appearing

**Symptoms**:
- Form displayed but no email input
- jQuery errors in console
- Input field missing

**Cause**: jQuery not loaded or form element not found

**Resolution**:
1. Load jQuery before Demo Auth script
2. Verify form element has id="form"
3. Check jQuery version compatibility (3.6.0+ recommended)
4. Review console for jQuery errors
5. Ensure script runs after DOM is ready

**jQuery Loading**:
```html
<!-- Load jQuery first -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- Then load Demo Auth script -->
<script>
  // Get Script Element output here
</script>
```

#### Issue: Chatbot Integration Fails

**Symptoms**:
- Chatbot won't authenticate
- Login fails in chatbot
- Error messages in chatbot

**Cause**: Configuration issue or authentication conflict

**Resolution**:
1. Verify Demo Authentication is deployed
2. Check chatbot is configured to use Demo Auth
3. Test authentication separately (not in chatbot)
4. Review chatbot configuration
5. Check for conflicting authentication extensions
6. Verify CORS headers are set
7. Review integration logs

**Chatbot Configuration**:
```
1. Navigate to chatbot settings
2. Verify authentication extension is Demo Authentication
3. Test chatbot login
4. Check chatbot logs for errors
```

#### Issue: Portal Integration Fails

**Symptoms**:
- Portal won't authenticate
- Login fails in portal
- Error messages in portal

**Cause**: Configuration issue or authentication conflict

**Resolution**:
1. Verify Demo Authentication is deployed
2. Check portal is configured to use Demo Auth
3. Test authentication separately (not in portal)
4. Review portal configuration
5. Check for conflicting authentication extensions
6. Verify CORS headers are set
7. Review integration logs

### API Issues

#### Issue: Login API Returns 500 Error

**Symptoms**:
- HTTP 500 error on login
- Internal server error
- Login fails

**Cause**: System error or invalid request

**Resolution**:
1. Check request format is correct (JSON)
2. Verify email parameter is provided
3. Review system logs for specific error
4. Check extension is deployed and running
5. Verify workspace admin rights
6. Try with different email
7. Contact support with error details

**Correct Request Format**:
```json
POST /authn/login
Content-Type: application/json

{
  "email": "user@company.com"
}
```

#### Issue: CORS Errors

**Symptoms**:
- CORS policy error in browser console
- Cross-origin request blocked
- Login fails from different domain

**Cause**: CORS headers not set or browser security

**Resolution**:
1. Verify extension sets CORS headers
2. Check browser console for specific CORS error
3. Ensure request includes proper headers
4. Try OPTIONS request first (preflight)
5. Verify extension is running
6. Check network tab in browser dev tools

**CORS Headers Set by Extension**:
```
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: POST, GET, OPTIONS
Access-Control-Allow-Headers: Content-Type
```

### Performance Issues

#### Issue: Slow Authentication

**Symptoms**:
- Login takes long time (> 5 seconds)
- Timeout errors
- Poor performance

**Cause**: System load, network issues, or database performance

**Resolution**:
1. Check system resources (CPU, memory)
2. Verify network connectivity
3. Review system logs for performance issues
4. Check database performance
5. Monitor concurrent users
6. Consider system optimization
7. Contact support if issue persists

**Expected Performance**:
- New user login: 200-300ms
- Existing user login: 100-150ms
- Logout: 50-100ms

#### Issue: High Memory Usage

**Symptoms**:
- Extension using excessive memory
- System slowdown
- Out of memory errors

**Cause**: Memory leak or excessive session storage

**Resolution**:
1. Monitor memory usage over time
2. Check for session cleanup
3. Review system logs
4. Restart extension if needed
5. Contact support with memory metrics

## Error Messages Reference

### Authentication Errors

| Error Message | Cause | Resolution |
|---------------|-------|------------|
| "Invalid email input." | Invalid email format | Use valid email format |
| "Please provide workspace admin access" | Missing admin rights | Deploy with admin rights |
| "Missing client session id." | Logout without session ID | Provide session ID in request |

### System Errors

| Error Message | Cause | Resolution |
|---------------|-------|------------|
| "System error occurred" | Internal server error | Check logs, retry, contact support |
| "Extension not found" | Extension not deployed | Deploy extension |
| "Configuration error" | Invalid configuration | Review configuration |

## Diagnostic Steps

### Step 1: Verify Extension Deployment

```
1. Navigate to Setup → Extensions
2. Find Demo Authentication
3. Verify status is "Deployed"
4. Check version is 2.0.0
5. Review deployment logs
```

### Step 2: Test Authentication

```
1. Open login page
2. Enter valid email
3. Click login
4. Verify authentication succeeds
5. Check user created in Setup → Users
```

### Step 3: Check Configuration

```
1. Navigate to Setup → Extensions → Demo Authentication
2. Verify default role configuration
3. Check role exists in Setup → Roles
4. Test with new user
```

### Step 4: Review Logs

```
1. Access system logs
2. Filter for Demo Authentication
3. Look for errors or warnings
4. Note timestamps and error messages
5. Correlate with user actions
```

### Step 5: Test API Directly

```
1. Use Postman or curl
2. Test login endpoint: POST /authn/login
3. Verify response
4. Test logout endpoint: POST /authn/logout
5. Test health check: GET /authn/type
```

## Getting Help

### Before Contacting Support

1. **Review Documentation**:
   - [Extension Configuration](pages/ExtensionConfiguration.md)
   - [Authentication](pages/Authentication.md)
   - [Dependencies](pages/Dependencies.md)

2. **Gather Information**:
   - Extension version
   - Platform version
   - Error messages (exact text)
   - Steps to reproduce
   - System logs
   - Screenshots if applicable

3. **Try Basic Troubleshooting**:
   - Restart extension
   - Clear browser cache
   - Try different browser
   - Test with different email

### Contacting Support

**Email**: support@kristasoft.com

**Include in Support Request**:
- Subject: "Demo Authentication - [Brief Description]"
- Extension version: 2.0.0
- Platform version: [Your version]
- Issue description
- Steps to reproduce
- Error messages
- System logs (if available)
- Screenshots (if applicable)

### Community Support

**Community Forum**: https://community.kristasoft.com

**Search Before Posting**:
- Search for similar issues
- Review existing solutions
- Check FAQ section

**When Posting**:
- Use descriptive title
- Provide detailed description
- Include error messages
- Share configuration (remove sensitive data)
- Describe what you've tried

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration
- [Authentication](pages/Authentication.md) - How authentication works
- [Dependencies](pages/Dependencies.md) - Integration requirements
- [Get Script Element](pages/GetScriptElement.md) - Login form integration
- [Release Notes](pages/ReleaseNotes.md) - Version history

