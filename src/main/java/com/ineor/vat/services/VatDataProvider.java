/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ineor.vat.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author rado
 */
@Service
public class VatDataProvider implements IDataProvider {

	@Autowired
	JSONParser parser;

	JSONArray vat_data = new JSONArray();

	/**
	 * Helper for loading data only at the start of the app.
	 *
	 * @return JsonObject
	 */
	public JSONArray Data() {
		if (this.vat_data == null || this.vat_data.isEmpty()) {
			this.vat_data = this.LoadData();
		}
		return this.vat_data;
	}

	@Override
	/**
	 * Data Should be loaded from the git, but VAT service is not accessable
	 * any more, so data is loaded from some existing file. The format
	 * should be the same as it was on the service
	 */
	public JSONArray LoadData() {
		var raw_loaded_data = new JSONObject();
		var converted_data = new JSONArray();
		try {
			try( InputStream inputStream = getClass().getResourceAsStream("/vat_rates.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				String contents = reader.lines()
					.collect(Collectors.joining(System.lineSeparator()));
				raw_loaded_data = (JSONObject) this.parser.parse(contents);
			}
			if (raw_loaded_data.containsKey("rates")) {
				var vats = (JSONArray) raw_loaded_data.get("rates");
				vats = this.CleanInputData(vats);
				converted_data = vats;
			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger(VatDataProvider.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException | ParseException ex) {
			Logger.getLogger(VatDataProvider.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(VatDataProvider.class.getName()).log(Level.SEVERE, null, ex);
		}
		return converted_data;
	}

	/**
	 * Extract country name and vat rate from the raw data and add them to simplified 
	 * json array.
	 * @param JSONArray
	 * @return 
	 */
	private JSONArray CleanInputData(JSONArray json) {
		final JSONArray result = new JSONArray();
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

		//Because of usage in labmda this is in place sort and not new object
		this.SortVatJsonData(result);

		return result;
	}
	
	/**
	 * Sort simplified vat data by vat rate
	 * @param data
	 * @return Sorted JsonArray
	 */
	private JSONArray SortVatJsonData(JSONArray data) {
		Collections.sort(data, (JSONObject a, JSONObject b) -> {
			var valA = (double) a.get("vat");
			var valB = (double) b.get("vat");

			return (int) (valA - valB);
		});
		return data;
	}

	@Override
	public OptionalDouble CalculateStandardVat(JSONArray vats) {
		return vats.stream().mapToDouble(i -> (Double) ((JSONObject) i).get("vat")).average();
	}

}
