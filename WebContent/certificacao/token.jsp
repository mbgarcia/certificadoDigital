<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>

<head>
    <title>Teste Token Signer Applet</title>
</head>

<body>
	<script type="text/javascript">
		function chamaAction(tarefa){
			var forward = "./certificacao.do?method=doFimCertificacao" + 
				"&idTarefa=" + tarefa;
			doSubmit(forward);
		}
	</script>

    <form>
    	<input type="hidden" name="appUrl" value=${pageContext.request.contextPath}>
        Código PIN:
        <input type="text" width="100" size=80 name="pinCode">
        <br>
        Nome:
        <input type="text" width="100" size=80 name="nome" value=${nome_request}>
    </form>

	<object
    	classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
    	codebase="http://java.sun.com/update/1.5.0/jinstall-1_5-windows-i586.cab#Version=5,0,0,5"
    	width="130" height="25" name="TokenSignerApplet">
    	<param name="code" value="br.com.certificacao.TokenSignerApplet">
    	<param name="archive" value="TokenSignerApplet.jar , ./certificacao/bcprov-jdk14-138.jar , ./certificacao/iText-2.0.8.jar">
    	<param name="mayscript" value="true">
	    <param name="type" value="application/x-java-applet;version=1.5">
    	<param name="scriptable" value="false">
    	<param name="signButtonCaption" value="Assinar Documento">
	    <param name="appUrlField" value="appUrl">
	    <param name="pinCodeField" value="pinCode">
	    <param name="nomeField" value="nome">

	    <comment>
			<embed
	            type="application/x-java-applet;version=1.5"
	            code="br.com.certificacao.TokenSignerApplet" 
	            archive="TokenSignerApplet.jar , ./certificacao/bcprov-jdk14-138.jar , ./certificacao/iText-2.0.8.jar"
	            width="130" height="25" scriptable="true"
		    	pluginspage="http://java.sun.com/products/plugin/index.html#download"
	            appUrlField="appUrl"
	            pinCodeField="pinCode"
	            signButtonCaption="Assinar Documento"
	            nomeField="nome">
			</embed>
	    </comment>
	</object>
</body>
</html>
