# Get Script Element

## Overview

The **Get Script Element** catalog request retrieves a JavaScript code snippet that creates and configures the login form for Demo Authentication. This script dynamically generates the email input field and handles the login UI integration.

## Request Details

- **Area**: Integration
- **Type**: QUERY_SYSTEM
- **Retry Support**: ✅ Yes (safe to retry, read-only operation)

## Input Parameters

This catalog request has **no input parameters**. It returns a static JavaScript code snippet.

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| _(none)_ | - | - | No input parameters required | - |

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Script Element | Text | JavaScript code snippet for login form |

### Script Element Structure

The returned script contains:

1. **prepareUserInterfaceClient Function**: Prepares the UI client
2. **updateLoginText Function**: Updates login text with user email
3. **Template Cloning**: Clones login form template
4. **Email Input Field**: Dynamically adds email input field
5. **jQuery Integration**: Uses jQuery for DOM manipulation

### Example Output

```javascript
<script>
    function prepareUserInterfaceClient(predicate, args) {
      predicate(args);
    }
    function updateLoginText(ref, data) {
      ref.loggedUserText.text(data.email);
    }
    const template = document.getElementById("template-form");
    const templateClone = template.content.cloneNode(true);
    document.getElementById("__hosted__container__").appendChild(templateClone);
    $(document).ready(function () {
      $("#form").prepend(`<input name="email" type="text" id="email" placeholder="Enter email" /><br />`);
    });
  </script>
```

## Validation Rules

This catalog request has **no validation rules** as it accepts no input parameters.

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| _(none)_ | - | No validation required |

## Error Handling

### System Errors (SYSTEM_ERROR)

**Cause**: System-level failure during script retrieval

**Error Message**: "System error occurred while retrieving script element"

**Common Scenarios**:
- Extension not properly initialized
- System resource unavailable
- Internal server error

**Resolution**:
1. Retry the request
2. Verify extension is deployed
3. Check system logs
4. Contact support if issue persists

## Usage Examples

### Example 1: Retrieve Login Script

**Scenario**: Get JavaScript code for login form

**Input**:
```
(No input parameters)
```

**Output**:
```javascript
<script>
    function prepareUserInterfaceClient(predicate, args) {
      predicate(args);
    }
    function updateLoginText(ref, data) {
      ref.loggedUserText.text(data.email);
    }
    const template = document.getElementById("template-form");
    const templateClone = template.content.cloneNode(true);
    document.getElementById("__hosted__container__").appendChild(templateClone);
    $(document).ready(function () {
      $("#form").prepend(`<input name="email" type="text" id="email" placeholder="Enter email" /><br />`);
    });
  </script>
```

**Result**: JavaScript code snippet returned successfully

### Example 2: Integrate with Custom UI

**Scenario**: Use script in custom login page

**Steps**:
```
1. Call "Get Script Element" catalog request
2. Retrieve JavaScript code
3. Inject code into login page HTML
4. Ensure template-form element exists
5. Ensure __hosted__container__ element exists
6. Load jQuery library
7. Test login form
```

**Result**: Login form displayed with email input field

### Example 3: Chatbot Integration

**Scenario**: Integrate login script with chatbot

**Steps**:
```
1. Call "Get Script Element" catalog request
2. Retrieve JavaScript code
3. Add code to chatbot login page
4. Configure chatbot to use Demo Auth
5. Test chatbot login flow
```

**Result**: Chatbot login form configured with email input

## Business Rules

1. **Static Script**: The script is static and does not change based on configuration
2. **jQuery Dependency**: The script requires jQuery library to be loaded
3. **Template Requirements**: Requires `template-form` and `__hosted__container__` elements in HTML
4. **Email Input**: Dynamically adds email input field to form
5. **No Customization**: Script cannot be customized via parameters

## Limitations

1. **Static Content**: Script is hardcoded and cannot be customized
2. **jQuery Required**: Requires jQuery library to be loaded on page
3. **Template Dependency**: Requires specific HTML template structure
4. **No Styling**: Script does not include CSS styling
5. **No Validation**: Script does not include client-side email validation
6. **Single Input**: Only creates email input field, no other fields

## Best Practices

### 1. Load jQuery First

Ensure jQuery is loaded before the script:

```html
<!-- Load jQuery first -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- Then load Demo Auth script -->
<script>
  // Get Script Element output here
</script>
```

### 2. Provide Required HTML Elements

Ensure required elements exist:

```html
<!-- Template for login form -->
<template id="template-form">
  <form id="form">
    <!-- Email input will be prepended here -->
    <button type="submit">Login</button>
  </form>
</template>

<!-- Container for form -->
<div id="__hosted__container__"></div>
```

### 3. Add CSS Styling

Add custom CSS for better appearance:

```css
#email {
  width: 300px;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

#form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}
```

### 4. Add Client-Side Validation

Add email validation before submission:

```javascript
$(document).ready(function() {
  $("#form").on("submit", function(e) {
    const email = $("#email").val();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (!emailRegex.test(email)) {
      e.preventDefault();
      alert("Please enter a valid email address");
      return false;
    }
  });
});
```

### 5. Handle Form Submission

Implement form submission handler:

```javascript
$(document).ready(function() {
  $("#form").on("submit", function(e) {
    e.preventDefault();
    
    const email = $("#email").val();
    
    // Call login API
    fetch("/authn/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email: email })
    })
    .then(response => response.json())
    .then(data => {
      // Handle successful login
      console.log("Login successful", data);
      // Store session, redirect, etc.
    })
    .catch(error => {
      // Handle error
      console.error("Login failed", error);
    });
  });
});
```

