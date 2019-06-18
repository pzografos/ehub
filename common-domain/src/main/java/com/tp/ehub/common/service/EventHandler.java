package com.tp.ehub.common.service;

import com.tp.ehub.common.event.Event;

public interface EventHandler <E extends Event>{

	void accept(E event);
}
