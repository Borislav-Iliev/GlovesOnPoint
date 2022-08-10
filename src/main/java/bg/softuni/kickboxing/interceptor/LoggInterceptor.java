package bg.softuni.kickboxing.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class LoggInterceptor implements HandlerInterceptor {

    private final PrintWriter printWriter;

    public LoggInterceptor(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.printWriter.println("-".repeat(20));
        this.printWriter.println(request.getRequestURI() + " " + request.getMethod() + " " +
                response.getStatus() + " " + LocalDateTime.now());
        this.printWriter.flush();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        this.printWriter.println(request.getRequestURI() + " " + request.getMethod() + " " +
                response.getStatus() + " " + LocalDateTime.now());
        this.printWriter.flush();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.printWriter.println(request.getRequestURI() + " " + request.getMethod() + " " +
                response.getStatus() + " " + request.getUserPrincipal().getName() + " " + LocalDateTime.now());
        this.printWriter.println("-".repeat(20));
        this.printWriter.flush();
    }
}
