package com.spring.dto.model;

import javax.persistence.Entity;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDentist {
	String tenBacSi;
//	List<CustomerProfileDTO> listCustommer;
	int TongSoDon;
}
