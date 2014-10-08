package es.uji.apps.par.exceptions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseMessage
{
    private boolean result;
    private String message;

    public ResponseMessage(boolean result, String message)
    {
        this.result = result;
        this.message = message;
    }

    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public ResponseMessage()
    {

    }
}
