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
 * Class representing a comment that users add to the website.
 */
public class Comment {

  /** The content of this comment. */
  private final String content;

  /** The user who wrote this comment. */
  private final String user;

  public Comment(String content, String user) {
      this.content = content;
      this.user = (user != "") ? user : "Anonymous" ;
  } 

  /** Returns the content of this comment. */
  public String getContent() {
    return content;
  }

  /** Returns the name of the user who wrote the comment. */
  public String getUser() {
    return user;
  }
}
