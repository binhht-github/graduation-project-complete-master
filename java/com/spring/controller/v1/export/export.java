package com.spring.controller.v1.export;



import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.service.export.exportServices;

@RestController
@RequestMapping("/api/v1/export")
public class export {
	@Autowired
	exportServices ex;
	
//	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> downloadFile(@PathVariable("id") Long id) throws IOException, InvalidFormatException {
		return ex.test(id);
	}
}
