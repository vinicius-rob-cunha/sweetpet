package br.com.sweetpet.task;

import java.time.LocalDate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.sweetpet.model.StatusVacina;
import br.com.sweetpet.model.Vacina;
import br.com.sweetpet.repository.VacinaRepository;

/**
 * Classe responsável por rodar a tarefa de verificar as vacinas, identificar quais
 * estão agendadas para daqui no máximo 14 dias e enviar um e-mail para os donos para
 * lembra-los
 * @author Vinicius Cunha
 */
@Component
public class LembrarVacinaTask {
	
	private static final Logger log = LoggerFactory.getLogger(LembrarVacinaTask.class);
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private VacinaRepository repo;
	
	/**
	 * Recupera, a cada 5 minutos, as vacinas que estão agendadas e envia um e-mail para os donos
	 * @throws Exception
	 */
	@Scheduled(fixedDelay=300000)
	public void verificaVacinas() throws Exception {
		log.info("Recuperando vacinas que estão agendadas em até 14 dias");
		repo.findByStatusAndDataBetween(StatusVacina.AGENDADA, LocalDate.now(), LocalDate.now().plusDays(14))
			.forEach(this::send);
	}
	
	/**
	 * Faz o envio do email para lembrar ao dono que sua fera precisa tomar vacina
	 * @param vacina
	 */
	private void send(Vacina vacina) {
		try {
			MimeMessage mail = emailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mail);
			helper.setTo(vacina.getAnimal().getDono().getEmail());
			helper.setFrom("lembrete@sweetpet.com.br");
			helper.setSubject("Vinicius da SweetPet - Não esqueça a vacina da sua fera");
			helper.setText("Esta na hora de levar seu cachorro para tomar a vacina " + vacina.getNome());
			emailSender.send(mail);
		} catch (MessagingException e) {
			//apenas logamos para envitar parar o restante do processamento por algum email cadastrado errado
			log.warn("falha ao enviar email para " + vacina.getAnimal().getDono());
		}
        
	}

}
