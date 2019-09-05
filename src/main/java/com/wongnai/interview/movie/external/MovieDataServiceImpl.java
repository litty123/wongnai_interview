package com.wongnai.interview.movie.external;

import com.wongnai.interview.movie.Movie;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;

@Component
public class MovieDataServiceImpl implements MovieDataService {
	public static final String MOVIE_DATA_URL
			= "https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json";

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public MoviesResponse fetchAll() {
		//TODO:
		// Step 1 => Implement this method to download data from MOVIE_DATA_URL and fix any error you may found.
		// Please noted that you must only read data remotely and only from given source,
		// do not download and use local file or put the file anywhere else.
		MoviesResponse response=new MoviesResponse();

		JSONParser parser = new JSONParser();
		try {
			URL oracle = new URL(MOVIE_DATA_URL); // URL to Parse
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONArray a = (JSONArray) parser.parse(inputLine);
//				System.out.println(a);
				System.out.println("1");
				// Loop through each item
				for (Object o : a) {

					JSONObject movie=(JSONObject) o;
					//{"cast":[],"year":1900,"genres":[],"title":"After Dark in Central Park"}

					MovieData moviedata=new MovieData();

					moviedata.setTitle((String) movie.get("title"));
					moviedata.setYear((Integer) movie.get("year"));
					moviedata.setCast((List<String>) movie.get("cast"));
					moviedata.setGenres((List<String>) movie.get("genres"));
					response.add(moviedata);

				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (net.minidev.json.parser.ParseException e) {
			e.printStackTrace();
		}

		return response;
	}
}
