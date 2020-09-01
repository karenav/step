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

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

public final class FindMeetingQuery {
  /**
   * Finds all the time ranges in which the requested meeting can take place.
   * TODO(karenav): should we check input validity?
   *
   * @param events: the events that are already occuring.
   * @param request: the request for the new event that we want to create.
   * @return the possible time ranges for the request.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> busyTimeRanges = getBusyTimeRanges(events, request);
    Collections.sort(busyTimeRanges, TimeRange.ORDER_BY_START);
    Collection<TimeRange> freeTimeBlocks = new ArrayList<>();
    int nextFreeStartTime = TimeRange.START_OF_DAY;
    for (TimeRange currBusyTime : busyTimeRanges) {
      int currBusyStartTime = currBusyTime.start();
      if (currBusyStartTime - nextFreeStartTime >= request.getDuration()) {
        freeTimeBlocks.add(TimeRange.fromStartEnd(nextFreeStartTime, currBusyStartTime, false));
      }
      nextFreeStartTime = Math.max(currBusyTime.end(), nextFreeStartTime);
    }
    if (TimeRange.END_OF_DAY - nextFreeStartTime >= request.getDuration()) {
      freeTimeBlocks.add(TimeRange.fromStartEnd(nextFreeStartTime, TimeRange.END_OF_DAY, true));
    }
    return freeTimeBlocks;
  }

  /**
   * Finds all the time ranges in which people who attend the request are already busy.
   * @param events: the events that are already occuring.
   * @param request: the request for the new event that we want to create.
   * @return the time ranges in which the request attendees are busy.
   */
  private List<TimeRange> getBusyTimeRanges(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> busyTimeRanges = new ArrayList<>();
    for (Event event : events) {
      for (String person : event.getAttendees()) {
        if ((person != null) && request.getAttendees().contains(person)) {
          busyTimeRanges.add(event.getWhen());
          break; // Meeting time was added to busyTimeRanges, so no need to check about other attendees
        }
      }
    }
    return busyTimeRanges;
  }
}
