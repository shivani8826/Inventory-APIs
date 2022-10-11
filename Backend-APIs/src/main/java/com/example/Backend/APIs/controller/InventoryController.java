package com.example.Backend.APIs.controller;

import com.example.Backend.APIs.entity.InventoryDetails;
import com.example.Backend.APIs.service.InventoryDetailAPIService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/inventory/")
@Log4j2
public class InventoryController {

    @Autowired
    private InventoryDetailAPIService apiService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String saveIntoInventoryDetailDB(@RequestParam("sampleInventoryFile") MultipartFile inventoryFile) {
        try {
            apiService.readCSVFile(inventoryFile);
            return "File Successfully parsed and stored into the DB!!";
        } catch (Exception e) {
            log.error("Some exception occurred : ", e);
        }
        return "Some Exception Occurred!!";
    }

    @RequestMapping(value = "/fetchBySupplierIdOrName", method = RequestMethod.GET)
    @ResponseBody
    public List<InventoryDetails> fetchBySupplierIdOrName(@RequestParam String supplierId, @RequestParam String supplierName) {
        try {
            return apiService.findBySupplierIdOrName(supplierId, supplierName);
        } catch (Exception e) {
            log.error("Some exception occurred: ", e);
        }
        return null;
    }

    @RequestMapping(value = "/fetchByProductName", method = RequestMethod.GET)
    @ResponseBody
    public List<InventoryDetails> fetchByProductName(@RequestParam String productName) {
        try {
            return apiService.findByProductName(productName);
        } catch (Exception e) {
            log.error("Some exception occurred: ", e);
        }
        return null;
    }

    @RequestMapping(value = "/fetchBySupplierList", method = RequestMethod.GET)
    @ResponseBody
    public List<InventoryDetails> fetchBySuppliersName(@RequestParam List<String> suppliersName) {
        try {
            return apiService.findBySupplierListString(suppliersName);
        } catch (Exception e) {
            log.error("Some exception occurred: ", e);
        }
        return null;
    }



}
