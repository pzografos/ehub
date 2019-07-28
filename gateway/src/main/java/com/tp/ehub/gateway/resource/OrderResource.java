package com.tp.ehub.gateway.resource;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.tp.ehub.common.domain.messaging.sender.MessageSender;
import com.tp.ehub.order.messaging.command.PlaceOrderCommand;

import reactor.core.publisher.Flux;

@Path("orders")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class OrderResource {

	@Context
	UriInfo uriInfo;

	@Inject
	MessageSender sender;

	@POST
	public Response placeOrder(PlaceOrderCommand command) {
		try {
			sender.send(Flux.just(command), PlaceOrderCommand.class);
			return Response.created(URI.create(uriInfo.getPath() + "/" + command.getKey())).build();
		} catch (Exception e) {
			throw new InternalServerErrorException(e);
		}
	}

}
