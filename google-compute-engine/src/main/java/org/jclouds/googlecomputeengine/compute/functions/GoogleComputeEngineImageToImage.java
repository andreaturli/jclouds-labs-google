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
package org.jclouds.googlecomputeengine.compute.functions;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.skip;
import static org.jclouds.compute.domain.Image.Status;

import java.util.List;

import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.googlecomputeengine.domain.Deprecated.State;
import org.jclouds.googlecomputeengine.domain.Image;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public final class GoogleComputeEngineImageToImage implements Function<Image, org.jclouds.compute.domain.Image> {
   @Override public org.jclouds.compute.domain.Image apply(Image image) {
      ImageBuilder builder = new ImageBuilder()
              .id(image.selfLink().toString())
              .providerId(image.id())
              .name(image.name())
              .providerId(image.id())
              .description(image.description())
              .status(Status.AVAILABLE)
              .uri(image.selfLink());

      if (image.deprecated() != null) {
         builder.userMetadata(ImmutableMap.of("deprecatedState", image.deprecated().state().name()));
         if (image.deprecated().state() == State.DELETED){
            builder.status(Status.DELETED);
         }
      }

      List<String> splits = Lists.newArrayList(image.name().split("-"));
      OperatingSystem.Builder osBuilder = defaultOperatingSystem(image);
      if (splits == null || splits.size() == 0 || splits.size() < 3) {
         return builder.operatingSystem(osBuilder.build()).build();
      }

      OsFamily family = OsFamily.fromValue(splits.get(0));
      if (family != OsFamily.UNRECOGNIZED) {
         osBuilder.family(family);
      }

      String version = on(".").join(limit(skip(splits, 1), splits.size() - 2));
      osBuilder.version(version);

      builder.version(getLast(splits));
      return builder.operatingSystem(osBuilder.build()).build();
   }

   private OperatingSystem.Builder defaultOperatingSystem(Image image) {
      return OperatingSystem.builder().family(OsFamily.LINUX).is64Bit(true).description(image.name());
   }
}
