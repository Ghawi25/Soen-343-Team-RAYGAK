import React, { useState } from "react";
// import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";

function Simulation() {
  const today = new Date().toJSON().slice(0, 10);
  const [startDate, setStartDate] = useState(new Date());

  const [timeSpeed, setTimeSpeed] = useState(1);

  // const handleTimeSpeedChange = (timeValue) => {
  //   setTimeSpeed(timeValue.target.value);
  // };

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
        <div className="temperature">
          <p>Outside Temp: 15°C</p>
        </div>
        <div className="datetime-picker">
          <p>datetime-picker: {today}</p>
          {/* <DatePicker
                selected={startDate}
                onChange={(date) => setStartDate(date)}
                showTimeSelect
                timeFormat="HH:mm"
                timeIntervals={15}
                timeCaption="time"
                dateFormat="MMMM d, yyyy h:mm aa"
              /> */}
        </div>
        <div className="time-speed">
          {/* Use case 9 - Time speed */}
          <p>Time speed</p>
          <input
            type="range"
            id="timeSpeed"
            name="timeSpeed"
            min="1"
            max="2"
            step="0.1"
            // value={timeSpeed}
            //onChange={handleTimeSpeedChange}
          />
        </div>
      </div>
    </>
  );
}

export default Simulation;
