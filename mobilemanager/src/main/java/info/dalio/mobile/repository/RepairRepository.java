package info.dalio.mobile.repository;

import info.dalio.mobile.domain.Repair;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Repair entity.
 */
public interface RepairRepository extends JpaRepository<Repair,Long> {

}
