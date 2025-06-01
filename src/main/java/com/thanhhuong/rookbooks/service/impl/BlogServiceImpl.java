package com.thanhhuong.rookbooks.service.impl;

import com.thanhhuong.rookbooks.dto.BlogSearchDTO;
import com.thanhhuong.rookbooks.entity.Blog;
import com.thanhhuong.rookbooks.repository.BlogRepository;
import com.thanhhuong.rookbooks.service.BlogService;
import com.thanhhuong.rookbooks.service.FileUploadService;
import com.thanhhuong.rookbooks.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BlogServiceImpl implements BlogService {

    BlogRepository blogRepository;

    FileUploadService fileUploadService;

    UserService userService;

    @Override
    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Page<Blog> findAllPaginated(Pageable pageable){
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> findAllByKeywordPaginated(BlogSearchDTO search, Pageable pageable) {
        Long userId = search.getUserId();
        String keyword = search.getKeyword();
        if(userId != null && keyword != null){
            return blogRepository.findByUser_IdAndTitleContaining(userId, keyword, pageable);
        } else if (userId != null){
           return blogRepository.findByUser_Id(userId, pageable);
        } else if (keyword != null){
            return blogRepository.findByTitleContaining(keyword, pageable);
        }
        return blogRepository.findAll(pageable);
    }

    @Override
    public void addBlog(Blog blog, MultipartFile coverImage) throws IOException {

        Blog savedBlog = blogRepository.save(blog);
        savedBlog.setThumbnail(fileUploadService.uploadFile(coverImage));
        blogRepository.save(blog);

    }

    @Override
    public void editBlog(Long blogId, Blog blog, MultipartFile thumbnail) throws IOException {
        Blog existedBlog = blogRepository.findById(blogId).orElse(null);
        if(existedBlog != null){
            existedBlog.setContent(blog.getContent());
            existedBlog.setTitle(blog.getTitle());
            existedBlog.setSummary(blog.getSummary());
            existedBlog.setContent(blog.getContent());
            existedBlog.setThumbnail(fileUploadService.uploadFile(thumbnail));
            blogRepository.save(existedBlog);

        }
    }


    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public Blog getBlogByTitle(String title) {
        return blogRepository.findByTitle(title);
    }

    @Override
    public List<Blog> getTop6RecentBlog() {
        return blogRepository.findTop6ByOrderByCreatedAtDesc();
    }
}
