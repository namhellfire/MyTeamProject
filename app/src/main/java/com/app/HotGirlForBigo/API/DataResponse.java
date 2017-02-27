package com.app.hotgirlforbigo.API;

/**
 * Created by nguyennam on 2/11/17.
 */

public class DataResponse {

    StringBuilder dataRes;
    int statusCode;
    String message;
    String response;
    String token;
    String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public StringBuilder getDataRes() {
        return dataRes;
    }

    public void setDataRes(StringBuilder dataRes) {
        this.dataRes = dataRes;
    }

}
