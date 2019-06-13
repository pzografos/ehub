package com.tp.ehub.common.types;

public interface EventHandler <E extends Event>{

	void accept(E event);
}
