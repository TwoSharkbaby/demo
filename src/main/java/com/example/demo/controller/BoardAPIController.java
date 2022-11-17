package com.example.demo.controller;

import com.example.demo.model.Board;
import com.example.demo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
class BoardAPIController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/boards")
    List<Board> all(@RequestParam(required = false, defaultValue = "") String title,
                    @RequestParam(required = false, defaultValue = "") String content){
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)){
            return boardRepository.findAll();
        } else {
            return boardRepository.findByTitleOrContent(title, content);
        }
    }

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board board){
        return boardRepository.save(board);
    }

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id){
        return boardRepository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board board, @PathVariable Long id){
        return boardRepository.findById(id).map(board1 -> {
            board1.setTitle(board.getTitle());
            board1.setContent(board.getContent());
            return boardRepository.save(board1);
    })
            .orElseGet(() -> {
                board.setId(id);
                return boardRepository.save(board);
            });
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id){
        boardRepository.deleteById(id);
    }



}
