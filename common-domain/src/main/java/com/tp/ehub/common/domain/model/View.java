package com.tp.ehub.common.domain.model;

import java.io.Serializable;

public interface View<K> extends Serializable{

	K getKey();
}
