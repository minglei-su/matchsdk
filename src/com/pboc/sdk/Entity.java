package com.pboc.sdk;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    private String id;//crccode
    private String creditcode;          // 机构信用代码
    private String sdeporgcode;         // 组织机构代码
    private String loancardcode;        // 贷款卡编码
    private String registertype;        // 登记注册类型
    private String registercode;        // 登记注册号
    private String name;                // 实体名字  中文英文  sdec,sde ee
    private String address;             // 实体地址
    private String type; //类型
    private String areacode; // areacode
    private String score;

    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    //开户许可证saccbaseliccode
    //国税地税 identity sdenationaltaxcode sdeplandtaxcode
    //country
    //ec>dispatchtimes
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    //转换为json,add/update索引时使用
    public JSONObject toJSON() {
        String nameAddrCode = "";
        if (name != null) {
            nameAddrCode += name.replaceAll("\\^","");
        }
        if (address != null) {
            nameAddrCode += "^" + address.replaceAll("\\^","");
        }
        if (areacode != null) {
            nameAddrCode += "^" + areacode.replaceAll("\\^","");
        }
        JSONObject jsonObject = JSONObject.fromObject(this);
        jsonObject.discard("areacode");
        jsonObject.discard("score");
        jsonObject.put("code_s",areacode);
        jsonObject = jsonObject.accumulate("name2_s",nameAddrCode);
        jsonObject.put("type","corp");
        return jsonObject.accumulate("TRANS_b",true);
    }

    //转换参数,查询时使用，因为entity中很多查询时不需要的字段。
    NameValuePair[] convertToNameValuePair() {
        List pair = new ArrayList();
        if (creditcode!=null) {
            pair.add(new NameValuePair("em.creditcode",creditcode));
        }
        if (sdeporgcode!=null) {
            pair.add(new NameValuePair("em.sdeporgcode",sdeporgcode));
        }
        if (registertype!=null) {
            pair.add(new NameValuePair("em.registertype",registertype));
        }
        if (registercode!=null) {
            pair.add(new NameValuePair("em.registercode",registercode));
        }
        if (name!=null) {
            pair.add(new NameValuePair("em.name",name));
        }
        if (address!=null) {
            pair.add(new NameValuePair("em.address",address));
        }
        if (areacode !=null) {
            pair.add(new NameValuePair("em.areacode", areacode));
        }
        int size = pair.size();
        NameValuePair[] nameValuePairs = new NameValuePair[size];
        for (int index=0;index < size;index ++) {
            nameValuePairs[index] = (NameValuePair)pair.get(index);
        }
        return nameValuePairs;
    }
    public Entity() {

    }
    public Entity(ResultSet resultSet) {
        try {
            ResultSetMetaData meta = resultSet.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnLabel(i);
                if (columnName ==null || columnName.trim().equals("")) {
                    columnName = meta.getColumnName(i);
                }

                if ("id".equalsIgnoreCase(columnName)) {
                    id = resultSet.getString("id");
                } else if ("creditcode".equalsIgnoreCase(columnName)) {
                    creditcode = resultSet.getString("creditcode");
                } else if ("sdeporgcode".equalsIgnoreCase(columnName)) {
                    sdeporgcode = resultSet.getString("sdeporgcode");
                } else if ("loancardcode".equalsIgnoreCase(columnName)) {
                    loancardcode = resultSet.getString("loancardcode");
                } else if ("registertype".equalsIgnoreCase(columnName)) {
                    registertype = resultSet.getString("registertype");
                } else if ("registercode".equalsIgnoreCase(columnName)) {
                    registercode = resultSet.getString("registercode");
                } else if ("name".equalsIgnoreCase(columnName)) {
                    name = resultSet.getString("name");
                } else if ("address".equalsIgnoreCase(columnName)) {
                    address = resultSet.getString("address");
                } else if ("areacode".equalsIgnoreCase(columnName)) {
                    areacode = resultSet.getString("areacode");
                } else {
                    throw new RuntimeException("Unknown column name: " + columnName);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to create Entity object from ResultSet" + resultSet, e);
        }
    }

    public Entity(String[] fields) {
        id              = fields[0];
        creditcode = fields[1];
        sdeporgcode = fields[2];
        loancardcode = fields[3];
        registertype = fields[4];
        registercode = fields[5];
        name            = fields[6];
        address         = fields[7];
        areacode = fields[8];
    }

    public Entity(String name) {
        this.name = name;
    }
    public Entity(String id, String name, String address, String areacode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.areacode = areacode;
    }

    public String getCreditcode() {
        return creditcode;
    }

    public void setCreditcode(String creditcode) {
        this.creditcode = creditcode;
    }

    public String getSdeporgcode() {
        return sdeporgcode;
    }

    public void setSdeporgcode(String sdeporgcode) {
        this.sdeporgcode = sdeporgcode;
    }

    public String getLoancardcode() {
        return loancardcode;
    }

    public void setLoancardcode(String loancardcode) {
        this.loancardcode = loancardcode;
    }

    public String getRegistertype() {
        return registertype;
    }

    public void setRegistertype(String registertype) {
        this.registertype = registertype;
    }

    public String getRegistercode() {
        return registercode;
    }

    public void setRegistercode(String registercode) {
        this.registercode = registercode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
