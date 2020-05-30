package com.morbis.api.config;

import com.morbis.model.member.entity.MemberRole;
import com.morbis.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    private final Set<String> memberRoles;

    private final static List<String> USED_METHODS = Stream.of("POST", "GET", "DELETE", "PUT")
            .collect(Collectors.toList());

    public AuthFilter(AuthService authService) {
        this.authService = authService;
        memberRoles = Stream.of(MemberRole.values()).map(Enum::toString).collect(Collectors.toSet());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!USED_METHODS.contains(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("authorization");

        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                System.out.println("Header: " + name + " value:" + request.getHeader(name));
            }
        }

        MemberRole requiredRole = getRequiredRole(request);

        if (requiredRole == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!authService.authorize(authorizationHeader, requiredRole)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private MemberRole getRequiredRole(HttpServletRequest request) {
        StringBuffer req = request.getRequestURL();
        int startOfRole = req.indexOf("api/") + 4;
        String roleString = "NA";
        
        if (req.length() > startOfRole && req.indexOf("/", startOfRole) != -1 )
            roleString = req.substring(startOfRole, req.indexOf("/", startOfRole))
                    .toUpperCase()
                    .replace("-", "_");

        if (memberRoles.contains(roleString))
            return MemberRole.valueOf(roleString);

        return null;
    }
}
