package com.tp.ehub.gateway.resource;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.common.domain.messaging.sender.MessageSender;
import com.tp.ehub.product.messaging.commands.CreateProductCommand;

import reactor.core.publisher.Flux;

@Path("products")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ProductResource {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

	@Context
	UriInfo uriInfo;

	@Inject
	MessageSender sender;

	@POST
	public Response createProduct(CreateProductCommand command) {

		try {
			sender.send(Flux.just(command), CreateProductCommand.class);
			return Response.created(URI.create(uriInfo.getPath() + "/" + command.getCode())).build();
		} catch (Exception e) {
			LOGGER.error("Create product failed", e);
			throw new InternalServerErrorException(e);
		}
	}

	@GET
	public Response getProducts() {
		LOGGER.info("This is a test info log");
		LOGGER.debug("This is a test debug log");
		return Response.ok("Hello from products").build();
	}

}
