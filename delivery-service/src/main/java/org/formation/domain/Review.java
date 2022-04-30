package org.formation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {

	private long id;
	
	private int note;
	
	private String commentaire;
}
