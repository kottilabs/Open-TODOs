package de.kottilabs.todobackend.dto;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import de.kottilabs.todobackend.config.SpringFoxConfig;
import de.kottilabs.todobackend.dao.Scope;
import de.kottilabs.todobackend.dao.TodoState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "Todo response")
@Data
@EqualsAndHashCode(callSuper = true)
public class TodoResponse extends TodoRequest {

	@ApiModelProperty(notes = SpringFoxConfig.TODO_ID)
	private UUID id;

	private String scopeName;
}
