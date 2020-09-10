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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FindMeetingQuery {
  /**
   * Finds all the time ranges in which the requested meeting can take place. Tries to find a time
   * range that is suitable for both regular and optional attendees, and if this attempt fails,
   * finds a time range that it suitable for all regular attendees.
   * @param events: the events that are already taking place.
   * @param request: the request for the new event that we want to create.
   * @return the possible time ranges for the request.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long duration = request.getDuration();
    Collection<String> allAttendees =
        Stream.concat(request.getAttendees().stream(), request.getOptionalAttendees().stream())
            .collect(Collectors.toList());
    Collection<TimeRange> possibleRangesWithOptional = getMeetingRanges(events, allAttendees, duration);
    return (possibleRangesWithOptional.isEmpty())
        ? getMeetingRanges(events, request.getAttendees(), duration)
        : possibleRangesWithOptional;
  }

  /**
   * Finds all the time ranges in which the meeting can be held.
   * @param events: all the existing events that we have to consider.
   * @param attendees: the attendees of the new event that we want to create.
   * @param duration: the duration of the new event that we want to create.
   * @return the time ranges that are free to use.
   */
  private Collection<TimeRange> getMeetingRanges(Collection<Event> events,
                                                 Collection<String> attendees, long duration) {
    List<TimeRange> busyTimeRanges = getBusyTimeRanges(events, attendees);
    return getFreeRanges(busyTimeRanges, duration);
  }

  /**
   * Finds all the time ranges that aren't busy and last at least as long as the given duration.
   * @param busyTimeRanges: the time ranges that already have relevant events.
   * @param duration: the duration of the new event that we want to create.
   * @return the time ranges that are free to use.
   */
  private Collection<TimeRange> getFreeRanges(List<TimeRange> busyTimeRanges, long duration) {
    Collection<TimeRange> freeTimeBlocks = new ArrayList<>();
    int nextFreeStartTime = TimeRange.START_OF_DAY;
    for (TimeRange currBusyTime : busyTimeRanges) {
      int currBusyStartTime = currBusyTime.start();
      if (currBusyStartTime - nextFreeStartTime >= duration) {
        freeTimeBlocks.add(TimeRange.fromStartEnd(nextFreeStartTime, currBusyStartTime, /*inclusive*/ false));
      }
      nextFreeStartTime = Math.max(currBusyTime.end(), nextFreeStartTime);
    }
    if (TimeRange.END_OF_DAY - nextFreeStartTime >= duration) {
      freeTimeBlocks.add(TimeRange.fromStartEnd(nextFreeStartTime, TimeRange.END_OF_DAY, /*inclusive*/ true));
    }
    return freeTimeBlocks;
  }

  /**
   * Finds all the time ranges in which people who attend the request are already busy.
   * @param events: the events that are already occuring.
   * @param attendees: the attendees the new event that we want to create.
   * @return the time ranges in which the request attendees are busy.
   */
  private List<TimeRange> getBusyTimeRanges(Collection<Event> events, Collection<String> attendees) {
    List<TimeRange> busyTimeRanges = new ArrayList<>();
    for (Event event : events) {
      for (String person : event.getAttendees()) {
        if (person != null && attendees.contains(person)) {
          busyTimeRanges.add(event.getWhen());
          break; // Meeting time is in busyTimeRanges, no need to check other meeting attendees
        }
      }
    }
    busyTimeRanges.sort(TimeRange.ORDER_BY_START);
    return busyTimeRanges;
  }
}
