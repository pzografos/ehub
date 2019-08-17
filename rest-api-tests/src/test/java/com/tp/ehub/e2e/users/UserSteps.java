package com.tp.ehub.e2e.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserSteps {
	
	//TODO: replace with a proper user system
	private static Map<UUID, List<String>> companyUsers = new HashMap<>();
	private static Map<UUID, String> companyNames = new HashMap<>();

	public void user_is_a_manager_of_company(String user, String company) {
		UUID companyId = companyNames.entrySet().stream()
					.filter(e -> e.getValue().equals(company))
					.map(Map.Entry::getKey)
					.findAny()
					.orElse(UUID.randomUUID());
		
		List<String> usersForCompany = companyUsers.getOrDefault(companyId, new ArrayList<>());
		usersForCompany.add(user);
		companyNames.putIfAbsent(companyId, company);
		companyUsers.put(companyId, usersForCompany);
	}
	
	public UUID getCompany(String user) {
		return companyUsers.entrySet().stream()
			.filter(e -> e.getValue().contains(user))
			.map(Map.Entry::getKey)
			.findAny()
			.orElseThrow();
	}
}
