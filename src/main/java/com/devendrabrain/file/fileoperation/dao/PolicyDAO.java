package com.devendrabrain.file.fileoperation.dao;

import com.devendrabrain.file.fileoperation.entity.Policy;

public interface PolicyDAO {

	public Policy save(Policy policy);
	public Policy getById(Long id);
}
