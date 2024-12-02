package com.apgautomation.utility.serverutility;

/**
 * Created by Android1 on 9/11/2017.
 */

public class Response {

    public  enum  msg {OK,FAILED};
    public String responseMessage, responseClientMessage="";
    public Object responseBody;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseClientMessage() {
        return responseClientMessage;
    }

    public void setResponseClientMessage(String responseClientMessage) {
        this.responseClientMessage = responseClientMessage;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }


}
