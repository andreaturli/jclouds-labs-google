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

import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_READONLY_SCOPE;
import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_SCOPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import javax.ws.rs.core.MediaType;

import org.jclouds.googlecomputeengine.internal.BaseGoogleComputeEngineApiExpectTest;
import org.jclouds.googlecomputeengine.parse.ParseHttpHealthCheckListTest;
import org.jclouds.googlecomputeengine.parse.ParseHttpHealthCheckTest;
import org.jclouds.googlecomputeengine.parse.ParseRegionOperationTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

@Test(groups = "unit")
public class HttpHealthCheckApiExpectTest extends BaseGoogleComputeEngineApiExpectTest {

   public void testGetHttpHealthCheckResponseIs2xx() throws Exception {
      HttpRequest get = HttpRequest
              .builder()
              .method("GET")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks/http-health-check-api-live-test")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/httphealthcheck_get.json")).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, get, operationResponse).getHttpHealthCheckApi("myproject");

      assertEquals(api.get("http-health-check-api-live-test"),
              new ParseHttpHealthCheckTest().expected());
   }

   public void testGetHttpHealthCheckResponseIs4xx() throws Exception {
      HttpRequest get = HttpRequest
              .builder()
              .method("GET")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks/http-health-check-test")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(404).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, get, operationResponse).getHttpHealthCheckApi("myproject");

      assertNull(api.get("http-health-check-test"));
   }

   public void testInsertHttpHealthCheckResponseIs2xx() {
      HttpRequest insert = HttpRequest
              .builder()
              .method("POST")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN)
              .payload(payloadFromResourceWithContentType("/httphealthcheck_insert.json", MediaType.APPLICATION_JSON))
              .build();

      HttpResponse insertHttpHealthCheckResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/global_operation.json")).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_SCOPE),
              TOKEN_RESPONSE, insert,
              insertHttpHealthCheckResponse).getHttpHealthCheckApi("myproject");
      assertEquals(api.insert("http-health-check", 0, 0), new ParseRegionOperationTest().expected());
   }

   public void testInsertHttpHealthCheckResponseIs4xx() {
      HttpRequest create = HttpRequest
              .builder()
              .method("POST")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN)
              .payload(payloadFromResourceWithContentType("/httphealthcheck_insert.json", MediaType.APPLICATION_JSON))
              .build();

      HttpResponse insertHttpHealthCheckResponse = HttpResponse.builder().statusCode(404).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_SCOPE),
              TOKEN_RESPONSE, create, insertHttpHealthCheckResponse).getHttpHealthCheckApi("myproject");

      assertNull(api.insert("http-health-check", 0, 0));
   }

   public void testDeleteHttpHealthCheckResponseIs2xx() {
      HttpRequest delete = HttpRequest
              .builder()
              .method("DELETE")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks/http-health-check")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/global_operation.json")).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_SCOPE),
              TOKEN_RESPONSE, delete, deleteResponse).getHttpHealthCheckApi("myproject");

      assertEquals(api.delete("http-health-check"),
              new ParseRegionOperationTest().expected());
   }

   public void testDeleteHttpHealthCheckResponseIs4xx() {
      HttpRequest delete = HttpRequest
              .builder()
              .method("DELETE")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks/http-health-check")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(404).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_SCOPE),
              TOKEN_RESPONSE, delete, deleteResponse).getHttpHealthCheckApi("myproject");

      assertNull(api.delete("http-health-check"));
   }

   public void testListHttpHealthChecksResponseIs2xx() {
      HttpRequest list = HttpRequest
              .builder()
              .method("GET")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/httphealthcheck_list.json")).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, list, operationResponse).getHttpHealthCheckApi("myproject");

      assertEquals(api.list().toString(),
              new ParseHttpHealthCheckListTest().expected().toString());
   }

   public void testListHttpHealthChecksResponseIs4xx() {
      HttpRequest list = HttpRequest
              .builder()
              .method("GET")
              .endpoint("https://www.googleapis.com/compute/v1/projects/myproject/global/httpHealthChecks")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(404).build();

      HttpHealthCheckApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, list, operationResponse).getHttpHealthCheckApi("myproject");

      assertTrue(api.list().concat().isEmpty());
   }

}
