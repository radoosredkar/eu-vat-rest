/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ineor.vat.controllers;

import com.ineor.vat.services.VatDataProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author rado
 */
@RestController
public class VatsController {

	@Autowired
	VatDataProvider vatDataProvider;

	@GetMapping(path = "/vats", produces = "application/json")
	public JSONArray vats() throws ParseException {
		JSONArray vats = null;
		try {
			vats = vatDataProvider.Data();
		} catch (Exception ex) {
			Logger.getLogger(VatsController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return vats;
	}

	@GetMapping(path = "/vats/lowest/{no_of_displayed}", produces = "application/json")
	public List vats_lowest(@PathVariable int no_of_displayed) throws ParseException {
		List result = new ArrayList();
		try {
			var vats = vatDataProvider.Data();
			if (vats != null) {
				result = vats.subList(0, no_of_displayed);
			}
		} catch (Exception ex) {
			Logger.getLogger(VatsController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return result.subList(0, no_of_displayed);
	}

	@GetMapping(path = "/vats/highest/{no_of_displayed}", produces = "application/json")
	public List vats_highest(@PathVariable int no_of_displayed) throws ParseException {
		List result = new ArrayList();
		try {
			var vats = vatDataProvider.Data();
			if (vats != null) {
				result = vats.subList(vats.size() - no_of_displayed - 1, vats.size() - 1);
				Collections.reverse(result);
			}

		} catch (Exception ex) {
			Logger.getLogger(VatsController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return result;
	}
}
