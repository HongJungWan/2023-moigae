package batch.spring.metanet.core.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static batch.spring.metanet.core.config.PrimaryGeneratorProtoType.encodeTimestamp;
import static batch.spring.metanet.core.config.PrimaryGeneratorProtoType.generateRandomString;

@Component
public class PrimaryGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return "c" + encodeTimestamp(System.currentTimeMillis()) + generateRandomString(10);
    }
}