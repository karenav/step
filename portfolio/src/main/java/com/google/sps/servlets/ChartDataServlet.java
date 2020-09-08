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
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentLoader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.function.Function;

/** Servlet that returns data to use in a chart: listing the words that appeared in the comments 
 * on my portfolio, and the number of times they appeared.
 */
@WebServlet("/chart-data")
public final class ChartDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    CommentLoader cl = new CommentLoader();
    List<Comment> comments = cl.getComments();

    Map<String, Long> wordsCount =
      comments.stream()
        .map(c -> c.getContent()) // Take content of each comment
        .map(c -> Arrays.asList(c.split(" |!|\\.|\\,|\\?"))) // Split each comment to words
        .flatMap(List::stream)
        .map(c -> c.trim())
        .filter(p -> !p.isEmpty())
        .collect( Collectors.groupingBy(p -> p.toLowerCase(), Collectors.counting())); // Count words appearances 

    response.setContentType("application/json");
    response.getWriter().write(new Gson().toJson(wordsCount));
  }
}