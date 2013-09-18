certificadoDigital
==================

Fiz este pequeno projeto para auxiliar na tarefa de assinatura digital de documentos.

O projeto consiste de DispatchActions, Servlets e Applets.

Os testes foram feitos com o token EPASS2000 da Pronova, onde o funcionamento depende de dois arquivos principais: pkcs11.cfg (arquivo de configuração) e ngp11v211.dll.

Merecem destaque as seguintes informações:
. Os arquivos bcprov-jdk14-138.jar e iText-2.0.8.jar são utilizados para assinar o pdf gerado.
. O arquivo TokenSignerApplet.jar contem a classe da applet
. O parâmetro java_codebase é importante de configurar para não dar erro na localização dos arquivos com as classes.
