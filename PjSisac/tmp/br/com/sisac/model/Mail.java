package br.com.sisac.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Classe para envio de emails do sistema, utilizando a API commons-email.
 *
 * @author Borduchi, Marcos
 * @since 2015-09-25
 */
public class Mail {

    //Dados de Login para envio
    private static String email = "sisac.gardem@gmail.com";
    private static String senha = "Esmigolcorvo10";

    private static String htmlMsg(String logo, String assunto, String mensagem) {    // Template HTML padrao para e-mails.
        return "<html>"
                + "<head>"
                + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>"
                + "</head>"
                + "<body>"
                + "<div style='background: green; height: 65px; border-bottom: 2px solid #FCB72D; z-index: -10; width: 520px; text-align: center;'>"
                + "<img src=\'cid:" + logo + "\' style='float: left; border-radius: 3px; margin-left: 15px;' height='100' width='90'/>"
                + "<div id='title' style='font-weight: 500; font-family: Arial,sans-serif; font-size: 36px; color: #FFFFFF; margin-left: 50px;'>"
                + "ASPP"
                + "<br>"
                + "<span style='color: #FBB72E; font-size: 16px;'>Suporte</span>"
                + "</div>"
                + "</div>"
                + "<br/>"
                + "<div id='content' style='margin-top: 20px; background-color: #E3E3E3; max-width: 500px; padding: 10px; font-family: Arial,sans-serif; border-radius: 3px;'>"
                + "<span style='font-weight: 600; font-size: 20px;'>" + assunto + "</span>"
                + "<br/>"
                + "<br/>"
                + mensagem
                + "<br/>"
                + "<br/>"
                + "<b>Mensagem enviada em: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) + "</b>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    public static String msgAjuda(String usuario, String email, String nome, String telefone, String assunto, String mensagem) {
        return "O usuário " + usuario + " (" + nome + ") solicitou ajuda."
                + "<br/>"
                + "E-mail: " + email
                + "<br/>"
                + "Telefone: " + telefone
                + "<br/>"
                + "Assunto: " + assunto
                + "<br/>"
                + "<b>Mensagem:</b>"
                + "<br/>"
                + mensagem;
    }

    public static boolean enviaEmail(String destinatarioEmail, String destinatarioNome, String assunto, String mensagem, HttpServletRequest request) {
    	
    	System.out.println("Email = " +destinatarioEmail);
        URL logourl = null;
        HtmlEmail mail = new HtmlEmail();

        StringBuilder url = new StringBuilder();
        url.append(request.getScheme()).append("://") // Monta a url da imagem da logo
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath())
                .append("/resources/images/capa.jpg");

        try {
            logourl = new URL(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

      
        try {
        	mail.setSmtpPort(587);
        	System.out.println("");
        	
        	mail.setDebug(true);
        	System.out.println("");

        	mail.setSSL(true);
        	System.out.println("");

            mail.setHostName("smtp.gmail.com"); 					// o servidor SMTP para envio do e-mail 
            System.out.println("");
            
            mail.setAuthentication(email, senha);					// login e-mail
            System.out.println("alterando hostname...");
            
            mail.addTo(destinatarioEmail, destinatarioNome);				// destinatário
            System.out.println("");
            
            mail.setFrom(email, "Sisac - Agendamento");					// remetente 
            mail.setSubject(assunto);                                                   // assunto do e-mail 
            mail.setHtmlMsg(htmlMsg(mail.embed(logourl, "Logo"), assunto, mensagem));	// conteudo do e-mail 
            mail.send();								// envia o e-mail
            return true;
            // retorna true se for enviado com sucesso
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }
}
