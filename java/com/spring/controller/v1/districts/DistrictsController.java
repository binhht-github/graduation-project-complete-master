package com.spring.controller.v1.districts;

import com.spring.dto.model.DistrictsDTO;
import com.spring.dto.response.Response;
import com.spring.exception.NotFoundException;
import com.spring.service.districts.DistrictsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/districts")
public class DistrictsController {

    @Autowired
    private DistrictsServices districtsServices;

    @GetMapping()
    public ResponseEntity<Response<List<DistrictsDTO>>> getList(){
    	 Response<List<DistrictsDTO>> response = new Response<>();
    	 response.setData(districtsServices.findAll());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response<DistrictsDTO>> getOne(@PathVariable("id") String id) throws NotFoundException {
        Response<DistrictsDTO> response = new Response<>();
        if (!districtsServices.existById(id)) {
            throw new NotFoundException("Huyện/Quận này Không tồn tại");

        }
        response.setData(this.districtsServices.findById(id));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/Provinces/{id}")
    public ResponseEntity<Response<List<DistrictsDTO>>> getByProvinces(@PathVariable("id") String id) throws NotFoundException {
    	Response<List<DistrictsDTO>> response = new Response<>();
   	 response.setData(districtsServices.findByProvinces(id));
       return ResponseEntity.ok(response);
    }
}
