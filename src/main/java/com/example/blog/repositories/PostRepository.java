package com.example.blog.repositories;

import com.example.blog.entites.Category;
import com.example.blog.entites.Post;
import com.example.blog.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

//    List<Post> findByTitleContaining(String title);
    @Query("select p from Post p where p.title like :key")
    List<Post> findByTitle(@Param("key") String title);


}
