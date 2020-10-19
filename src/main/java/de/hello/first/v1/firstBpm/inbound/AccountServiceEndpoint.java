package de.hello.first.v1.firstBpm.inbound;

import de.hello.bpm.account.model.Account;
import de.hello.bpm.account.model.AccountDetailsRequest;
import de.hello.bpm.account.model.AccountDetailsResponse;
import de.hello.bpm.account.model.EnumAccountStatus;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.servlet.http.HttpServletRequest;

@Endpoint
public class AccountServiceEndpoint {

    private static final String ANFRAGE_NAMESPACE_URL = "http://com/blog/demo/webservices/accountservice";
    private static final String ANFRAGE_LOCAL_NAME = "AccountDetailsRequest";
    private HttpServletRequest httpServletRequest;

    public AccountServiceEndpoint(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @PayloadRoot(namespace = ANFRAGE_NAMESPACE_URL, localPart = ANFRAGE_LOCAL_NAME)
    @ResponsePayload
    public AccountDetailsResponse getAccountDetails(@RequestPayload AccountDetailsRequest request) {
        return createResponse();
    }

    protected AccountDetailsResponse createResponse() {
        AccountDetailsResponse response = new AccountDetailsResponse();
        Account account = new Account();
        account.setAccountBalance(12.2);
        account.setAccountName("Max");
        account.setAccountNumber("777");
        account.setAccountStatus(EnumAccountStatus.ACTIVE);
        response.setAccountDetails(account);
        return response;
    }
}
