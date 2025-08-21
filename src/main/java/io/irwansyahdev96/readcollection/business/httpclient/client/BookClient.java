package io.irwansyahdev96.readcollection.business.httpclient.client;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import io.irwansyahdev96.readcollection.base.constant.Server;
import io.irwansyahdev96.readcollection.base.dao.BaseClient;
import io.irwansyahdev96.readcollection.base.dto.res.BaseTransactionResDto;
import io.irwansyahdev96.readcollection.business.httpclient.dto.BookUpdateStatusReqDto;
import io.irwansyahdev96.readcollection.business.httpclient.model.Book;
import io.irwansyahdev96.readcollection.business.httpclient.model.Status;
import io.irwansyahdev96.readcollection.util.DatetimeUtil;

@Component
public class BookClient extends BaseClient{
    
    public List<Map<String,Object>> findAll(Integer page, Integer limit){
        StringBuilder sb = new StringBuilder();

        sb.append(Server.SERVER_CORE)
        .append(Server.PATH_BOOK)
        .append(String.format("?page=%d&limit=%d", page, limit));

        Map<String,Object> map = get(sb.toString());
        if(map.isEmpty())
            return null;

        @SuppressWarnings("unchecked")
        List<Map<String,Object>> books = (List<Map<String,Object>>) map.get("data");

        if(books.isEmpty())
            return null;

        return books;
    }

    public List<Map<String,Object>> findAll(Integer page, Integer limit, String search, String status){
        StringBuilder sb = new StringBuilder();

        StringBuilder selection = new StringBuilder();
        
        if(!search.isEmpty())
            selection.append("&search=").append(search);

        if(!status.isEmpty())
            selection.append("&status=").append(status);
        
        sb.append(Server.SERVER_CORE)
        .append(Server.PATH_BOOK)
        .append(String.format("?page=%d&limit=%d", page, limit))
        .append(selection.toString());

        Map<String,Object> map = get(sb.toString());
        if(map.isEmpty())
            return null;

        @SuppressWarnings("unchecked")
        List<Map<String,Object>> books = (List<Map<String,Object>>) map.get("data");

        if(books.isEmpty())
            return null;

        return books;
    }

    public Integer countOfBook(){
        StringBuilder sb = new StringBuilder();
        
        sb.append(Server.SERVER_CORE)
        .append(Server.PATH_BOOK)
        .append(String.format("?page=%d&limit=%d", 1, 1));

        Map<String,Object> map = get(sb.toString());

        if(map.isEmpty())
            return null;

        Integer countOfbooks = (Integer) map.get("countOfData");

        return countOfbooks;
    }

    public Integer countOfBook(String search, String status){
        StringBuilder sb = new StringBuilder();

        StringBuilder selection = new StringBuilder();
        
        if(!search.isEmpty())
            selection.append("&search=").append(search);

        if(!status.isEmpty())
            selection.append("&status=").append(status);
        
        sb.append(Server.SERVER_CORE)
        .append(Server.PATH_BOOK)
        .append(String.format("?page=%d&limit=%d", 1, 1))
        .append(selection.toString());

        Map<String,Object> map = get(sb.toString());

        if(map.isEmpty())
            return null;

        Integer countOfbooks = (Integer) map.get("countOfData");

        return countOfbooks;
    }

    public Boolean isExistBook(String issbn){
        return findByPK(issbn) != null;
    }

    public Book findByPK(String issbn){
        StringBuilder sb = new StringBuilder();
        sb.append(Server.SERVER_CORE)
        .append(Server.PATH_BOOK)
        .append(String.format("/%s/issbn", issbn));

        Map<String,Object> map = get(sb.toString());
        if(map.isEmpty())
            return null;

        @SuppressWarnings("unchecked")
        Map<String,Object> bookMap = (Map<String,Object>) map.get("data");

        if(bookMap.isEmpty())
            return null;

        Book book = null;
        try {
            Status status = new Status();
            status.setStatusCode((String) bookMap.get("statusCode"));
            status.setStatusName((String) bookMap.get("statusName"));

            book = new Book();
            book.setIssbn((String) bookMap.get("issbn"));
            book.setTitle((String) bookMap.get("title"));
            book.setStatus(status);
            book.setNumberOfPage((Integer) bookMap.get("page"));
            book.setAuthorName((String) bookMap.get("authorName"));
            book.setPublisher((String) bookMap.get("publisher"));
            book.setDescription((String) bookMap.get("description"));
            book.setCategory((String) bookMap.get("category"));
            
            book.setReleaseDate(DatetimeUtil.localDateToEpochMilli(LocalDate.parse((String)bookMap.get("releaseDate"))));

        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return book;
    }

    public BaseTransactionResDto update(Book book){
        StringBuilder sb = new StringBuilder();
        sb.append(Server.SERVER_CORE)
        .append(Server.PATH_BOOK)
        .append("/update-status");

        // req data
        BookUpdateStatusReqDto reqDto = new BookUpdateStatusReqDto();
        reqDto.setIssbn(book.getIssbn());
        reqDto.setStatusCode(book.getStatus().getStatusCode());

        BaseTransactionResDto put = put(sb.toString(), reqDto);
        
        return put;
    }
}
