package com.example.chatgpt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.ssm.SsmClient;
//import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
//import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Configuration
public class awsConfig {

    // Use this code snippet in your app.
// If you need more information about configurations or implementing the sample
// code, visit the AWS docs:
// https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html

// Make sure to import the following packages in your code
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
// import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
// import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

//    @Bean
//    public String getSecret() {
//
//        SsmClient ssmClient = SsmClient.builder()
//                .region(Region.of("deine-region"))  // Ersetze "deine-region" durch die entsprechende AWS-Region
//                .build();
//
//        GetParameterRequest parameterRequest = GetParameterRequest.builder()
//                .name("dein-parameter-name")  // Ersetze "dein-parameter-name" durch den Namen des Parameters
//                .withDecryption(true)
//                .build();
//
//        GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
//
//        return parameterResponse.parameter().value();
//
//    }
}
