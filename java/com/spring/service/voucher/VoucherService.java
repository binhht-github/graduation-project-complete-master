package com.spring.service.voucher;

import com.spring.dto.model.VoucherDTO;
import com.spring.dto.response.Response;
import com.spring.exception.NotFoundException;
import com.spring.model.Voucher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface VoucherService {

    VoucherDTO save(VoucherDTO dto);

    VoucherDTO update(VoucherDTO dto);

    List<VoucherDTO> findByTitle(String title);

    Optional<VoucherDTO> findByIdAndDeleteAtFalse(String id);

    Optional<VoucherDTO> findById(String id);

    List<VoucherDTO> findAll();

    List<Voucher> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    void hardDelete(String id) throws NotFoundException;

    Response<?> sentVoucherByAdmin(Long idCustomer, Double sale);

    //check hạn sử dụng voucher
    List<VoucherDTO> checkExprVoucher(LocalDateTime endDate, LocalDateTime nowDate);

    //soft-delete
    VoucherDTO softDelete(String id) throws NotFoundException;
}
