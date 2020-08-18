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
      ["pet's name: Marco!", 'favourite food: lasagnia!', 'favourite sport: ultimate frisbee!', "favourite color: can't choose just one!"];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

/**
 * Creates a semi-random story and adds it to the page.
 */
function randomizeStory() {
    var inputFromUser = document.getElementById('inputName').value;
    var inputName = (inputFromUser == '') ? "Karen" : inputFromUser;
    var objectsList = ["calculator", "adventure book", "arrow", "wand", "ear plugs"]
    var characterList = ["snail", "witch", "king", "rabbit", "lion", "clown"]
    var text = "";

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
    var newDiv = document.createElement('div');
    fetch('/data').then(response => response.json()).then((jsonIn) => {
      newDiv.innerHTML = "<hr>";
      for (let i of jsonIn) {
        newDiv.innerHTML += "<p>" + i + "</p>";
      }
    });
    document.body.appendChild(newDiv);
}