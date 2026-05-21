package com.hotel.hotel.modules.files.controller;

import com.hotel.hotel.modules.files.dto.FileResponse;
import com.hotel.hotel.modules.files.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService service;

    @GetMapping("/room/{id}")
    public ResponseEntity<List<FileResponse>> listImagesByRoom(@PathVariable Long id) {
        List<FileResponse> images = service.listImagesByRoom(id);
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        FileResponse file = service.findById(id);
        return ResponseEntity.ok(file);
    }
}
