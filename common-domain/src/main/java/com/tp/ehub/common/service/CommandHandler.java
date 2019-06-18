package com.tp.ehub.common.service;

import com.tp.ehub.common.model.Command;

public interface CommandHandler{

	void accept(Command command);
	
	void handleError(Command command);
}
