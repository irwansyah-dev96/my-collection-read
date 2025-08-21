package io.irwansyahdev96.readcollection.business.readbook.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.server.ResponseStatusException;

import io.irwansyahdev96.readcollection.base.constant.Message;
import io.irwansyahdev96.readcollection.base.dto.res.BaseResListDto;
import io.irwansyahdev96.readcollection.base.dto.res.BaseTransactionResDto;
import io.irwansyahdev96.readcollection.base.dto.validation.ValidationRuntimeException;
import io.irwansyahdev96.readcollection.business.httpclient.client.BookClient;
import io.irwansyahdev96.readcollection.business.httpclient.model.Book;
import io.irwansyahdev96.readcollection.business.httpclient.model.Status;
import io.irwansyahdev96.readcollection.business.readbook.dao.ReadBookDao;
import io.irwansyahdev96.readcollection.business.readbook.dto.ReadBookInsertReqDto;
import io.irwansyahdev96.readcollection.business.readbook.model.ReadBook;

@Service
public class ReadBookService {

    @Autowired
    private BookClient bookClient;

    @Autowired
    private ReadBookDao readBookDao;

    public BaseResListDto<Map<String, Object>> getAllByIssbn(String issbn, Integer page, Integer limit){
        List<Map<String,Object>> all = readBookDao.findAllByIssbn(issbn, page, limit);

        BaseResListDto<Map<String,Object>> baseResListDto = new BaseResListDto<>();
        baseResListDto.setData(all);
        baseResListDto.setCountOfData(readBookDao.count(ReadBook.class));
        baseResListDto.setLimit(limit);
        baseResListDto.setPage(page);

        return baseResListDto;
    }

    public BaseResListDto<Map<String, Object>> getAll(Integer page, Integer limit){
        List<Map<String,Object>> all = bookClient.findAll(page, limit);

        for (int i = 0; i < all.size(); i++) {
            Map<String,Object> data = all.get(i);
            String issbn = (String) data.get("issbn"); 

            Integer count = readBookDao.count(ReadBook.class, Map.of("issbn", issbn));
            data.put("totalOfReads", count);

            all.set(i, data);
        }

        BaseResListDto<Map<String,Object>> baseResListDto = new BaseResListDto<>();
        baseResListDto.setData(all);
        baseResListDto.setCountOfData(bookClient.countOfBook());
        baseResListDto.setLimit(limit);
        baseResListDto.setPage(page);

        return baseResListDto;
    }

    public BaseResListDto<Map<String, Object>> getAll(Integer page, Integer limit, String search, String status){
        List<Map<String,Object>> all = bookClient.findAll(page, limit, search, status);

        for (Map<String,Object> data : all) {
            
        }

        BaseResListDto<Map<String,Object>> baseResListDto = new BaseResListDto<>();
        baseResListDto.setData(all);
        baseResListDto.setCountOfData(bookClient.countOfBook());
        baseResListDto.setLimit(limit);
        baseResListDto.setPage(page);

        return baseResListDto;
    }

    /**
     * 
     * validation - request forbidden (400)
     * - Issbn is must required: v
     * - page of read is must required: v
     * 
     * - data book isnt available: v
     * - page of read isn't change: v
     * - Status code and data read book isn't match -> (data > 0 == ! 'R' || data = 0 ==  ! 'N' ): v
     * - Page of read out of reach: v
     * 
     * - reading complete if page of read to equal max book: v
     * - reading read if page of read to unless max book: v
     
     * - book has been complete(max book) and is read false: v
     * 
     * @param readBookInsertReqDto
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    public BaseTransactionResDto readingByPage(ReadBookInsertReqDto readBookInsertReqDto){
        BaseTransactionResDto baseInsertResDto = new BaseTransactionResDto();

        String issbn = readBookInsertReqDto.getIssbn();
        Integer pageOfRead = readBookInsertReqDto.getPageOfRead();
        String note = readBookInsertReqDto.getNote();
        Boolean isReread = readBookInsertReqDto.getIsReread();
        isReread = (isReread != null) ?isReread:false;

        // check issbn is not exist
        if(!bookClient.isExistBook(issbn)){ // receiver
            BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(readBookInsertReqDto, "readBookInsertReqDto");
            bindingResult.rejectValue("issbn", "empty", "Issbn isn't available");

            throw new ValidationRuntimeException(bindingResult);
        }

        Book bookByCore = bookClient.findByPK(issbn); // receiver
        Integer numberOfPageBook = bookByCore.getNumberOfPage();

        Status statusForUpdate = bookByCore.getStatus();
        String statusCodeForUpdate = statusForUpdate.getStatusCode();

        /*  no reread book validation */
        if(!isReread)
            // validation complete read
            if(statusCodeForUpdate.equals("C"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book has been complete");
        else // reread book
            statusCodeForUpdate = "R";
        
        /*  global validation */        
        // check data read book and status book update not match (data > 0 == ! 'R' || data = 0 ==  ! 'N' )
        if(readBookDao.isExistReadBook(issbn) && !statusCodeForUpdate.equals("R") ||
            !readBookDao.isExistReadBook(issbn) && !statusCodeForUpdate.equals("N"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status code and read-book data isn't match");

        // validation page 
        if(pageOfRead > numberOfPageBook && pageOfRead < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page of read out of reach");
        
        // check if last data is same
        Map<String,Object> lastByIssbn = readBookDao.findLastByIssbn(issbn);
        if(pageOfRead.equals(lastByIssbn.get("pageOfRead")) && lastByIssbn.size() > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page of read isn't change");

        ReadBook readBook = new ReadBook();
        readBook.setPageOfRead(pageOfRead);
        readBook.setDateOfRead(System.currentTimeMillis());
        readBook.setBookIssbn(bookByCore.getIssbn());
        readBook.setNote(note != null?note:"No comment");
            
        ReadBook readBookInsert = readBookDao.save(readBook);

        if(readBookInsert != null){     

            // if page equal to number of page
            if(pageOfRead.equals(numberOfPageBook))
                statusCodeForUpdate = "C";
            
            // read book isn't exist
            if(!readBookDao.isExistReadBook(issbn))
                statusCodeForUpdate = "R";

            String statusCodeBookFirst = bookByCore.getStatus().getStatusCode();
            if(!statusCodeBookFirst.equals(statusCodeForUpdate)){
                statusForUpdate.setStatusCode(statusCodeForUpdate);
                bookByCore.setStatus(statusForUpdate); // receiver
                bookClient.update(bookByCore); // receiver
            }
            
            baseInsertResDto.setId(issbn);
            baseInsertResDto.setMessage(Message.SUCCESS_SAVE.getMessage());
        }else
            throw new RuntimeException("Failed to save");
        
        return baseInsertResDto;
    }

}
