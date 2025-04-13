import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 10,           // 10 clients
  duration: '1m',    // last for 1 mins
};

export default function () {
  http.get('http://dsgt2025westeu.westeurope.cloudapp.azure.com:3020/rest/meals');
  sleep(1);
}
