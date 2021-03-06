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
package linqs.gaia.graph.datagraph;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import linqs.gaia.graph.SystemDataManager;
import linqs.gaia.identifiable.ID;

/**
 * System data manager for Data Graph.
 * Of note is that in this implementation, we assume that
 * for in most cases, there will be more ID objects
 * than keys.  We take advantage of this by storing the ID
 * dependent data inside maps first keyed by the key.
 * This reduces the number of maps we have to initialize
 * (which can be expensive in terms of memory) to that of the number of keys.
 * 
 * @author namatag
 *
 */
public class DGSystemDataManager implements SystemDataManager {
	Map<String, String> sdmanager = new ConcurrentHashMap<String,String>();
	Map<String, Map<ID,String>> dec_sdmanager =
		new ConcurrentHashMap<String,Map<ID,String>>();
	
	public void removeAllSystemData() {
		sdmanager.clear();
		dec_sdmanager.clear();
	}

	public void removeSystemData(ID id) {
		Set<Entry<String, Map<ID,String>>> entries = dec_sdmanager.entrySet();
		for(Entry<String, Map<ID,String>> e: entries) {
			Map<ID,String> sdmap = e.getValue();
			if(sdmap.containsKey(id)) {
				sdmap.remove(id);
			}
		}
	}

	public String getSystemData(String key) {
		return sdmanager.get(key);
	}

	public String getSystemData(ID id, String key) {
		if(!this.dec_sdmanager.containsKey(key)) {
			return null;
		}
		
		return this.dec_sdmanager.get(key).get(id);
	}

	public synchronized void setSystemData(String key, String value) {
		this.sdmanager.put(key, value);
	}

	public synchronized void setSystemData(ID id, String key, String value) {
		if(!this.dec_sdmanager.containsKey(key)) {
			this.dec_sdmanager.put(key, new ConcurrentHashMap<ID,String>());
		}
		
		this.dec_sdmanager.get(key).put(id, value);
	}

	public void removeSystemData(String key) {
		this.sdmanager.remove(key);
	}

	public void removeSystemData(ID id, String key) {
		if(this.dec_sdmanager.containsKey(key)) {
			this.dec_sdmanager.get(key).remove(id);
		}
	}
}
