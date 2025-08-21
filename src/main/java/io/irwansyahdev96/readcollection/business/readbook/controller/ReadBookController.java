package io.irwansyahdev96.readcollection.business.readbook.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.irwansyahdev96.readcollection.base.dto.res.BaseResListDto;
import io.irwansyahdev96.readcollection.base.dto.res.BaseTransactionResDto;
import io.irwansyahdev96.readcollection.business.readbook.dto.ReadBookInsertReqDto;
import io.irwansyahdev96.readcollection.business.readbook.service.ReadBookService;

@RestController
@RequestMapping("read-books")
public class ReadBookController {
    
    @Autowired
    private ReadBookService readBookService;

    @GetMapping("/{issbn}/issbn")
    public ResponseEntity<BaseResListDto<?>> getAllByIssbn(@PathVariable("issbn") String issbn,Integer page, Integer limit){
        BaseResListDto<?> baseResListDto = readBookService.getAllByIssbn(issbn,page, limit);
        
        return new ResponseEntity<>(baseResListDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BaseResListDto<?>> getAll(@RequestParam(value="search",required = false,defaultValue = "") String search, 
                                                            @RequestParam(value="status",required = false,defaultValue = "") String status,
                                                             Integer page, Integer limit){
        BaseResListDto<?> baseResListDto = null;

        if(search.isEmpty() && status.isEmpty()){
            baseResListDto = readBookService.getAll(page, limit);
        }else{
            baseResListDto = readBookService.getAll(page, limit, search, status);
        }
        
        return new ResponseEntity<>(baseResListDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseTransactionResDto> add(@Valid @RequestBody ReadBookInsertReqDto readBookInsertReqDto){
        BaseTransactionResDto baseInsertResDto = readBookService.readingByPage(readBookInsertReqDto);

        return new ResponseEntity<>(baseInsertResDto, HttpStatus.CREATED);
    }

}
