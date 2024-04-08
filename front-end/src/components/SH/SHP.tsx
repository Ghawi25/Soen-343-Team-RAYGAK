// TODO: replace all occurences of 'your_api_endpoint' with the actual API endpoint

import React, { useState } from "react";

// Interfaces for motion detector and temperature monitoring
interface MotionDetector {
  detectorID?: string;
  detectorLocation: string;
}

function SHP() {
  const [awayMode, setAwayMode] = useState<boolean>(false);
  const [selectedDetector, setSelectedDetector] = useState<string>("");
  const [newDetectorLocation, setSelectedNewDetectorLocation] = useState<string>("");
  const [notificationTimer, setNotificationTimer] = useState<number>(0);

  const toggleAwayMode = async () => {
    setAwayMode(!awayMode);
    await fetch("your_api_endpoint/awayMode", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ on: !awayMode }),
    });
    // Trigger doors and windows to close if away mode is activated
    if (!awayMode) {
      await fetch("your_api_endpoint/closeDoorsWindows", {
        method: "POST",
      });
    }
  };

  const updateNotificationTimer = async (e: React.FormEvent) => {
    e.preventDefault();
    //Send PUT request to update notification timer
      await fetch('your_api_endpoint/notificationTimer', {
        method: 'PUT', 
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ timer : notificationTimer}), 
      });
    }

    //fuction to add a new motion detector to a location
    const addDetector = async (e: React.FormEvent) => {
      e.preventDefault();

      const newDetector: MotionDetector = {
        detectorLocation : newDetectorLocation
      } 

      await fetch('your_api_endpoint/motionDetectors', {
        method: 'POST', 
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({newDetector}), 
      });
      setSelectedNewDetectorLocation('');
    };

    const deleteDetector = async () => {
      await fetch(`your_api_endpoint/motionDetectors/${selectedDetector}`, {
          method: 'DELETE', 
        });
        setSelectedDetector('');
    };

  return (
    <div id="SHP">
      <br></br>
      <section>
        <button onClick={toggleAwayMode}>
          {awayMode ? "Deactivate Away Mode" : "Activate Away Mode"}
        </button>
      </section>

      <section>
        <h3>Motion Detectors:</h3>
          <form onSubmit={addDetector}>
            <label>Detector location you wish to add:</label>
              <input
              type="text"
              value = {newDetectorLocation}
              onChange={(e) => setSelectedNewDetectorLocation(e.target.value)}/>
              <p></p>
              <button type="submit">Add Detector</button> 
          </form>
          
        <br></br>
        
          <form>
            <label>Detector location you wish to delete:</label>
              <input
              type="text"
              value = {selectedDetector}
              onChange={(e) => setSelectedDetector(e.target.value)}/>
              <p></p>
              <button onClick={deleteDetector}>Delete Detector</button>
          </form>
      </section>

      <section>
        <h3>Alert Timer:</h3>
        <div>
          <form onSubmit={updateNotificationTimer}>
            <label htmlFor="notificationTimer">Notification Timer (minutes):</label>
              <input
                id="notificationTimer"
                type="number"
                value={notificationTimer}
                onChange={(e) => setNotificationTimer(Number(e.target.value))}
                min="0"
              />
            <button type="submit">Update Timer</button>
          </form>
        </div>
      </section>
    </div>
  );
}

export default SHP;