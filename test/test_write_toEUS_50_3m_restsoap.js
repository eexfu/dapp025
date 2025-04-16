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
            <gs:address>leuven</gs:address>
            <gs:meals>
                <gs:mealName>Steak</gs:mealName>
                <gs:mealName>Portobello</gs:mealName>
                <gs:mealName>Fish and Chips</gs:mealName>
            </gs:meals>
        </gs:addOrderRequest>
    </soapenv:Body>
</soapenv:Envelope>`;

const soapHeaders = {
  'Content-Type': 'text/xml;charset=UTF-8',
  'SOAPAction': 'GET',
};

const address = "Leuven,Tiensestraat12";
const url = `http://dapp25-fx.eastus.cloudapp.azure.com:8082/rest/addOrderAndMeals/${encodeURIComponent(address)}`;

const jsonPayload = JSON.stringify([
  {
    id: "meal-001",
    name: "Pizza",
    description: "Delicious Pizza",
    mealType: "MEAT",
    kcal: 800,
    price: 12.5
  },
  {
    id: "meal-002",
    name: "Salad",
    description: "Fresh Salad",
    mealType: "VEGAN",
    kcal: 300,
    price: 5.5
  }
]);

const headers = {
  'Content-Type': 'application/json'
};

export default function () {
  http.put(url, jsonPayload, { headers });

  http.post('http://dapp25-fx.eastus.cloudapp.azure.com:8081/ws/meals', soapPayload, { headers: soapHeaders });

  sleep(1);
}

