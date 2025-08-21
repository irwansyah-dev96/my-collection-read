package io.irwansyahdev96.readcollection.dto.readbook;

import javax.validation.constraints.NotNull;

import io.irwansyahdev96.readcollection.dto.book.BookInsertReadBookReqDto;

public class ReadBookInsertReqDto {

    @NotNull(message = "page of read is must required")
    private Integer pageOfRead;

    private BookInsertReadBookReqDto book;

    public Integer getPageOfRead() {
        return pageOfRead;
    }

    public void setPageOfRead(Integer pageOfRead) {
        this.pageOfRead = pageOfRead;
    }

    public BookInsertReadBookReqDto getBook() {
        return book;
    }

    public void setBook(BookInsertReadBookReqDto book) {
        this.book = book;
    }
    
}
