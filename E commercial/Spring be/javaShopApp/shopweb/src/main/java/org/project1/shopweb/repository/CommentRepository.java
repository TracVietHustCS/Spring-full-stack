package org.project1.shopweb.repository;

import org.project1.shopweb.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByProductId(Long id);

    List<Comment> findByProductIdAndUserId(Long product_id, Long user_id);



}
