package io.irwansyahdev96.readcollection.business.readbook.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import io.irwansyahdev96.readcollection.base.dao.BaseDao;
import io.irwansyahdev96.readcollection.business.readbook.model.ReadBook;
import io.irwansyahdev96.readcollection.util.DatetimeUtil;

@Repository
public class ReadBookDao extends BaseDao{

    public Map<String, Object> findLastByIssbn(String issbn){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT trb.date_of_read, trb.page_of_read, trb.note, trb.issbn ")
        .append("FROM tb_read_book trb ")
        .append("WHERE trb.issbn = :issbn ")
        .append("ORDER BY trb.date_of_read DESC ");

        Map<String, Object> result = new LinkedHashMap<>();
        try {
            List<?> obj = getEM()
                .createNativeQuery(sql.toString())
                .setParameter("issbn", issbn)
                .getResultList();

            if(obj.isEmpty())
                return result;

                
            
            Object[] o = (Object[]) obj.get(0);
            
            
            result.put("dateOfRead", o[0]);
            result.put("pageOfRead", (Integer) o[1]);
            result.put("note", o[2]);
            result.put("issbn", o[3]);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
 
    public boolean isExistReadBook(String issbn){
        return count(ReadBook.class, Map.of("issbn", issbn)) > 0;
    }

    public List<Map<String, Object>> findAllByIssbn(String issbn, Integer page, Integer data){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT trb.date_of_read, trb.page_of_read, trb.note ")
        .append("FROM tb_read_book trb ")
        .append("WHERE trb.issbn = :issbn ")
        .append("ORDER BY trb.date_of_read ");

        List<Map<String, Object>> result = new LinkedList<>();
        try {
            List<?> resultList = getEM()
                .createNativeQuery(sql.toString())
                .setParameter("issbn", issbn)
                .setFirstResult((page - 1) * data)   // offset
                .setMaxResults(data)
                .getResultList();

            for (Object obj : resultList) {
                Object[] o = (Object[]) obj;
                
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("dateOfRead", DatetimeUtil.epochMillisToDate(((BigInteger)o[0]).longValue(), LocalDateTime.class));
                map.put("pageOfRead", o[1]);
                map.put("note", o[2]);
                result.add(map);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
