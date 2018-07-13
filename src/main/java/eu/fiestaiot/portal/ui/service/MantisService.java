package eu.fiestaiot.portal.ui.service;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import eu.fiestaiot.portal.ui.web.rest.vm.SendQuestionRequest;
import eu.fiestaiot.portal.ui.web.rest.vm.SendQuestionResponse;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;


@Service
public class MantisService {

    private final Logger log = LoggerFactory.getLogger(MantisService.class);

    public SendQuestionResponse postQuestion(SendQuestionRequest request) {
        try {
            SendQuestionResponse message = new SendQuestionResponse();
            log.info("Start post question with name: {}, email: {}, question: {}", request.getName(), request.getEmail(), request.getBody());

            List<Header> headers = new ArrayList<>();
            Header headerContentType = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            headers.add(headerContentType);

            CloseableHttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();
            HttpPost httpPost = new HttpPost("http://fiesta-iot.eu/wp-admin/admin-ajax.php");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("action", "send_mantis_ticket"));
            params.add(new BasicNameValuePair("username", request.getName()));
            params.add(new BasicNameValuePair("email", request.getEmail()));
            params.add(new BasicNameValuePair("question", request.getBody()));

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = client.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);


            client.close();
            if (response.getStatusLine().getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(result);
                message.setQuestionId(jsonObject.getInt("success"));
                message.setStatus(true);
                return message;
            } else {
                message.setQuestionId(-1);
                message.setStatus(false);
                return message;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            log.info("Exception: {}", ex.toString());
            return null;
        }
    }
}
