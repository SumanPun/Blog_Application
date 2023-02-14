package com.example.blog.services;

import com.example.blog.entites.Category;
import com.example.blog.entites.Post;
import com.example.blog.entites.User;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.payloads.PostDto;
import com.example.blog.payloads.PostResponse;
import com.example.blog.repositories.CategoryRepository;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId){
        User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","userId",userId));
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","categoryId",categoryId));
        Post post = this.modelMapper.map(postDto,Post.class);
        post.setUser(user);
        post.setCategory(category);
        post.setAddedDate(new Date());
        post.setImageName("default.png");
        Post savedPost = this.postRepository.save(post);
        return this.modelMapper.map(savedPost,PostDto.class);
    }

    public PostDto updatePost(PostDto postDto,Integer postId){
        Post post = this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","PostID",postId));
        post.setTitle(postDto.getPostTitle());
        post.setContent(postDto.getPostContent());
        post.setImageName(postDto.getImageName());
        Post updatePost = this.postRepository.save(post);
        return this.postToDto(updatePost);
    }

    public PostDto getPost(Integer postId){
        Post post = this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","postId",postId));
        return this.postToDto(post);
    }

    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy,String sortDirection){
        Sort sort = (sortDirection.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending());
        Pageable p = PageRequest.of(pageNumber,pageSize, sort);
        Page<Post> postPage = this.postRepository.findAll(p);
        List<Post> postList = postPage.getContent();
        List<PostDto> posts = postList.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(posts);
        postResponse.setPageNumber(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLastPage(postPage.isLast());
        return postResponse;
    }

    public void deletePost(Integer postId){
        Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        this.postRepository.delete(post);
    }

    public List<PostDto> getPostByCategory(Integer categoryId){
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        List<Post> posts = this.postRepository.findByCategory(category);
        List<PostDto> postDtos = posts.stream().map((post)-> this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return  postDtos;
    }

    public List<PostDto> getPostByUser(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","userId",userId));
        List<Post> postList = this.postRepository.findByUser(user);
        List<PostDto> postDtoList = postList.stream().map((post)->this.postToDto(post)).collect(Collectors.toList());
        return postDtoList;
    }

    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = this.postRepository.findByTitle("%"+keyword+"%");
        List<PostDto> postDtos = posts.stream().map(post -> postToDto(post)).collect(Collectors.toList());
        return postDtos;
    }

    private PostDto postToDto(Post post) {
        return this.modelMapper.map(post,PostDto.class);
    }
    private Post dtoToPost(PostDto postDto) {
        return this.modelMapper.map(postDto,Post.class);
    }
}
