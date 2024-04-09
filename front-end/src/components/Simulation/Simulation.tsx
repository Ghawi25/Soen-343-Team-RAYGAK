import { useEffect, useState } from "react";
import DateTimePicker from "./DateTimePicker";
import { userAuth } from "../../contexts/AuthProvider";

function Simulation() {
  const auth = userAuth();
  const today = new Date().toJSON().slice(0, 10);
  var [temperature, setTemperature] = useState('--'); // Initialize temperature with placeholder
  const [image, setImage] = useState("");

  const handleSetTemperature = (newTemperature: string) => {
    setTemperature(newTemperature);
  };

  useEffect(() => {
    if(!auth.user) return;
    fetch(`http://localhost:8080/api/photos/user/${auth.user.username}`)
    .then(res => {
      return res.blob();
    })
    .then(blob => {
      setImage(URL.createObjectURL(blob));
    })
  }, [auth])

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
        <div>
          {auth.user ? (
            <img className="avatar" src={image} alt="pfp" />
          ) : (
            <img className="avatar" src="blank-avatar.webp" alt="pfp" />
          )}
        </div>
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
