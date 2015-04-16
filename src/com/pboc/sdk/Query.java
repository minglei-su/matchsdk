package com.pboc.sdk;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.spi.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by mingleis on 2014/12/4.
 */
public class Query {
    private String serverurl = null;
    private String rules = null;
    private String fl = null;
    private HttpClient client = null;
    public Query( ) {
        try {
            Properties properties = new Properties();
            InputStream inputStream = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+Constants.CONFIG_PATH));
            properties.load(inputStream);
            serverurl = properties.getProperty("server.url");
            rules = properties.getProperty("match.rule");
            fl = properties.getProperty("fl");
            client = new HttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据查询条件查询符合条件的实体
     * @param entity
     * @param rows
     * @return arraylist ,each element is Map,which the key is entity's atttibute and the value is entity's value
     * @throws Exception  返回个数//null
     *///public Entity[] queryMatchedByPost(Entity entity,int num)

    public Entity[] queryMatchedByPost(Entity entity,int rows){
        //init post method
        String methodurl = serverurl + Constants.QUERY_PATH;

        URI uri = null;
        try {
            uri = new URI(methodurl,false,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        PostMethod method = new PostMethod(uri.toString());
        method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        //init post parameter
        NameValuePair[] entitypair = entity.convertToNameValuePair();
        NameValuePair[] pairs = new NameValuePair[entitypair.length + 4];
        for (int i = 0; i < entitypair.length; i ++) {
            pairs[i] = entitypair[i];
        }
        pairs[entitypair.length ] = new NameValuePair("fl",fl);
        pairs[entitypair.length + 1] = new NameValuePair("rows",String.valueOf(rows));
        pairs[entitypair.length + 2] = new NameValuePair("em.rules",rules);
        pairs[entitypair.length + 3] = new NameValuePair("wt","json");
        method.setRequestBody(pairs);

        try {
            client.executeMethod(method);
            return convertJson2Entity(method.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return null;
    }
    /**
     *
     */
    private Entity[] convertJson2Entity(String  inputStream) {
        JSONObject jsonObject = JSONObject.fromObject(inputStream);
        if (jsonObject.getJSONObject(Constants.RESP_HEAD).getInt("status") != 0) {
            return null;
        } else {
            JSONObject response = jsonObject.getJSONObject("response");
            int numFound = 0;
            if ((numFound = Integer.valueOf(response.get("numFound").toString()).intValue()) > 0) {
                Entity entity[] = new Entity[numFound];
                JSONArray jsonArray = response.getJSONArray("docs");
                for (int index = 0; index < jsonArray.size(); index++) {
                    JSONObject object = jsonArray.getJSONObject(index);
                    if (object.get("code_s") != null) {
                        object = object.accumulate("areacode",object.getString("code_s"));
                    }
                    Entity entity1 = (Entity) JSONObject.toBean(object, Entity.class);
                    entity[index] = entity1;
                }
                return entity;
            } else {
                return null;
            }
        }
    }

    /**
     * 增加/更新索引，以crccode为主键
     * @param entity
     */
    public boolean add(Entity entity) {
        String uris = serverurl + Constants.UPDATE_SOLR;
        URI uri = null;
        try {
             uri = new URI(uris,false,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject object = entity.toJSON();
        JSONArray array = new JSONArray();
        array.add(object);
        PostMethod method = new PostMethod(uri.toString());
        method.setRequestHeader("Content-Type","application/json;charset=utf-8");
        try {
            method.setRequestEntity(new StringRequestEntity(array.toString(),null,"utf-8"));
            client.executeMethod(method);
            return returnHttpResult(method.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            method.releaseConnection();
            return false;
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * 根据docId删除索引，docId可设置为CRCcode
     * @param id
     * @return
     */
    public boolean deleteIndexById(String id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("delete",jsonObject);
        String url = serverurl + Constants.UPDATE_SOLR;
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Content-Type","application/json;charset=utf-8");
        postMethod.setRequestEntity(new StringRequestEntity(jsonObject1.toString()));
        try {
            client.executeMethod(postMethod);
            return returnHttpResult(postMethod.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            postMethod.releaseConnection();
        }
    }
    private boolean returnHttpResult(String response) {
        JSONObject jsonObject = JSONObject.fromObject(response);
        int status = jsonObject.getJSONObject("responseHeader").getInt("status");
        if (status == 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 提交缓存,成功返回true，错误返回false,批量时使用
     */
    public boolean flush() {
        GetMethod getMethod = new GetMethod(serverurl + Constants.FLUSH_SOLR);
        try {
            client.executeMethod(getMethod);
            JSONObject jsonObject = JSONObject.fromObject(getMethod.getResponseBodyAsString());
            int status = jsonObject.getJSONObject("responseHeader").getInt("status");
            if (status == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
