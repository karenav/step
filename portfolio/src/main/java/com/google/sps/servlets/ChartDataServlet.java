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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns data to use in a chart: listing the words that appeared in the comments 
 * on my portfolio, and the number of times they appeared.
 */
@WebServlet("/chart-data")
public final class ChartDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Load from datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    Map wordsCount = new HashMap<String, Integer>();

    // Go over all the comments
    for (Entity entity : results.asIterable()) {
      String content = (String) entity.getProperty("content");

      // Extract and clean the words from each comment
      String[] words = content.split(" |!|\\.|\\,|\\?");
      for (String word : words) {
        word = word.trim().toLowerCase();
        if (word.isEmpty()) {
          continue;
        }
        int wordCount = wordsCount.containsKey(word) ? (int) wordsCount.get(word) : 0;
        wordCount += 1;
        wordsCount.put(word, wordCount);
      }
    }

    response.setContentType("application/json");
    response.getWriter().write(new Gson().toJson(wordsCount));
  }
}