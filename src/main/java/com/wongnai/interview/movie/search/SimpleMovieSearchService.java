package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class
		List<Movie> result=new ArrayList<>();
		if(queryText.contains(" ")){
			//no space allow in query
			return result;
		}
		MoviesResponse data=movieDataService.fetchAll();

		//transform title into list of words separate by space, then check if querytext is in list
		queryText=queryText.toLowerCase();
		for (MovieData each : data){
			List<String> words= Arrays.asList(each.getTitle().split(" "));
			words.replaceAll(String::toLowerCase);
			if(words.contains(queryText)){
				Movie movie=new Movie(each.getTitle());
				movie.setActors(each.getCast());
				result.add(movie);

			}
		}



		return result;
	}
}
