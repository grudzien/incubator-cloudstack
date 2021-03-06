// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// the License.  You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.utils;

import javax.persistence.Table;

import org.apache.log4j.Logger;


public class AnnotationHelper extends Object {
	// This class contains routines to help query annotation elements of objects.

	public static final Logger s_logger = Logger.getLogger(AnnotationHelper.class.getName());

	public static String getTableName(Object proxyObj) {
		// The cglib class is generated by cglib during runtime.

		Class<?> curClass = proxyObj.getClass();
		if (curClass == null) {
            s_logger.trace("Could not retrieve class information for proxy object");
			return null;
		}

		while (curClass.getSuperclass() != null && curClass.getSuperclass().getName() != "java.lang.Object") {
			curClass = curClass.getSuperclass();
		}
		// At this point, curClass is the root base class of proxyObj's class, and curClass is not java.lang.Object.

		Table tabObj = curClass.getAnnotation(Table.class);

		if (tabObj == null) {
            s_logger.trace(curClass + "does not have a Table annotation");
			return null;
		}

		return tabObj.name();
	}

}




