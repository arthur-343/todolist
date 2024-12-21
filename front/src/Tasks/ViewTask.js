import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

export default function ViewTask() {
    let { id } = useParams(); // Obtenha o ID da URL

    const [task, setTask] = useState({
        title: "",
        completed: "",
        description: ""
    });

    useEffect(() => {
        loadTask();
    }, []);

    const loadTask = async () => {
        try {
            const result = await axios.get(`http://localhost:8080/tasks/${id}`);
            setTask(result.data); // Carrega a tarefa no estado
        } catch (error) {
            console.error('Erro ao buscar tarefa:', error);
        }
    };

    return (
        <div className='container'>
            <div className='row'>
                <div className='col-md-6 offset-md-3 border rounded p-4 mt-2'>
                    <h2 className='text-center m-4'>View Task</h2>

                    <div className='mb-3'>
                        <label htmlFor='title' className='form-label'>Title</label>
                        <input
                            type='text'
                            className='form-control'
                            name='title'
                            id='title'
                            value={task.title}
                            readOnly
                        />
                    </div>
                    <div className='mb-3'>
                        <label htmlFor='completed' className='form-label'>Completed</label>
                        <input
                            type='text'
                            className='form-control'
                            name='completed'
                            id='completed'
                            value={task.completed ? 'Yes' : 'No'}
                            readOnly
                        />
                    </div>
                    <div className='mb-3'>
                        <label htmlFor='description' className='form-label'>Description of the task</label>
                        <input
                            type='text'
                            className='form-control'
                            name='description'
                            id='description'
                            value={task.description}
                            readOnly
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}
