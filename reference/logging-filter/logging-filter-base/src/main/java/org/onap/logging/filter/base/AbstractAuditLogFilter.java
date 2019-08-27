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

import javax.servlet.http.HttpServletRequest;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public abstract class AbstractAuditLogFilter<GenericRequest, GenericResponse> {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractAuditLogFilter.class);

    protected void pre(MDCSetup mdcSetup, SimpleMap headers, GenericRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            String requestId = mdcSetup.getRequestId(headers);
            MDC.put(ONAPLogConstants.MDCs.REQUEST_ID, requestId);
            mdcSetup.setInvocationId(headers);
            setServiceName(request);
            mdcSetup.setMDCPartnerName(headers);
            mdcSetup.setServerFQDN();
            mdcSetup.setClientIPAddress(httpServletRequest);
            mdcSetup.setInstanceID();
            mdcSetup.setEntryTimeStamp();
            MDC.put(ONAPLogConstants.MDCs.RESPONSE_STATUS_CODE, ONAPLogConstants.ResponseStatus.INPROGRESS.toString());
            additionalPreHandling(request);
            mdcSetup.setLogTimestamp();
            mdcSetup.setElapsedTime();
            logger.info(ONAPLogConstants.Markers.ENTRY, "Entering");
        } catch (Exception e) {
            logger.warn("Error in AbstractInboundFilter pre", e);
        }
    }

    protected void post(MDCSetup mdcSetup, GenericResponse response) {
        try {
            int responseCode = getResponseCode(response);
            mdcSetup.setResponseStatusCode(responseCode);
            MDC.put(ONAPLogConstants.MDCs.RESPONSE_CODE, String.valueOf(responseCode));
            mdcSetup.setResponseDescription(responseCode);
            mdcSetup.setLogTimestamp();
            mdcSetup.setElapsedTime();
            logger.info(ONAPLogConstants.Markers.EXIT, "Exiting.");
            additionalPostHandling(response);
        } catch (Exception e) {
            logger.warn("Error in AbstractInboundFilter post", e);
        } finally {
            MDC.clear();
        }
    }

    protected abstract int getResponseCode(GenericResponse response);

    protected abstract void setServiceName(GenericRequest request);

    protected void additionalPreHandling(GenericRequest request) {
        // override to add additional pre handling
    }

    protected void additionalPostHandling(GenericResponse response) {
        // override to add additional post handling
    }

}
