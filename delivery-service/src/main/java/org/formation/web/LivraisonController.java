package org.formation.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.formation.domain.Livraison;
import org.formation.domain.Livreur;
import org.formation.dto.BaseLivraisonDto;
import org.formation.interceptor.Logged;
import org.formation.service.LivraisonService;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;

@Path("/livraisons")
@RolesAllowed("admin")
public class LivraisonController {

	@Inject
	private LivraisonService livraisonService;
	
	@Inject
    private JsonWebToken callerPrincipal;
	
	@GET
	@Path("/token")
	public JsonWebToken displayToken() {
		return callerPrincipal;
	}
    @GET
    @Logged
    @RolesAllowed("user")
	public Multi<BaseLivraisonDto> findAll() {
    	Log.debug("Message from Controller");
		return livraisonService.findAll().map(l -> new BaseLivraisonDto(l));
	}

    @GET
    @Path("{id}")
    @RolesAllowed("user")
    public Livraison findOne(@RestPath Long id) {
    	return livraisonService.load(Livraison.builder().id(id).build()).orElseThrow(() -> new RuntimeException());
    }


    @POST
    @ResponseStatus(201)
    @Counted(name = "nbLivraisons")
    @Timed
    public Livraison create(@RestQuery String noCommande) {
    	return livraisonService.create(noCommande);
    }
    
    @PUT
    @Path("{id}")
    @ResponseStatus(204)
    public void update(@RestPath Long id, Livraison livraison) {
    	
    }

    @PUT
    @Path("{id}/start")
    @ResponseStatus(204)
    public void start(@RestPath Long id) {
    	livraisonService.start(Livraison.builder().id(id).build());
    }
    @PUT
    @Path("{id}/affect/{livreurId}")
    @ResponseStatus(204)
    public void affect(@RestPath Long id, @RestPath Long livreurId) {
    	livraisonService.affect(Livraison.builder().id(id).build(), Livreur.builder().id(livreurId).build());
    }
    @PUT
    @Path("{id}/complete")
    @ResponseStatus(204)
    public void complete(@RestPath Long id, @RestPath Long livreurId) {
    	livraisonService.complete(Livraison.builder().id(id).build());
    }

    @DELETE
    @Path("{id}")
    @ResponseStatus(204)
    public void delete(@RestPath Long id) {
    	
    }

}
