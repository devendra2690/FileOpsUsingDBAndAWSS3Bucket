package com.devendrabrain.file.fileoperation.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.devendrabrain.file.fileoperation.entity.Policy;
import com.devendrabrain.file.fileoperation.repository.PolicyRepository;

@Repository
public class PolicyDAOImpl implements PolicyDAO{

	@Autowired
	PolicyRepository policyRepository;
	
	@Override
	public Policy save(Policy policy) {
		return policyRepository.save(policy);
	}

	@Override
	public Policy getById(Long id) {
		return policyRepository.findById(id).orElse(null);
	}

}
