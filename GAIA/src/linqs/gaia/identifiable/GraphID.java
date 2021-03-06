/*
* This file is part of the GAIA software.
* Copyright 2011 University of Maryland
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package linqs.gaia.identifiable;

import linqs.gaia.exception.StringFormatException;
import linqs.gaia.global.Global;
import linqs.gaia.graph.registry.GraphRegistry;

/**
 * Unique identifier for graphs
 * <p>
 * Graphs are unique given a schema id and an object id.
 * 
 * @author namatag
 *
 */
public class GraphID extends ID {
	private static final long serialVersionUID = 1L;

	public GraphID(String schemaid, String objid) {
		super(schemaid, objid);
	}
	
	/**
	 * Output id in the form:<br>
	 * &lt;graph schema id&gt;.&lt;graph object id&gt;
	 * <br>
	 * i.e., SocialNetwork.Facebook.Person.BobSmith
	 */
	public String toString() {
		return this.schemaid+"."+this.objid;
	}
	
	/**
	 * Parse a graph from a string in the same
	 * format as that output by toString.
	 * 
	 * @param s String to parse
	 * @return Graph ID
	 */
	public static GraphID parseGraphID(String s) {
		String[] parts = s.split("\\.");
		
		if(parts.length!=2) {
			throw new StringFormatException("Unable to parse: "+s);
		}
		
		return new GraphID(parts[0], parts[1]);
	}
	
	/**
	 * Generate a graph id for the given schema.
	 * The ID is guaranteed not to conflict with another graph
	 * already in the {@link GraphRegistry}.
	 
	 * @param schemaid Schema ID
	 * @return GraphID
	 */
	public static GraphID generateGraphID(String schemaid) {
		return GraphID.generateGraphID(schemaid, "");
	}
	
	/**
	 * Generate a graph id for the given schema.
	 * The ID is guaranteed not to conflict with another graph
	 * already in the {@link GraphRegistry}.
	 * 
	 * @param schemaid Schema ID
	 * @param objidprefix Prefix to add to the object ID
	 * @return GraphID
	 */
	public static GraphID generateGraphID(String schemaid, String objidprefix) {
		GraphID newid = null;
		do {
			newid = new GraphID(schemaid, objidprefix+Global.requestGlobalCounterValue());
		} while(GraphRegistry.isRegistered(newid));
		
		return newid;
	}
}
