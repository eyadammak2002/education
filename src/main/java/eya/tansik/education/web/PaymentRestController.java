package eya.tansik.education.web;


import java.io.IOException;

import java.time.LocalDate;
import java.util.List;

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
import eya.tansik.education.services.PaymentService;

@RestController

public class PaymentRestController {
 
	private StudentRepository studentRepository ;
	private PaymentRepository paymentRepository ;
	private PaymentService paymentService ;
	
	public PaymentRestController(StudentRepository studentRepository ,PaymentRepository paymentRepository,PaymentService paymentService) {
		this.studentRepository=studentRepository;
		this.paymentRepository=paymentRepository;
		this.paymentService=paymentService;
				
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
		return paymentService.updatePaymentStatus(status,id);
	}
	
	@PostMapping(path="/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Payment savePayment(@RequestParam MultipartFile file,
	                           @RequestParam double amount,
	                           @RequestParam PaymentType type,
	                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	                           @RequestParam String studentCode) throws IOException {
	    return paymentService.savePayment(file, amount, type, date, studentCode);
	}

    @GetMapping(path="/paymentFile/{paymentId}",produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPaymentFile(@PathVariable Long paymentId) throws IOException {
    	return paymentService.getPaymentFile(paymentId);

    }
	
}



	    

