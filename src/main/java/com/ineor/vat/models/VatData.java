/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ineor.vat.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author rado
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VatData {
	private String details;
	private String version;
}
