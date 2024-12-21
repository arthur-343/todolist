import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Navbar from './layout/Navbar';
import Home from './pages/Home';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AddTask from "./Tasks/AddTask";
import EditTask from './Tasks/EditTask';
import ViewTask from './Tasks/ViewTask'; 

function App() {
  return (
    <div className="App">
      <Router>
        <Navbar />
        <Routes>
          <Route exact path='/' element={<Home />} />
          <Route exact path='/addtask' element={<AddTask />} />
          <Route exact path='/edittask/:id' element={<EditTask />} />
          <Route exact path='/viewtask/:id' element={<ViewTask />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
