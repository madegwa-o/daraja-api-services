package com.daraja.api.darajaPayments.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CallbackMetadata{
	@JsonProperty("Item")
	private List<Item> item;
}