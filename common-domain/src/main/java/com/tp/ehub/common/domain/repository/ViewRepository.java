package com.tp.ehub.common.domain.repository;

import java.util.Optional;

import com.tp.ehub.common.domain.model.View;

public interface ViewRepository{

	<K, V extends View<K>> Optional<V> get(K key, Class<V> viewClass);

	<K, V extends View<K>> void save(V view, Class<V> viewClass);
}
