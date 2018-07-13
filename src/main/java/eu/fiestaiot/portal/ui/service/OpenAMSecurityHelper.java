package eu.fiestaiot.portal.ui.service;


import eu.fiestaiot.portal.ui.config.FiestaProperties;
import eu.fiestaiot.portal.ui.service.dto.FiestaUser;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OpenAMSecurityHelper {

    private final org.slf4j.Logger log = LoggerFactory
            .getLogger(OpenAMSecurityHelper.class);
     @Inject
    private FiestaProperties fiestaProperties;

    public  String getToken(HttpServletRequest request) {
        String token =  getTokenFromCookie(request);
        //String token = "AQIC5wM2LY4Sfczdd-BujcF9HDLrEBB4_9aUQDyclPP_B9U.*AAJTSQACMDEAAlNLABM3NTkzNzE2OTgwNTcwNDUwMTIwAAJTMQAA*";

        if(token!= null){
            return token;
        } else {
            return getAuthorizationToken(request);
        }
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        try {
            String token = request.getParameter("iPlanetDirectoryPro");
            if (token == null) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName()
                            .equalsIgnoreCase("iPlanetDirectoryPro")) {
                        token = cookie.getValue();
                    }
                }
            }

            return token;
        } catch (Exception ex) {
            return null;
        }

    }

    public String getAuthorizationToken(HttpServletRequest request) {
        try {
            String authorization = request.getHeader("iPlanetDirectoryPro");
            return authorization;
        } catch (Exception ex) {
            return null;
        }

    }

    private String getContent(InputStream input) {
        StringBuilder sb = new StringBuilder();
        byte[] b = new byte[1024];
        int readBytes = 0;
        try {
            while ((readBytes = input.read(b)) >= 0) {
                sb.append(new String(b, 0, readBytes, "UTF-8"));
            }
            input.close();
            return sb.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    public Boolean isAdmin(String userID, String token) {
        try {
            JSONArray groups = getUserGroups(userID, token);

            if(groups == null || groups.length() ==0) return false;
            else return groups.get(0).toString().contains("cn=fiestaAdmin");
        } catch (JSONException ex) {
            Logger.getLogger(OpenAMSecurityHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public JSONObject login(String userID, String password) {

        JSONObject userObject = null;
        try {
            URL url = new URL(
                    fiestaProperties.getEnpoints().getAuthenticateUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-OpenAM-Username", userID);
            conn.setRequestProperty("X-OpenAM-Password", password);
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.flush();
            wr.close();
            int responseMC = conn.getResponseCode();
            if (responseMC == HttpURLConnection.HTTP_OK) {
                String security = getContent(conn.getInputStream());
                log.info("get openam user --: {}", security);
                userObject = new JSONObject(security);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userObject;
    }

    public JSONArray getUserGroups(String userID, String token) {
        JSONArray groups = null;
        try {

            List<Header> headers = new ArrayList<>();
            Header headerContentType = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            Header iPlanetDirectoryProHeader = new BasicHeader("iPlanetDirectoryPro", token);

            headers.add(headerContentType);
            headers.add(iPlanetDirectoryProHeader);
            HttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();

            String userInforUrl = fiestaProperties.getEnpoints().getGetUserInfoUrl();


            HttpUriRequest request = RequestBuilder.get().setUri(userInforUrl + userID)
                    .build();

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String results = EntityUtils.toString(responseEntity);

            log.info("getUserGroups results: {}",results );

            JSONObject object = new JSONObject(results);

            groups = object.getJSONArray("isMemberOf");
            if (statusCode != 200) {
                throw new RuntimeException("REGISTER_SERVICE_CALL_FAILED_STATUS_CODE" + statusCode);
            }

        } catch (IOException ex) {
            Logger.getLogger(OpenAMSecurityHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(OpenAMSecurityHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(OpenAMSecurityHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return groups;
    }

    public String getUserID(String token) {
        String userID = "";
        try {
           URL url = new URL(
                    fiestaProperties.getEnpoints().getGetUserIdUrl());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("iPlanetDirectoryPro", token);
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.flush();
            wr.close();
            int responseMC = conn.getResponseCode();

            log.info("getUserID responseMC: {}", responseMC);
            if (responseMC == HttpURLConnection.HTTP_OK) {
                String security = getContent(conn.getInputStream());
                JSONObject jObject = new JSONObject(security);
                if (jObject.has("id")) {
                    userID = jObject.getString("id");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userID;
    }

    public boolean logout(String token) {
        try {

            HttpClient httpClient = HttpClients.createDefault();
            String logoutUrl = fiestaProperties.getEnpoints().getLogoutUrl();
            final HttpPost postRequest = new HttpPost(logoutUrl);
            postRequest.addHeader("iPlanetDirectoryPro", token);
            final RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000).build();
            postRequest.setConfig(config);
            HttpResponse httpResponse = httpClient.execute(postRequest);
            final int sc = httpResponse.getStatusLine().getStatusCode();
            if (sc != HttpStatus.SC_OK) {

                return  false;
            } else {
                Cookie c = new Cookie("iPlanetDirectoryPro", "");
                c.setMaxAge(0);
                c.setValue("");
                c.setPath("/");
                c.setDomain(".fiesta-iot.eu");
                //response.setContentType("text/html");
               // response.addCookie(c);
                return true;
            }
        } catch (IOException e) {
               log.info("[-] logout exeption: {}", e.toString());
            return  false;
        }
    }
    public List<String> getUserRoles(JSONObject object) {

        List<String> result = new ArrayList<>();
        try {
            JSONArray groups = object.getJSONArray("isMemberOf");

            if (groups != null && groups.length() > 0) {

                for (int i = 0; i < groups.length(); i++) {
                    try {
                        String role = groups.getString(i);
                        if (role.contains("cn=fiestaExperimenters") || role.contains("cn=fiestaexperimenters")) {
                            result.add("experimenterAdmin");
                        }
                        if (role.contains("cn=testbedadmin") || role.contains("cn=testbedAdmin")) {
                            result.add("testbedAdmin");
                        }
                        if (role.contains("cn=fiestaAdmin") || role.contains("cn=fiestaadmin")) {
                            result.add("fiestaAdmin");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        } catch (Exception ex) {
            log.info("[-] Exeption getUserRoles:{}", ex.toString());
        }
        result.add("authenticated");
        return result;
    }
    public FiestaUser getUserRole(String userID, String token){

           FiestaUser user = new FiestaUser();

        try {


            List<Header> headers = new ArrayList<>();
            Header headerContentType = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            Header iPlanetDirectoryProHeader = new BasicHeader("iPlanetDirectoryPro", token);

            headers.add(headerContentType);
            headers.add(iPlanetDirectoryProHeader);
            HttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();

            String userInforUrl = fiestaProperties.getEnpoints().getGetUserInfoUrl();

            HttpUriRequest request = RequestBuilder.get().setUri(userInforUrl + userID)
                .build();

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String results = EntityUtils.toString(responseEntity);

            if (statusCode != 200) {
                throw new RuntimeException("REGISTER_SERVICE_CALL_FAILED_STATUS_CODE" + statusCode);
            }

            log.info("getUserGroups results: {}",results );

            JSONObject object = new JSONObject(results);
            List<String> roles = getUserRoles(object);

            user.setRoles(roles);
            String firstName = "";
            String lastName = "";
            if(object.getJSONArray("givenName") == null || object.getJSONArray("givenName").length() ==0) {
                firstName = "";
            } else {
                firstName = object.getJSONArray("givenName").getString(0);
            }
            if(object.getJSONArray("sn") == null || object.getJSONArray("sn").length() ==0) {
                lastName = "";
            } else {
                lastName = object.getJSONArray("sn").getString(0);
            }

            String givenName = firstName + " " + lastName;

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setName(givenName);
            return user;

        } catch (IOException ex) {
           log.info("[-] IOException:", ex.toString());
        } catch (ParseException ex) {
            log.info("[-] ParseException:", ex.toString());
        } catch (JSONException ex) {
            log.info("[-] JSONException:", ex.toString());
        }

        return  user;
    }
}
