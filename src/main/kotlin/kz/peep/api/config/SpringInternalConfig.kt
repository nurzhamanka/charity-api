package kz.peep.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

@Configuration
class SpringInternalConfig {

    companion object {
        @Bean
        @JvmStatic
        fun propertyConfigurer() = PropertySourcesPlaceholderConfigurer().apply {
            setPlaceholderPrefix("%{")
        }
    }
}