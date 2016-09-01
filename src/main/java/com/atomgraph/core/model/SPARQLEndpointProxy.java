/**
 *  Copyright 2014 Martynas Jusevičius <martynas@atomgraph.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.atomgraph.core.model;

import javax.ws.rs.core.MediaType;
import com.atomgraph.core.client.SPARQLClient;

/**
 * A class representing a SPARQL endpoint proxy.
 * 
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
public interface SPARQLEndpointProxy extends Proxy
{

    SPARQLClient getClient();
    
    MediaType[] getReadableModelMediaTypes();

    MediaType[] getReadableResultSetMediaTypes();
    
}