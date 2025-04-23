package org.project1.shopweb.service.comments;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.dto.CommentDTO;
import org.project1.shopweb.model.Comment;
import org.project1.shopweb.model.Product;
import org.project1.shopweb.model.User;
import org.project1.shopweb.repository.CommentRepository;
import org.project1.shopweb.repository.ProductRepository;
import org.project1.shopweb.repository.UserRepository;
import org.project1.shopweb.exception.NotFoundException;
import org.project1.shopweb.respon.CommentRespon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private  final ProductRepository  productRepository;
    private  final UserRepository userRepository;
    private  final CommentRepository commentRepository;
    @Override
    @Transactional
    public Comment insertCommnet(CommentDTO commentDTO) {
        User user =userRepository.findById(commentDTO.getUserId())
                .orElse(null);
        Product product =  productRepository.findById(commentDTO.getProductId())
                .orElse(null);
        if (user == null || product == null) {
            throw new IllegalArgumentException("User or product not found");
        }
        Comment comment = Comment.builder()
                .user(user)
                .product(product)
                .content(commentDTO.getContent())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void updateComment(Long id,CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("this commnet does not exist"));
        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);
    }

    @Override
    public List<CommentRespon> getCommentsByProduct(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return comments.stream().map(CommentRespon::fromRespon).toList();

    }

    @Override
    public List<CommentRespon> getCommentsByUserAndProduct(Long userId, Long productId) {
        List<Comment> comments = commentRepository.findByProductIdAndUserId( productId,userId);
        return comments.stream().map(CommentRespon::fromRespon).toList();
    }



}
