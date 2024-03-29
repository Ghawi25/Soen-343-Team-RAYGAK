import { useState } from "react";
import DateTimePicker from "./DateTimePicker";

function Simulation() {
  const today = new Date().toJSON().slice(0, 10);
  var [temperature, setTemperature] = useState('--'); // Initialize temperature with placeholder

  const handleSetTemperature = (newTemperature: string) => {
    setTemperature(newTemperature);
  };

  return (
    <>
      <header className="sim-header">
        <h2>DASHBOARD</h2>
        <div className="power"></div>
        <div className="toggle-switch">
          <label>
            <input type="checkbox" />
            <span className="slider round"></span>
          </label>
        </div>
      </header>
      <div className="profile">
        <div className="avatar"></div>
        <div className="profile-text">
          <p>Role (Parent, Child) </p>
          <p>Location (Room)</p>
        </div>
      </div>
      <div className="simulation-details">
        <DateTimePicker onTemperatureFetch={handleSetTemperature}/>
        <div className="temperature">
          <p>
            Temperature fetched: <span id="temp-fetched">{temperature} °C</span>
          </p>
        </div>
        <div className="time-speed">
          <p>Time speed</p>
          <input
            type="range"
            id="timeSpeed"
            name="timeSpeed"
            min="1"
            max="2"
            step="0.1"
          />
        </div>
      </div>
    </>
  );
}

export default Simulation;
