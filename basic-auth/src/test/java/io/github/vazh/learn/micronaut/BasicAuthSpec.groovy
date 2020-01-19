package io.github.vazh.learn.micronaut;

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;

import javax.inject.Inject;

@MicronautTest
public class BasicAuthSpec {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    @Client("/")
    RxHttpClient client;

    def "Verify HTTP Basic Auth works"() {
        when: 'Accessing a secured URL without authenticating'
        client.toBlocking().exchange(HttpRequest.GET("/"))

        then: 'returns unaothorized'
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status == HttpStatus.UNAUTHORIZED

        when: 'A secured URL is accessed with Basic Auth'
        HttpRequest request = HttpRequest.GET("/").basicAuth("sherlock", "password")
        HttpResponse<String> rsp = client.toBlocking().exchange(request, String)

        then: 'the endpoint can be accessed'
        rsp.status() == HttpStatus.OK
        rsp.body() == 'sherlock'
    }
}
