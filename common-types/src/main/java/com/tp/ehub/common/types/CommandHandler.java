package com.tp.ehub.common.types;

public interface CommandHandler{

	void accept(Command command);
	
	void handleError(Command command);
}
