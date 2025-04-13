import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 10,           // 10 clients
  duration: '1m',    // last for 1 mins
};

export default function () {
  http.get('http://dapp25-fx.eastus.cloudapp.azure.com:8082/rest/meals');
  sleep(1);
}
