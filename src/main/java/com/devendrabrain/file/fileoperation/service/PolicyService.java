package com.devendrabrain.file.fileoperation.service;

import java.io.ByteArrayOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.devendrabrain.file.fileoperation.entity.Policy;

public interface PolicyService {

	public Policy save(Policy policy, MultipartFile file);
	public Policy getById(Policy policy);
	public ByteArrayOutputStream getByIdFromAWSS3(Policy policy);
}
