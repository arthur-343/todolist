const express = require('express');
const app = express();
const PORT = 3001; // Mude a porta para 3001 ou qualquer outra disponível

app.get('/', (req, res) => {
  res.send('Hello, World!');
});

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
