package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

import com.wongnai.interview.movie.InvertedMovieIndex;
import com.wongnai.interview.movie.InvertedMovieIndexRepository;
import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;

import java.util.*;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private InvertedMovieIndexRepository invertedMovieIndexRepository;

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository
		MoviesResponse allData=movieDataService.fetchAll();

		//to make sure there will be no duplicate
		invertedMovieIndexRepository.deleteAll();
		movieRepository.deleteAll();

        InvertedMovieIndex invertedMovieIndex=new InvertedMovieIndex();

		HashMap<String,Set<Long>> tempInvertedIndex=new HashMap<>();
		for(MovieData i:allData){

		    //save all movie from MovieResponse into movieRepository
			Movie movie=new Movie(i.getTitle());
			movie.setActors(i.getCast());
			movieRepository.save(movie);


			//inverted index sync
            //put words from movie title into Map with key=indexValue
			List<String> words= Arrays.asList(movie.getName().split(" "));
			for(String word:words){
				word=word.toLowerCase();
				Set<Long> value=tempInvertedIndex.get(word);

				try {
					value.add(movie.getId());
					tempInvertedIndex.put(word, value);
				}
				catch(Exception e){
					value=new HashSet<>();
					value.add(movie.getId());
					tempInvertedIndex.put(word, value);
				}
			}

		}

		//loop through Completed Map and create InvertedMovieIndex Entity
		//https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
		for (Map.Entry<String, Set<Long>> entry : tempInvertedIndex.entrySet()) {
			InvertedMovieIndex invertedIndex=new InvertedMovieIndex();
			String key = entry.getKey();
			Set<Long> value = entry.getValue();
			invertedIndex.setKey(key);
			invertedIndex.setIndex(value);



			invertedMovieIndexRepository.save(invertedIndex);

		}

	}
}
