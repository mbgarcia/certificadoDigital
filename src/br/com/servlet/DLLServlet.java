package br.com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DLLServlet extends HttpServlet {
	private static final long serialVersionUID = -3363073130063055864L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DO-post");
		try {
			
			InputStream url  = this.getServletContext().getResourceAsStream("\\dll\\ngp11v211.dll");
			OutputStream output = response.getOutputStream();

			int b = 0;
			while (-1 != ((b = url.read()))) {
				output.write(b);
			}
			output.close();
			url.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doGet(request, response);
	}
}
