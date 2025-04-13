import http from 'k6/http';

export let options = {
  scenarios: {
    ramping_rate_test: {
      executor: 'ramping-arrival-rate',
      startRate: 10,
      timeUnit: '1s',
      preAllocatedVUs: 50,
      maxVUs: 300,
      stages: [
        { target: 10, duration: '1m' },
        { target: 30, duration: '1m' },
        { target: 60, duration: '1m' },
        { target: 80, duration: '1m' },
        { target: 100, duration: '1m' },
      ],
    },
  },
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
  const url = 'dsgt2025westeu.westeurope.cloudapp.azure.com:3010/ws/meals';
  http.post(url, soapPayload, { headers: headers });
}

