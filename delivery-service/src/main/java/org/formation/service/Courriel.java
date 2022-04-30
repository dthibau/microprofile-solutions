package org.formation.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Courriel {

	public String to;
	public String subject;
	public String text;
	public String status;
	
}
