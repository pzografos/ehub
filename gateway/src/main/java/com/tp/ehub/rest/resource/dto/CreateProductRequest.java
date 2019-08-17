package com.tp.ehub.rest.resource.dto;

import java.io.Serializable;
import java.util.UUID;

public class CreateProductRequest implements Serializable{

	private String code;

	private String name;

	private String description;

	private Long quantity;

	private UUID companyId;

	public CreateProductRequest() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "CreateProductRequest [code=" + code + ", name=" + name + ", description=" + description + ", quantity=" + quantity + ", companyId=" + companyId + "]";
	}
}
