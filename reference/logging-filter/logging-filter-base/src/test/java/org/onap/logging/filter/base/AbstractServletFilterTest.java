/*-
 * ============LICENSE_START=======================================================
 * ONAP - Logging
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.logging.filter.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class AbstractServletFilterTest extends AbstractServletFilter {
    @Test
    public void getBasicAuthUser() {
        String value = "Basic dXNlcjpwYXNz"; // decodes to user:pass
        String userName = getBasicAuthUserName(value);
        assertEquals("user", userName);
    }

    @Test
    public void getInvalidcAuthUser() {
        String value = "Basic user:pass"; // not encoded, will cause an exception
        String userName = getBasicAuthUserName(value);
        assertNull(userName);
    }

}