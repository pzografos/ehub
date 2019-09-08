package com.tp.ehub.rest.resource;

import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.Response.created;
import static reactor.core.publisher.Flux.just;

import java.net.URI;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.common.domain.messaging.sender.ReactiveMessageSender;
import com.tp.ehub.common.domain.repository.ViewRepository;
import com.tp.ehub.common.domain.request.Request;
import com.tp.ehub.common.infra.request.RequestHandler;
import com.tp.ehub.common.views.model.ProductCatalogueView;
import com.tp.ehub.product.messaging.commands.CreateProductCommand;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.rest.resource.dto.CreateProductRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Path("{companyUUID}/product")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ProductResource {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

	@Context
	UriInfo uriInfo;

	@Inject
	ReactiveMessageSender sender;

	@Inject
	RequestHandler requestHandler;
	
	@Inject
	ViewRepository viewRepository;

	@POST
	public Response createProduct(@PathParam("companyUUID") String companyUUID, CreateProductRequest createProductRequest) {

		LOGGER.debug("Received request {}", createProductRequest);

		Request request = requestHandler.createRequest();		

		CreateProductCommand command = new CreateProductCommand();
		command.setCode(createProductRequest.getCode());
		command.setCompanyId(createProductRequest.getCompanyId());
		command.setDescription(createProductRequest.getDescription());
		command.setName(createProductRequest.getName());
		command.setQuantity(createProductRequest.getQuantity());
		command.setProductId(randomUUID());
		command.setRequestId(request.getId());
		command.setTimestamp(ZonedDateTime.now());
			
		sender.send(just(command), CreateProductCommand.class);
		
		Flux<Request> poller = Mono.fromSupplier( () -> requestHandler.getRequest(request.getId()))
								.repeatWhen(completed -> completed.delayElements(Duration.ofMillis(500L)))
								.filter(Optional::isPresent)
								.map(Optional::get)
								.takeUntil(r -> r.getStatus() == Request.Status.ACCEPTED || r.getStatus() == Request.Status.FAILED);
		
		Request readRequest = poller.blockLast(Duration.ofSeconds(60L));
			
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
	public Response getProducts(@PathParam("companyUUID") String companyUUID) {
		LOGGER.info("This is a test info log");
		LOGGER.debug("This is a test debug log");
		return Response.ok("Hello from products").build();
	}
	
	@GET
	@Path("{productUUID}")
	public Response getProduct(@PathParam("companyUUID") String companyUUID, @PathParam("productUUID") String productUUID) {
		UUID productKey = UUID.fromString(productUUID);
		UUID companyKey = UUID.fromString(companyUUID);
		ProductCatalogueView view = viewRepository.get(companyKey, ProductCatalogueView.class).orElseThrow(() -> new BadRequestException("Could not find company catalogue"));
		if (view.getProducts().containsKey(productKey)) {
			Product product = view.getProducts().get(productKey);
			return Response.ok(product).build();
		} else {
			throw new BadRequestException("Could not find product");
		}
	}
	
	@GET
	@Path("code/{code}")
	public Response getProductByCode(@PathParam("companyUUID") String companyUUID, @PathParam("code") String code) {
		UUID companyKey = UUID.fromString(companyUUID);
		ProductCatalogueView view = viewRepository.get(companyKey, ProductCatalogueView.class).orElseThrow(() -> new BadRequestException("Could not find company catalogue"));
		
		Product product = view.getProducts().values().stream()
			.filter( p -> p.getCode().equals(code))
			.findAny()
			.orElseThrow(() -> new BadRequestException("Could not find product"));
		
		return Response.ok(product).build();
	}
}
