/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ineor.vat;

import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author rado
 */
@Configuration
public class Config {
	JSONParser parser;
	
	@Bean
	JSONParser parser (){
		return new JSONParser();
	}
}
