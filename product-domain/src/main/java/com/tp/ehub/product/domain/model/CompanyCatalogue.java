package com.tp.ehub.product.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.types.Entity;

public class CompanyCatalogue implements Entity<UUID>{
	
	private UUID companyId;
	
	private Map<UUID, String> codes = new HashMap<UUID, String>();
	
	public CompanyCatalogue(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	public UUID id() {
		return companyId;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public Map<UUID, String> getCodes() {
		return codes;
	}

}
