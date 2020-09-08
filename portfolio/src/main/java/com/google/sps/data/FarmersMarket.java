// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

/**
 * Class representing a farmers' market that we have information about.
 */
public final class FarmersMarket {
  // TODO(karenav): make this an AutoValue: https://github.com/google/auto/blob/master/value/userguide/index.md 
  private final String name;
  private final String website;
  private final double lat;
  private final double lng;

  public FarmersMarket(String name, String website, double latitude, double longtitude) {
    this.name = name;
    this.website = (website != "") ? website : "unknown" ;
    this.lat = latitude;
    this.lng = longtitude;
  } 
}
