package eya.tansik.education.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import eya.tansik.education.entities.Payment;
import eya.tansik.education.entities.PaymentStatus;
import eya.tansik.education.entities.PaymentType;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{

	List<Payment> findByStudentCode(String Code);
	List<Payment> findByStatus(PaymentStatus status);
	List<Payment> findByType(PaymentType type);

	}