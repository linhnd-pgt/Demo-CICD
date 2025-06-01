package com.thanhhuong.rookbooks.controller;

import com.thanhhuong.rookbooks.entity.Comment;
import com.thanhhuong.rookbooks.controller.common.BaseController;
import com.thanhhuong.rookbooks.entity.Blog;
import com.thanhhuong.rookbooks.service.BlogService;
import com.thanhhuong.rookbooks.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/blogs")
@AllArgsConstructor
public class BlogController extends BaseController {

    private final BlogService blogService;
    private final CommentService commentService;

    @GetMapping
    public String getBlogPage(@RequestParam(name = "page", defaultValue = "1") int page,
                              Model model){

        Pageable pageable = PageRequest.of(page - 1, 6);
        Page<Blog> blogPage = blogService.findAllPaginated(pageable);
        model.addAttribute("blogList", blogPage);
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("currentPage", blogPage.getNumber());
        model.addAttribute("top6RecentBlogList", blogService.getTop6RecentBlog());

        return "user/blog";
    }

    @GetMapping("/get/{blog_id}")
    public String viewBlogDetail(@PathVariable Long blog_id, Model model) {
        Blog blog = blogService.getBlogById(blog_id);
        List<Comment> commentList = commentService.findByBlogIdOrderByCreatedAtDesc(blog_id);
        model.addAttribute("blog", blog);
        model.addAttribute("top6RecentBlogList", blogService.getTop6RecentBlog());
        model.addAttribute("comments", commentList);
        return "user/blog_details";
    }

}
