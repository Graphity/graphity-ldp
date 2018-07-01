/*
 * Copyright 2015 Martynas Jusevičius <martynas@atomgraph.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atomgraph.core.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import com.atomgraph.core.exception.ClientException;

/**
 * Maps client exception to response.
 * Needs to be registered in the JAX-RS application.
 * 
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
public class ClientExceptionMapper implements ExceptionMapper<ClientException>
{

    @Override
    public Response toResponse(ClientException exception)
    {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    
}
