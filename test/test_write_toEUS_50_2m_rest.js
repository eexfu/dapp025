import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 50,           // 50 clients
  duration: '2m',    // last fro 2 mins
};

const jsonPayload = JSON.stringify({
  address: 'leuven',
  meals: ['Steak', 'Portobello', 'Fish and Chips']
});

const headers = {
  'Content-Type': 'application/json'
};

export default function () {
  http.post('http://dapp25-fx.eastus.cloudapp.azure.com:8082/rest/addOrderAndMeals', jsonPayload, { headers });
  sleep(1);
}
