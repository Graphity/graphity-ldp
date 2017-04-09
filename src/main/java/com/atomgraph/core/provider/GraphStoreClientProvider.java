/*
 * Copyright 2016 Martynas Jusevičius <martynas@atomgraph.com>.
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
package com.atomgraph.core.provider;

import com.atomgraph.core.client.GraphStoreClient;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.PerRequestTypeInjectableProvider;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
@Provider
@Deprecated
public class GraphStoreClientProvider extends PerRequestTypeInjectableProvider<Context, GraphStoreClient> implements ContextResolver<GraphStoreClient>
{
    
    private static final Logger log = LoggerFactory.getLogger(GraphStoreClientProvider.class);

    private final GraphStoreClient graphStoreClient;
    
    public GraphStoreClientProvider(final GraphStoreClient graphStoreClient)
    {
        super(GraphStoreClient.class);
        this.graphStoreClient = graphStoreClient;
    }

    @Override
    public Injectable<GraphStoreClient> getInjectable(ComponentContext cc, Context context)
    {
	return new Injectable<GraphStoreClient>()
	{
	    @Override
	    public GraphStoreClient getValue()
	    {
		return getGraphStoreClient();
	    }
	};
    }
        
    @Override
    public GraphStoreClient getContext(Class<?> type)
    {
        return getGraphStoreClient();
    }

    public GraphStoreClient getGraphStoreClient()
    {
        return graphStoreClient;
    }
    
}
