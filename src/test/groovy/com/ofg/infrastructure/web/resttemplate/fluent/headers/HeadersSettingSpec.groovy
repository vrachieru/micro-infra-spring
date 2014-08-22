package com.ofg.infrastructure.web.resttemplate.fluent.headers

import com.ofg.infrastructure.web.resttemplate.fluent.HttpMethodBuilder
import com.ofg.infrastructure.web.resttemplate.fluent.common.HttpMethodSpec
import groovy.transform.TypeChecked
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType

import static org.springframework.http.HttpMethod.*
import static org.springframework.http.MediaType.APPLICATION_JSON

@TypeChecked
class HeadersSettingSpec extends HttpMethodSpec {

    public static final String TEMPLATE_URL = 'http://some.url/api/objects/{objectId}'
    
    def "should fill out headers for get method"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
        when:
            httpMethodBuilder
                .get()
                    .onUrlFromTemplate(TEMPLATE_URL)            
                        .withVariables(OBJECT_ID)
                    .withHeaders()
                        .header('key', 'value')
                        .headers([anotherKey : 'value'])
                        .accept([APPLICATION_JSON])
                        .cacheControl('no-cache')
                        .contentType('application/vnd.mymoid-adapter.v1+json')
                        .expires(1000)
                        .lastModified(2000)
                        .location(new URI('localhost'))
                    .andExecuteFor()                    
                        .anObject()
                        .ofType(BigDecimal)
        then:
            1 * restTemplate.exchange(TEMPLATE_URL,
                    GET,
                    { HttpEntity httpEntity -> httpEntity.headers.keySet().every isPresentInSetHeaders() } as HttpEntity,
                    _ as Class,
                    _ as Long)
    }

    def "should fill out headers for post method"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
        when:
            httpMethodBuilder
                .post()
                    .onUrlFromTemplate(TEMPLATE_URL)            
                        .withVariables(OBJECT_ID)
                    .body('')
                    .withHeaders()
                        .header('key', 'value')
                        .headers([anotherKey : 'value'])
                        .accept([APPLICATION_JSON])
                        .cacheControl('no-cache')
                        .contentType('application/vnd.mymoid-adapter.v1+json')
                        .expires(1000)
                        .lastModified(2000)
                        .location(new URI('localhost'))
                    .andExecuteFor()                    
                        .anObject()
                        .ofType(BigDecimal)
        then:
            1 * restTemplate.exchange(TEMPLATE_URL,
                    POST,
                    { HttpEntity httpEntity -> httpEntity.headers.keySet().every isPresentInSetHeaders() } as HttpEntity,
                    _ as Class,
                    _ as Long)
    }

    def "should fill out headers for head method"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
        when:
            httpMethodBuilder
                    .head()
                        .onUrlFromTemplate(TEMPLATE_URL)
                            .withVariables(OBJECT_ID)
                        .withHeaders()
                            .header('key', 'value')
                            .headers([anotherKey : 'value'])
                            .accept([APPLICATION_JSON])
                            .cacheControl('no-cache')
                            .contentType('application/vnd.mymoid-adapter.v1+json')
                            .expires(1000)
                            .lastModified(2000)
                            .location(new URI('localhost'))
                        .andExecuteFor()
                        .aResponseEntity()
            then:
        1 * restTemplate.exchange(TEMPLATE_URL,
                HEAD,
                { HttpEntity httpEntity -> httpEntity.headers.keySet().every isPresentInSetHeaders() } as HttpEntity,
                _ as Class,
                _ as Long)
    }


    def "should fill out headers for options method"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
        when:
            httpMethodBuilder
                    .options()
                        .onUrlFromTemplate(TEMPLATE_URL)
                        .withVariables(OBJECT_ID)
                    .withHeaders()
                        .header('key', 'value')
                        .headers([anotherKey : 'value'])
                        .accept([APPLICATION_JSON])
                        .cacheControl('no-cache')
                        .contentType('application/vnd.mymoid-adapter.v1+json')
                        .expires(1000)
                        .lastModified(2000)
                        .location(new URI('localhost'))
                    .andExecuteFor()
                        .aResponseEntity()
                        .ofType(String)
        then:
            1 * restTemplate.exchange(TEMPLATE_URL,
                    OPTIONS,
                    { HttpEntity httpEntity -> httpEntity.headers.keySet().every isPresentInSetHeaders() } as HttpEntity,
                    _ as Class,
                    _ as Long)
    }


    def "should fill out headers for delete method"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
        when:
            httpMethodBuilder
                    .delete()
                        .onUrlFromTemplate(TEMPLATE_URL)
                        .withVariables(OBJECT_ID)
                    .withHeaders()
                        .header('key', 'value')
                        .headers([anotherKey : 'value'])
                        .accept([APPLICATION_JSON])
                        .cacheControl('no-cache')
                        .contentType('application/vnd.mymoid-adapter.v1+json')
                        .expires(1000)
                        .lastModified(2000)
                        .location(new URI('localhost'))
                    .andExecuteFor()
                        .aResponseEntity()
        then:
            1 * restTemplate.exchange(TEMPLATE_URL,
                    DELETE,
                    { HttpEntity httpEntity -> httpEntity.headers.keySet().every isPresentInSetHeaders() } as HttpEntity,
                    _ as Class,
                    _ as Long)
    }

    def "should fill out content type from media type"() {
        given:
            httpMethodBuilder = new HttpMethodBuilder(restTemplate)
        when:
            httpMethodBuilder
                .get()
                    .onUrlFromTemplate(TEMPLATE_URL)            
                        .withVariables(OBJECT_ID)
                    .withHeaders()
                        .contentType(new MediaType('application', 'vnd.mymoid-adapter.v1+json'))
                    .andExecuteFor()
                        .anObject()
                        .ofType(BigDecimal)
        then:
            1 * restTemplate.exchange(TEMPLATE_URL,
                    GET,
                    { HttpEntity httpEntity -> httpEntity.headers.keySet().every { it in ['Content-Type'] } } as  HttpEntity,
                    _ as Class,
                    _ as Long)
            
    }

    public final Closure<Boolean> isPresentInSetHeaders() {
        return { it in ['key', 'anotherKey', 'Accept', 'Cache-Control', 'Content-Type', 'Expires', 'Last-Modified', 'Location'] }
    }

}
