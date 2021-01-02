package manj.springframework.spring5webfluxrest.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
	@Id
	private String id;

	private String firstName;
	private String lastName;
}
