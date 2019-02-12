package com.example.Posting_Comments.POsts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @RequestMapping("/posts")
    public List<Post> getAllPosts() {
        List<Post> posts=new ArrayList<>();
        postRepository.findAll().forEach(posts::add);
        return posts;
    }

    @RequestMapping(method = RequestMethod.POST , value="/posts")
    public Post createPost(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }

    @RequestMapping(method = RequestMethod.PUT,value="/posts/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post post) {
        return postRepository.save(post);
    }


    @RequestMapping(method = RequestMethod.DELETE,value="/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
    }
}
