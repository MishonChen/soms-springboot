package xyz.ontip.service.token;

import java.util.List;

public interface TokenService {

    void batchDeleteToken(List<String> tokens);
    void batchInsertToken(List<String> tokens);

}
