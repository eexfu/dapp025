import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 50,
  duration: '3m',
};

const soapPayload = `
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:gs="http://foodmenu.io/gt/webservice">
    <soapenv:Header/>
    <soapenv:Body>
        <gs:addOrderRequest>
            <gs:order>
                <gs:address>DagobertStraat, Leuven</gs:address>
                <gs:mealNames>Steak</gs:mealNames>
                <gs:mealNames>Portobello</gs:mealNames>
            </gs:order>
        </gs:addOrderRequest>
    </soapenv:Body>
</soapenv:Envelope>`;

const soapHeaders = {
  'Content-Type': 'text/xml;charset=UTF-8',
  'SOAPAction': 'GET',
};

export default function () {
  http.get('http://dapp25-fx.eastus.cloudapp.azure.com:8082/rest/addOrderAndMeals');

  http.post('http://dsgt2025westeu.westeurope.cloudapp.azure.com:3010/ws/meals', soapPayload, { headers: soapHeaders });

  sleep(1);
}

