/*************************************************************************
* Copyright (c) 2015 Lemberg Solutions
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**************************************************************************/

package com.xlu.uis.maps.interfaces;


import com.xlu.bases.model.MapObject;

public interface Layer
{
	/**
	 * Adds map object to the layer.
	 * @param mapObject - map object.
	 */
    void addMapObject(MapObject mapObject);
	
	/**
	 * Removes map object from the layer.
	 * @param id - id of the map object.
	 */
    void removeMapObject(Object id);
	
	/**
	 * Returns map object.
	 * @param id - id of the map object.
	 */
    MapObject getMapObject(Object id);
	

	/**
	 * Returns map object by index
	 * @param index
	 * @return instance of MapObject
	 */
    MapObject getMapObjectByIndex(int index);
	
	/**
	 * Returns the count of map objects on the layer
	 * @return number of map objects
	 */
    int getMapObjectCount();
	
	/**
	 * Removes all map objects from the layer.
	 */
    void clearAll();
	
	/**
	 * Shows whether the layer is visible or not.
	 * @return - true if layer is visible, false otherwise.
	 */
    boolean isVisible();

	/**
	 * Sets layer visibility. 
	 * @param visible - true if layer should be visible, false otherwise.
	 */
    void setVisible(boolean visible);
}
