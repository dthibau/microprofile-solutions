package org.formation.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.formation.config.NotificationServiceConfig;
import org.formation.domain.Livraison;
import org.formation.domain.Livreur;
import org.formation.domain.Review;
import org.formation.domain.Status;
import org.formation.interceptor.Logged;
import org.formation.service.Courriel;
import org.formation.service.LivraisonService;
import org.formation.service.NotificationService;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
@Logged
public class LivraisonServiceImpl implements LivraisonService {

	@Inject
	NotificationServiceConfig notificationServiceConfig;
	
    @RestClient 
    NotificationService notificationService;
    
	
	List<Livraison> livraisons;
	
	@ConfigProperty(name = "quarkus.http.port") 
	String port;
		
	@PostConstruct
	public void init() {
		livraisons = new ArrayList<>();

		List<Review> reviews = new ArrayList<>();
		reviews.add(new Review(1,5,"GOOD"));
		reviews.add(new Review(1,3,"BAD"));
		
		Livreur livreur = Livreur.builder().id(1).nom("Speedy Gonzales").telephone("O6-49-79-99-69").reviews(reviews).build();
		livraisons.add(Livraison.builder().id(1).noCommande("1").creationDate(Instant.now()).status(Status.DISTRIBUE).livreur(livreur).build());
		livraisons.add(Livraison.builder().id(1).noCommande("2").creationDate(Instant.now()).status(Status.DISTRIBUE).livreur(livreur).build());
		livraisons.add(Livraison.builder().id(1).noCommande("3").creationDate(Instant.now()).status(Status.DISTRIBUE).livreur(livreur).build());
		livraisons.add(Livraison.builder().id(1).noCommande("4").creationDate(Instant.now()).status(Status.DISTRIBUE).livreur(livreur).build());		
	

	}
	@Override
	public Multi<Livraison> findAll() {
		System.out.println("Notificaiton Service Config " + notificationServiceConfig);
		return Multi.createFrom().items(livraisons.stream()); 
	}

	@Override
	public Optional<Livraison> load(Livraison livraison) {
		int index = livraisons.indexOf(livraison);
		return index == -1 ? Optional.empty() : Optional.of(livraisons.get(index));
	}
		
	@Override
	public Livraison create(String noCommande) {
		Livraison livraison = Livraison.builder().id(livraisons.size()+1).noCommande(noCommande).creationDate(Instant.now()).status(Status.CREE).build();
		livraisons.add(livraison);
		
		_notify(livraison);

		return livraison;
	}

	@Override
	public void affect(Livraison livraison, Livreur livreur) {
		int index = livraisons.indexOf(livraison);
		livraisons.get(index).setLivreur(livreur);
		
	}

	@Override
	public void start(Livraison livraison) {
		int index = livraisons.indexOf(livraison);
		livraisons.get(index).setStatus(Status.EN_COURS);
		
	}

	@Override
	public void complete(Livraison livraison) {
		int index = livraisons.indexOf(livraison);
		livraisons.get(index).setStatus(Status.DISTRIBUE);
	}

	@Retry(maxRetries = 3, retryOn = Exception.class, delay = 1, delayUnit = ChronoUnit.SECONDS)
	@CircuitBreaker(successThreshold = 10, requestVolumeThreshold = 4, failureRatio=0.75, delay = 1000)
	@Fallback(fallbackMethod= "fallbackNotify")
	public void _notify(Livraison livraison) {
		Log.info("Trying to send message for " + livraison);
		notificationService.sendMail(Courriel.builder().to("david.thibau@gmail.com").subject("Création Livraison").text(livraison.toString()).build());
	}
	
	@SuppressWarnings("unused")
	private void  fallbackNotify(Livraison livraison) {
		Log.error("Falling Back ");
	}
	

}
