import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';

// Soak / Endurance Testing Configuration
export let options = {
  stages: [
    { duration: '2m', target: 50 },    // Ramp-up to 50 users
    { duration: '60m', target: 50 },   // Soak at 50 users for 1 hour
    { duration: '2m', target: 0 },     // Ramp-down
  ],
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    http_req_failed: ['rate<0.01'],
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
    'response time < 500ms': (r) => r.timings.duration < 500,
  });

  if (res.timings.duration > 500) {
    console.warn(`⚠️ High response time: ${res.timings.duration}ms | VU ${__VU} | ${new Date().toISOString()}`);
  }

  sleep(Math.random() * 2 + 1); // Simulate user delay
}

