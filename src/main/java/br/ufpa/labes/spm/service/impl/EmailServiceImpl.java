package br.ufpa.labes.spm.service.impl;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.email.IEmailDAO;
import org.qrconsult.spm.dtos.agenda.TaskDTO;
import org.qrconsult.spm.dtos.email.EmailDTO;
import org.qrconsult.spm.dtos.formAbility.AbilityDTO;
import org.qrconsult.spm.dtos.formAgent.AgentAffinityAgentDTO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formAgent.AgentHasAbilityDTO;
import org.qrconsult.spm.dtos.formAgent.AgentsDTO;
import org.qrconsult.spm.dtos.formAgent.ConfigurationDTO;
import org.qrconsult.spm.dtos.formGroup.GroupDTO;
import org.qrconsult.spm.dtos.formRole.RoleDTO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.Email;
import br.ufpa.labes.spm.service.interfaces.AgentServices;
import br.ufpa.labes.spm.service.interfaces.EmailServices;
import org.qrconsult.spm.util.Md5;

@Stateless
public class EmailServiceImpl implements EmailServices {

	IEmailDAO emailDAO;
	private static final String EMAIL_CLASSENAME = Email.class.getSimpleName();
	private Query query;
	Converter converter = new ConverterImpl();

	private Email email;


	@Override
	public EmailDTO saveConfiEmail(EmailDTO emailDTO) {
		System.out.println("caiu no email");
		email =  new Email();
		try {
			email = this.convertEmailDTOToEmail(emailDTO);
			emailDAO.save(email);
			email = null;
		} catch (Exception e) {
			e.printStackTrace();
		}


		return emailDTO;
	}

	@Override
	public EmailDTO updateConfiEmail(EmailDTO emailDTO) {

		email = new Email();
		try {
			email = this.convertEmailDTOToEmail(emailDTO);
			System.out.print(email.getServerHost()+email.getServerPort()+email.getUserName()+email.getPassword()+email.isServicoTls()+email.isServicoSsl()+email.getOid().toString());

			emailDAO.update(email);
			email = null;
		} catch (Exception e) {
			e.printStackTrace();
		}


		return emailDTO;
	}


	private Email convertEmailDTOToEmail(EmailDTO emailDTO) {
		try {

			Email email = new Email();
			email = (Email) converter.getEntity(emailDTO, email);

			return email;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}



	class SMTPAuthenticator extends Authenticator {
	    String login;
	    String password;

	    //Recebendo usuario e senha e guardando nas variaveis
	    public SMTPAuthenticator(String login, String password) {
	        this.login = login;
	        this.password = password;
	    }

	    //Esse é o metodo usado para enviar usuario e senha
	    public PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication(this.login, this.password);
	    }
	}

	@Override
	public EmailDTO testeEmail(EmailDTO emailDTO) {
		System.out.println("dados do email: "+emailDTO.getServerHost());
		  try {

	            //Variaveis
	            String d_email = emailDTO.getUserName(),
	                    d_password = emailDTO.getPassword(),
	                    d_host = emailDTO.getServerHost(),
	                    //d_host = "smtp.gmail.com",
	                    		d_port = emailDTO.getServerPort(),
	                   // d_port = "465",
	                    m_to = emailDTO.getUserName(),
	                    m_subject = "teste",
	                    m_text = "teste de conexão servidor email.";

	            //Propriedades Necessarias
	            Properties props = new Properties();

	            //Modo debug para verificar os passos do envio
	            props.put("mail.debug", "true");

	            //Servidor SMTP
	            props.put("mail.host", d_host);
	             //liga tls
	              props.put("mail.smtp.starttls.enable", emailDTO.isServicoTls());
	            //Porta
	            props.put("mail.smtp.port", d_port);

	            //Necessario autenticacao
	            props.put("mail.smtp.auth", "true");

	            //Liga o SSL
	            props.put("mail.smtp.ssl.enable", emailDTO.isServicoSsl());

	            //Cria a sessao
	            Session session = Session.getInstance(props,  new SMTPAuthenticator(d_email, d_password));

	            //Pega a sessao com usuario e senha
	            MimeMessage msg = new MimeMessage(session);

	            //Coloca O corpo do titulo
	            msg.setText(m_text);

	            //Coloca o assunto
	            msg.setSubject(m_subject);

	            //Coloca quem enviou
	            msg.setFrom(new InternetAddress(d_email));

	            //Coloca para quem sera enviado
	            msg.addRecipient(Message.RecipientType.TO,  new InternetAddress(m_to));

	            //Envia a mensagem
	            Transport.send(msg);

	            System.out.println("Terminado");
	            emailDTO.setTeste(true);
	        } catch (MessagingException mex) {
	        	emailDTO.setTeste(false);
	            System.out.println("Falha no envio, exception: " + mex);
	        } catch (Exception e) {
	        	emailDTO.setTeste(false);
	            e.printStackTrace();
	        }
		return emailDTO;
	}
	public EmailDTO getConfiEmail(EmailDTO email) {

		try {
			query = emailDAO
					.getPersistenceContext()
					.createQuery(
							"SELECT email FROM "
									+ EMAIL_CLASSENAME
									+ " AS email");


			System.out.println(query.getSingleResult());

			return convertEmailToEmailDTO((Email) query.getSingleResult());

		} catch (Exception e) {
e.printStackTrace();
			return null;
		}
	}


