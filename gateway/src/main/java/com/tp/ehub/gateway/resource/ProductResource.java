package com.tp.ehub.gateway.resource;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;

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

import com.tp.ehub.command.CreateProductCommand;
import com.tp.ehub.gateway.service.CommandService;

@Path("products")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ProductResource {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

	@Context
	UriInfo uriInfo;

	@Inject
	CommandService commandService;

	@POST
	public Response createProduct() {

		try {

			String productCode = "1234";

			CreateProductCommand command = new CreateProductCommand();
			command.setCompanyId(UUID.randomUUID());
			command.setCode("1234");
			command.setDescription("very good product");
			command.setName("theProduct");
			command.setQuantity(2L);
			command.setTimestamp(ZonedDateTime.now());

			commandService.create(command);
			return Response.created(URI.create(uriInfo.getPath() + "/" + productCode)).build();
		} catch (Exception e) {
			LOGGER.error("Create product failed", e);
			throw new InternalServerErrorException(e);
		}
	}
	
	@GET
	public Response getProducts() {
		return Response.ok("Hello from products").build();
	}

}
