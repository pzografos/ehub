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

import com.tp.ehub.command.CreateOrderCommand;
import com.tp.ehub.gateway.service.CommandService;

@Path("orders")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class OrderResource {

	@Context
	UriInfo uriInfo;

	@Inject
	CommandService commandService;

	@POST
	public Response createOrder(CreateOrderCommand createOrderCommand) {
		try {
			commandService.create(createOrderCommand);
			return Response.created(URI.create(uriInfo.getPath() + "/" + createOrderCommand.getKey())).build();
		} catch (Exception e) {
			throw new InternalServerErrorException(e);
		}
	}

}
