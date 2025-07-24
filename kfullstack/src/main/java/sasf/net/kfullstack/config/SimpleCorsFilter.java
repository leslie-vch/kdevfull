package sasf.net.kfullstack.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SimpleCorsFilter extends OncePerRequestFilter {


    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var method = request.getMethod();
        var path = request.getRequestURI();
        var address = request.getRemoteAddr();

        logger.info(String.format("Request %s %s from IP %s", method, path, address));

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, Content-Type, X-Auth-Token, Accept, Authorization, X-Request-With");

        filterChain.doFilter(request, response);
    }
}