package com.tp.ehub.gateway.resource;

import static javax.ws.rs.core.Response.created;
import static reactor.core.publisher.Flux.just;

import java.net.URI;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
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
import com.tp.ehub.common.domain.request.Request;
import com.tp.ehub.common.infra.request.RequestHandler;
import com.tp.ehub.gateway.dto.CreateProductRequest;
import com.tp.ehub.product.messaging.commands.CreateProductCommand;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Path("product")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ProductResource {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

	@Context
	UriInfo uriInfo;

	@Inject
	MessageSender sender;

	@Inject
	RequestHandler requestHandler;

	@POST
	public Response createProduct(CreateProductRequest createProductRequest) {

		Request request = requestHandler.createRequest();

		CreateProductCommand command = new CreateProductCommand();
		command.setCode(createProductRequest.getCode());
		command.setCompanyId(createProductRequest.getCompanyId());
		command.setDescription(createProductRequest.getDescription());
		command.setName(createProductRequest.getName());
		command.setProductId(createProductRequest.getProductId());
		command.setQuantity(createProductRequest.getQuantity());
		command.setRequestId(request.getId());
		command.setTimestamp(ZonedDateTime.now());

			
		sender.send(just(command), CreateProductCommand.class);
		
		Flux<Request> poller = Mono.fromSupplier( () -> requestHandler.getRequest(request.getId()))
								.repeatWhen(completed -> completed.delayElements(Duration.ofMillis(500L)))
								.filter(Optional::isPresent)
								.map(Optional::get)
								.takeUntil(r -> r.getStatus() == Request.Status.ACCEPTED || r.getStatus() == Request.Status.FAILED);
		
		Request readRequest = poller.blockFirst(Duration.ofSeconds(5L));
			
		if (Objects.isNull(readRequest) || readRequest.getStatus() == Request.Status.PENDING) {
			LOGGER.error("Create product timed out");
			throw new InternalServerErrorException();
		} else if (readRequest.getStatus() == Request.Status.FAILED) {
			throw new BadRequestException(readRequest.getMessage());
		} else {
			return created(URI.create(uriInfo.getPath() + "/" + command.getCode())).build();
		}
	}

	@GET
	public Response getProducts() {
		LOGGER.info("This is a test info log");
		LOGGER.debug("This is a test debug log");
		return Response.ok("Hello from products").build();
	}
}
