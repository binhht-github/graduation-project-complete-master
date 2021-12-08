package com.spring.service.email;

import com.spring.model.email.context.EmailContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@SpringBootApplication
@EnableScheduling
public class MailServices {

	@Autowired
	JavaMailSender sender;
	List<MimeMessage> queue = new ArrayList<>();

	public void push(String to, String subject, String body){
		EmailContext mail = new EmailContext(to, subject, body);
		this.push(mail);
	}

	public void push(EmailContext mailModel) {
		try {
			MimeMessage mess = sender.createMimeMessage();

			MimeMessageHelper mailhellper = new MimeMessageHelper(mess);
			mailhellper.setFrom(mailModel.getFrom());
			mailhellper.setSubject(mailModel.getSubject());
			mailhellper.setTo(mailModel.getTo());
			mailhellper.setText(mailModel.getBody(), true);
			for (String ccAdress : mailModel.getCc()) {
				mailhellper.addCc(ccAdress);
			}
			for (String bccAdress : mailModel.getBcc()) {
				mailhellper.addBcc(bccAdress);
			}
			for (File file : mailModel.getFile()) {
				mess.setFileName(file.getName());
//				mailhellper.addAttachment(file.getName(), file);
			}
			queue.add(mess);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Scheduled(fixedDelay = 2000)
	public void RunEmail() {
		int succes = 0;
		int error = 0;
		int rest = 0;
		try {
			if (queue.isEmpty()) {
				succes = 0;
				error = 0;
				rest = 0;
			}
			while (!queue.isEmpty()) {
				MimeMessage mailModelFirst = queue.remove(0);
				sender.send(mailModelFirst);
				succes++;
				rest = queue.size();
				System.out.println("success: " + succes + " Error: " + error + " còn lại: " + rest);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error++;
		}
	}
}
