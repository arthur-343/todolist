const express = require('express');
const cors = require('cors'); // Importar o módulo CORS
const app = express();
const PORT = 8080;

// Permitir solicitações de qualquer origem
app.use(cors());

app.get('/', (req, res) => {
  res.send('Hello, World!');
});

// Rota exemplo para /tasks
app.get('/tasks', (req, res) => {
  const tasks = [
    { id: 1, firstName: 'Mark', lastName: 'Otto', handle: '@mdo' },
    { id: 2, firstName: 'Jacob', lastName: 'Thornton', handle: '@fat' },
    { id: 3, firstName: 'Larry', lastName: 'Bird', handle: '@twitter' }
  ];
  res.json(tasks);
});

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
