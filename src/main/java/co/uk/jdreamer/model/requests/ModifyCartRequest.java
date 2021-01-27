package co.uk.jdreamer.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModifyCartRequest {
	
	@JsonProperty
	private String username;
	
	@JsonProperty
	private long itemId;
	
	@JsonProperty
	private int quantity;

}
