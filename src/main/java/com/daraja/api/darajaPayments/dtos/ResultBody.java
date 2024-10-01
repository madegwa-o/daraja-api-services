package com.daraja.api.darajaPayments.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultBody{
	@JsonProperty("Body")
	private Body body;
}