package com.crafaelsouza.crm.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The Class BeanConfig.
 * 
 * @author Carlos Souza
 */
@Configuration
public class BeanConfig {

	/**
	 * Validator factory bean.
	 *
	 * @return the validator
	 */
	@Bean
	public Validator validatorFactoryBean() {
	   return Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	/**
	 * Object mapper builder.
	 *
	 * @return the jackson 2 object mapper builder
	 */
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
	    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
	    builder.serializationInclusion(JsonInclude.Include.NON_NULL);
	    return builder;
	}
}
