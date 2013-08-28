package es.uji.apps.par.services.rest;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RestResponse
{
    private Boolean success;
    private List<?> data;
    private int total;

    public RestResponse()
    {
        this(true, Collections.EMPTY_LIST, Collections.EMPTY_LIST.size());
    }

    public RestResponse(boolean success)
    {
        this(success, Collections.EMPTY_LIST, Collections.EMPTY_LIST.size());
    }

    public RestResponse(boolean success, List<?> data, int total)
    {
        this.success = success;
        this.data = data;
        this.total = total;
    }

    public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public List<?> getData()
    {
        return data;
    }

    public void setData(List<?> data)
    {
        this.data = data;
    }
}