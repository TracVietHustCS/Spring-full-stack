package org.project1.shopweb.service.comments;

import org.project1.shopweb.dto.CommentDTO;
import org.project1.shopweb.model.Comment;
import org.project1.shopweb.respon.CommentRespon;

import java.util.List;

public interface ICommentService {


    Comment insertCommnet(CommentDTO commentDTO);

    void deleteComment(Long id);

    void updateComment(Long id,CommentDTO commentDTO);

    List<CommentRespon> getCommentsByProduct(Long productId);

    List<CommentRespon> getCommentsByUserAndProduct(Long userId, Long productId);

}
