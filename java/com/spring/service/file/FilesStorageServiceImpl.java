package com.spring.service.file;


import com.spring.exception.NotParsableContentException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesStorageServiceImpl implements FileStorageService {

    @Value("${ftp.server.hostname}")
    private String hostname;

    @Value("${ftp.server.port}")
    private int port;

    @Value("${ftp.server.username}")
    private String username;

    @Value("${ftp.server.password}")
    private String password;

    @Override
    public void handleUploadFile(String filename, MultipartFile uploadedFile) {

        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath);
        String folderPath = rootPath + "/src/main/images";
        File myUploadFolder = new File(folderPath);

// Kiểm tra thư mục lưu trữ file có tồn tại? nếu ko thì tạo mới thư mục
        if (!myUploadFolder.exists()) {
            myUploadFolder.mkdirs();
        }
        File savedFile = null;
        try {
            savedFile = new File(myUploadFolder, filename);
            uploadedFile.transferTo(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Path getPath(String path, String filename) {

        File dir = Paths.get(path).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);

    }

    @Override
    public byte[] handleDownloadFile(String filename) throws IOException {
        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath);
        String folderPath = rootPath + "/src/main/images";

        Path path = this.getPath(folderPath, filename);
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException();
        }
//        return new byte[0];
    }

    @Override
    public void save(String filename, MultipartFile file) throws IOException {

        FTPClient ftpClient = new FTPClient();
        if (file != null) {
            try {
                if (file.getOriginalFilename().contains("..")) {
                    throw new NotParsableContentException("Sorry! Filename contains invalid path sequence " + file.getOriginalFilename());
                }

                ftpClient.connect(hostname, port);
                if (ftpClient.login(username, password)) {
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                    ftpClient.storeFile(filename, file.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    @Override
    public byte[] downloadFtpFile(String filename) throws IOException {

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(hostname, port);
            if (ftpClient.login(username, password)) {
                System.out.println("connecting...");
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    ftpClient.retrieveFile(filename, outputStream);
                    return outputStream.toByteArray();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
        return null;
    }
}
