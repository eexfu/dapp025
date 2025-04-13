import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 100,           // 100 clients
  duration: '3m',    // last for 3 mins
};

export default function () {
  http.get('http://dsgt2025westeu.westeurope.cloudapp.azure.com:3020/rest/meals');
  sleep(1);
}
