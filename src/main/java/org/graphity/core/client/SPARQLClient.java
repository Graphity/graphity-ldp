/*
 * Copyright 2015 Martynas Jusevičius <martynas@graphity.org>.
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

package org.graphity.core.client;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.update.UpdateRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;
import org.graphity.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
public class SPARQLClient
{

    private static final Logger log = LoggerFactory.getLogger(SPARQLClient.class);

    private final WebResource webResource;

    protected SPARQLClient(WebResource webResource)
    {
        this.webResource = webResource;
    }
    
    public WebResource getWebResource()
    {
        return webResource;
    }

    public static SPARQLClient create(WebResource webResource)
    {
        return new SPARQLClient(webResource);
    }

    /**
     * Loads RDF model from a remote SPARQL endpoint using a query and optional request parameters.
     * Only <code>DESCRIBE</code> and <code>CONSTRUCT</code> queries can be used with this method.
     * 
     * @param query query object
     * @param acceptedTypes accepted media types
     * @param params name/value pairs of request parameters or null, if none
     * @return result RDF model
     * @see <a href="http://www.w3.org/TR/2013/REC-sparql11-query-20130321/#describe">DESCRIBE</a>
     * @see <a href="http://www.w3.org/TR/2013/REC-sparql11-query-20130321/#construct">CONSTRUCT</a>
     */
    public ClientResponse query(Query query, javax.ws.rs.core.MediaType[] acceptedTypes, MultivaluedMap<String, String> params)
    {
	if (log.isDebugEnabled()) log.debug("Remote service {} Query: {}", getWebResource().getURI(), query);
	if (query == null) throw new IllegalArgumentException("Query must be not null");
	if (acceptedTypes == null) throw new IllegalArgumentException("Accepted MediaType[] must be not null");

        MultivaluedMap formData = new MultivaluedMapImpl();
        if (params != null) formData.putAll(params);
        formData.putSingle("query", query.toString());
        
        return getWebResource().accept(acceptedTypes).
            type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).
            post(ClientResponse.class, formData);
    }
    
    /**
     * Loads RDF model from a remote SPARQL endpoint using a query and optional request parameters.
     * Only <code>DESCRIBE</code> and <code>CONSTRUCT</code> queries can be used with this method.
     * This is a convenience method for {@link #loadModel(String,Query,MultivaluedMap<String, String>)}
     * with null request parameters.
     * 
     * @param query query object
     * @param acceptedTypes accepted media types
     * @return RDF model result
     */
    public ClientResponse query(Query query, javax.ws.rs.core.MediaType[] acceptedTypes)
    {
	return query(query, acceptedTypes, null);
    }
    
    /**
     * Executes update request on a remote SPARQL endpoint.
     * 
     * @param updateRequest update request
     * @param params name/value pairs of request parameters or null, if none
     * @return client response
     */
    public ClientResponse update(UpdateRequest updateRequest, MultivaluedMap<String, String> params)
    {
	if (log.isDebugEnabled()) log.debug("Remote service {} Query: {} ", getWebResource().getURI(), updateRequest);
	if (updateRequest == null) throw new IllegalArgumentException("UpdateRequest must be not null");
	//if (acceptedTypes == null) throw new IllegalArgumentException("Accepted MediaType[] must be not null");

        MultivaluedMap formData = new MultivaluedMapImpl();
        if (params != null) formData.putAll(params);
        formData.putSingle("update", updateRequest.toString());
        
	return getWebResource().
            //accept(acceptedTypes).
            type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).
            post(ClientResponse.class, formData);
    }
    
}