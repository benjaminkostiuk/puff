package com.unityTest.testrunner.configuration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.unityTest.testrunner.utils.AnnotationProxy;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.*;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger.api.title}")
    private String title;

    @Value("${swagger.api.description}")
    private String description;

    @Value("${swagger.api.contact.name}")
    private String contactName;

    @Value("${swagger.api.contact.url}")
    private String contactUrl;

    @Value("${swagger.api.contact.email}")
    private String contactEmail;

    @Value("${swagger.api.license.name}")
    private String licenseName;

    @Value("${swagger.api.license.url}")
    private String licensceUrl;

    @Value("${swagger.api.version}")
    private String version;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${swagger.auth.token-url}")
    private String authTokenUrl;

    @Value("${swagger.auth.client-secret}")
    private String authClientSecret;

    @Value("${swagger.auth.client-id}")
    private String authClientId;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.unityTest.testrunner.restImpl"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiEndPointsInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(securitySchema()));
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        return new SecurityConfiguration(authClientId, authClientSecret, realm, "", "", Collections.emptyMap(), false);
    }

    private OAuth securitySchema() {
        List<GrantType> grantTypes = Lists.newArrayList(new ResourceOwnerPasswordCredentialsGrant(authTokenUrl));
        return new OAuth("oauth2", Collections.EMPTY_LIST, grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Lists.newArrayList(new SecurityReference("oauth2", new AuthorizationScope[0])))
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title(title)
                .description(description)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .license(licenseName)
                .licenseUrl(licensceUrl)
                .version(version)
                .build();
    }

    @Bean
    public AlternateTypeRuleConvention springDataWebPropertiesConvention(final SpringDataWebProperties springDataWebProperties) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }
            @Override
            public List<AlternateTypeRule> rules() {
                return Collections.singletonList(
                        // Set alternate definition for Pageable class
                        newRule(Pageable.class, alternatePageableType(springDataWebProperties.getPageable(), springDataWebProperties.getSort()))
                );
            }
        };
    }

    private Type alternatePageableType(SpringDataWebProperties.Pageable pageable, SpringDataWebProperties.Sort sort) {
        final String firstPage = pageable.isOneIndexedParameters() ? "1" : "0";
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .property(property(pageable.getPageParameter(), Integer.class, ImmutableMap.of(
                        "value", "Page " + (pageable.isOneIndexedParameters() ? "Number" : "Index"),
                        "defaultValue", firstPage,
                        "allowableValues", String.format("range[%s, %s]", firstPage, Integer.MAX_VALUE)
                )))
                .property(property(pageable.getSizeParameter(), Integer.class, ImmutableMap.of(
                        "value", "Page Size",
                        "defaultValue", String.valueOf(pageable.getDefaultPageSize()),
                        "allowableValues", String.format("range[1, %s]", pageable.getMaxPageSize()),
                        "example", String.valueOf(5)
                )))
                .property(property(sort.getSortParameter(), String[].class, ImmutableMap.of(
                        "value", "Page Multi-Sort: fieldName,(asc|desc)"
                )))
                .build();
    }

    private AlternateTypePropertyBuilder property(String name, Class<?> type, Map<String, Object> parameters) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true)
                .withAnnotations(Collections.singletonList(AnnotationProxy.of(ApiParam.class, parameters)));
    }
}
