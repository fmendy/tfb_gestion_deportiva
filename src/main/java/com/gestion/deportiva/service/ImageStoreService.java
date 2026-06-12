package com.gestion.deportiva.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoreService {

	public Map<String, String> uploadImage(MultipartFile file) throws IOException;
}
