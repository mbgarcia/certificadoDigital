package br.com.certificacao;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.swing.JOptionPane;

import netscape.javascript.JSObject;


import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;

public class TokenSignerApplet extends Applet {
	private static final long serialVersionUID = 4023705496194390163L;

	private static final String PIN_CODE_PARAM = "pinCodeField";
	private static final String NOME_PARAM = "nomeField";
	private static final String APP_URL_PARAM = "appUrlField";
	private static final String SIGN_BUTTON_CAPTION_PARAM = "signButtonCaption";	
	
	private Button mSignButton;

	private SignatureManager manager = new SignatureManager();	

    /**
     * Cria e inicializa a interface grafica da applet.
     */
	public void init() {
    	criarArquivoToken();
        String signButtonCaption = this.getParameter(SIGN_BUTTON_CAPTION_PARAM);
        mSignButton = new Button(signButtonCaption);
        mSignButton.setLocation(0, 0);
        Dimension appletSize = this.getSize();
        mSignButton.setSize(appletSize);
        
        mSignButton.setBackground(new Color(190, 1, 1));
        mSignButton.setForeground(new Color(255,255,255));
        mSignButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                assinaStream();
            }
        });
        this.setLayout(null);
        this.add(mSignButton);
    }

    public void assinaStream(){
    	JSObject browserWindow = JSObject.getWindow(this);
    	JSObject mainForm = (JSObject)browserWindow.eval("document.forms[0]");
    	String pinCodeParam = getParameter(PIN_CODE_PARAM);
    	JSObject pinCodeField = (JSObject)mainForm.getMember(pinCodeParam);
    	String pinCode = (String)pinCodeField.getMember("value");

    	String nomeParam = getParameter(NOME_PARAM);
    	JSObject nomeField = (JSObject)mainForm.getMember(nomeParam);
    	String nome = (String)nomeField.getMember("value");
        
        String appParam = this.getParameter(APP_URL_PARAM);
        JSObject appField = (JSObject) mainForm.getMember(appParam);
        String appUrl = (String) appField.getMember("value");
    	
        try{
        	//Chama o servlet para leitura do pdf.
        	String urlArquivoServlet = appUrl + "/ArquivoServlet";
            URL url = new URL(urlArquivoServlet);
            HttpURLConnection connectionGet = (HttpURLConnection)url.openConnection();
            connectionGet.setRequestProperty("Request-Method", "GET");
            connectionGet.setDoInput(true);
            connectionGet.setDoOutput(false);
            connectionGet.connect();

            //Stream de LEITURA de dados do pdf
            InputStream input = connectionGet.getInputStream();

            SignatureManager manager = new SignatureManager();
            if(manager.login(pinCode, this)){
                java.security.PrivateKey key = manager.getPrivateKey();
                java.security.cert.Certificate certificate[] = manager.getCertificate();
                try{
                    PdfReader pdfReader = new PdfReader(input);
                    //Monta a conexao com o Servlet para escrever o arquivo assinado digitalmente 
                    HttpURLConnection connectionPost = (HttpURLConnection)url.openConnection();
                    connectionPost.setRequestProperty("Content-Type", "application/pdf");
                    connectionPost.setDoOutput(true);
                    connectionPost.connect();
                    OutputStream output = connectionPost.getOutputStream();
                    //Assina PDF
                    PdfStamper stamper = PdfStamper.createSignature(pdfReader, output, '\0');
                    PdfSignatureAppearance signAppearance = stamper.getSignatureAppearance();
                    signAppearance.setCrypto(key, certificate, null, PdfSignatureAppearance.WINCER_SIGNED);
                    signAppearance.setReason("Empresa S/A");
                    signAppearance.setLocation("Sao Paulo - SP");
                    signAppearance.setSignDate(Calendar.getInstance());
                    signAppearance.setCertificationLevel(1);
                    stamper.close();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connectionPost.getInputStream()));
                    String line;
                    while((line = rd.readLine()) != null){
                        System.out.println(line);
                    }
                    output.close();
                    input.close();
                    rd.close();
                    //chamada de uma funcao javascript no jsp 
                    browserWindow.call("chamaAction", new Object[] {nome});
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            } else{
                JOptionPane.showMessageDialog(this, "Erro ao assinar documento.");
            }
            input.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	
	private void criarArquivoToken(){
		try {
	        JSObject browserWindow = JSObject.getWindow(this);
	        JSObject mainForm = (JSObject) browserWindow.eval("document.forms[0]");

	        String appParam = this.getParameter(APP_URL_PARAM);
	        JSObject appField = (JSObject) mainForm.getMember(appParam);
	        String app = (String) appField.getMember("value");
	        
	        manager.createTokenFiles(app);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
}