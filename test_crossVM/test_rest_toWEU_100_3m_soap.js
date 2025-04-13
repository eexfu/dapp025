import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 100,
  duration: '3m',
};

const soapPayload = `
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:gs="http://foodmenu.io/gt/webservice">
    <soapenv:Header/>
    <soapenv:Body>
        <gs:getMealRequest>
            <gs:name>Portobello</gs:name>
        </gs:getMealRequest>
    </soapenv:Body>
</soapenv:Envelope>`;

const headers = {
  'Content-Type': 'text/xml;charset=UTF-8',
  'SOAPAction': 'GET',
};

export default function () {
  http.post('http://dsgt2025westeu.westeurope.cloudapp.azure.com:3010/ws/meals', soapPayload, { headers: headers });

  sleep(1);
}

