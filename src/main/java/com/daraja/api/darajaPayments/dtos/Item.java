package com.daraja.api.darajaPayments.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Item{
	@JsonProperty("Value")
	private Object value;
	@JsonProperty("Name")
	private String name;
}