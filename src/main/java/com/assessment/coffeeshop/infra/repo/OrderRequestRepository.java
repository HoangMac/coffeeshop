package com.assessment.coffeeshop.infra.repo;

import com.assessment.coffeeshop.infra.repo.entity.OrderRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, String> {

  List<OrderRequest> findByQueueIdAndInQueueTrue(String queueId);

}
