/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.biu.modulebase.binfenjiari.widget.wheeltime;

import android.content.Context;

import com.biu.modulebase.binfenjiari.model.CityVO;
import com.biu.modulebase.binfenjiari.model.SchoolVO;

import java.util.ArrayList;

/**
 * The simple Array wheel adapter
 * 
 * @param <T>
 *            the element type
 */
public class ArrayListWheelAdapter<T> implements WheelAdapter {

    /** The default items length */
    public static final int DEFAULT_LENGTH = -1;

    // items
    private ArrayList<T> items;
    // length
    private int length;

    /**
     * Constructor
     * 
     * @param context
     *            the current context
     * @param items
     *            the items
     */
    public ArrayListWheelAdapter(Context context, ArrayList<T> items) {
	this.items = items;
    }

    @Override
    public String getItem(int index) {
	if (index >= 0 && index < items.size()) {
	    try {
		   CityVO item = (CityVO) items.get(index);
		   return item.getName();
	    } catch (Exception e) {
		   SchoolVO item = (SchoolVO) items.get(index);
		   return item.getName();
	    }
	   
	}
	return null;
    }

    @Override
    public int getItemsCount() {
	return items.size();
    }

    @Override
    public int getMaximumLength() {
	return length;
    }

}
