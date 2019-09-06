package com.wongnai.interview.movie;



import javax.persistence.*;
import java.util.*;

@Entity
public class InvertedMovieIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String key;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> index;


    public InvertedMovieIndex(){
        index=new HashSet<>();
        key="";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<Long> getIndex() {
        return index;
    }

    public void setIndex(Set<Long> index) {
        this.index = index;
    }




}
