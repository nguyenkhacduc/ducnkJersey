import com.example.employ.EmployManage;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {
    public static void main(String[] args) {
        Server server = new Server(8080);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

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