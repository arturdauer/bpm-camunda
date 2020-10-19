package de.hello.first.backend.v1.account;

import de.hello.bpm.account.model.AccountService;
import de.hello.first.backend.v1.config.soap.AbstractSoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountServiceConfiguration extends AbstractSoapClient<AccountService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final String endpoint;
    private final String user;
    private final String password;

    public AccountServiceConfiguration(@Value("$service.account.endpoint") final String endpoint,
                                       @Value("$service.account.user") final String user,
                                       @Value("$service.account.password") final String password) {
        this.endpoint = endpoint;
        this.user = user;
        this.password = password;
    }

    @Bean(name = "accountService")
    public AccountService accountServicePortType() {
        return createPortType(AccountService.class);
    }

    @Override
    protected String getEndpoint() {
        return endpoint;
    }

    @Override
    protected String getUserName() {
        return user;
    }

    @Override
    protected String getPassword() {
        return password;
    }
}
