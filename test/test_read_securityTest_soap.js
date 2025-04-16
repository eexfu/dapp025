import http from 'k6/http';
import { sleep, check } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

// 🔢 自定义指标
const malformedRequestErrors = new Counter('malformed_request_errors');
const securityResponseTimes = new Trend('security_response_times');
const failureRate = new Rate('failure_rate');

// 📋 测试配置：运行 20 个并发用户，持续 30 秒
export let options = {
  scenarios: {
    malformed_requests: {
      executor: 'ramping-vus',
      startVUs: 5,
      stages: [
        { duration: '1m', target: 20 },
        { duration: '3m', target: 20 },
        { duration: '30s', target: 0 }
      ],
    },
  },
};

// 🚫 多个非法 SOAP 请求示例
const malformedPayloads = [
  // 缺少结束标签
  `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:gs="http://foodmenu.io/gt/webservice">
      <soapenv:Header/>
      <soapenv:Body>
          <gs:getMealRequest>
              <gs:name>Portobello</gs:name>
          </gs:getMealRequest>
  `,

  // 错误命名空间
  `
  <soapenv:Envelope xmlns:soapenv="http://wrongnamespace.org/">
      <soapenv:Header/>
      <soapenv:Body>
          <gs:getMealRequest>
              <gs:name>Portobello</gs:name>
          </gs:getMealRequest>
      </soapenv:Body>
  </soapenv:Envelope>
  `,

  // 错误字段名
  `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:gs="http://foodmenu.io/gt/webservice">
      <soapenv:Header/>
      <soapenv:Body>
          <gs:getFood>
              <gs:name>Portobello</gs:name>
          </gs:getFood>
      </soapenv:Body>
  </soapenv:Envelope>
  `,

  // 空 body
  `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
      <soapenv:Header/>
      <soapenv:Body></soapenv:Body>
  </soapenv:Envelope>
  `,
];

// 🎯 测试函数：每次发送一个随机的 malformed SOAP 请求
export default function () {
  const url = 'http://dapp25-fx.eastus.cloudapp.azure.com:8081/ws/meals';
  const headers = { 'Content-Type': 'text/xml' };

  const payload = malformedPayloads[Math.floor(Math.random() * malformedPayloads.length)];

  const res = http.post(url, payload, { headers });

  check(res, {
    'Received 4xx or 5xx (expected)': (r) => r.status >= 400,
    'No 502/503 (server stable)': (r) => r.status !== 502 && r.status !== 503,
  });

  if (res.status >= 500) {
    malformedRequestErrors.add(1);
    failureRate.add(1);
  }

  securityResponseTimes.add(res.timings.duration);
  sleep(1);
}

