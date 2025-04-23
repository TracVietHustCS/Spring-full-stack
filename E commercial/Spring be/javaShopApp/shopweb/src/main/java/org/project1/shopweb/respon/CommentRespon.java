package org.project1.shopweb.respon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.project1.shopweb.model.Comment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRespon {

    @JsonProperty("user")
    private UserRespon userRespon;

    @JsonProperty("content")
    private String content;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CommentRespon fromRespon(Comment comment){
        return CommentRespon.builder()
                .content(comment.getContent())
                .userRespon(UserRespon.fromUser(comment.getUser()))
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
