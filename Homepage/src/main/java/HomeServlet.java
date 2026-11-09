import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "HomeServlet", value = "")
public class HomeServlet extends HttpServlet {


  private String getWebappsDir() {
    String base = System.getProperty("catalina.base");
    if (base == null || base.isEmpty()) {
      base = System.getProperty("catalina.home", ".");
    }
    return base + File.separator + "webapps";
  }


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    File webappsDir = new File(getWebappsDir());
    ArrayList<String> modules = new ArrayList<>();

    if (webappsDir.isDirectory()) {
      File[] dirs = webappsDir.listFiles(File::isDirectory);
      if (dirs != null) {
        for (File d : dirs) {
          String name = d.getName();
          // skip ROOT & Tomcat defaults
          if (name.equalsIgnoreCase("ROOT") ||
                  name.equalsIgnoreCase("manager") ||
                  name.equalsIgnoreCase("host-manager")) {
            continue;
          }
          // only real webapps (must have WEB-INF)
          if (new File(d, "WEB-INF").isDirectory()) {
            modules.add(name);
          }
        }
      }
    } else {
      log("Webapps dir not found: " + webappsDir.getAbsolutePath());
    }

    // sort and expose as REQUEST attribute
    modules.sort(String.CASE_INSENSITIVE_ORDER);

    req.setAttribute("modules", modules);

    // Forward to the quiz page
    getServletContext()
            .getRequestDispatcher("/homepage.jsp")
            .forward(req, resp);
  }

}