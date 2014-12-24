import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import es.uji.apps.par.config.Configuration;
import org.junit.Ignore;

@Ignore
public class BaseResourceTest extends JerseyTest
{
    public BaseResourceTest(WebAppDescriptor build)
    {
        super(build);

        desactivarAppenderLogGmail();
    }

    private void desactivarAppenderLogGmail()
    {
        Configuration.desactivaLogGmail();
    }


    /*@Override
    protected int getPort(int defaultPort) {
        return 9002;
    }*/
}