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

/**
 * Adds a random fact about me to the page.
 */
function addRandomFact() {
  const facts =
      ["pet's name: Marco!", 'favourite food: lasagnia!', 'favourite sport: ultimate frisbee!', "favourite ice cream flavour: chocolate!"];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
  factContainer.style.visibility = 'visible';
}

/**
 * Creates a semi-random story and adds it to the page.
 */
function randomizeStory() {
  const inputFromUser = document.getElementById('inputName').value;
  const inputName = (inputFromUser == '') ? "Karen" : inputFromUser;
  const objectsList = ["calculator", "adventure book", "arrow", "wand", "ear plugs"];
  const characterList = ["snail", "witch", "king", "rabbit", "lion", "clown"];
  let text = "";

  text = text + "This is a story about a " + getRandomItemFromArray(characterList) + 
    " called " + inputName + ". " + inputName + " had to find the magical " + 
    getRandomItemFromArray(objectsList) + " in order to cure their beloved sick " + 
    getRandomItemFromArray(characterList) + ". " + inputName + 
    " sacrifised everything for it, including losing their favourite " + 
    getRandomItemFromArray(objectsList) + 
    ". But it was all worth it, and they lived happily ever after."

  document.getElementById("storyBox").innerHTML = text;
  document.getElementById("storyBox").style.visibility = 'visible';
}

/**
* Returns a semi-ramdom item from the given array.
*/ 
function getRandomItemFromArray(array) {
  return array[Math.floor(Math.random() * (array.length))];
}

/**
 * Fetch information from the 'data' servlet.
 */
function fetchFromData() { 
  let commentsEl = document.getElementById("all-comments");
  commentsEl.innerHTML = "";
  let commentNum = document.getElementById("comment-num").value;
  fetch('/data?comments-num=' + commentNum).then(response => response.json()).then((comments) => {
    commentsEl.style.visibility = (comments.length > 0 ? 'visible' : 'hidden');
    comments.forEach((singleComment) => {
      commentsEl.appendChild(createTableElement(singleComment));
    });
  });
}

/** 
 * Creates a record in a table. 
 */
function createTableElement(comment) {
  const tableElement = document.createElement('tr');
  let content = "<td class = 'comment-content'>" + comment.content + "</td>";
  let user = "<td class = 'comment-user-name'>" + comment.user + "</td>";
  tableElement.innerHTML = content + user;
  return tableElement;
}


/**
 * Delete all the current data and then fetch the empty data .
 */
function deleteDataAndFetch() { 
  let commentsEl = document.getElementById("all-comments");
  commentsEl.innerHTML = "";
  fetch('/delete-data', {method: "POST"}).then(() => fetchFromData());
}

