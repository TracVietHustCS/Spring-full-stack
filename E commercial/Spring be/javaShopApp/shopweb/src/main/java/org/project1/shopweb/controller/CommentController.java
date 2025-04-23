package org.project1.shopweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.dto.CommentDTO;
import org.project1.shopweb.model.User;
import org.project1.shopweb.service.comments.ICommentService;
import org.project1.shopweb.respon.CommentRespon;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService  iCommentService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> insertCm(@Valid @RequestBody CommentDTO commentDTO){

            User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(loginUser.getId() != commentDTO.getUserId()){
                return ResponseEntity.badRequest().body("can not comment as another user");
            }
            iCommentService.insertCommnet(commentDTO);
            return ResponseEntity.ok("insert sucessuflly");

    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> updateComment( @Valid @RequestBody CommentDTO commentDTO,@PathVariable long id){

            User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(loginUser.getId() != commentDTO.getUserId()) {
                return ResponseEntity.badRequest().body("can not comment as another user");
            }

            iCommentService.updateComment(id,commentDTO);
            return ResponseEntity.ok("done update");


    }
    @GetMapping("")
    public ResponseEntity<?> getComment(
            @RequestParam("product_id") Long productId,
            @RequestParam(value = "user_id", required = false) Long userId
    ){
        List<CommentRespon> commentRespons;
        if(userId == null){
            commentRespons = iCommentService.getCommentsByProduct(productId);
        } else {
            commentRespons = iCommentService.getCommentsByUserAndProduct(userId, productId);
        }

        return ResponseEntity.ok(commentRespons);
    }






}
