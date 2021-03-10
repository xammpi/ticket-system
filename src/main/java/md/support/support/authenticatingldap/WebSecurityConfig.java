package md.support.support.authenticatingldap;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/css/**").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                // .loginProcessingUrl("/login")
                //.failureUrl("/login?error")
                .defaultSuccessUrl("/current-applications", true)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        ActiveDirectoryLdapAuthenticationProvider adProvider =
                new ActiveDirectoryLdapAuthenticationProvider("47paralel.md", "ldap://192.168.6.5:389");
        adProvider.setConvertSubErrorCodesToExceptions(true);
        adProvider.setUseAuthenticationRequestCredentials(true);
        auth.authenticationProvider(adProvider);

    }

}