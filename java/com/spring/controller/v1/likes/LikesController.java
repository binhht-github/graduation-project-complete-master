package com.spring.controller.v1.likes;

import com.spring.dto.model.LikesDTO;
import com.spring.dto.response.Response;
import com.spring.service.likes.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
public class LikesController {
    @Autowired
    private LikesService likesService;

    // lay tat ca like cua post ai cũng xem được
    @GetMapping("/all/{id}")
    public ResponseEntity<Response<List<LikesDTO>>> getAll(@PathVariable("id") Long id) {
        Response<List<LikesDTO>> response = new Response<>();
        response.setData(this.likesService.getLikeByPost(id));
        return ResponseEntity.ok(response);
    }

    // phải đăng nhập
    // tim like theo acc vs post(dùng để hiển thị nút like của acc "like hay chưa
    // like")
    // xủ lý fontend nếu tìm thấy - delete, nếu ko tìm thấy - create
    @GetMapping("/acc/{ida}/post/{idp}")
    public ResponseEntity<Response<LikesDTO>> getLikes(@PathVariable("ida") Long ida, @PathVariable("idp") Long idp) {
        Response<LikesDTO> response = new Response<>();
        response.setData(this.likesService.getLikeByAccAndPost(ida, idp));
        return ResponseEntity.ok(response);
    }

    // phải đăng nhập
    // create
    @PostMapping()
    public ResponseEntity<Response<LikesDTO>> create(@RequestBody LikesDTO likesDTO) {
        Response<LikesDTO> response = new Response<>();
        response.setData(likesService.create(likesDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // phải đăng nhập
    // delete
    @DeleteMapping("{id}")
    public ResponseEntity<Response<LikesDTO>> delete(@PathVariable("id") Long id) {

        Response<LikesDTO> response = new Response<>();
        response.setData(likesService.delete(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}