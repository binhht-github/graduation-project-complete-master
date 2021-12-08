package com.spring.service.communes;

import com.spring.dto.model.CommunesDTO;
import com.spring.exception.NotFoundException;

import java.util.List;

public interface CommunesServices {

    List<CommunesDTO> findAll();

    CommunesDTO findById(String id);

    List<CommunesDTO> findByDistricts(String id) throws NotFoundException;
    
    boolean existById (String id);
}
