package com.tp.ehub.common.domain.repository;

import com.tp.ehub.common.domain.model.View;

public interface ViewRepository<K, V extends View<K>>{

	V get(K key);

	void save(V view);
}
