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
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.FarmerMarket;

/** Servlet that returns information about farmer markets. */
@WebServlet("/farmer-market")
public class FarmerMarketServlet extends HttpServlet {

  private List<FarmerMarket> markets;

  private static final int COL_NUM_OF_MARKET_NAME = 1;
  private static final int COL_NUM_OF_MARKET_WEBSITE = 2; 
  private static final int COL_NUM_OF_MARKET_LATITUDE = 21;
  private static final int COL_NUM_OF_MARKET_LONGITUDE = 20;

  @Override
  public void init() {
    markets = new ArrayList<>();

    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/farmers_markets_from_usda.csv"));
    String line;
    String[] cells;

    // Read the first line (header)
    if (scanner.hasNextLine()) {
      line = scanner.nextLine();
    }

    while (scanner.hasNextLine()) {
      line = scanner.nextLine();
      cells = line.split(",");

      String name = cells[COL_NUM_OF_MARKET_NAME];
      String website = cells[COL_NUM_OF_MARKET_WEBSITE];
      String lat = cells[COL_NUM_OF_MARKET_LATITUDE];
      String lng = cells[COL_NUM_OF_MARKET_LONGITUDE];

      if (lat.isEmpty() || lng.isEmpty()) {
        continue; // We don't have the location of the market, so we don't wan't to keep it
      }

      try {
        double finalLat = Double.parseDouble(lat);
        double finalLng = Double.parseDouble(lng);

        markets.add(new FarmerMarket(name, website, finalLat, finalLng));
      } catch (NumberFormatException e) {
        System.err.println("Invalid input line - latitude or longitude were not represented as numbers.");
      }
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getWriter().write(new Gson().toJson(markets));
  }
}