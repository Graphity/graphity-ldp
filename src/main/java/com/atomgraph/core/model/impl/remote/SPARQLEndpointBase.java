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

package com.atomgraph.core.model.impl.remote;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import com.atomgraph.core.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atomgraph.core.client.SPARQLClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Proxy implementation of SPARQL endpoint.
 * This class forwards requests to a remote origin.
 * 
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
// @Path("/sparql")
public class SPARQLEndpointBase extends com.atomgraph.core.model.impl.SPARQLEndpointBase implements com.atomgraph.core.model.remote.SPARQLEndpoint
{
    private static final Logger log = LoggerFactory.getLogger(SPARQLEndpointBase.class);

    private final String uri;
    private final SPARQLClient sparqlClient;

    /**
     * Constructs SPARQL endpoint proxy from request metadata and origin URI.
     * 
     * @param client HTTP client
     * @param mediaTypes supported media types
     * @param maxGetRequestSize max GET URL size in bytes
     * @param uri endpoint URI
     * @param request request
     * @param authUser HTTP Basic username
     * @param authPwd HTTP Basic password
     */
    public SPARQLEndpointBase(@Context Client client, @Context MediaTypes mediaTypes, Integer maxGetRequestSize, String uri, String authUser, String authPwd, @Context Request request)
    {
        super(request, mediaTypes);
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        if (uri == null) throw new IllegalArgumentException("URI string cannot be null");
        this.uri = uri;
        
        if (maxGetRequestSize != null)
            this.sparqlClient = SPARQLClient.create(client.resource(uri), mediaTypes, maxGetRequestSize);
        else
            this.sparqlClient = SPARQLClient.create(client.resource(uri), mediaTypes);
        
        if (authUser != null && authPwd != null)
        {
            ClientFilter authFilter = new HTTPBasicAuthFilter(authUser, authPwd);
            this.sparqlClient.getWebResource().addFilter(authFilter);
        }
    }
    
    @Override
    public Model loadModel(Query query, List<URI> defaultGraphUris, List<URI> namedGraphUris)
    {
        if (defaultGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");
        if (namedGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");

        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        
        for (URI defaultGraphUri : defaultGraphUris)
            params.add(DEFAULT_GRAPH_URI, defaultGraphUri.toString());
        for (URI namedGraphUri : namedGraphUris)
            params.add(NAMED_GRAPH_URI, namedGraphUri.toString());

        return getSPARQLClient().loadModel(query, params, null);
    }

    @Override
    public ResultSetRewindable select(Query query, List<URI> defaultGraphUris, List<URI> namedGraphUris)
    {
        if (defaultGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");
        if (namedGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");
        
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        
        for (URI defaultGraphUri : defaultGraphUris)
            params.add(DEFAULT_GRAPH_URI, defaultGraphUri.toString());
        for (URI namedGraphUri : namedGraphUris)
            params.add(NAMED_GRAPH_URI, namedGraphUri.toString());
        
        return getSPARQLClient().select(query, params, null);
    }
  
    @Override
    public boolean ask(Query query, List<URI> defaultGraphUris, List<URI> namedGraphUris)
    {
        if (defaultGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");
        if (namedGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");
        
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        
        for (URI defaultGraphUri : defaultGraphUris)
            params.add(DEFAULT_GRAPH_URI, defaultGraphUri.toString());
        for (URI namedGraphUri : namedGraphUris)
            params.add(NAMED_GRAPH_URI, namedGraphUri.toString());
        
        return getSPARQLClient().ask(query, params, null);
    }

    @Override
    public void update(UpdateRequest updateRequest, List<URI> usingGraphUris, List<URI> usingNamedGraphUris)
    {
        if (usingGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");
        if (usingNamedGraphUris == null) throw new IllegalArgumentException("List<URI> cannot be null");
        
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        
        for (URI usingGraphUri : usingGraphUris)
            params.add(USING_GRAPH_URI, usingGraphUri.toString());
        for (URI usingNamedGraphUri : usingNamedGraphUris)
            params.add(USING_NAMED_GRAPH_URI, usingNamedGraphUri.toString());

        getSPARQLClient().update(updateRequest, params, null);
    }
        
    @Override
    public String getURI()
    {
        return uri;
    }
    
    @Override
    public SPARQLClient getSPARQLClient()
    {
        return sparqlClient;
    }

}