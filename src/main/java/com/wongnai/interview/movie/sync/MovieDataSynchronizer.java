package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

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

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository
		MoviesResponse allData=movieDataService.fetchAll();

		TreeMap<String,List<Long>> invertedIndexMovie= new TreeMap<>();

		for(MovieData i:allData){
			Movie movie=new Movie(i.getTitle());
			movie.setActors(i.getCast());
			movieRepository.save(movie);

			//inverted index
			List<String> words= Arrays.asList(movie.getName().split(" "));
			for(String word:words){
				List<Long> value=invertedIndexMovie.get(word);
				try {
					value.add(movie.getId());
					invertedIndexMovie.put(word, value);
				}
				catch(Exception e){
					value=new ArrayList<>();
					value.add(movie.getId());
					invertedIndexMovie.put(word, value);
				}
			}
		}


//		//create inverted index
//		TreeMap<String,Long> invertedIndexMovie=new TreeMap<String,Long>();
//		for(MovieData i:allData){
//			List<String> words= Arrays.asList(i.getTitle().split(" "));
//			for(String word:words){
//				invertedIndexMovie.put(word,i.get)
//			}
//		}



		System.out.println(invertedIndexMovie.get("Glorious"));
	}
}
