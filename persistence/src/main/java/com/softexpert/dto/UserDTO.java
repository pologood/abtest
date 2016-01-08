package com.softexpert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	public String code;
	public String name;
	public String login;
	public String host;
	public String department;

}
