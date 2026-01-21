# Extension Configuration

## Overview

This guide provides step-by-step instructions for deploying and configuring the Demo Authentication Extension in your Krista workspace.

> **⚠️ Security Warning**: This extension is designed for demonstrations and development purposes only. Do not use in production environments.

## Prerequisites

Before configuring the Demo Authentication Extension, ensure you have:

1. ✅ **Workspace Admin Rights**: Required to deploy and configure extensions
2. ✅ **Krista Platform 3.4.0+**: Minimum platform version required
3. ✅ **Java 21+**: Required runtime environment
4. ✅ **Role Created** (Optional): Default role for new users (or use built-in "Krista Client User")

## Configuration Parameters

The Demo Authentication Extension has a single optional configuration parameter:

| Parameter | Type | Required | Description | Example | Default |
|-----------|------|----------|-------------|---------|---------|
| Default Role | Text | No | Role name assigned to new users | `"Demo User"` | `"Krista Client User"` |

### Parameter Details

#### Default Role

The role name that will be automatically assigned to newly created user accounts.

**Requirements**:
- Must be a valid role name
- Role will be created if it doesn't exist
- Can be empty (uses default)
- Case-sensitive

**Default Value**:
- If not specified: `"Krista Client User"`
- Built-in role with basic access

**Example Values**:
```
"Demo User"
"Trial User"
"Guest User"
"Krista Client User"
```

**Role Creation**:
- If role doesn't exist, extension creates it automatically
- Role created with basic permissions
- Can be modified after creation in Setup → Roles

## Step-by-Step Setup

### Step 1: Access Extensions

1. Log in to Krista workspace as admin
2. Navigate to **Setup** (gear icon)
3. Click **Extensions** in the left sidebar
4. You should see the Extensions management page

### Step 2: Find Demo Authentication Extension

1. In the Extensions list, locate **Demo Authentication**
2. Extension details:
   - **Name**: Demo Authentication
   - **Domain**: Authentication
   - **Ecosystem**: Krista
   - **Version**: 2.0.0

### Step 3: Deploy Extension

1. Click **Deploy** button next to Demo Authentication
2. Wait for deployment to complete (usually 10-30 seconds)
3. Status should change to "Deployed"
4. If deployment fails, check:
   - You have admin rights
   - Platform version is 3.4.0+
   - Java 21+ is installed
   - Review error logs

### Step 4: Configure Default Role (Optional)

1. Click on the deployed **Demo Authentication** extension
2. You should see the configuration page
3. Locate the **Default Role** field
4. Options:
   - **Leave empty**: Uses "Krista Client User" (recommended for most cases)
   - **Enter role name**: Custom role for new users
5. Click **Save** or **Apply**

### Step 5: Verify Configuration

1. Configuration saved successfully
2. Extension ready to use
3. Default role will be used for new accounts

### Step 6: Test Authentication

1. Open your Krista application in a new browser tab
2. You should see a login page with email input
3. Enter a test email address
4. Verify you are logged in
5. Check user name and role in Setup → Users

## Configuration Examples

### Example 1: Use Default Role

**Scenario**: Use built-in "Krista Client User" role

**Configuration**:
```
Default Role: (leave empty)
```

**Steps**:
1. Deploy Demo Authentication
2. Leave Default Role field empty
3. Save configuration
4. Test authentication

**Result**: New users get "Krista Client User" role

### Example 2: Custom Demo Role

**Scenario**: Create custom role for demo users

**Configuration**:
```
Default Role: Demo User
```

**Steps**:
1. Deploy Demo Authentication
2. Enter "Demo User" in Default Role field
3. Save configuration
4. Test authentication
5. Verify "Demo User" role created in Setup → Roles

**Result**: New users get "Demo User" role

### Example 3: Trial User Role

**Scenario**: Configure for trial users

**Configuration**:
```
Default Role: Trial User
```

**Steps**:
1. Deploy Demo Authentication
2. Enter "Trial User" in Default Role field
3. Save configuration
4. Customize "Trial User" role permissions in Setup → Roles
5. Test authentication

**Result**: New users get "Trial User" role with custom permissions

## Updating Configuration

### Changing Default Role

To change the default role:

1. Navigate to **Setup → Extensions → Demo Authentication**
2. Update the **Default Role** field
3. Click **Save**
4. New accounts will use new role
5. Existing accounts keep their current roles

> **📝 Note**: Changing the default role only affects newly created accounts. Existing accounts retain their assigned roles.

### Clearing Configuration

To revert to default "Krista Client User" role:

1. Navigate to **Setup → Extensions → Demo Authentication**
2. Clear the **Default Role** field
3. Click **Save**
4. New accounts will use "Krista Client User" role

## Role Management

### Creating Custom Roles

To create a custom role for demo users:

1. Navigate to **Setup → Roles**
2. Click **Add Role**
3. Enter role name (e.g., "Demo User")
4. Configure permissions:
   - **Workspace Access**: Enable
   - **Catalog Access**: Configure as needed
   - **Conversation Access**: Configure as needed
   - **Other Permissions**: Configure as needed
5. Save role
6. Configure Demo Authentication to use this role

### Modifying Role Permissions

To modify permissions for demo users:

