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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import app.krista.extension.common.KristaError;

public class KristaExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        String message = exception.getMessage() == null ? "Authentication failure." : exception.getMessage();
        exception.printStackTrace();
        int statusCode = 500;
        if (exception instanceof WebApplicationException) {
            statusCode = ((WebApplicationException) exception).getResponse().getStatus();
        }
        return Response.status(statusCode).entity(new KristaError(message, "Demo Auth - 500")).build();
    }

}
