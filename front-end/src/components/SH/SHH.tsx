// TODO: replace all occurences of 'your_api_endpoint' with the actual API endpoint

import React, { useState, useEffect } from "react";

// interfaces for Zone and Room
interface Zone {
  zoneID: string;
  zoneType: string;
  roomIds: string[];
}

// interface Room {
//   roomID?: string;
//   roomName: string;
//   temperature?: number;
// }

// The SHH component
function SHH() {
  const [zones, setZones] = useState<Zone[]>([]);
  const [selectedZone, setSelectedZone] = useState<string>("");
  const [selectedRoom, setSelectedRoom] = useState<string>("");
  const [roomTemperature, setRoomTemperature] = useState<number>(20);
  const [isHeatingOn, setIsHeatingOn] = useState<boolean>(false);

  // State for creating a new zone
  const [newZoneName, setNewZoneName] = useState<string>("");
  const [newZoneType, setNewZoneType] = useState<string>("");
  const [newRoomNames, setNewRoomNames] = useState<string>("");

  // Fetch zones on component mount
  useEffect(() => {
    fetchZones();
  }, []);

  const fetchZones = async () => {
    const response = await fetch("http://localhost:8080/api/SHH/zones");
    const data = await response.json();
    setZones(data);
  };

  const handleZoneChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedZone(e.target.value);
  };

  const handleRoomChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const room = zones
      .find((zone) => zone.zoneID === selectedZone)
      ?.roomIds.find((id) => id === e.target.value);
    setSelectedRoom(e.target.value);
    if (room) setRoomTemperature(20);
  };

  const deleteRoom = async () => {
    // Send DELETE request to delete room
    await fetch(`your_api_endpoint/rooms/${selectedRoom}`, {
      method: "DELETE",
    });
    fetchZones(); // Refresh zones after deletion
  };

  const deleteZone = async () => {
    // Send DELETE request to delete zone
    await fetch(`http://localhost:8080/api/SHH/zones/${selectedZone}`, {
      method: "DELETE",
    });
    fetchZones(); // Refresh zones after deletion
  };

  const updateRoomTemperature = async (e: React.FormEvent) => {
    e.preventDefault();
    // Send PUT request to update room temperature
    await fetch(`your_api_endpoint/rooms/${selectedRoom}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ temperature: roomTemperature }),
    });
  };

  const toggleHeatingSystem = async () => {
    setIsHeatingOn(!isHeatingOn);
    await fetch(`http://localhost:8080/api/SHH/heatingSystem/${isHeatingOn}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });
  };

  const createZone = async (e: React.FormEvent) => {
    e.preventDefault();
    const rooms = newRoomNames.split(",");

    const newZone = {
      zoneName: newZoneName,
      zoneType: newZoneType,
      roomIds: rooms,
    };

    // Send POST request to create a new zone
    await fetch("http://localhost:8080/api/SHH/zones", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newZone),
    });

    fetchZones(); // Refresh the zone list
    // Optionally reset form fields here
    setNewZoneName("");
    setNewZoneType("");
    setNewRoomNames("");
  };

  return (
    <div id="SHH">
      <section id="SHH__switch">
        <button onClick={toggleHeatingSystem}>
          {isHeatingOn ? "Turn Off" : "Turn On"} SHH
        </button>
      </section>

      <section>
        <h3>Select a zone:</h3>
        <select value={selectedZone} onChange={handleZoneChange}>
          {zones.map((zone) => (
            <option key={zone.zoneID} value={zone.zoneID}>
              {zone.zoneID}
            </option>
          ))}
        </select>
        <button onClick={deleteZone}>Delete Zone</button>

        <h3>Select a room:</h3>
        <select
          value={selectedRoom}
          onChange={handleRoomChange}
          disabled={!selectedZone}
        >
          {selectedZone &&
            zones
              .find((zone) => zone.zoneID === selectedZone)
              ?.roomIds.map((id) => (
                <option key={id} value={id}>
                  {id}
                </option>
              ))}
        </select>
        <button onClick={deleteRoom} disabled={!selectedRoom}>
          Delete Room
        </button>

        <h3>Temperature settings:</h3>
        <div className="field--SHH">
          Selected Room Temperature: {roomTemperature} °C
        </div>
        <form onSubmit={updateRoomTemperature} className="field-SHH">
          <span>Update temperature: </span>
          <input
            type="number"
            value={roomTemperature}
            onChange={(e) => setRoomTemperature(Number(e.target.value))}
            placeholder="°C"
          />
          <button className="btn--shift-down" type="submit">
            Update
          </button>
        </form>
      </section>

      <section>
        <h3>Create new zone:</h3>
        <form onSubmit={createZone} className="create-zone">
          <div>
            <label>Zone Name:</label>
            <input
              type="text"
              value={newZoneName}
              onChange={(e) => setNewZoneName(e.target.value)}
            />
          </div>
          <div>
            <label>Zone Type:</label>
            <input
              type="text"
              value={newZoneType}
              onChange={(e) => setNewZoneType(e.target.value)}
            />
          </div>
          <div>
            <label>Rooms:</label>
            <input
              type="text"
              value={newRoomNames}
              onChange={(e) => setNewRoomNames(e.target.value)}
              placeholder="room1, room2, room3..."
            />
          </div>
          <button id="btn--create-zone" type="submit">
            Create Zone
          </button>
        </form>
      </section>
    </div>
  );
}

export default SHH;
