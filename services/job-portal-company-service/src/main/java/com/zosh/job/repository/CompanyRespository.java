package com.zosh.job.repository;

import com.zosh.job.domain.CompanyStatus;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import com.zosh.job.modal.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRespository extends JpaRepository<Company, Long> {

    Company findByOwnerId(Long ownerId);

    boolean existsByOwnerId(Long ownerId);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    boolean existsByEmail(String email);

    boolean existsByRegistrationNumber(String registrationNumber);

    @Query("select c from Company c where " +
            "(:companyType IS NULL OR c.companyType=:companyType) AND " +
            "(:industryType IS NULL OR c.industryType=:industryType) AND " +
            "(:status IS NULL OR c.status=:status)"
    )
    List<Company> findByFilters(
            @Param("companyType") CompanyType companyType,
            @Param("industryType") IndustryType industryType,
            @Param("status") CompanyStatus status);
}
