package com.spring.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileStorageService {

    void handleUploadFile(String filename, MultipartFile uploadedFile);

    byte[] handleDownloadFile(String filename) throws IOException;

    void save(String filename, MultipartFile file) throws IOException;

    byte[] downloadFtpFile(String filename) throws IOException;
}
