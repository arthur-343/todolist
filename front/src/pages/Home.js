import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function Home() {
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    try {
      const timestamp = new Date().getTime(); // Cria um timestamp único
      const result = await axios.get(`http://localhost:8080/tasks?timestamp=${timestamp}`, {
        headers: {
          'Cache-Control': 'no-cache',
        },
      });
      console.log('Tasks fetched:', result.data.length, result.data); // Verifique a quantidade de dados
      // Ordena as tarefas por ID antes de definir o estado
      const sortedTasks = result.data.sort((a, b) => a.id - b.id);
      setTasks(sortedTasks);
    } catch (error) {
      console.error('Erro ao buscar tarefas:', error);
    }
  };

  return (
    <div className='container'>
      <div className='py-4'>
        <table className="table border shadow">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Title</th>
              <th scope="col">Completed</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody>
            {tasks.map((task) => {
              console.log('Rendering task:', task); // Verifique cada tarefa individualmente
              return (
                <tr key={task.id}>
                  <th scope="row">{task.id}</th>
                  <td>{task.title}</td>
                  <td>{task.completed ? 'Yes' : 'No'}</td>
                  <td>
                    <button className='btn btn-primary mx-2'>View</button>
                    <button className='btn btn-outline-primary mx-2'>Edit</button>
                    <button className='btn btn-danger mx-2'>Delete</button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}
