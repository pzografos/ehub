package com.tp.ehub.model.messaging;

import java.io.Serializable;
import java.time.ZonedDateTime;

public interface Message extends Serializable{

	ZonedDateTime getTimestamp(); 
}