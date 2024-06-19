package eya.tansik.education.services;
import eya.tansik.education.entities.Payment;
import eya.tansik.education.entities.PaymentStatus;
import eya.tansik.education.entities.PaymentType;
import eya.tansik.education.entities.Student;
import eya.tansik.education.repository.PaymentRepository;
import eya.tansik.education.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository paymentRepository;
    private StudentRepository studentRepository;

    public PaymentService(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
    }
    

    public Payment savePayment(MultipartFile file, double amount, PaymentType type,
            LocalDate date, String studentCode) throws IOException {
    	Path folderPath = Paths.get(System.getProperty("user.home"),"enset-students","payments");
    	if(!Files.exists(folderPath)){
    		Files.createDirectories(folderPath);
    	}
    	String fileName = UUID.randomUUID().toString();
    	Path filePath = Paths.get(System.getProperty("user.home"),"enset-students","payments",fileName+".pdf");
    	Files.copy(file.getInputStream(), filePath);
    	Student student = studentRepository.findByCode(studentCode);
    	Payment payment=Payment.builder()
    			.type(type)
    			.status(PaymentStatus.CREATED)
    			.date(date)
    			.student(student)
    			.amount(amount)
    			.file(filePath.toUri().toString())
    			.build();
    	return paymentRepository.save(payment);

    }
    

    public byte[] getPaymentFile(Long id) throws IOException {
        Payment payment = paymentRepository.findById(id).get();
        return Files.readAllBytes(Path.of(URI.create(payment.getFile())));

    }
    
    
    public Payment updatePaymentStatus(PaymentStatus status, Long paymentId){
        Payment payment = paymentRepository.findById(paymentId).get();
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }


	}
