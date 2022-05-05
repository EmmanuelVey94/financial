package com.tkt.financial.repository;

import com.tkt.financial.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository  extends JpaRepository<Company, Integer> {

}
