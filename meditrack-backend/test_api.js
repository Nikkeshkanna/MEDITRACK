const http = require('http');

const options = {
  hostname: 'localhost',
  port: 8080,
  path: '/api/auth/login',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  }
};

const req = http.request(options, (res) => {
  let data = '';
  res.on('data', (chunk) => {
    data += chunk;
  });
  res.on('end', () => {
    const response = JSON.parse(data);
    const token = response.data.token;
    console.log("Token:", token);
    
    // Now request nurse queue
    const queueOptions = {
      hostname: 'localhost',
      port: 8080,
      path: '/api/nurse/queue',
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    };
    
    const queueReq = http.request(queueOptions, (qRes) => {
      let qData = '';
      qRes.on('data', (chunk) => qData += chunk);
      qRes.on('end', () => console.log("Queue Response:", qData));
    });
    queueReq.end();
  });
});

req.write(JSON.stringify({
  email: 'nurse@meditrack.com',
  password: 'password'
}));
req.end();
