package com.spring.service.account;

import com.spring.dto.model.AccountsDTO;
import com.spring.exception.NotFoundException;
import com.spring.exception.NotParsableContentException;
import com.spring.model.Accounts;
import com.spring.model.VerificationToken;
import com.spring.repository.AccountRepository;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface AccountService {

	AccountsDTO register(AccountsDTO userDTO);

	AccountsDTO updatePassword(AccountsDTO userDTO);

	AccountsDTO update(AccountsDTO userDTO);

	Optional<Accounts> checkIfEmailExistsAndDeletedAt(String email) throws NotParsableContentException;

	Optional<Accounts> checkTelephone(String sdt);

	List<AccountsDTO> findAll();

	AccountsDTO findById(Long id) throws NotFoundException;

	void delete(Long id);

	void sendRegistrationConfirmationEmail(Accounts account) throws MessagingException;

	void sendResetPasswordEmail(Accounts account) throws MessagingException;

	boolean verifyAccount(Optional<VerificationToken> verifyToken);

	boolean verifyChangePassword(Optional<VerificationToken> verifyToken);

	String getRoleByAccountId(Long id);
}
