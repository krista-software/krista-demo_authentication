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

import app.krista.extension.authorization.MustAuthenticateException;
import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.common.ClientSessionUtil;
import app.krista.extension.request.ProtoRequest;
import app.krista.extension.request.ProtoResponse;
import app.krista.extension.request.protos.http.HttpRequest;
import app.krista.ksdk.authentication.SessionManager;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.model.field.NamedField;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class ExtensionRequestAuthenticator implements RequestAuthenticator {

    private final AuthorizationContext authorizationContext;
    private final SessionManager sessionManager;

    public ExtensionRequestAuthenticator(AuthorizationContext authorizationContext,
            SessionManager sessionManager) {
        this.authorizationContext = authorizationContext;
        this.sessionManager = sessionManager;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public Set<String> getSupportedProtocols() {
        return null;
    }

    @Override
    public String getAuthenticatedAccountId(ProtoRequest protoRequest) {
        try {
            HttpRequest httpRequest = (HttpRequest) protoRequest;
            httpRequest.bufferBody();
            String authenticatedAccountId = ClientSessionUtil.getAuthenticatedAccountId(sessionManager, httpRequest);
            if (authenticatedAccountId == null) {
                authenticatedAccountId = handleLoginRequest(httpRequest);
            }
            System.out.println("Get Authenticated AccountId: " + authenticatedAccountId);
            return authenticatedAccountId;
        } catch (IOException | ExecutionException | InterruptedException cause) {
            cause.printStackTrace();
            throw new IllegalStateException("Failed to authenticate." +
                    (cause.getMessage() == null || cause.getMessage().isBlank() ? "" : cause.getMessage()), cause);
        }

    }

    @Override
    public boolean setServiceAuthorization(String s) {
        return false;
    }

    @Override
    public Map<String, NamedField> getAttributeFields() {
        return null;
    }

    @Override
    public ProtoResponse getMustAuthenticateResponse(MustAuthenticateException cause, ProtoRequest request) {
        return null;
    }

    @Override
    public AuthorizationResponse getMustAuthenticateResponse(MustAuthenticateException cause) {
        return null;
    }

    @Override
    public ProtoResponse getMustAuthorizeResponse(MustAuthorizeException cause, ProtoRequest request) {
        return null;
    }

    @Override
    public AuthorizationResponse getMustAuthorizeResponse(MustAuthorizeException cause) {
        return null;
    }

    private String handleLoginRequest(ProtoRequest protoRequest) {
        if (!(protoRequest instanceof HttpRequest)) {
            return null;
        }
        String path = ((HttpRequest) protoRequest).getUri().getPath();
        System.out.println(path);
        if (Objects.equals("/login", path) || Objects.equals("/docs", path) || Objects.equals("/docs/", path)) {
            System.out.println("Authorization Context: " + authorizationContext.getAuthorizedAccount().getAccountId());
            return authorizationContext.getAuthorizedAccount().getAccountId();
        }
        return null;
    }

}
