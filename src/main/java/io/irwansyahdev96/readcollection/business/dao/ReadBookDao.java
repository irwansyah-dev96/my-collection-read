package io.irwansyahdev96.readcollection.business.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.irwansyahdev96.readcollection.base.dao.BaseDao;
import io.irwansyahdev96.readcollection.model.ReadBook;

@Repository
public class ReadBookDao extends BaseDao{
    
    @SuppressWarnings("unchecked")
    public List<ReadBook> getByBookId(String bookId){
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM tb_read_book WHERE book_id = :bookId");

        List<ReadBook> lists = getEM().createNativeQuery(sql.toString(),ReadBook.class)
        .setParameter("bookId", bookId)
        .getResultList();

        return lists;
    }
}
