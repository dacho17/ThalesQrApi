package com.thales.qrapi.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thales.qrapi.dtos.ResponseObject;

public class ApiAccessDeniedHandler implements AccessDeniedHandler {
	
	private static final String unauthorizedUser = "This user is unauthorized to perform the requested operation.";
	
	private String transformResToJsonString(String excStr, Exception exc) {
		Gson gson = new GsonBuilder().create();
		
		return gson.toJson(new ResponseObject<String>(excStr, exc.getMessage()));
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print(transformResToJsonString(unauthorizedUser, accessDeniedException));
        out.flush();
	}
}
