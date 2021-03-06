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

package com.atomgraph.core.riot;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.LangBuilder;
import com.atomgraph.core.MediaType;

/**
 * Adds new RDF languages to Jena's registry.
 * 
 * @author Martynas Jusevičius {@literal <martynas@atomgraph.com>}
 * @see org.apache.jena.riot.RDFLanguages
 */
public class RDFLanguages extends org.apache.jena.riot.RDFLanguages
{

    public static final String strLangRDFPOST = "RDF/POST" ;
    
    public static final Lang RDFPOST = LangBuilder.create(strLangRDFPOST, MediaType.APPLICATION_RDF_URLENCODED).
            addFileExtensions("rpo").
            build();


}
