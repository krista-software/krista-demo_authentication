/*
 * Demo Authentication Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>. 
 */

package app.krista.extensions.krista.authentication.demo_authentication;

import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import app.krista.extension.authorization.AuthorizationException;
import app.krista.extension.common.AuthenticationResponse;
import app.krista.extension.common.CommonUtils;
import app.krista.extension.executor.Invoker;
import app.krista.ksdk.accounts.Account;
import app.krista.ksdk.accounts.AccountManager;
import app.krista.ksdk.accounts.ModifiableAccount;
import app.krista.ksdk.authentication.AuthenticationSettings;
import app.krista.ksdk.authentication.SessionManager;
import app.krista.ksdk.authorization.ModifiableRole;
import app.krista.ksdk.authorization.Role;
import app.krista.ksdk.authorization.RoleManager;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RuntimeContext;
import app.krista.model.field.util.GsonJsonMapper;

@SuppressWarnings("deprecation")
@Path("/")
public class ExtensionResource {

    private final static GsonJsonMapper GSON_JSON_MAPPER = GsonJsonMapper.create();
    private final static String DEFAULT_ROLE = "Krista Client User";
    public static final String AUTHENTICATION_TYPE = "Demo Authentication";
    private final SessionManager sessionManager;
    private final AccountManager accountManager;
    private final RuntimeContext runtimeContext;
    private final RoleManager roleManager;
    private final AuthenticationSettings authenticationSettings;
    private final Invoker invoker;
    private final AuthorizationContext authorizationContext;

    @Inject
    public ExtensionResource(SessionManager sessionManager, AccountManager accountManager,
            RuntimeContext runtimeContext, RoleManager roleManager,
            AuthenticationSettings authenticationSettings, AuthorizationContext authorizationContext,
            @Named("self") Invoker invoker) {
        this.sessionManager = sessionManager;
        this.accountManager = accountManager;
        this.runtimeContext = runtimeContext;
        this.roleManager = roleManager;
        this.authenticationSettings = authenticationSettings;
        this.invoker = invoker;
        this.authorizationContext = authorizationContext;
    }

    @GET
    @Path("authenticator.js")
    public InputStream getAuthenticator() {
        return getClass().getClassLoader().getResourceAsStream("authenticator.js");
    }

    @GET
    @Path("/type")
    public String getAuthType() {
        return AUTHENTICATION_TYPE;
    }

    @OPTIONS
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginOptions(@Context HttpHeaders headers, String email) {
        return getResponseBuilderWithCORSHeaders().build();
    }