## Common Use Cases

### 1. Basic Login Page

**Scenario**: Create simple login page for demo

**Implementation**:
```html
<!DOCTYPE html>
<html>
<head>
  <title>Demo Login</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style>
    #email { width: 300px; padding: 10px; margin: 10px 0; }
    #form { max-width: 400px; margin: 50px auto; }
  </style>
</head>
<body>
  <template id="template-form">
    <form id="form">
      <h2>Demo Login</h2>
      <button type="submit">Login</button>
    </form>
  </template>
  
  <div id="__hosted__container__"></div>
  
  <!-- Get Script Element output -->
  <script>
    // Insert script from catalog request here
  </script>
</body>
</html>
```

**Result**: Simple login page with email input

### 2. Chatbot Login Integration

**Scenario**: Integrate login with Krista Chatbot

**Steps**:
```
1. Call "Get Script Element" catalog request
2. Retrieve JavaScript code
3. Add to chatbot login page template
4. Configure chatbot authentication
5. Test chatbot login
```

**Result**: Chatbot with email-based login

### 3. Portal Login Integration

**Scenario**: Integrate login with Krista Portal

**Steps**:
```
1. Call "Get Script Element" catalog request
2. Retrieve JavaScript code
3. Add to portal login page
4. Configure portal authentication
5. Test portal login
```

**Result**: Portal with email-based login

### 4. Custom Application Integration

**Scenario**: Integrate login with custom application

**Steps**:
```
1. Call "Get Script Element" catalog request
2. Retrieve JavaScript code
3. Customize HTML template
4. Add CSS styling
5. Implement form submission handler
6. Test login flow
```

**Result**: Custom application with Demo Auth login

## Related Catalog Requests

This is the only catalog request in the Demo Authentication Extension. For authentication operations, use the REST API endpoints:

- **Login**: `POST /authn/login`
- **Logout**: `POST /authn/logout`
- **Health Check**: `GET /authn/type`

## Technical Implementation

### Helper Class

This catalog request is implemented directly in the `IntegrationArea` class:

- **Class**: `IntegrationArea`
- **Package**: `app.krista.extensions.krista.authentication.demo_authentication.catalog`
- **Method**: `getScriptElement()`
- **Return Type**: `String`

### Implementation Details

<augment_code_snippet path="extensions/demo-authentication/src/main/java/app/krista/extensions/krista/authentication/demo_authentication/catalog/IntegrationArea.java" mode="EXCERPT">
````java
@CatalogRequest(description = "Get Script Element",
        name = "Get Script Element",
        area = "Integration",
        type = CatalogRequest.Type.QUERY_SYSTEM)
@Field(name = "Script Element", type = "Text", attributes = {}, options = {})
public String getScriptElement() {
    return "<script>\n" +
            "    function prepareUserInterfaceClient(predicate, args) {\n" +
            "      predicate(args);\n" +
            "    }\n" +
            ...
````
</augment_code_snippet>

### Script Components

#### 1. prepareUserInterfaceClient Function

```javascript
function prepareUserInterfaceClient(predicate, args) {
  predicate(args);
}
```

**Purpose**: Prepares the UI client by calling predicate function

#### 2. updateLoginText Function

```javascript
function updateLoginText(ref, data) {
  ref.loggedUserText.text(data.email);
}
```

**Purpose**: Updates login text with user's email after authentication

#### 3. Template Cloning

```javascript
const template = document.getElementById("template-form");
const templateClone = template.content.cloneNode(true);
document.getElementById("__hosted__container__").appendChild(templateClone);
```

**Purpose**: Clones login form template and appends to container

#### 4. Email Input Creation

```javascript
$(document).ready(function () {
  $("#form").prepend(`<input name="email" type="text" id="email" placeholder="Enter email" /><br />`);
});
```

**Purpose**: Dynamically adds email input field to form

## Troubleshooting

### Issue: Script Not Working

**Symptoms**:
- Login form not displayed
- Email input not appearing
- JavaScript errors in console

**Cause**: Missing dependencies or HTML elements

**Solution**:
1. Verify jQuery is loaded
2. Check `template-form` element exists
3. Check `__hosted__container__` element exists
4. Review browser console for errors
5. Verify script is properly injected

### Issue: Email Input Not Appearing

**Symptoms**:
- Form displayed but no email input
- jQuery errors in console

**Cause**: jQuery not loaded or form element not found

**Solution**:
1. Load jQuery before script
2. Verify form element has id="form"
3. Check jQuery version compatibility
4. Review console for errors

### Issue: Template Not Found

**Symptoms**:
- Error: "Cannot read property 'content' of null"
- Form not displayed

**Cause**: Missing template-form element

**Solution**:
1. Add `<template id="template-form">` to HTML
2. Ensure template contains form element
3. Verify template ID is correct
4. Check template is in DOM before script runs

### Issue: Container Not Found

**Symptoms**:
- Error: "Cannot read property 'appendChild' of null"
- Form not displayed

**Cause**: Missing __hosted__container__ element

**Solution**:
1. Add `<div id="__hosted__container__">` to HTML
2. Verify container ID is correct
3. Check container is in DOM before script runs

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration
- [Authentication](pages/Authentication.md) - How authentication works
- [Dependencies](pages/Dependencies.md) - Integration requirements
- [Troubleshooting](pages/Troubleshooting.md) - Common issues and solutions
- [Release Notes](pages/ReleaseNotes.md) - Version history

