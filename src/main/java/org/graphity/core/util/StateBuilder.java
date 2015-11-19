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

package org.graphity.core.util;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.ResourceUtils;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
public class StateBuilder
{
    private final Resource resource;
    private final UriBuilder uriBuilder;
    
    private StateBuilder(UriBuilder uriBuilder, Model model)
    {
	if (uriBuilder == null) throw new IllegalArgumentException("UriBuilder cannot be null");
	if (model == null) throw new IllegalArgumentException("Model cannot be null");
        
        resource = model.createResource();
        this.uriBuilder = uriBuilder;
    }
    
    public static StateBuilder fromUri(URI uri, Model model)
    {
        return new StateBuilder(UriBuilder.fromUri(uri), model);
    }

    public static StateBuilder fromUri(String uri, Model model)
    {
        return new StateBuilder(UriBuilder.fromUri(uri), model);
    }
    
    protected Resource getResource()
    {
        return resource;
    }
    
    protected UriBuilder getUriBuilder()
    {
        return uriBuilder;
    }

    public StateBuilder property(Property property, RDFNode value)
    {
        if (property == null) throw new IllegalArgumentException("Property cannot be null");        
        if (value == null) throw new IllegalArgumentException("Object cannot be null");        

        getResource().addProperty(property, value);
        getUriBuilder().queryParam(property.getLocalName(), value);
        
        return this;
    }

    public StateBuilder replaceProperty(Property property, RDFNode value)
    {
        if (property == null) throw new IllegalArgumentException("Property cannot be null");        
        if (value == null) throw new IllegalArgumentException("Object cannot be null");        

        getResource().removeAll(property).addProperty(property, value);
        getUriBuilder().replaceQueryParam(property.getLocalName(), value);
        
        return this;
    }
    
    public StateBuilder literal(Property property, Object value)
    {
        if (property == null) throw new IllegalArgumentException("Property cannot be null");        
        if (value == null) throw new IllegalArgumentException("Object cannot be null");        

        getResource().addLiteral(property, value);
        getUriBuilder().queryParam(property.getLocalName(), value);
        
        return this;
    }

    public StateBuilder replaceLiteral(Property property, Object value)
    {
        if (property == null) throw new IllegalArgumentException("Property cannot be null");        
        if (value == null) throw new IllegalArgumentException("Object cannot be null");        

        getResource().removeAll(property).addLiteral(property, value);
        getUriBuilder().replaceQueryParam(property.getLocalName(), value);
        
        return this;
    }
    
    public Resource build()
    {
        return ResourceUtils.renameResource(getResource(), getUriBuilder().build().toString());
    }
    
}
