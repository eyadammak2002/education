package eya.tansik.education.web;


import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import eya.tansik.education.entities.Payment;
import eya.tansik.education.entities.PaymentStatus;
import eya.tansik.education.entities.PaymentType;
import eya.tansik.education.entities.Student;
import eya.tansik.education.repository.PaymentRepository;
import eya.tansik.education.repository.StudentRepository;

@RestController
public class PaymentRestController {
 
	private StudentRepository studentRepository ;
	private PaymentRepository paymentRepository ;
	
	public PaymentRestController(StudentRepository studentRepository ,PaymentRepository paymentRepository) {
		this.studentRepository=studentRepository;
		this.paymentRepository=paymentRepository;
	}
	
	
	@GetMapping(path="/payments")
	public List<Payment> allPayments(){
		return paymentRepository.findAll();
	}
	
	@GetMapping(path="/students/{code}/payments")
	public List<Payment> PaymentsByStudent(@PathVariable String code){
		return paymentRepository.findByStudentCode(code);
	}
	
	@GetMapping(path="/payments/ByStatus")
	public List<Payment> PaymentsByStatus(@PathVariable PaymentStatus status){
		return paymentRepository.findByStatus(status);
	}
	
	@GetMapping(path="/payments/ByType")
	public List<Payment> PaymentsByType(@PathVariable PaymentType type){
		return paymentRepository.findByType(type);
	}
	
	@GetMapping(path="/payments/{id}")
	public Payment getPaymentById(@PathVariable Long id) {
		return paymentRepository.findById(id).get();
	}
	
	@GetMapping(path="/students")
	public List<Student>allStudents(){
		return studentRepository.findAll();
	}
	
	
	@GetMapping(path="/students/{code}")
	public Student getStudentByCode(@PathVariable String code) {
		return studentRepository.findByCode(code);
	}
	
	@GetMapping(path="/studentsByProgramId")
	public List<Student> getStudentsByProgramId(@RequestParam String programId){
		return studentRepository.findByProgramId(programId);
	}
	
	@PutMapping(path="/payments/{id}")
	public Payment updateStatus(@RequestParam PaymentStatus status,@PathVariable Long id) {
		Payment payment=paymentRepository.findById(id).get();
		payment.setStatus(status);
		return paymentRepository.save(payment);
	}
	
	  @PostMapping(path = "/students", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public Payment savePayment(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	            @RequestParam("amount") double amount,
	            @RequestParam("type") PaymentType type,
	            @RequestParam("studentCode") String studentCode) throws IOException {

	        Path folderPath = Paths.get(System.getProperty("user.home"), "enset-data", "payments");
	        if (!Files.exists(folderPath)) {
	            Files.createDirectories(folderPath);
	        }

	        String fileName = UUID.randomUUID().toString();
	        Path filePath = Paths.get(System.getProperty("user.home"), "enset-data", "payments", fileName + ".pdf");
	        Files.copy(file.getInputStream(), filePath);

	        Student student = studentRepository.findByCode(studentCode);
	        if (student == null) {
	            throw new IllegalArgumentException("Student not found with code: " + studentCode);
	        }

	        Payment payment = Payment.builder()
	                .date(date)
	                .type(type)
	                .student(student)
	                .amount(amount)
	                .file(filePath.toUri().toString())
	                .status(PaymentStatus.CREATED)
	                .build();

	        return paymentRepository.save(payment);
		
	}
	  

	    @GetMapping(path="/paymentFile/{paymentId}",produces = MediaType.APPLICATION_PDF_VALUE)
	    public byte[] getPaymentFile(@PathVariable Long paymentId) throws IOException {
	    	Payment payment=paymentRepository.findById(paymentId).get();
	       return Files.readAllBytes(Path.of(URI.create(payment.getFile())));

	    }
	    
}
