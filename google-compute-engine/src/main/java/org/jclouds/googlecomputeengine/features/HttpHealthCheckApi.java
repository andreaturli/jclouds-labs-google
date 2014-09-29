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

import org.jclouds.Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404;
import org.jclouds.Fallbacks.EmptyPagedIterableOnNotFoundOr404;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.collect.PagedIterable;
import org.jclouds.googlecomputeengine.domain.HttpHealthCheck;
import org.jclouds.googlecomputeengine.domain.ListPage;
import org.jclouds.googlecomputeengine.domain.Operation;
import org.jclouds.googlecomputeengine.functions.internal.ParseHttpHealthChecks;
import org.jclouds.googlecomputeengine.options.ListOptions;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.oauth.v2.config.OAuthScopes;
import org.jclouds.oauth.v2.filters.OAuthAuthenticator;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.PATCH;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.annotations.SkipEncoding;
import org.jclouds.rest.annotations.Transform;
import org.jclouds.rest.binders.BindToJsonPayload;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_READONLY_SCOPE;
import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_SCOPE;

/**
 * Provides access to HttpHealthChecks via their REST API.
 *
 * @see <a href="https://developers.google.com/compute/docs/reference/latest/httpHealthChecks"/>
 */
@SkipEncoding({'/', '='})
@RequestFilters(OAuthAuthenticator.class)
public interface HttpHealthCheckApi {

   /**
    * Returns the specified HttpHealthCheck resource.
    *
    * @param httpHealthCheck the name of the HttpHealthCheck resource to return.
    * @return a HttpHealthCheck resource.
    */
   @Named("HttpHealthChecks:get")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks/{httpHealthCheck}")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   HttpHealthCheck get(@PathParam("httpHealthCheck") String httpHealthCheck);

   /**
    * Creates a HttpHealthCheck resource in the specified project and region using the data included in the request.
    *
    * @param httpHealthCheckName the name of the forwarding rule.
    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
    *         you, and look for the status field.
    */
   @Named("HttpHealthChecks:insert")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks")
   @OAuthScopes({COMPUTE_SCOPE})
   @MapBinder(BindToJsonPayload.class)
   Operation create(@PayloadParam("name") String httpHealthCheckName);

   /**
    * Creates a HttpHealthCheck resource in the specified project and region using the data included in the request.
    *
    * @param httpHealthCheckName the name of the forwarding rule.
    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
    *         you, and look for the status field.
    */
   @Named("HttpHealthChecks:insert")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks")
   @OAuthScopes({COMPUTE_SCOPE})
   @MapBinder(BindToJsonPayload.class)
   Operation create(@PayloadParam("name") String httpHealthCheckName, @PayloadParam("timeoutSec") int
           timeoutSec, @PayloadParam("unhealthyThreshold") int unhealthyThreshold);

   /**
    * Deletes the specified TargetPool resource.
    *
    * @param httpHealthCheck name of the persistent forwarding rule resource to delete.
    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
    *         you, and look for the status field.
    */
   @Named("HttpHealthChecks:delete")
   @DELETE
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks/{httpHealthCheck}")
   @OAuthScopes(COMPUTE_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   Operation delete(@PathParam("httpHealthCheck") String httpHealthCheck);

   /**
    * @see HttpHealthCheckApi#listAtMarker(String, org.jclouds.googlecomputeengine.options.ListOptions)
    */
   @Named("HttpHealthChecks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseHttpHealthChecks.class)
   @Fallback(EmptyIterableWithMarkerOnNotFoundOr404.class)
   ListPage<HttpHealthCheck> listFirstPage();

   /**
    * @see HttpHealthCheckApi#listAtMarker(String, org.jclouds.googlecomputeengine.options.ListOptions)
    */
   @Named("HttpHealthChecks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseHttpHealthChecks.class)
   @Fallback(EmptyIterableWithMarkerOnNotFoundOr404.class)
   ListPage<HttpHealthCheck> listAtMarker(@QueryParam("pageToken") @Nullable String marker);

   /**
    * Retrieves the listPage of http health check resources contained within the specified project and zone.
    * By default the listPage as a maximum size of 100, if no options are provided or ListOptions#getMaxResults() has
    * not been set.
    *
    * @param marker      marks the beginning of the next list page
    * @param listOptions listing options
    * @return a page of the listPage
    * @see org.jclouds.googlecomputeengine.options.ListOptions
    * @see org.jclouds.googlecomputeengine.domain.ListPage
    */
   @Named("HttpHealthChecks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseHttpHealthChecks.class)
   @Fallback(EmptyIterableWithMarkerOnNotFoundOr404.class)
   ListPage<HttpHealthCheck> listAtMarker(@QueryParam("pageToken") @Nullable String marker, ListOptions listOptions);

   /**
    * @return a Paged, Fluent Iterable that is able to fetch additional pages when required
    * @see org.jclouds.collect.PagedIterable
    * @see HttpHealthCheckApi#listAtMarker(String, org.jclouds.googlecomputeengine.options.ListOptions)
    */
   @Named("HttpHealthChecks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseHttpHealthChecks.class)
   @Transform(ParseHttpHealthChecks.ToPagedIterable.class)
   @Fallback(EmptyPagedIterableOnNotFoundOr404.class)
   PagedIterable<HttpHealthCheck> list();

   @Named("HttpHealthChecks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseHttpHealthChecks.class)
   @Transform(ParseHttpHealthChecks.ToPagedIterable.class)
   @Fallback(EmptyPagedIterableOnNotFoundOr404.class)
   PagedIterable<HttpHealthCheck> list(ListOptions options);

   /**
    * Changes target url for forwarding rule.
    *
    * @param httpHealthCheck the name of the HttpHealthCheck resource to update. .
    * @param httpHealthCheck The URL of the target resource to receive traffic from this forwarding rule.
    *               It must live in the same region as this forwarding rule.
    *
    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
    *         you, and look for the status field.
    */
   @Named("HttpHealthChecks:patch")
   @PATCH
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/global/httpHealthChecks/{httpHealthCheck}")
   @OAuthScopes(COMPUTE_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @MapBinder(BindToJsonPayload.class)
   @Nullable
   Operation patch(@PathParam("httpHealthCheck") String httpHealthCheck, @PayloadParam("httpHealthCheck")
   HttpHealthCheck httpHealthChecks);
}
