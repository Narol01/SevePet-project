package ait.cohort34.security;

import ait.cohort34.accounting.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

// Собираем конфигурацию для определения доступов для различных путей и методов
// в приложении с использованием Spring Security
@Configuration
// Lombok создает конструктор, который принимает аргументы для всех final полей класса
// и инициализирует их значениями, переданными в качестве аргументов конструктора
@RequiredArgsConstructor
public class AuthorizationConfiguration {

    final CustomWebSecurity webSecurity;//привязали бин к конфигурации

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception{

// конфигурируем базовую HTTP-аутентификацию с настройками по умолчанию
        http.httpBasic(Customizer.withDefaults());
// отключается CSRF защита, которая предотвращает атаки межсайтовой подделки запросов и
// теперь м. пропускать не только get-requests
        http.csrf(csrf->csrf.disable());
// гарантирует, что для каждого запроса будет создаваться новый сеанс, если сеанс еще не существует
// создание сеанса для каждого запроса может привести к большему потреблению памяти и ресурсов сервера,
// особенно при большом количестве пользователей
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http.cors(request -> getCorsConfiguration());

// прописываем доступы для разных путей и методов
        http.authorizeHttpRequests(authorize->authorize


// ?- символ, *- 0 или одно слово, **- 0 или более директорий в пути, {}- regex-выражение
                .requestMatchers("/account/register", "/pet/found/**").permitAll()

                .requestMatchers("/account/user/{login}/role").hasRole(Role.ADMINISTRATOR.name())

                .requestMatchers(HttpMethod.GET,"/account/users").hasRole(Role.ADMINISTRATOR.name())//Почему только админ может получить User;

                .requestMatchers(HttpMethod.PUT,"/account/user/{login}")
//дали доступ только тому, у кого имя логина совпадает с именем пользователя: обновить юзера м. только он сам
//заодно и пароль м. сменить только юзер
                        .access(new WebExpressionAuthorizationManager("#login == authentication.name"))

                .requestMatchers(HttpMethod.DELETE,"/account/user/{login}")
//дали доступ только тому, у кого имя логина совпадает с именем пользователя или админ:
// удалить юзера м. только он сам или админ
                        .access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')"))

                .requestMatchers(HttpMethod.POST, "/pet/{author}")
                        .access(new WebExpressionAuthorizationManager("#author == authentication.name "))

                .requestMatchers(HttpMethod.PUT,"/pet/update/{caption}")
//определяется логика доступа к указанному эндпоинту.
// Это лямбда-выражение, которое принимает объекты authentication и context.
// Внутри него вызывается метод webSecurity.checkPostAuthor, который проверяет,
// является ли текущий пользователь автором поста с указанным идентификатором
                        .access((authentication, context) -> new AuthorizationDecision(webSecurity.checkPostAuthor( context.getVariables().get("caption"),authentication.get().getName())))

                .requestMatchers(HttpMethod.DELETE, "/pet/{caption}")
                        .access((authentication, context) -> {
                            boolean checkAuthor = webSecurity.checkPostAuthor( context.getVariables().get("caption"),authentication.get().getName());
                            boolean checkAdministrator = context.getRequest().isUserInRole(Role.ADMINISTRATOR.name());
                            return new AuthorizationDecision(checkAuthor || checkAdministrator);
                                }
                                )
                .anyRequest().authenticated()
        );
        return http.build();
    }

    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("*"));
        return corsConfiguration;
    }
}
//у спринг секьюрити есть свой контекст, из него мы и вынимаем различные значения, в том числе роли, id и т.д.
//principal- всегда есть после аутентификации - это база, кот. гарантирует лишь наличие имени,
// его м. расширить, как мы создавали UserPrincipal в фильтрах
//authentication- расширенный принципал, объект с ролями
//context- еще более полный объект, включающий информацию о запросе