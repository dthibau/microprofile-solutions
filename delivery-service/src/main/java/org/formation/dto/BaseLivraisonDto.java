package org.formation.dto;

import java.time.Instant;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

import org.formation.domain.Livraison;
import org.formation.domain.Status;

import lombok.Data;

@Data
@JsonbPropertyOrder({"id","creationDate","noCommande","status","livreurId"})
public class BaseLivraisonDto {

	final Long id;
	@JsonbProperty("creation-date")
	@JsonbDateFormat("dd/MM/yyyy HH:mm")
	final Instant creationDate;
	@JsonbProperty("no-commande")
	final String noCommande;
	final Status status;
	@JsonbProperty("id-livreur")
	final Long livreurId;
	
	public BaseLivraisonDto(Livraison livraison) {
		this.id = livraison.getId();
		this.creationDate = livraison.getCreationDate();
		this.noCommande =livraison.getNoCommande();
		this.status = livraison.getStatus();
		this.livreurId = livraison.getLivreur() != null ? livraison.getLivreur().getId() : null;
		
	}
	
	
}
