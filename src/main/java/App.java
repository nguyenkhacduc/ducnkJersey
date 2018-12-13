import com.example.employ.EmployManage;
import com.example.filter.Limited;
import com.example.filter.Logg;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class App {
    public static void main(String[] args) {
        String port = System.getenv("PORT") != null ? System.getenv("PORT") : "8080";
        Server server = new Server(Integer.parseInt(port));
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        final EnumSet<DispatcherType> REQUEST_SCOPE = EnumSet.of(DispatcherType.REQUEST);
        contextHandler.addFilter(Limited.class, "/*", REQUEST_SCOPE);
        contextHandler.addFilter(Logg.class, "/*", REQUEST_SCOPE);

        ServletHolder servletHolder = new ServletHolder(new ServletContainer());
        servletHolder.setInitParameter("jersey.config.server.provider.classnames", EmployManage.class.getName());
        servletHolder.setInitOrder(0);
        contextHandler.addServlet(servletHolder, "/*");

        try {
            System.out.println("======START=======");
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
