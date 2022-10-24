package ro.uti.ran.core.ws.fault;

import org.junit.Test;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.RequestCodes;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by bogdan.ardeleanu on 9/14/2015.
 */
public class RanExceptionTest {

    @Test
    public void testRanException() {
        RequestValidationException requestValidationExceptionIntervalSize = new RequestValidationException(RequestCodes.INVALID_INTERVAL_SIZE, 30);
        buildAndLogRanException(requestValidationExceptionIntervalSize);

        RequestValidationException requestValidationExceptionInvalidFormat = new RequestValidationException(RequestCodes.INVALID_INTERVAL_FORMAT);
        buildAndLogRanException(requestValidationExceptionInvalidFormat);


    }

    private void buildAndLogRanException(RanBusinessException ranBusinessException) {
        RanException exceptionOnWs = new RanException(ranBusinessException);
        exceptionOnWs.printStackTrace();
    }

    private String toBouncyX500Name(String issuer) {

        String[] RDN = issuer.split(",");

        StringBuffer buf = new StringBuffer(issuer.length());
        for(int i = RDN.length - 1; i >= 0; i--){
            if(i != RDN.length - 1)
                buf.append(',');

            buf.append(RDN[i]);
        }

        return buf.toString();
    }

    @Test
    public void testX509() throws Exception {
        String x509 = "-----BEGIN CERTIFICATE----- MIIGIzCCBQugAwIBAgIKYeBCzgAAAAABQTANBgkqhkiG9w0BAQUFADA6MRIwEAYK CZImiZPyLGQBGRYCcm8xEzARBgoJkiaJk/IsZAEZFgNyYXIxDzANBgNVBAMTBkNB LVJBUjAeFw0xNTEwMDIwNjAzMDJaFw0xNzEwMDEwNjAzMDJaMG4xEjAQBgoJkiaJ k/IsZAEZFgJybzETMBEGCgmSJomT8ixkARkWA3JhcjEOMAwGA1UEAxMFVXNlcnMx ETAPBgNVBAMTCG9wZW5maXJlMSAwHgYJKoZIhvcNAQkBFhFvcGVuZmlyZUByYXJv bS5ybzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMmk9kyOznhAdT/4 qIeZ3y/0cSDM/6oSznc9yx1xeKGeQ0Ql9/oZ+kjccwdMRBv7aOpPYoy5jPILYioW epMQrWgP6+nHm4UNgY++BG1mirI8lLjuaS/RWeXjdTSmlGRcSYlKV2jBJ1FIcwgV tPKW72z3rEi619wTqyyhg4FGauR0inNkUqW9z3NlkV9zGfR0HsiWoOnmLBXwAO/3 V5mGM479KQlBOix/Ymk7iVk5/VE6XEVY5K6T5UifJejUoHXwC2bg/JnJq1It5yL/ FVScfg5Cx5JfvbR5x9fvk6zgc363MfMVwcmi/SVGge+4nwBzQfNfMUpPoCBQJdKG VWRXvxMCAwEAAaOCAvUwggLxMA4GA1UdDwEB/wQEAwIFoDA7BgkrBgEEAYI3FQcE LjAsBiQrBgEEAYI3FQiBjYlxmspbtZcMgrKxQYOdtRx1grHiBIeh8Q4CAWQCAQIw RAYJKoZIhvcNAQkPBDcwNTAOBggqhkiG9w0DAgICAIAwDgYIKoZIhvcNAwQCAgCA MAcGBSsOAwIHMAoGCCqGSIb3DQMHMB0GA1UdDgQWBBR+hAj3zUGjnGLa2bGBAiAk tIgkfDAfBgNVHSMEGDAWgBTWtgOfD+asGsZsoI8ux5wlbrxWqjCBxAYDVR0fBIG8 MIG5MIG2oIGzoIGwhoGtbGRhcDovLy9DTj1DQS1SQVIsQ049US1HUkktREMtMks4 LENOPUNEUCxDTj1QdWJsaWMlMjBLZXklMjBTZXJ2aWNlcyxDTj1TZXJ2aWNlcyxD Tj1Db25maWd1cmF0aW9uLERDPXJhcixEQz1ybz9jZXJ0aWZpY2F0ZVJldm9jYXRp b25MaXN0P2Jhc2U/b2JqZWN0Q2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnQwgbMG CCsGAQUFBwEBBIGmMIGjMIGgBggrBgEFBQcwAoaBk2xkYXA6Ly8vQ049Q0EtUkFS LENOPUFJQSxDTj1QdWJsaWMlMjBLZXklMjBTZXJ2aWNlcyxDTj1TZXJ2aWNlcyxD Tj1Db25maWd1cmF0aW9uLERDPXJhcixEQz1ybz9jQUNlcnRpZmljYXRlP2Jhc2U/ b2JqZWN0Q2xhc3M9Y2VydGlmaWNhdGlvbkF1dGhvcml0eTApBgNVHSUEIjAgBggr BgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcKAwQwNQYJKwYBBAGCNxUKBCgwJjAK BggrBgEFBQcDAjAKBggrBgEFBQcDBDAMBgorBgEEAYI3CgMEMD0GA1UdEQQ2MDSg HwYKKwYBBAGCNxQCA6ARDA9vcGVuZmlyZUByYXIucm+BEW9wZW5maXJlQHJhcm9t LnJvMA0GCSqGSIb3DQEBBQUAA4IBAQAnb9CTY39Yk4WXZ6s6Rphr4fet7MMNoGdQ Wj+M3EzLO/mjs+ZGJmAM+35FXF1aCo9S0/u0n64IaAqodgf9IFMn7YZo7dKFdC66 +8UsVs205tP0iy0tMfC7HgERTEuvHaEkWGZZzbFeEeoSFoxisNvNKS6lZ9Y1qpDR 7SCBC5Q4z/iK+sQMSNBSeqDDZkxfXMtZUoNHwzCtbp7jx4aW0narIpgesXIVI1Xd NPu4X7eKxFjGCT129++XpnYDQG8AfuOqq2AUWw4g6py6a9BPF/X6ue4B88aLdWfv 1N6LFZzWPFfv3RZZlRVtwuYOwfCpB5v0HmW4S3h5H730vUCX3FSI -----END CERTIFICATE-----";
        x509 = x509.replace("BEGIN CERTIFICATE", "BEGIN_CERTIFICATE");
        x509 = x509.replace("END CERTIFICATE", "END_CERTIFICATE");
        x509 = x509.replace(" ", "\n");
        x509 = x509.replace("BEGIN_CERTIFICATE", "BEGIN CERTIFICATE");
        x509 = x509.replace("END_CERTIFICATE", "END CERTIFICATE");

        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(x509.getBytes("UTF-8")));
        System.out.println(cert.getSerialNumber().toString());
        System.out.println(cert.getIssuerDN().toString());
        System.out.println(toBouncyX500Name(cert.getIssuerDN().toString()));
    }
}