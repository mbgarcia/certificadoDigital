package br.com.certificacao;

import java.applet.Applet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * Classe responsável por gerenciar a assinatura contida em 
 * um token. É necessário efetuar o login no dispositivo
 * antes de utilizá-lo.
 */
public class SignatureManager {

	private KeyStore keyStore;
	private char[] PIN;
 	
	private final String CONFIG_DIR_NAME = "C:\\assinatura\\";
	private final String CONFIG_FILE_NAME = "pkcs11.cfg";
	private final String WINDOWS_DIR_NAME = "C:\\Windows\\System32\\";
	private final String DLL_FILE_NAME = "ngp11v211.dll";
	
	private String arquivoDLLApp = "";
 	
	
	public void createTokenFiles(String urlApp){
		criarDiretorio();
		criarDLL(urlApp);
		createConfigFile();
	}
	
	private void criarDiretorio(){
		try {
			File dir = new File(CONFIG_DIR_NAME);
			if (!dir.exists()){
				dir.mkdirs();
			}
		}catch (Exception e) {
		}
	}
	
	private void createConfigFile(){
		try {
			File file = new File(CONFIG_DIR_NAME + CONFIG_FILE_NAME);
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write("name = PKCS11\n");
			writer.write("library = " + arquivoDLLApp + "\n");
			writer.write("slot = 1\n");
			writer.write("disabledMechanisms = {\n");
			writer.write("    CKM_SHA1_RSA_PKCS\n");
			writer.write("}\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /**
	 * Metodo que verifica a situação da DLL do Token e-Pass da Pronova.
	 * 
	 * Verifica se a DLL já está criada na pasta Windows\System32.
	 * 
	 * Se o arquivo não existir, cria uma cópia na pasta apontada pela variável CONFIG_DIR_NAME.
	 * 
	 * @param urlApp
	 */
	
	private void criarDLL(String urlApp){
		try {
			File dllWindows = new File(WINDOWS_DIR_NAME + DLL_FILE_NAME);
			if (dllWindows.exists()){
				arquivoDLLApp = WINDOWS_DIR_NAME + DLL_FILE_NAME;
			}else{
				arquivoDLLApp = CONFIG_DIR_NAME + DLL_FILE_NAME;
				
				URL url = new URL(urlApp+ "DLLServlet");
				HttpURLConnection connectionGet = (HttpURLConnection) url.openConnection();
				connectionGet.setRequestProperty("Request-Method", "GET");  
				connectionGet.setDoInput(true);
				connectionGet.setDoOutput(false);
				
				connectionGet.connect();
				
				// imprime o numero do resultado
				System.out.println("Resultado: "+ connectionGet.getResponseCode()+ "/"+ connectionGet.getResponseMessage());			
				
				InputStream input = connectionGet.getInputStream();
				
				File file = new File(arquivoDLLApp);
				file.createNewFile();
				FileOutputStream output = new FileOutputStream(file);
				
				int b = 0;
				while (-1 != ((b = input.read()))) {
					output.write(b);
				}
				output.close();		
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * @param password representa o PIN informado pelo usuário
	 * @return true se houve sucesso no login ou false se ocorreu 
	 * algum erro
	 */
	public boolean login(String password, Applet applet)throws Exception{
		boolean status = true;

		try {
			PIN = password.toCharArray();
			
			//A configuracao do provider eh possivel diante de tres opcoes:
			//1.Configuracao do java.security
			//  p. ex:security.provider.10=sun.security.pkcs11.SunPKCS11 D:\\desenvolvimento\\projetos\\funasa\\pkcs11.cfg
			//2.Pedindo para o usuario fornecer o arquivo de configuracao, através de um uploader (argh!!)
			//3.Manualmente, no codigo da applet

			//Foi escolhida a forma manual.
			Provider p = new sun.security.pkcs11.SunPKCS11(CONFIG_DIR_NAME + CONFIG_FILE_NAME);
			Security.addProvider(p);
			
			keyStore = KeyStore.getInstance("PKCS11");
						
			keyStore.load(null, PIN);
			
		}catch(Exception e){
			throw new Exception(e);
		}
		return status;
	}

	/**
	 * @return chain representa o certificado armazenado no token
	 */
	public Certificate[] getCertificate(){
		Certificate[] chain = null;
		try {
			Enumeration<String> aliasesEnum = keyStore.aliases();
			while (aliasesEnum.hasMoreElements()){
				String alias = aliasesEnum.nextElement();
				if(keyStore.isKeyEntry(alias)){
					chain = keyStore.getCertificateChain(alias);
					break;
				}
			}
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} 
		return chain;
	}

	/**
	 * @return privateKey representa a chave privada armazenada no Token
	 */
	public PrivateKey getPrivateKey() {
		PrivateKey privateKey = null;
		try {
			Enumeration<String> aliasesEnum = keyStore.aliases();
			while (aliasesEnum.hasMoreElements()){
				String alias = aliasesEnum.nextElement();
				if(keyStore.isKeyEntry(alias)){
					privateKey = (PrivateKey)keyStore.getKey(alias, PIN);
					
					keyStore.getCertificateChain(alias);
					break;
				}
			}
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} 
		return privateKey;
	}
}
