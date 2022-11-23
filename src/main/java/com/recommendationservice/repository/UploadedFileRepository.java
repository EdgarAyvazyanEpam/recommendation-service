package com.recommendationservice.repository;

import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Edgar_Ayvazyan
 * @created 18/11/2022
 * @project recommendation-service
 */
@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFileEntity, Long> {
    Optional<UploadedFileEntity> findFirstByFileNameAndFileStatus(String fileName, UploadedFIleStatusEnum status);

    Optional<UploadedFileEntity> findUploadedFileEntityByFileName(String fileName);
}
