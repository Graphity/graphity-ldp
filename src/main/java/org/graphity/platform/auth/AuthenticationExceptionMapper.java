/*
 * Copyright (C) 2012 Martynas Jusevičius <martynas@graphity.org>
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graphity.platform.auth;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException>
{

    @Override
    public Response toResponse(AuthenticationException ae)
    {
        if (ae.getRealm() != null)
            return Response.
                    status(Status.UNAUTHORIZED).
                    header("WWW-Authenticate", "Basic realm=\"" + ae.getRealm() + "\"").
                    type(MediaType.TEXT_PLAIN).
                    entity(ae.getMessage()).
                    build();
	else return Response.
                    status(Status.UNAUTHORIZED).
                    type(MediaType.TEXT_PLAIN).
                    entity(ae.getMessage()).
                    build();
    }
    
}