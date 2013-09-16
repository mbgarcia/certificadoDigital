package br.com.struts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @author marcelo.garcia
 * @version 1.0
 */
public class ActionCertificacao extends DispatchAction {

	public ActionForward doInicioCertificacao(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		System.out.println("Metodo inicial da certificacao");
		
        return mapping.findForward("token");
	}

	/**
	 * Metodo que finaliza o processo de assinatura digital.
	 * Chamado por uma funcao javascript no token.jsp. 
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward doFimCertificacao(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		System.out.println("Metodo final da certificacao ");
		
        return null;
	}
}