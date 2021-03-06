//package com.rupp.swagger;
//
//import javax.servlet.Filter;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.RequestPostProcessor;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.Base64Utils;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//@RunWith(SpringRunner.class)
//public class MockMvcBase {
//    private static final String DEFAULT_AUTHORIZATION = "Resource is public.";
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @Autowired
//    private Filter springSecurityFilterChain;
//
//    protected MockMvc mockMvc;
//
//    @Rule
//    public final JUnitRestDocumentation restDocumentation =
//            new JUnitRestDocumentation(
//                    System.getProperties().getProperty("org.springframework.restdocs.outputDir"));
//
//    @Before
//    public void setUp() throws Exception {
//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                //.addFilters(springSecurityFilterChain)
//                .alwaysDo(prepareJackson(objectMapper))
//                .alwaysDo(document("{class-name}/{method-name}",
//                        preprocessRequest(), commonResponsePreprocessor()))
//                .apply(documentationConfiguration(restDocumentation)
//                        .uris()
//                        .withScheme("http")
//                        .withHost("localhost")
//                        .withPort(8080)
//                        .and().snippets()
//                        .withDefaults(curlRequest(), httpRequest(), httpResponse(),
//                                requestFields(), responseFields(), pathParameters(),
//                                requestParameters(), description(), methodAndPath(),
//                                section(), authorization(DEFAULT_AUTHORIZATION)))
//                .build();
//    }
//
//    protected OperationResponsePreprocessor commonResponsePreprocessor() {
//        return preprocessResponse(replaceBinaryContent(), limitJsonArrayLength(objectMapper),
//                prettyPrint());
//    }
//
//    protected RequestPostProcessor userToken() {
//        return new RequestPostProcessor() {
//            @Override
//            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//                // If the tests requires setup logic for users, you can place it here.
//                // Authorization headers or cookies for users should be added here as well.
//                String accessToken;
//                try {
//                    accessToken = getAccessToken("test", "test");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                request.addHeader("Authorization", "Bearer " + accessToken);
//                return documentAuthorization(request, "User access token required.");
//            }
//        };
//    }
//
//    private String getAccessToken(String username, String password) throws Exception {
//        String authorization = "Basic "
//                + new String(Base64Utils.encode("app:very_secret".getBytes()));
//        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";
//
//        String body = mockMvc
//                .perform(
//                        post("/oauth/token")
//                                .header("Authorization", authorization)
//                                .contentType(
//                                        MediaType.APPLICATION_FORM_URLENCODED)
//                                .param("username", username)
//                                .param("password", password)
//                                .param("grant_type", "password")
//                                .param("scope", "read write")
//                                .param("client_id", "app")
//                                .param("client_secret", "very_secret"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(contentType))
//                .andExpect(jsonPath("$.access_token", is(notNullValue())))
//                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
//                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
//                .andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
//                .andExpect(jsonPath("$.scope", is(equalTo("read write"))))
//                .andReturn().getResponse().getContentAsString();
//
//        return body.substring(17, 53);
//    }
//}
