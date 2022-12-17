package org.catcom.classreserver.config;

import org.hibernate.boot.model.naming.*;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateNamingStrategyConfig
{
    @Bean
    public PhysicalNamingStrategy physicalNamingStrategy()
    {
        return new PhysicalNamingStrategyStandardImpl();
    }

    @Bean
    public ImplicitNamingStrategy implicitNamingStrategy()
    {
        return new ImplicitNamingStrategyLegacyHbmImpl();
    }

}
