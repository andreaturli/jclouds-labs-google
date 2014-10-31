/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.googlecomputeengine.features;

import org.jclouds.collect.IterableWithMarker;
import org.jclouds.googlecomputeengine.domain.HttpHealthCheck;
import org.jclouds.googlecomputeengine.internal.BaseGoogleComputeEngineApiLiveTest;
import org.jclouds.googlecomputeengine.options.HttpHealthCheckCreationOptions;
import org.jclouds.googlecomputeengine.options.ListOptions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static com.google.common.base.Optional.fromNullable;

public class HttpHealthCheckApiLiveTest extends BaseGoogleComputeEngineApiLiveTest {

   private static final String HTTP_HEALTH_CHECK_NAME = "http-health-check-api-live-test";
   private static final int TIME_WAIT = 60;
   private static final int HEALTHY_THRESHOLD = 30;
   private static final int UNHEALTHY_THRESHOLD = 15;
   private static final int PORT = 56;
   private static final String DESCRIPTION = "A First Health Check!";
   private static final int TIMEOUT_SEC = 40;
   private static final int CHECK_INTERVAL_SEC = 40;

   private static final int OFFSET = 2;

   private HttpHealthCheckApi api() {
      return api.getHttpHealthCheckApi(userProject.get());
   }

   @Test(groups = "live")
   public void testInsertHttpHealthCheck() {
      HttpHealthCheckCreationOptions options = new HttpHealthCheckCreationOptions()
                                                         .port(PORT)
                                                         .checkIntervalSec(CHECK_INTERVAL_SEC)
                                                         .timeoutSec(TIMEOUT_SEC)
                                                         .healthyThreshold(HEALTHY_THRESHOLD)
                                                         .unhealthyThreshold(UNHEALTHY_THRESHOLD)
                                                         .description(DESCRIPTION);
      assertGlobalOperationDoneSucessfully(api().insert(HTTP_HEALTH_CHECK_NAME, options), TIME_WAIT);
   }

   @Test(groups = "live", dependsOnMethods = "testInsertHttpHealthCheck")
   public void testGetHttpHealthCheck() {
      HttpHealthCheck httpHealthCheck = api().get(HTTP_HEALTH_CHECK_NAME);
      assertNotNull(httpHealthCheck);
      assertEquals(httpHealthCheck.getName(), HTTP_HEALTH_CHECK_NAME);
      assertEquals(httpHealthCheck.getPort(), fromNullable(PORT));
      assertEquals(httpHealthCheck.getCheckIntervalSec(), fromNullable(CHECK_INTERVAL_SEC));
      assertEquals(httpHealthCheck.getTimeoutSec(), fromNullable(TIMEOUT_SEC));
      assertEquals(httpHealthCheck.getHealthyThreshold(), fromNullable(HEALTHY_THRESHOLD));
      assertEquals(httpHealthCheck.getUnhealthyThreshold(), fromNullable(UNHEALTHY_THRESHOLD));
      assertEquals(httpHealthCheck.getDescription(), fromNullable(DESCRIPTION));
   }

   @Test(groups = "live", dependsOnMethods = "testInsertHttpHealthCheck")
   public void testListHttpHealthCheck() {
      IterableWithMarker<HttpHealthCheck> httpHealthCheck = api().list(new ListOptions.Builder()
              .filter("name eq " + HTTP_HEALTH_CHECK_NAME));
      assertEquals(httpHealthCheck.toList().size(), 1);
   }

   @Test(groups = "live", dependsOnMethods = "testGetHttpHealthCheck")
   public void testPatchHttpHealthCheck() {
      HttpHealthCheckCreationOptions options = new HttpHealthCheckCreationOptions()
         .port(PORT + OFFSET)
         .checkIntervalSec(CHECK_INTERVAL_SEC + OFFSET)
         .timeoutSec(TIMEOUT_SEC + OFFSET);
      assertGlobalOperationDoneSucessfully(api().patch(HTTP_HEALTH_CHECK_NAME, options), TIME_WAIT);

      // Check changes happened and others unchanged.
      HttpHealthCheck httpHealthCheck = api().get(HTTP_HEALTH_CHECK_NAME);
      assertNotNull(httpHealthCheck);
      assertEquals(httpHealthCheck.getName(), HTTP_HEALTH_CHECK_NAME);
      assertEquals(httpHealthCheck.getPort(), fromNullable(PORT + OFFSET));
      assertEquals(httpHealthCheck.getCheckIntervalSec(), fromNullable(CHECK_INTERVAL_SEC + OFFSET));
      assertEquals(httpHealthCheck.getTimeoutSec(), fromNullable(TIMEOUT_SEC + OFFSET));
      assertEquals(httpHealthCheck.getHealthyThreshold(), fromNullable(HEALTHY_THRESHOLD));
      assertEquals(httpHealthCheck.getUnhealthyThreshold(), fromNullable(UNHEALTHY_THRESHOLD));
      assertEquals(httpHealthCheck.getDescription(), fromNullable(DESCRIPTION));
   }

   @Test(groups = "live", dependsOnMethods = "testPatchHttpHealthCheck")
   public void testUpdateHttpHealthCheck() {
      HttpHealthCheckCreationOptions options = new HttpHealthCheckCreationOptions()
         .checkIntervalSec(CHECK_INTERVAL_SEC - OFFSET)
         .timeoutSec(TIMEOUT_SEC - OFFSET);
      assertGlobalOperationDoneSucessfully(api().update(HTTP_HEALTH_CHECK_NAME, options), TIME_WAIT);

      // Check changes happened.
      HttpHealthCheck httpHealthCheck = api().get(HTTP_HEALTH_CHECK_NAME);
      assertNotNull(httpHealthCheck);
      assertEquals(httpHealthCheck.getName(), HTTP_HEALTH_CHECK_NAME);
      assertEquals(httpHealthCheck.getCheckIntervalSec(), fromNullable(CHECK_INTERVAL_SEC - OFFSET));
      assertEquals(httpHealthCheck.getTimeoutSec(), fromNullable(TIMEOUT_SEC - OFFSET));
      // Update overwrites unspecified parameters to their defaults.
      assertNotEquals(httpHealthCheck.getHealthyThreshold(), fromNullable(HEALTHY_THRESHOLD));
      assertNotEquals(httpHealthCheck.getUnhealthyThreshold(), fromNullable(UNHEALTHY_THRESHOLD));
      assertNotEquals(httpHealthCheck.getDescription(), fromNullable(DESCRIPTION));
   }

   @Test(groups = "live", dependsOnMethods = {"testListHttpHealthCheck", "testUpdateHttpHealthCheck"})
   public void testDeleteHttpHealthCheck() {
      assertGlobalOperationDoneSucessfully(api().delete(HTTP_HEALTH_CHECK_NAME), TIME_WAIT);
   }
}
