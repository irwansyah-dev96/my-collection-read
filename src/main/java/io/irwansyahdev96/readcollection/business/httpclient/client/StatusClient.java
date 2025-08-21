package io.irwansyahdev96.readcollection.business.httpclient.client;

import java.util.Map;

import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import io.irwansyahdev96.readcollection.base.constant.Server;
import io.irwansyahdev96.readcollection.base.dao.BaseClient;
import io.irwansyahdev96.readcollection.business.httpclient.model.Status;


@Component
public class StatusClient extends BaseClient{
    
    public Status findByPK(String statusCode){
        StringBuilder sb = new StringBuilder();
        sb.append(Server.SERVER_CORE)
        .append(Server.PATH_STATUS)
        .append(String.format("%s/status", statusCode));

        Map<String,Object> map = get(sb.toString());

        if(map.isEmpty())
            return null;

        @SuppressWarnings("unchecked")
        Map<String,Object> statusMap = (Map<String,Object>) map.get("data");

        Status status = null;
        try {
            status =  new Status();
            status.setStatusCode((String) statusMap.get("statusCode"));
            status.setStatusName((String) statusMap.get("statusName"));
            
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return status;
    }
}
