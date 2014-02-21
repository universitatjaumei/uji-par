package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventoParaSync
{
    private String id;
    private String url;

    public EventoParaSync()
    {
    }

    public EventoParaSync(String id, String url)
    {
        this.setId(id);
        this.setUrl(url);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}