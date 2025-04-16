import http from 'k6/http';
import { check } from 'k6';

export let options = {
  stages: [
    { duration: '2m', target: 100 },
    { duration: '2m', target: 200 },
    { duration: '2m', target: 300 },
    { duration: '2m', target: 400 },
    { duration: '2m', target: 500 },
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'],         // Less than 1% requests should fail
    http_req_duration: ['p(95)<1000'],      // 95% of requests should complete under 1000ms
  },
};

export default function () {
  const url = 'http://dapp25-fx.eastus.cloudapp.azure.com:8081/ws/meals';

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
    'Content-Type': 'text/xml',
  };

  const res = http.post(url, soapPayload, { headers });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 1s': (r) => r.timings.duration < 1000,
  });
  
  if (res.timings.duration > 1000) {
    console.warn(`High response time: ${res.timings.duration}ms, Current VUs: ${__VU}`);
  }
}

