package com.example.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BlogModel {

    @ValueMapValue
    private String title;

    @ChildResource
    private Resource user;

    @ChildResource
    private List<Resource> articles;

    @ChildResource
    private List<Resource> comments;

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user != null ? user.adaptTo(User.class) : null;
    }

    public List<Article> getArticles() {
        return articles.stream().map(article -> article.adaptTo(Article.class)).collect(Collectors.toList());
    }

    public List<Comment> getComments() {
        return comments.stream().map(comment -> comment.adaptTo(Comment.class)).collect(Collectors.toList());
    }

    public String formatDate(String dateString) {
        // Implement date formatting logic here
        return new SimpleDateFormat("MM/dd/yyyy").format(new Date(dateString));
    }
}
