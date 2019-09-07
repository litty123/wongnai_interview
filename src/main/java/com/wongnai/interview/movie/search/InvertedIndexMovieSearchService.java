package com.wongnai.interview.movie.search;

import java.util.*;


import com.wongnai.interview.movie.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
    private InvertedMovieIndexRepository invertedMovieIndexRepository;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
		// You must find a way to build inverted index before you do an actual search.
		// Inverted index would looks like this:
		// -------------------------------
		// |  Term      | Movie Ids      |
		// -------------------------------
		// |  Star      |  5, 8, 1       |
		// |  War       |  5, 2          |
		// |  Trek      |  1, 8          |
		// -------------------------------
		// When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
		// there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
		// Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
		// from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
		// you have to return can be union or intersection of those 2 sets of ids.
		// By the way, in this assignment, you must use intersection so that it left for just movie id 5.
		List<String> words= Arrays.asList(queryText.split(" "));
		Set<Long> index=new HashSet<>();
		int count=0;

		//find intersection of all Index from query words
		for(String word:words){
			InvertedMovieIndex invertedMovieIndex=invertedMovieIndexRepository.findByKey(word);

			//for first word with data, copy the index
			//else intersect result
			if(count==0){
				try {
					index = invertedMovieIndex.getIndex();
					count++;
				}
				catch (Exception e){

				}

			}
			else{
				try {
					index.retainAll(invertedMovieIndex.getIndex());
				}
				catch(Exception e ){

				}
			}


		}

		//get movie name from index result
		List<Movie> result=new ArrayList<>();
		for(Long i:index){
			Optional<Movie> m=movieRepository.findById(i);
			result.add(m.orElse(null));
		}

		return result;
	}
}
