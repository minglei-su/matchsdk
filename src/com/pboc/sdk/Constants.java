package com.pboc.sdk;

/**
 * Created by mingleis on 2014/12/8.
 */
public class Constants {
    static String CONFIG_PATH  = "/server.properties";
    static String INDEX_PATH = "/collection1/dataimport?";
    static String QUERY_PATH = "/em";
    static final String UPDATE_SOLR="/update?wt=json&commit=true";
    static final String FLUSH_SOLR = UPDATE_SOLR + "&commit=true";

    static final String RESP_HEAD = "responseHeader";
    static final String RESP_RESP = "response";
    static final String RESP_STAT = "status";
    static final String RESP_DOCS = "docs";
    static final String RESP_NUMF = "numFound";
}
