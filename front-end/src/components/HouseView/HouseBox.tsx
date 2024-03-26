import { render } from "@testing-library/react";
import axios from "axios";
import { useEffect, useState } from "react";

// function to generate a rectangle for the room
const Room = ({ x, y, width, height }) => {
  return <rect x={x} y={y} width={width} height={height} fill="black"/>
}

// Function to render all rooms
const renderRooms = (rooms) => {
  rooms.map((room) => {
    
  })
}

function HouseBox() {
  const [house, setHouse] = useState(null);

  // Get house information
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/house")
      .then((res) => setHouse(res.data))
      .catch((err) => console.log(err));
  }, []);

  if (!house) {
    return <div>Loading...</div>
  } else {
    const rooms = house.rooms;
    return (
      <svg width="100%" height="100%">
        {renderRooms(rooms)}
      </svg>
    )
  }
}

export default HouseBox;
