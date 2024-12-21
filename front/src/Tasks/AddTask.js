import React, { useState } from 'react';
import axios from 'axios'; // Adicione esta linha
import { useNavigate } from 'react-router-dom';

export default function AddTask() {
    let navigate = useNavigate();

    const [task, setTask] = useState({
        title: "",
        completed: "",
        description: ""
    });

    const { title, completed, description } = task;

    const onInputChange = (e) => {
        setTask({ ...task, [e.target.name]: e.target.value });
    };

    const onSubmit = async (e) => {
        e.preventDefault();

        // Converter 'Yes' ou 'No' para true ou false
        const taskToSubmit = {
            ...task,
            completed: completed.toLowerCase() === "yes"
        };

        await axios.post("http://localhost:8080/tasks", taskToSubmit);
        navigate("/");
    };

    return (
        <div className='container'>
            <div className='row'>
                <div className='col-md-6 offset-md-3 border rounded p-4 mt-2'>
                    <h2 className='text-center m-4'>Register Task</h2>

                    <form onSubmit={(e) => onSubmit(e)}>
                        <div className='mb-3'>
                            <label htmlFor='title' className='form-label'>
                                Title
                            </label>
                            <input
                                type='text'
                                className='form-control'
                                placeholder='Enter the Task'
                                name='title'
                                id='title'
                                value={title}
                                onChange={(e) => onInputChange(e)}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='completed' className='form-label'>
                                Completed
                            </label>
                            <input
                                type='text'
                                className='form-control'
                                placeholder='Is the task completed? (Yes/No)'
                                name='completed'
                                id='completed'
                                value={completed}
                                onChange={(e) => onInputChange(e)}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='description' className='form-label'>
                                Description of the task
                            </label>
                            <input
                                type='text'
                                className='form-control'
                                placeholder='Enter the complete description of the Task'
                                name='description'
                                id='description'
                                value={description}
                                onChange={(e) => onInputChange(e)}
                            />
                        </div>
                        <button type='submit' className='btn btn-outline-primary'>Submit</button>
                        <button type='button' className='btn btn-outline-danger mx-2'>Cancel</button>
                    </form>
                </div>
            </div>
        </div>
    );
}
