package com.wongnai.interview.movie;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvertedMovieIndexRepository extends CrudRepository<InvertedMovieIndex, Long> {
    @Query("select i from InvertedMovieIndex i where lower(i.key) = lower(:keyword)")
    InvertedMovieIndex findByKey(@Param("keyword") String keyword);

}
