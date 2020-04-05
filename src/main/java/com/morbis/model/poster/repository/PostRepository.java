package com.morbis.model.poster.repository;

import com.morbis.model.poster.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
