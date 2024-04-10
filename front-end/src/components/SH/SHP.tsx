// TODO: replace all occurences of 'your_api_endpoint' with the actual API endpoint

import React, { useState } from "react";

function SHP() {
  const [awayMode, setAwayMode] = useState<boolean>(false);
  const [selectedDetector, setSelectedDetector] = useState<string>("");
  const [newDetectorLocation, setSelectedNewDetectorLocation] =
    useState<string>("");
  const [notificationTimer, setNotificationTimer] = useState<number>(0);

  const toggleAwayMode = async () => {
    setAwayMode(!awayMode);
    const isAway = !awayMode;
    await fetch(`http://localhost:8080/api/SHP/awayMode/${isAway}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });
    // Trigger doors and windows to close if away mode is activated
    if (isAway) {
      await fetch("http://localhost:8080/api/SHP/closeDoorsWindows", {
        method: "POST",
      });
    }
  };

  const updateNotificationTimer = async (e: React.FormEvent) => {
    e.preventDefault();
    //Send PUT request to update notification timer
    await fetch(
      `http://localhost:8080/api/SHP/notificationTimer/${notificationTimer}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  };

  //fuction to add a new motion detector to a location
  const addDetector = async (e: React.FormEvent) => {
    e.preventDefault();

    await fetch(
      `http://localhost:8080/api/SHP/motionDetectors/${newDetectorLocation}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    setSelectedNewDetectorLocation("");
  };

  const deleteDetector = async (e: React.FormEvent) => {
    e.preventDefault();

    await fetch(
      `http://localhost:8080/api/SHP/motionDetectors/${selectedDetector}`,
      {
        method: "DELETE",
      }
    );
    setSelectedDetector("");
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
            value={newDetectorLocation}
            onChange={(e) => setSelectedNewDetectorLocation(e.target.value)}
          />
          <p></p>
          <button type="submit">Add Detector</button>
        </form>

        <br></br>

        <form>
          <label>Detector location you wish to delete:</label>
          <input
            type="text"
            value={selectedDetector}
            onChange={(e) => setSelectedDetector(e.target.value)}
          />
          <p></p>
          <button onClick={deleteDetector}>Delete Detector</button>
        </form>
      </section>

      <section>
        <h3>Alert Timer:</h3>
        <div>
          <form onSubmit={updateNotificationTimer}>
            <label htmlFor="notificationTimer">
              Notification Timer (minutes):
            </label>
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
