/**
 *  Copyright 2012 Martynas Jusevičius <martynas@atomgraph.com>
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
package com.atomgraph.core.model.impl;

import org.apache.jena.rdf.model.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;
import com.atomgraph.core.MediaTypes;
import com.atomgraph.core.model.Resource;
import javax.ws.rs.core.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class of generic read-only Linked Data resources.
 * 
 * @see <a href="http://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/rdf/model/Resource.html">Jena Resource</a>
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
public abstract class ResourceBase implements Resource
{
    private static final Logger log = LoggerFactory.getLogger(ResourceBase.class);

    private final com.atomgraph.core.Application system;
    private final com.atomgraph.core.model.Application application;    
    private final UriInfo uriInfo;
    private final Request request;
    private final URI uri;
    private final com.atomgraph.core.model.impl.Response response;

    /** 
     * JAX-RS-compatible resource constructor with injected request metadata.
     * The URI of the resource being created is the absolute path of the current request URI.
     * 
     * @param application application
     * @param uriInfo URI information of the request
     * @param request current request object
     * @see <a href="http://docs.oracle.com/javaee/6/api/javax/ws/rs/core/UriInfo.html#getAbsolutePath()">JAX-RS UriInfo.getAbsolutePath()</a>
     */
    public ResourceBase(@Context Application application, @Context UriInfo uriInfo, @Context Request request)
    {
        this((com.atomgraph.core.Application)application, (com.atomgraph.core.model.Application)application, 
                uriInfo, request, uriInfo.getAbsolutePath());
    }
    
    protected ResourceBase(final com.atomgraph.core.Application system, final com.atomgraph.core.model.Application application,
            final UriInfo uriInfo, final Request request, URI uri)
    {
        if (system == null) throw new IllegalArgumentException("Application cannot be null");
        if (application == null) throw new IllegalArgumentException("Application cannot be null");
        if (uriInfo == null) throw new IllegalArgumentException("UriInfo cannot be null");
        if (request == null) throw new IllegalArgumentException("Request cannot be null");
        if (uri == null) throw new IllegalArgumentException("URI cannot be null");

        this.system = system;
        this.application = application;
        this.uriInfo = uriInfo;
        this.request = request;
        this.uri = uri;
        this.response = com.atomgraph.core.model.impl.Response.fromRequest(request);
        if (log.isDebugEnabled()) log.debug("Request URI: {}", uriInfo.getRequestUri());        
    }
    
    /**
     * Returns response for the given RDF model.
     * 
     * @param model RDF model
     * @return response object
     */
    public Response getResponse(Model model)
    {
        return getResponseBuilder(model).build();
    }

    /**
     * Returns response builder for the given RDF model.
     * 
     * @param model RDF model
     * @return response builder
     */
    public ResponseBuilder getResponseBuilder(Model model)
    {
        return com.atomgraph.core.model.impl.Response.fromRequest(getRequest()).
                getResponseBuilder(model, getVariants(getWritableMediaTypes())).
                cacheControl(getCacheControl());
    }
    
    /**
     * Builds a list of acceptable response variants
     * 
     * @param mediaTypes
     * @return supported variants
     */
    public List<Variant> getVariants(List<MediaType> mediaTypes)
    {
        return getResponse().getVariantListBuilder(mediaTypes, getLanguages(), getEncodings()).add().build();
    }
        
    public MediaTypes getMediaTypes()
    {
        return getSystem().getMediaTypes();
    }

    public List<javax.ws.rs.core.MediaType> getWritableMediaTypes()
    {
        return getMediaTypes().getWritable(Model.class);
    }
    
    public List<Locale> getLanguages()
    {
        return new ArrayList<>();
    }

    public List<String> getEncodings()
    {
        return new ArrayList<>();
    }

    /**
     * Returns URI of this resource
     * 
     * @return URI of this resource
     */
    @Override
    public final URI getURI()
    {
        return uri;
    }

    /**
     * Returns URI information.
     * 
     * @return URI info object
     */
    public UriInfo getUriInfo()
    {
        return uriInfo;
    }

    /**
     * Returns current request.
     * 
     * @return request object
     */
    public Request getRequest()
    {
        return request;
    }

    public com.atomgraph.core.model.impl.Response getResponse()
    {
        return response;
    }
        
    public com.atomgraph.core.Application getSystem()
    {
        return system;
    }
    
    public com.atomgraph.core.model.Application getApplication()
    {
        return application;
    }
    
    /**
     * Returns <pre>Cache-Control</pre> header value.
     * 
     * @return cache control object
     */
    public CacheControl getCacheControl()
    {
        return getSystem().getCacheControl();
    }
    
}