package graphql.kickstart.autoconfigure.editor.graphiql;

import java.io.IOException;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** @author Andrew Potter */
@Slf4j
@Controller
public class ServletGraphiQLController extends GraphiQLController {

  public ServletGraphiQLController(GraphiQLProperties graphiQLProperties) {
    super(graphiQLProperties);
  }

  @Override
  @PostConstruct
  public void onceConstructed() throws IOException {
    super.onceConstructed();
  }

  @GetMapping(value = "${graphql.graphiql.mapping:/graphiql}")
  public void graphiql(
      HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable Map<String, String> params)
      throws IOException {
    response.setContentType("text/html; charset=UTF-8");
    Object csrf = request.getAttribute("_csrf");
    byte[] graphiqlBytes = super.graphiql(request.getContextPath(), params, csrf);
    response.getOutputStream().write(graphiqlBytes);
  }
}
