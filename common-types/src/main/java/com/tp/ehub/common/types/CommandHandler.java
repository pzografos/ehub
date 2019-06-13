package com.tp.ehub.common.types;

public interface CommandHandler <C extends Command>{

	void accept(C command);
	
	void handleError(C command);
}
