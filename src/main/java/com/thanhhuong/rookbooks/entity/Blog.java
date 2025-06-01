package com.thanhhuong.rookbooks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "blogs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Blog extends AbstractBase{

    @Column(name = "title")
    private String title;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore()
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private Set<Comment> comments;

}
