package com.example.Backend.APIs.service;

import com.example.Backend.APIs.dao.InventoryDetailsDao;
import com.example.Backend.APIs.entity.InventoryDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class InventoryDetailAPIService {

    @Autowired
    private InventoryDetailsDao inventoryDetailsDao;

    public static final Integer pageNumber = 60;
    public static final Integer pageSize = 50;

    public void readCSVFile(MultipartFile multipartFile) throws Exception {
        String fileData = new String(multipartFile.getBytes());
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(',').withQuote('"').withSkipHeaderRecord();
        CSVParser csvParser = CSVParser.parse(fileData, csvFormat);
        InventoryDetails inventoryDetails = null;
        log.info("Started parsing CSV File...");
        List<InventoryDetails> inventoryDetailsList = new ArrayList<>();

        for (CSVRecord csvRecord : csvParser) {
            inventoryDetails = InventoryDetails.builder().
                    code(csvRecord.get(0))
                    .name(csvRecord.get(1))
                    .batch(csvRecord.get(2))
                    .stock(Integer.parseInt(csvRecord.get(3)))
                    .deal(Integer.parseInt(csvRecord.get(4)))
                    .free(Integer.parseInt(csvRecord.get(5)))
                    .mrp(Double.parseDouble(csvRecord.get(6)))
                    .rate(Double.parseDouble(csvRecord.get(7)))
                    .exp(csvRecord.get(8))
                    .company(csvRecord.get(9))
                    .supplier(csvRecord.get(10)).
                            build();

            inventoryDetailsList.add(inventoryDetails);
        }

        log.info("Saving all details into DB");
        inventoryDetailsDao.saveAll(inventoryDetailsList);
    }

    public List<InventoryDetails> findBySupplierIdOrName(String supplierId, String supplierName) throws Exception {
        log.info("Fetching details wrt supplier id: {}, or name: {}", supplierId, supplierName);
        Pageable p = PageRequest.of(pageNumber,pageSize);  //hardcoding this for now
        Page<InventoryDetails> list = inventoryDetailsDao.findBySupplierIdOrName(supplierId, supplierName,p);
        log.info("DEBUG LOGS: finalFetchedList size: {}", list.getSize());
        return list.stream().toList();
    }

    public List<InventoryDetails> findByProductName(String productName) throws Exception {
        log.info("Fetching details wrt productName: {}", productName);
        Pageable p = PageRequest.of(pageNumber,pageSize);  //hardcoding this for now
        Page<InventoryDetails> finalList = inventoryDetailsDao.findByProductName(productName,p);
        //Arrays.stream(inventoryDetailsList).forEach(curr -> finalList.add((InventoryDetails) curr));
        log.info("DEBUG LOGS: finalFetchedList size: {}", finalList.getSize());
        return finalList.stream().toList();
    }


    public List<InventoryDetails> findBySupplierListString(List<String> listOfSuppliers) throws Exception {
        String supplierList = join(listOfSuppliers);
        Pageable p = PageRequest.of(pageNumber,pageSize);  //hardcoding this for now
        Page<InventoryDetails> list = inventoryDetailsDao.findBySupplierList(supplierList,p);
        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
        Date currDate = sdformat.parse(new Date().toString());
        List<InventoryDetails>finalList = new ArrayList<>();
        list.stream().forEach(curr -> {
            try {
                Date date = sdformat.parse(curr.getExp());
                if(date.compareTo(currDate)<0){
                    //supplier date is less than current date, means that it is not expired
                    finalList.add(curr);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        log.info("DEBUG LOGS: finalFetchedList size: {}", list.getSize());
        return finalList;
    }

    private String join(List<String> namesList) {
        return String.join(",", namesList
                .stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()));
    }

}
