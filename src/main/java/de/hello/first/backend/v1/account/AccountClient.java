package de.hello.first.backend.v1.account;

import de.hello.bpm.account.model.Account;
import de.hello.bpm.account.model.AccountDetailsRequest;
import de.hello.bpm.account.model.AccountDetailsResponse;
import de.hello.bpm.account.model.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccountClient.class);

    private AccountService accountPortType;

    @Autowired
    public AccountClient(AccountService accountPortType) {
        this.accountPortType = accountPortType;
    }

    public Account getAccountInfo(String id) {
        AccountDetailsRequest request = new AccountDetailsRequest();
        request.setAccountNumber(id);

        AccountDetailsResponse response = accountPortType.getAccountDetails(request);
        Account account = response.getAccountDetails();

        return account;
    }
}