    /**
     * Takes user's email address for authentication and verify
     * if present in the current workspace where extension is added.
     * <p>
     * If user email is not present in workspace, the user gets added to workspace witb 'Default Role'
     * provided as part of extension setup page or 'Krista Client User' as default role.
     * <p>
     * The extension can be used to 'Demo' the various usecases with other
     * extensions like 'Krista Chatbot', 'Portal' where actual authentication mechanism is not present.
     *
     * @param headers
     * @param loginInput
     * @return
     * @throws AuthorizationException
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders headers, Map<String, String> loginInput) throws AuthorizationException {
        if (!authorizationContext.isWorkspaceAdmin()) {
            throw new IllegalArgumentException("Please provide workspace admin access");
        }

        if (loginInput != null && loginInput.containsKey("email") && isEmailAddressValid(loginInput.get("email"))) {
            String email = loginInput.get("email");
            addDomainToWorkspaceIfNotPresent(email);
            Account account = getAccount(email.substring(0, email.indexOf("@")), email, getUserAgent(headers));
            String sessionId = sessionManager.createSession(account.getAccountId());
            AuthenticationResponse authenticationResponse = createAuthenticationResponse(account, sessionId);
            return getResponseBuilderWithCORSHeaders().entity(GSON_JSON_MAPPER.toString(authenticationResponse))
                    .build();
        }
        throw new IllegalArgumentException("Invalid email input.");
    }

    @OPTIONS
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logoutOptions(@Context javax.ws.rs.core.HttpHeaders headers, String email) {
        try {
            return getResponseBuilderWithCORSHeaders().build();
        } catch (Exception cause) {
            throw new IllegalStateException("Failed to logout." +
                    (cause.getMessage() == null || cause.getMessage().isBlank() ? "" : cause.getMessage()), cause);
        }
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@Context javax.ws.rs.core.HttpHeaders headers, LogoutDTO logoutDTO) {
        try {
            String clientSessionId = logoutDTO.getClientSessionId();
            if (clientSessionId == null || clientSessionId.isEmpty()) {
                throw new IllegalArgumentException("Missing client session id.");
            }
            sessionManager.deleteSession(clientSessionId);
            Response.ResponseBuilder responseBuilderWithCORSHeaders =
                    getResponseBuilderWithCORSHeaders();
            return responseBuilderWithCORSHeaders.entity("Successfully logged out.").build();
        } catch (Exception cause) {
            throw new IllegalStateException("Failed to logout." +
                    (cause.getMessage() == null || cause.getMessage().isBlank() ? "" : cause.getMessage()), cause);
        }
    }

    private void addDomainToWorkspaceIfNotPresent(String email) throws AuthorizationException {
        List<String> supportedDomainsForWorkspace = authenticationSettings.getSupportedDomains();
        String supportedDomain = String.join(",", supportedDomainsForWorkspace);
        String invokerDomains = email.substring(email.indexOf("@") + 1);
        CommonUtils.validateIfSupportedDomain(email, supportedDomain, invokerDomains);
        CommonUtils.addSupportedDomainsToWorkspace(email, supportedDomainsForWorkspace, authenticationSettings);
    }

    private AuthenticationResponse createAuthenticationResponse(Account account, String sessionId)
            throws AuthorizationException {
        return new AuthenticationResponse(sessionId, account.getPerson().getPersonName(),
                account.getPerson().getAvatarUrl(), account.getAccountId(),
                runtimeContext.getKristaAccount().getAccountId(), account.getPerson().getPersonId(),
                getListOfRoles(account.getRoles()), account.getInboxId(), roleManager.isWorkspaceAdmin(account),
                false,
                Map.of("email", account.getPrimaryEmailAddress()),
                Map.of("creationTime", getCurrentDateTime()));
    }

    private List<String> getListOfRoles(Set<Role> roles) {
        List<String> allRoles = new ArrayList<>();
        for (Role role : roles) {
            allRoles.add(role.getRoleId());
        }
        return allRoles;
    }

    private Account getAccount(String name, String email, String userAgent)
            throws AuthorizationException {
        String defaultRole = createRoleIfNotPresent();
        ModifiableAccount account = accountManager.lookupAccount(email);
        List<String> roleNames = ensureHasAllRoles(account, defaultRole);
        if (account == null) {
            account = accountManager.createAccount(name, email,
                    new HashSet<>(roleNames),//Default role
                    Map.of("ORG", "KristaSoft", "KRISTA_SOURCE", "EXTENSION_DEMO_AUTHENTICATION",
                            "KRISTA_LAST_LOGIN",
                            getCurrentDateTime(), "KRISTA_USER_AGENT_INFO", userAgent == null ? "" : userAgent));
        }
        return account;
    }

    private List<String> ensureHasAllRoles(ModifiableAccount modifiableAccount, String roles)
            throws AuthorizationException {
        List<String> accountRoles = new ArrayList<>();
        accountRoles.add(roles);
        List<String> allRoles = new ArrayList<>();
        Iterable<ModifiableRole> workspaceRoles = roleManager.getRoles();
        for (String accountRole : accountRoles) {
            boolean roleExit = false;
            for (ModifiableRole workspaceRole : workspaceRoles) {
                if (Objects.equals(accountRole, workspaceRole.getName())) {
                    allRoles.add(workspaceRole.getRoleId());
                    roleExit = true;
                    break;
                }
            }
            if (!roleExit) {
                ModifiableRole role = roleManager.createRole(accountRole);
                allRoles.add(role.getRoleId());
            }
        }
        if (modifiableAccount != null) {
            for (Role role : modifiableAccount.getRoles()) {
                allRoles.add(role.getRoleId());
            }
            modifiableAccount.addRole(allRoles.toArray(String[]::new));
        }
        return allRoles;
    }

    private String createRoleIfNotPresent() throws AuthorizationException {
        String defaultRole = getDefaultRole();
        boolean isRolePresent = isDefaultRolePresentInWorkspace(defaultRole);
        if (!isRolePresent) {
            roleManager.createRole(defaultRole);
        }
        return defaultRole;
    }

    private boolean isDefaultRolePresentInWorkspace(String defaultRole) throws AuthorizationException {
        Iterable<ModifiableRole> roles = roleManager.getRoles();
        if (roles != null) {
            for (ModifiableRole role : roles) {
                if (Objects.equals(defaultRole, role.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getDefaultRole() {
        String defaultRole = DEFAULT_ROLE;
        Object defaultRoleObject = invoker.getAttributes().get(DemoAuthenticationExtension.DEFAULT_ROLE_KEY);
        if (defaultRoleObject instanceof String && !((String) defaultRoleObject).trim().isEmpty()) {
            defaultRole = (String) defaultRoleObject;
        }
        return defaultRole;
    }

    private String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private boolean isEmailAddressValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    private Response.ResponseBuilder getResponseBuilderWithCORSHeaders() {
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type");
    }

    private String getUserAgent(HttpHeaders headers) {
        String userAgent = "NOT FOUND";
        if (headers != null && headers.firstValue("User-Agent").isPresent()) {
            userAgent = headers.firstValue("User-Agent").get();
        }
        return userAgent;
    }

}
