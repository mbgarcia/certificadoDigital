package br.com.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que gerencia o armazenamento do arquivo assinado.
 *  
 * @author marcelo.garcia
 *
 */
public class ArquivoServlet extends HttpServlet {
	private static final long serialVersionUID = 4102000674926121528L;

	/**
	 * Recupera um arquivo e envia para o cliente (por ex. um applet)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ArquivoServlet - doGet");
		lerArquivo(request, response);
	}

	/**
	 * Recebe o arquivo assinado e grava em disco no servidor.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ArquivoServlet - doPost");
		try {
			
			InputStream input = request.getInputStream();

			File file = new File("D:\\temp\\ResumoServletCriado.pdf");
			file.createNewFile();
			FileOutputStream output = new FileOutputStream(file);

			int b = 0;
			while (-1 != ((b = input.read()))) {
				output.write(b);
			}
			output.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recupera um arquivo pdf
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void lerArquivo (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ArquivoServlet - lerArquivo");
		try {
			response.setContentType("application/pdf");
			InputStream is = new FileInputStream("D:\\temp\\Resumo.pdf");
			
			geraOutput(response, is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gera saída do arquivo PDF gerado
	 * 
	 * @param response
	 * @param is
	 * @throws IOException
	 */
	private void geraOutput(HttpServletResponse response, InputStream is) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		try {
			response.setCharacterEncoding("iso-8859-1");
			int c;
			while ((c = is.read()) != -1) {
	             byte[] b = new byte[1];
	             b[0] = (byte)c;
	             out.write(b);
			}
		} finally {
			if (is != null)
				is.close();
			if (out != null){
				out.flush();
				out.close();
			}
		}
	}	
}
