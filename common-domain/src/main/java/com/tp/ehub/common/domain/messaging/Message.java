package com.tp.ehub.common.domain.messaging;

import java.io.Serializable;
import java.time.ZonedDateTime;

public interface Message extends Serializable {

	ZonedDateTime getTimestamp();
}