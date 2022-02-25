package com.devendrabrain.file.fileoperation.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devendrabrain.file.fileoperation.entity.Policy;
import com.devendrabrain.file.fileoperation.service.PolicyService;

@RestController
public class PolicyFilesController {

	@Autowired
	PolicyService policyService;
	
	@PostMapping("/policy/upload")
	public String uploadFiles(@RequestParam("file") MultipartFile multipartFile, @RequestParam("policyName") String policyName) {
		
		Policy policy = new Policy();
		policy.setFileName(multipartFile.getOriginalFilename());
		policy.setPolicyName(policyName);
		policy.setFileType(multipartFile.getContentType());
		try {
			policy.setPolicy_file(multipartFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		policy = policyService.save(policy, multipartFile);
		
		return "File with name"+multipartFile.getOriginalFilename()+"Uploaded.!! URL to access it is "+policy.getFileUrl();
	}
	
	@GetMapping("/downloadFile/{policyId}")
	public ResponseEntity<byte[]> fileToDownload(@PathVariable("policyId") Long policyId) {
		
		Policy policy = new Policy();
		policy.setPolicyId(policyId);
		
		policy = policyService.getById(policy);
		
		return ResponseEntity.ok()
                .contentType(contentType(policy.getFileName()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + policy.getFileName() + "\"")
                .body(policy.getPolicy_file());	
		}
	
	@GetMapping("/downloadFile/aws/{policyId}")
	public ResponseEntity<byte[]> fileToDownloadFromAWS(@PathVariable("policyId") Long policyId) {
		
		Policy policy = new Policy();
		policy.setPolicyId(policyId);
		
		// Bad Call but cannot help
		policy = policyService.getById(policy);

		
		ByteArrayOutputStream byteArrayOutputStream = policyService.getByIdFromAWSS3(policy);
		
		 return ResponseEntity.ok()
	                .contentType(contentType(policy.getFileName()))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + policy.getFileName() + "\"")
	                .body(byteArrayOutputStream.toByteArray());
	}


	private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
