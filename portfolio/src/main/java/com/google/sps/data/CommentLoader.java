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

import java.lang.Integer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.gson.Gson;

/**
 * A utility class for loading comments from datastore.
 */
public final class CommentLoader {
  private final DatastoreService datastore;
  private final Query query;

  public CommentLoader() {
    datastore = DatastoreServiceFactory.getDatastoreService();
    query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
  }

  public List<Comment> getComments(int numComments) {
    List comments = new ArrayList<Comment>();
    List<Entity> results = datastore.prepare(query).asList(
      FetchOptions.Builder.withLimit(numComments)
    );
    for (Entity entity : results) {
      String content = (String) entity.getProperty("content");
      String user = (String) entity.getProperty("user");
      comments.add(new Comment(content, user));
    }
    return comments;
  }

  public List<Comment> getComments() {
    return getComments(Integer.MAX_VALUE);
  }
}
