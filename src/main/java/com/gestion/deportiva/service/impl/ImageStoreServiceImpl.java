package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gestion.deportiva.service.ImageStoreService;

@Service
public class ImageStoreServiceImpl implements ImageStoreService {

	private Cloudinary cloudinary;

	public ImageStoreServiceImpl(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> uploadImage(MultipartFile file) throws IOException {
		return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
	}

}
