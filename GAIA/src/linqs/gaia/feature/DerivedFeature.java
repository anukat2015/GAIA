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
package linqs.gaia.feature;

import linqs.gaia.configurable.Configurable;
import linqs.gaia.feature.decorable.Decorable;
import linqs.gaia.feature.values.FeatureValue;

/**
 * Interface {@link Feature} for all features where the value
 * is computed from some source or set of current values rather
 * than being explicitly specified by the user e.g. aggregate features.
 *  
 * @author namatag
 *
 */
public interface DerivedFeature extends Feature, Configurable {
	/**
	 * Specify if the feature is caching
	 * 
	 * @return True if caching.  False otherwise.
	 */
	boolean isCaching();
	
	/**
	 * Set whether or not feature should cache
	 * 
	 * @param shouldCache True if the feature should cache, false otherwise.
	 */
	void setCache(boolean shouldCache);
	
	/**
	 * Remove all cached values
	 */
	void resetCache();
	
	/**
	 * Clear cached value for the given item
	 * 
	 * @param d Decorable item
	 */
	void resetCache(Decorable d);
	
	/**
	 * Return the feature value for the Decorable item 
	 * 
	 * @param di Decorable Item
	 * @return Feature value for item
	 */
	FeatureValue getFeatureValue(Decorable di);
}
