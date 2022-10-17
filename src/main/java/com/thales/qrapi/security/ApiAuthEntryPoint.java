package com.thales.qrapi.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thales.qrapi.dtos.ResponseObject;

@Component
public class ApiAuthEntryPoint implements AuthenticationEntryPoint {
	
	private static final String unauthenticatedUser = "This user is unauthenticated and can not perform the requested operation.";
	
	private String transformResToJsonString(String excStr, Exception exc) {
		Gson gson = new GsonBuilder().create();
		
		return gson.toJson(new ResponseObject<String>(excStr, exc.getMessage()));
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(transformResToJsonString(unauthenticatedUser, authException));
        out.flush();
 	}
}
