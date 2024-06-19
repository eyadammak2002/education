package eya.tansik.education;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import eya.tansik.education.entities.Payment;
import eya.tansik.education.entities.PaymentStatus;
import eya.tansik.education.entities.PaymentType;
import eya.tansik.education.entities.Student;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import eya.tansik.education.repository.PaymentRepository;
import eya.tansik.education.repository.StudentRepository;

@SpringBootApplication
public class EducationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducationApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
                                        PaymentRepository paymentRepository) {
        return args -> {
            studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
                    .firstName("Mohamed").code("112233").programId("SDIA").build());

            studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
                    .firstName("eya").code("272002").programId("GLSID").build());

            studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
                    .firstName("yasmine").code("123456").programId("BDCC").build());

            studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
                    .firstName("najet").code("112266").programId("GLSID").build());

            PaymentType[] paymentTypes = PaymentType.values();
            Random random = new Random();
            studentRepository.findAll().forEach(st -> {
                for (int i = 0; i < 10; i++) {
                    int index = random.nextInt(paymentTypes.length);
                    Payment payment = Payment.builder()
                            .amount(1000 + (int) (Math.random() + 20000))
                            .type(paymentTypes[index])
                            .status(PaymentStatus.CREATED)
                            .date(LocalDate.now())
                            .student(st)
                            .build();
                    paymentRepository.save(payment);
                }
            });
        };
    }
}





