package com.coreApplication.Repositories;

import com.coreApplication.Model.Post;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
@Configuration
public interface PostRepository extends MongoRepository<Post, Long> {
}
