import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 50,
  duration: '2m',
};

const soapPayload = `
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:gs="http://foodmenu.io/gt/webservice">
    <soapenv:Header/>
    <soapenv:Body>
        <gs:addOrderRequest>
            <gs:address>leuven</gs:address>
            <gs:meals>
                <gs:mealName>Steak</gs:mealName>
                <gs:mealName>Portobello</gs:mealName>
                <gs:mealName>Fish and Chips</gs:mealName>
            </gs:meals>
        </gs:addOrderRequest>
    </soapenv:Body>
</soapenv:Envelope>`;

const headers = {
  'Content-Type': 'text/xml;charset=UTF-8',
  'SOAPAction': 'GET',
};

export default function () {
  http.post('http://dapp25-fx.eastus.cloudapp.azure.com:8081/ws/meals', soapPayload, { headers: headers });

  sleep(1);
}

