package de.sonallux.example.graphql.post;

import java.util.ArrayList;
import java.util.List;

public record Post(String id, String authorId, String title, String content, List<String> likes) {
    public Post(String id, String authorId, String title, String content) {
        this(id, authorId, title, content, new ArrayList<>());
    }
}