1. Navigate to **Setup → Roles**
2. Find the default role (e.g., "Demo User")
3. Click to edit
4. Modify permissions as needed
5. Save changes
6. All users with this role will have updated permissions

### Assigning Multiple Roles

To assign additional roles to demo users:

1. Navigate to **Setup → Users**
2. Find user account
3. Click to edit
4. Add additional roles
5. Save changes

## Security Considerations

### Admin Rights Requirement

The Demo Authentication Extension requires workspace admin rights to deploy and configure.

**Why Admin Rights?**
- Prevents unauthorized authentication configuration
- Ensures only trusted admins can configure
- Protects workspace security
- Controls account creation

**Granting Admin Rights**:
1. Navigate to **Setup → Users**
2. Select user
3. Assign **Workspace Admin** role
4. Save changes

### Account Creation

The extension automatically creates accounts for new users:

**Account Creation Process**:
1. User enters email address
2. Extension validates email format
3. Extension checks if account exists
4. If not exists:
   - Creates account with email
   - Assigns default role
   - Sets user attributes
   - Creates session
5. If exists:
   - Authenticates existing account
   - Creates session

**Security Implications**:
- Anyone with valid email can create account
- No approval process
- No email verification
- Automatic role assignment

**Mitigation**:
- Use only in trusted environments
- Monitor account creation
- Review user list regularly
- Implement network-level access controls

### Email Validation

The extension validates email format:

**Validation Rules**:
- Must contain @ symbol
- Must have domain (e.g., .com, .org)
- Must match email regex pattern
- Case-sensitive

**Invalid Email Examples**:
```
❌ "user" (no domain)
❌ "user@" (incomplete domain)
❌ "@domain.com" (no username)
❌ "user domain.com" (no @)
```

**Valid Email Examples**:
```
✅ "user@company.com"
✅ "test.user@example.org"
✅ "demo+tag@domain.co.uk"
```

## Troubleshooting Configuration

### Issue: Cannot Deploy Extension

**Cause**: Insufficient permissions or platform issues

**Solution**:
1. Verify you have workspace admin rights
2. Check Krista Platform version is 3.4.0+
3. Verify Java 21+ is installed
4. Review deployment logs for errors
5. Contact support if issue persists

### Issue: Configuration Not Saving

**Cause**: Validation failure or system error

**Solution**:
1. Check role name is valid
2. Verify no special characters in role name
3. Try refreshing page and retry
4. Check browser console for errors
5. Review system logs

### Issue: Default Role Not Applied

**Cause**: Configuration issue or role creation failure

**Solution**:
1. Verify default role is configured correctly
2. Check role exists in Setup → Roles
3. Test with new user account
4. Review system logs for role creation errors

### Issue: Users Getting Wrong Role

**Cause**: Configuration changed or role assignment issue

**Solution**:
1. Verify default role configuration
2. Check role name spelling
3. Verify role exists
4. Test with new account
5. Review existing user roles in Setup → Users

## Advanced Configuration

### Multiple Workspaces

If you have multiple workspaces:

**Each workspace requires separate configuration**:
1. Deploy extension in each workspace
2. Configure default role for each workspace
3. Roles can be same or different per workspace
4. Accounts are workspace-specific

### Domain Management

The extension automatically manages email domains:

**Domain Creation**:
- Extension extracts domain from email (e.g., "company.com" from "user@company.com")
- Adds domain to workspace if not present
- Associates accounts with domain
- Enables domain-based organization

**Domain Benefits**:
- Organize users by company/organization
- Track users by domain
- Enable domain-based reporting

## Best Practices

### 1. Use Descriptive Role Names

Create clear, descriptive role names:

```
✅ Good:
- "Demo User"
- "Trial User"
- "Guest User"

❌ Bad:
- "Role1"
- "Test"
- "Temp"
```

### 2. Configure Appropriate Permissions

Set appropriate permissions for demo users:

```
✅ Good:
- Read-only access to demos
- Limited conversation access
- No admin permissions

❌ Bad:
- Full admin access
- Unrestricted permissions
- Production data access
```

### 3. Monitor Account Creation

Regularly review created accounts:

```
✅ Good:
- Weekly account review
- Monitor for suspicious accounts
- Remove inactive accounts

❌ Bad:
- Never review accounts
- Unlimited account creation
- No monitoring
```

### 4. Document Configuration

Document your configuration:

```
✅ Good:
- Environment: Demo
- Default Role: Demo User
- Purpose: Product demonstrations
- Configured: 2024-10-10

❌ Bad:
- No documentation
- Unknown configuration
```

### 5. Plan Migration Path

Plan for production authentication:

```
✅ Good:
- Use Demo Auth for demos
- Plan OAuth 2.0 for production
- Document migration steps

❌ Bad:
- Use Demo Auth indefinitely
- No production auth plan
```

## See Also

- [Authentication](pages/Authentication.md) - How authentication works
- [Dependencies](pages/Dependencies.md) - Integration requirements
- [Get Script Element](pages/GetScriptElement.md) - Login form integration
- [Troubleshooting](pages/Troubleshooting.md) - Common issues and solutions
- [Release Notes](pages/ReleaseNotes.md) - Version history

