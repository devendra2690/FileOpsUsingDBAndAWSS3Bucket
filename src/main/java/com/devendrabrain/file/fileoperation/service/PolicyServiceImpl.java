package com.devendrabrain.file.fileoperation.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.devendrabrain.file.fileoperation.dao.PolicyDAO;
import com.devendrabrain.file.fileoperation.entity.Policy;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	PolicyDAO policyDAO;

	@Autowired
	AmazonS3 amazonS3;

	@Override
	public Policy save(Policy policy, MultipartFile file) {

		boolean bucketExist = amazonS3.doesBucketExistV2("techgeeknextbucketcoco");
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());

		if (bucketExist) {
			try {
				amazonS3.putObject("techgeeknextbucketcoco", policy.getFileName(), file.getInputStream(), metadata);
			} catch (SdkClientException | IOException e) {
				e.printStackTrace();
			}

			policy.setFileUrl(amazonS3.generatePresignedUrl("techgeeknextbucketcoco", policy.getFileName(), addDays(new Date(),1)).toString());
		}

		policy = policyDAO.save(policy);
		
		return policy;
	}

	@Override
	public Policy getById(Policy policy) {
		
		policy = policyDAO.getById(policy.getPolicyId());		
		return policy;
	}
	
	@Override
	public ByteArrayOutputStream getByIdFromAWSS3(Policy policy) {

		policy = policyDAO.getById(policy.getPolicyId());

		S3Object s3object = amazonS3.getObject("techgeeknextbucketcoco", policy.getFileName());
		InputStream inputStream = s3object.getObjectContent();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[4096];
        try {
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
			    outputStream.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        return outputStream;		 
	}

	
	private Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}


}
