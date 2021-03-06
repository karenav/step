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
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentLoader;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/** Servlet that returns data for my portfolio. */
@WebServlet("/data")
public final class DataServlet extends HttpServlet {

  private static final int DEFAULT_NUM_COMMENTS = 10;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int commentsMaxNum = DEFAULT_NUM_COMMENTS;
    try {
      commentsMaxNum = new Integer(request.getParameter("comments-num"));
    } catch (NumberFormatException e) {
      System.err.println("Num of comments wasn't well defined.");
    }

    CommentLoader cl = new CommentLoader();
    List<Comment> comments = cl.getComments(commentsMaxNum);

    response.setContentType("application/json");
    response.getWriter().write(new Gson().toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String content = request.getParameter("user-comment");
    String userName = request.getParameter("user-name");
    if (content.isEmpty()) {
      System.err.println("Unfortunately, you didn't write anything.");
      response.sendRedirect("/index.html");
      return;
    }
    
    // Store in datastore
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("content", content);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
    commentEntity.setProperty("user", userName);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    
    response.sendRedirect("/index.html");
  }
}
