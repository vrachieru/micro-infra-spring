package com.ofg.infrastructure.web.resttemplate.fluent.post

import com.ofg.infrastructure.web.resttemplate.fluent.HttpMethodBuilder
import com.ofg.infrastructure.web.resttemplate.fluent.common.HttpMethodSpec
import groovy.transform.TypeChecked
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.OK

@TypeChecked
class PostHttpMethodBuilderSpec extends HttpMethodSpec {

    public static final String REQUEST_BODY = '''{"sample":"request"}'''
    public static final String RESPONSE_BODY = '''{"sample":"response"}'''
    public static final Class<String> RESPONSE_TYPE = String
    
    def "should query for location when sending a post request on given address"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
            URI expectedLocation = new URI('localhost')
        when:
            URI actualLocation = httpMethodBuilder
                                                .post()
                                                    .onUrl(expectedLocation)
                                                    .body(REQUEST_BODY)
                                                    .forLocation()
        then:
            1 * restTemplate.exchange(expectedLocation, 
                                      POST, 
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity, 
                                      RESPONSE_TYPE) >> responseEntityWith(expectedLocation)
            actualLocation == expectedLocation
    }
    
    def "should query for location when sending a post request on given template address"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
            String templateUrl = 'http://some.url/api/objects/{objectId}'
            URI expectedLocation = new URI('localhost')
        when:
            URI actualLocation = httpMethodBuilder
                                                .post()
                                                    .onUrlFromTemplate(templateUrl)
                                                        .withVariables(OBJECT_ID)
                                                    .body(REQUEST_BODY)
                                                    .forLocation()
        then:
            1 * restTemplate.exchange(templateUrl, 
                                      POST, 
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity, 
                                      RESPONSE_TYPE , 
                                      OBJECT_ID) >> responseEntityWith(expectedLocation)
            actualLocation == expectedLocation
    }

    def "should query for location when sending a post request on service template address"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(SERVICE_URL, restTemplate)
            URI expectedLocation = new URI('localhost')
        when:
            URI actualLocation = httpMethodBuilder
                                                .post()
                                                    .onUrlFromTemplate(URL_TEMPLATE)
                                                        .withVariables(OBJECT_ID)
                                                    .body(REQUEST_BODY)
                                                    .forLocation()
        then:
            1 * restTemplate.exchange(FULL_URL, 
                                      POST, 
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity, 
                                      RESPONSE_TYPE, 
                                      OBJECT_ID) >> responseEntityWith(expectedLocation)
            actualLocation == expectedLocation
    }

    def "should query for location when sending a post request on service template address using map for url vars"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(SERVICE_URL, restTemplate)
            URI expectedLocation = new URI('localhost')
        when:
            URI actualLocation = httpMethodBuilder
                                                .post()
                                                    .onUrlFromTemplate(URL_TEMPLATE)
                                                        .withVariables([objectId: OBJECT_ID])
                                                    .body(REQUEST_BODY)
                                                    .forLocation()
        then:
            1 * restTemplate.exchange(FULL_URL, 
                                      POST, 
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity, 
                                      RESPONSE_TYPE,
                                      [objectId: OBJECT_ID]) >> responseEntityWith(expectedLocation)
            actualLocation == expectedLocation
    }

    def "should query for object when sending a post request on given template address"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
            String templateUrl = 'http://some.url/api/objects/{objectId}'
        when:
            String actualResponseBody = httpMethodBuilder
                                                        .post()
                                                            .onUrlFromTemplate(templateUrl)
                                                                .withVariables(OBJECT_ID)
                                                            .body(REQUEST_BODY)
                                                            .andExecuteFor()
                                                                .anObject()
                                                                .ofType(RESPONSE_TYPE)
        then:
            1 * restTemplate.exchange(templateUrl,
                                      POST,
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity,
                                      RESPONSE_TYPE,
                                      OBJECT_ID) >> responseEntityWith(RESPONSE_BODY)
            actualResponseBody == RESPONSE_BODY
    }

    def "should query for object when sending a post request on service template address"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(SERVICE_URL, restTemplate)
        when:
            String actualResponseBody = httpMethodBuilder
                                                        .post()
                                                            .onUrlFromTemplate(URL_TEMPLATE)
                                                                .withVariables(OBJECT_ID)
                                                            .body(REQUEST_BODY)
                                                            .andExecuteFor()
                                                                .anObject()
                                                                .ofType(RESPONSE_TYPE)
        then:
            1 * restTemplate.exchange(FULL_URL,
                                      POST,
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity,
                                      RESPONSE_TYPE,
                                      OBJECT_ID) >> responseEntityWith(RESPONSE_BODY)
            actualResponseBody == RESPONSE_BODY
    }

    def "should query for entity when sending a post request on given template address"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
            String templateUrl = 'http://some.url/api/objects/{objectId}'
            ResponseEntity<String> expectedResponseEntity = responseEntityWith(RESPONSE_BODY)
        when:
            ResponseEntity<String> actualResponseEntity = httpMethodBuilder
                                                                        .post()
                                                                            .onUrlFromTemplate(templateUrl)
                                                                                .withVariables(OBJECT_ID)
                                                                            .body(REQUEST_BODY)
                                                                            .andExecuteFor()
                                                                                .aResponseEntity()
                                                                                .ofType(RESPONSE_TYPE)
        then:
            1 * restTemplate.exchange(templateUrl,
                                      POST,
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity,
                                      RESPONSE_TYPE,
                                      OBJECT_ID) >> expectedResponseEntity
            actualResponseEntity == expectedResponseEntity
    }

    def "should query for entity when sending a post request on service template address"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(SERVICE_URL, restTemplate)
            ResponseEntity<String> expectedResponseEntity = responseEntityWith(RESPONSE_BODY)
        when:
            ResponseEntity<String> actualResponseEntity = httpMethodBuilder
                    .post()
                    .onUrlFromTemplate(URL_TEMPLATE)
                        .withVariables(OBJECT_ID)
                    .body(REQUEST_BODY)
                    .andExecuteFor()
                        .aResponseEntity()
                        .ofType(RESPONSE_TYPE)
        then:
            1 * restTemplate.exchange(FULL_URL,
                                      POST,
                                      { HttpEntity httpEntity -> httpEntity.body == REQUEST_BODY } as HttpEntity,
                                      RESPONSE_TYPE,
                                      OBJECT_ID) >> expectedResponseEntity
            actualResponseEntity == expectedResponseEntity
    }

    private ResponseEntity responseEntityWith(URI expectedLocation) {
        new ResponseEntity<>(new HttpHeaders(location: expectedLocation), OK)
    }
    
    private ResponseEntity responseEntityWith(Object body) {
        new ResponseEntity<>(body, OK)
    }


}
