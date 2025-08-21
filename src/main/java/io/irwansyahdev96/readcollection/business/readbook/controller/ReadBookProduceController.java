package io.irwansyahdev96.readcollection.business.readbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.irwansyahdev96.readcollection.base.dto.res.BaseResSingleDto;
import io.irwansyahdev96.readcollection.business.readbook.service.ReadBookProduceService;

@RestController
@RequestMapping("read-books/produce")
public class ReadBookProduceController {
    
    @Autowired
    private ReadBookProduceService readBookService;

    @GetMapping("/{issbn}/readbook")
    public ResponseEntity<BaseResSingleDto<?>> getAllByIssbn(@PathVariable("issbn") String issbn){
        BaseResSingleDto<?> baseResListDto = readBookService.isExistReadBook(issbn);
        
        return new ResponseEntity<>(baseResListDto, HttpStatus.OK);
    }


}
