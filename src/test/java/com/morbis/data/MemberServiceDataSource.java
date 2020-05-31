package com.morbis.data;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.model.poster.entity.Post;
import com.morbis.model.poster.entity.PosterData;

import java.util.LinkedList;

public class MemberServiceDataSource extends ViewableEntitySource {

    public static PosterData posterData;
    public static Post post;
    public static Member simpleMember;

    public static void initWithoutID(){
        simpleMember = Fan.newFan().fromMember("hodOron","111222","hod","ron@gmail.com").build();
        ViewableEntitySource.initWithoutID();
        posterData = PosterData.newPosterData().build();
        post = Post.newPost("post test", "just testing 123").build();
    }

    public static void initWithID(){
        simpleMember = Fan.newFan().fromMember("hodOron","111222","hod","ron@gmail.com").withId(50).build();

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
