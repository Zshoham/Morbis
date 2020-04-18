package com.morbis.data;

import com.morbis.model.poster.entity.Post;
import com.morbis.model.poster.entity.PosterData;

import java.util.LinkedList;

public class MemberServiceDataSource extends ViewableEntitySource{

    public static PosterData posterData;
    public static Post post;

    public static void initWithoutID(){
        ViewableEntitySource.initWithoutID();
        posterData = PosterData.newPosterData().build();
        post = Post.newPost("post test", "just testing 123").build();
    }

    public static void initWithID(){
        ViewableEntitySource.initWithID();
        posterData = PosterData.newPosterData()
                .withId(5)
                .withFollowers(new LinkedList<>())
                .build();
        post = Post.newPost("post test", "just testing 123")
                .withId(13)
                .build();

    }
}
