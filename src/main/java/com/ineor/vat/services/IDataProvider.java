/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ineor.vat.services;

import org.json.simple.JSONArray;

/**
 *
 * @author rado
 */
public interface IDataProvider {
	JSONArray LoadData();
	public JSONArray Data();
}
