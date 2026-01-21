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

import java.util.Map;
import javax.inject.Inject;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.impl.anno.*;
import app.krista.ksdk.authentication.SessionManager;
import app.krista.ksdk.context.AuthorizationContext;

@SuppressWarnings("deprecation")
@Java(version = Java.Version.JAVA_21)
@Field(name = DemoAuthenticationExtension.DEFAULT_ROLE_KEY, type = "Text", required = false)
@Domain(id = "catEntryDomain_db053e8f-a194-4dde-aa6f-701ef7a6b3a7",
        name = "Authentication",
        ecosystemId = "catEntryEcosystem_d3b05047-07b0-4b06-95a3-9fb8f7f608d9",
        ecosystemName = "Krista",
        ecosystemVersion = "3e7e09ed-688f-41fa-ab7c-ff879e750011")
@Extension(jaxrsId = "authn", implementingDomainIds = "catEntryDomain_db053e8f-a194-4dde-aa6f-701ef7a6b3a7", requireWorkspaceAdminRights = true, version = "3.5.8", name = "Demo Authentication")
@StaticResource(path = "docs", file = "docs")
public class DemoAuthenticationExtension {

    public static final String DEFAULT_ROLE_KEY = "default_role";

    private final AuthorizationContext authorizationContext;
    private final SessionManager sessionManager;

    @Inject
    public DemoAuthenticationExtension(AuthorizationContext authorizationContext,
            SessionManager sessionManager) {
        this.authorizationContext = authorizationContext;
        this.sessionManager = sessionManager;
    }

    @InvokerRequest(InvokerRequest.Type.AUTHENTICATOR)
    public RequestAuthenticator getAuthenticatedAccountId() {
        return new ExtensionRequestAuthenticator(authorizationContext, sessionManager);
    }

    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> customTab() {
        return Map.of("Documentation", "static/docs");
    }

}
