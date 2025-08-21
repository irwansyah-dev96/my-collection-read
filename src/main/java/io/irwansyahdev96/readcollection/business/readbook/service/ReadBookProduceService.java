package io.irwansyahdev96.readcollection.business.readbook.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.irwansyahdev96.readcollection.base.dto.res.BaseResListDto;
import io.irwansyahdev96.readcollection.base.dto.res.BaseResSingleDto;
import io.irwansyahdev96.readcollection.business.readbook.dao.ReadBookDao;
import io.irwansyahdev96.readcollection.business.readbook.model.ReadBook;

@Service
public class ReadBookProduceService {

    @Autowired
    private ReadBookDao readBookDao;

    public BaseResSingleDto<Map<String, Boolean>> isExistReadBook(String issbn){
        Boolean existReadBook = readBookDao.isExistReadBook(issbn);

        Map<String, Boolean> readBookData = new HashMap<>();
        readBookData.put("isReadbook", existReadBook);

        BaseResSingleDto<Map<String, Boolean>> baseResSingleDto = new BaseResSingleDto<>();
        baseResSingleDto.setData(readBookData);

        return baseResSingleDto;
    }
}
