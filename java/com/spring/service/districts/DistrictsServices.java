package com.spring.service.districts;

import com.spring.dto.model.DistrictsDTO;
import com.spring.exception.NotFoundException;

import java.util.List;

public interface DistrictsServices {
    List<DistrictsDTO> findAll();

    DistrictsDTO findById(String id);
    
    List<DistrictsDTO> findByProvinces(String id) throws NotFoundException;

    boolean existById (String id);

}
