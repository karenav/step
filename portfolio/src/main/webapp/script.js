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
  const commentNum = document.getElementById("comment-num").value;
  const commentNumParam = "comments-num";
  fetch('/data?' + commentNumParam + '=' + commentNum).then(response => response.json()).then((comments) => {
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
  const content = "<td class = 'comment-content'>" + comment.content + "</td>";
  const user = "<td class = 'comment-user-name'>" + comment.user + "</td>";
  tableElement.innerHTML = content + user;
  return tableElement;
}

/**
 * Deletes all the current data and then fetches the empty data .
 */
function deleteDataAndFetch() { 
  let commentsEl = document.getElementById("all-comments");
  commentsEl.innerHTML = "";
  fetch('/delete-data', {method: "POST"}).then(() => fetchFromData());
  window.location.href = '/index.html';
}

/** 
 * Creates a map and adds it to the page. 
 * Notice: This function isn't currently in use, but might be used later.
 */
function createMap() {
  const OFFICE_LOC = {lat: 32.0700, lng: 34.7941};
  const OFFICE_DESCRIPTION = 'Tel Aviv Google office';

  const map = new google.maps.Map(
    document.getElementById('map'), {
      center: OFFICE_LOC,
      zoom: 15,
      mapTypeControlOptions: { mapTypeIds: ['roadmap', 'satellite', 'hybrid', 'terrain']}
    }
  );

  const officeMarker = new google.maps.Marker({
    position: OFFICE_LOC,
    map: map,
    title: OFFICE_DESCRIPTION,
    animation: google.maps.Animation.DROP
  });

  const infoWindow = new google.maps.InfoWindow({
    content: OFFICE_DESCRIPTION
  });

  // when marker is clicked, the info window is opened
  officeMarker.addListener("click", () => {
    infoWindow.open(map, officeMarker);
  });
    
  // When center of the map changes, after 3 seconds we pan back to the marker.
  map.addListener('center_changed', function() {
    window.setTimeout(function() {
      map.panTo(officeMarker.getPosition());
    }, 3000);
  });
}

function createMarketMap() {

  const CENTER_OF_USA_LAT = 39.842507;
  const CENTER_OF_USA_LONG = -97.058318;
  const LOW_ZOOM_LEVEL = 4;

  fetch('/farmers-market').then(response => response.json()).then((markets) => {
  // Create map
  const map = new google.maps.Map(
    document.getElementById('map'),
    {center: {lat: CENTER_OF_USA_LAT, lng: CENTER_OF_USA_LONG}, zoom: LOW_ZOOM_LEVEL}
  );

  // Add markers to the map
  markets.forEach((market) => {
    const marker = new google.maps.Marker(
      {position: {lat: market.lat, lng: market.lng}, map: map}
    );

    const marketContent = "<b>Market's name:</b> " + market.name + ".";
    const websiteContent = market.website.startsWith("http") 
      ? (" <b>Website:</b> " + "<a href='" + market.website + "'>" + market.website + "</a>") 
      : "";
    const infoWindow = new google.maps.InfoWindow({
      content: marketContent + websiteContent
    });

    marker.addListener("click", () => {
      infoWindow.open(map, marker); 
    });
  });
  });
}

/** Loading the google chart preperation. */
function initChart() {
  google.charts.load('current', {'packages':['corechart']});
  google.charts.setOnLoadCallback(drawChart);
}

/** Creates a chart and adds it to the page. */
function drawChart() {  
  fetch('/chart-data').then(response => response.json()).then((wordsCount) => {    
    const data = new google.visualization.DataTable();        
    
    data.addColumn('string', 'Word');    
    data.addColumn('number', 'Times It Appeared');
    
    Object.keys(wordsCount).forEach((word) => {      
      data.addRow([word, wordsCount[word]]);    
    });

    data.sort({column: 1, desc: true});
    
    const options = {      
      'title': "Most popular words in my portfolio's comments",      
      'titleTextStyle': { color: '#8d0404', fontName: "Courier New", fontSize: 16},
      'width': '70%',
      'height':300,
      'is3D': true,
      'backgroundColor': "none"
    };

    const chart = new google.visualization.PieChart(document.getElementById('chart-container'));    
    chart.draw(data, options);  }
  );
}