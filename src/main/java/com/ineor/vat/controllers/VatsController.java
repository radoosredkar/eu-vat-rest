/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ineor.vat.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.util.ResourceUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author rado
 */
@RestController
public class VatsController {

	@GetMapping(path = "/vats", produces = "application/json")
	public String vats() throws ParseException {
		JSONArray vats = null;
		try {
			File file = ResourceUtils.getFile("classpath:vat_rates.json");
			JSONParser parser = new JSONParser();
			var json = (JSONObject) parser.parse(new FileReader(file));
			vats = (JSONArray) json.get("rates");
			vats = CleanInputData((JSONArray) vats);

		} catch (IOException e) {

		}
		return vats.toString();
	}

	private JSONArray CleanInputData(JSONArray json) {
		JSONArray result = new JSONArray();
		json.forEach(item -> {
			var country = ((JSONObject) item).get("name");
			var periods = ((JSONObject) item).get("periods");
			var rate = ((JSONObject) ((JSONObject) ((JSONArray) periods).get(0)).get("rates"));
			var vat = ((JSONObject) rate).get("standard");
			JSONObject itm = new JSONObject();
			itm.put("country", country);
			itm.put("vat", vat);
			result.add(itm);
		});
		Collections.sort(result, new Comparator<JSONObject>() {

			public int compare(JSONObject a, JSONObject b) {
				double valA = -1.0;
				double valB = -1.0;

				valA = (double) a.get("vat");
				valB = (double) b.get("vat");

				return (int) (valA - valB);
			}
		});
		return result;
	}
}

class SortByRate implements Comparator<JSONObject> {

	@Override
	public int compare(JSONObject arg0, JSONObject arg1) {
		return -1;
	}

}