	private EmailDTO convertEmailToEmailDTO(Email email) {
		EmailDTO emailDTO = new EmailDTO();
		try {
			emailDTO = (EmailDTO) converter.getDTO(email, EmailDTO.class);

			return emailDTO;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public AgentDTO enviaSenhaEmail(EmailDTO emailDTO,AgentDTO email) {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		String novaSenha = myRandom.substring(0,6);

		System.out.println("dados do email: "+email.geteMail()+" "+email.getEMail());
		  try {


	            //Variaveis
	            String d_email = emailDTO.getUserName(),
	                    d_password = emailDTO.getPassword(),
	                    d_host = emailDTO.getServerHost(),
	                    //d_host = "smtp.gmail.com",
	                    		d_port = emailDTO.getServerPort(),
	                   // d_port = "465",
	                    m_to = email.getEMail(),
	                    m_subject = "SPF - Recupera��o de senha",
	                    m_text = ".";

	            //Propriedades Necessarias
	            Properties props = new Properties();

	            //Modo debug para verificar os passos do envio
	            props.put("mail.debug", "true");

	            //Servidor SMTP
	            props.put("mail.host", d_host);
	             //liga tls
	              props.put("mail.smtp.starttls.enable", emailDTO.isServicoTls());
	            //Porta
	            props.put("mail.smtp.port", d_port);

	            //Necessario autenticacao
	            props.put("mail.smtp.auth", "true");

	            //Liga o SSL
	            props.put("mail.smtp.ssl.enable", emailDTO.isServicoSsl());

	            //Cria a sessao
	            Session session = Session.getInstance(props,  new SMTPAuthenticator(d_email, d_password));

	            //Pega a sessao com usuario e senha
	            MimeMessage msg = new MimeMessage(session);
	            String rodape = "<br></br>Esta � uma mensagem autom�tica gerada pelo sistema SPF. Favor n�o responde-la.<p></p>Atenciosamente,<p></p>Time do SPF.";
	            msg.setContent(" <img src=\"http://68.169.54.200/spm/wp-content/uploads/2013/10/logo.jpg\">"
		                + "<p></p>Sr(a) "+email.getName()+", <p></p>Informamos que sua senha foi alterada no sistema SPF. <p></p>Utilize a senha abaixo para acessar o sistema.Recomendamos alter�-la no primeiro acesso.<p></p>"
		                + "Nova senha: <h4>" + novaSenha.toString()+"</h4><p></p>"+ rodape, "text/html");


	            //Coloca quem enviou
	            msg.setFrom(new InternetAddress(d_email));
	             msg.setSubject(m_subject);
	            //Coloca para quem sera enviado
	            msg.addRecipient(Message.RecipientType.TO,  new InternetAddress(m_to));

	            //Envia a mensagem
	            Transport.send(msg);

	            System.out.println("Terminado");

	            email.setPassword(novaSenha);




	        } catch (MessagingException mex) {

	            System.out.println("Falha no envio, exception: " + mex);

	        } catch (Exception e) {

	            e.printStackTrace();

	        }
		return email;
	}

}
